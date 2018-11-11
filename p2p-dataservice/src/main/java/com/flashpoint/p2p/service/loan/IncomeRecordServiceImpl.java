package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.loan.BidInfoMapper;
import com.flashpoint.p2p.mapper.loan.IncomeRecordMapper;
import com.flashpoint.p2p.mapper.loan.LoanInfoMapper;
import com.flashpoint.p2p.mapper.user.FinanceAccountMapper;
import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.loan.IncomeRecord;
import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IncomeRecordServiceImpl implements  IncomeRecordService{
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public List<IncomeRecord> queryIncomeRecordServiceByUid(Map<String, Object> map) {
        return incomeRecordMapper.selectIncomeRecordListbyPage(map);
    }

    @Override
    public PaginationVO<IncomeRecord> queryIncomeRecordListByPage(Map<String, Object> paramMap) {
       PaginationVO<IncomeRecord> paginationVO=new PaginationVO<>();
       paginationVO.setTotal(incomeRecordMapper.selectCountByUid(paramMap));
       paginationVO.setDataList(incomeRecordMapper.selectIncomeRecordListbyPage(paramMap));
        return paginationVO;
    }

    @Override
    public void generateIncomePlan() {

        //获取已满标的产品-->
        List<LoanInfo> loanInfoList= loanInfoMapper.selectByProductStatus(1);

        //遍历满标得产品
        for(LoanInfo loanInfo:loanInfoList){
            //获取投资记录
            List<BidInfo> bidInfoList= bidInfoMapper.selectBidInfoListByLoanId(loanInfo.getId());

            //遍历投资记录，将当前的投资记录生成收益记录
            for (BidInfo bidInfo:bidInfoList){
                //获取到每一条投资记录，将当前的投资记录生成对应的收益记录
                IncomeRecord incomeRecord=new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setIncomeStatus(0);

                //收益时间(Date)=满标时间(Date)+产品周期(int)
                Date incomeDate=null;

                //收益金额=投资金额*天利率*投资天数
                double incomeMoney=0;

                if(Constant.PRODUCT_TYPE_X==loanInfo.getProductType()){
                    //新手宝
                    incomeDate= DateUtils.getDateByAddDays(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney=bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle();

                }else{
                    //优选和散标
                    incomeDate=DateUtils.getDateByAddMontys(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney=bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle()*30;
                }

                incomeMoney=Math.round(incomeMoney*Math.pow(10,2))/Math.pow(10,2);
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);

                int insertIncomeCount= incomeRecordMapper.insertSelective(incomeRecord);

            }
            //将当前产品状态更新为2且生成收益计划
            LoanInfo updateLoanInfo=new LoanInfo();
            updateLoanInfo.setProductStatus(2);
            updateLoanInfo.setId(loanInfo.getId());
            loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
        }
    }

    @Override
    public void generateIncomeBack() {

        //查询收益时间与当前时间相等，并且收益状态为0的收益记录

        List<IncomeRecord> incomeRecordList= incomeRecordMapper.selectIncomeRecordByIncomeDateAndIncomeStatus(0);

        //将收益进行循环
        for(IncomeRecord incomeRecord:incomeRecordList){
            //准备参数
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("uid",incomeRecord.getUid());
            paramMap.put("bidMoney",incomeRecord.getBidMoney());
            paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());
            //返还给用户
            int updataFinanceAccountCount= financeAccountMapper.updateFinanceAccountByUid(paramMap);

            if(updataFinanceAccountCount>0){
                //返还成功
                //将当前收益记录的状态更新为1 收益已返还
                IncomeRecord updateIncomeRecord=new IncomeRecord();
                updateIncomeRecord.setIncomeStatus(1);
                updateIncomeRecord.setId(incomeRecord.getId());

                incomeRecordMapper.updateByPrimaryKey(updateIncomeRecord);
            }

        }
    }
}
