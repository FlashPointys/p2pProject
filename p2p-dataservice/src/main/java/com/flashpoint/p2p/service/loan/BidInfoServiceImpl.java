package com.flashpoint.p2p.service.loan;

import com.flashpoint.p2p.common.Constant;
import com.flashpoint.p2p.mapper.loan.BidInfoMapper;
import com.flashpoint.p2p.mapper.loan.LoanInfoMapper;
import com.flashpoint.p2p.mapper.user.FinanceAccountMapper;
import com.flashpoint.p2p.model.loan.BidInfo;
import com.flashpoint.p2p.model.loan.LoanInfo;
import com.flashpoint.p2p.model.vo.BidUserTop;
import com.flashpoint.p2p.model.vo.PaginationVO;
import com.flashpoint.p2p.model.vo.ResultObject;
import com.flashpoint.p2p.service.user.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.lang.ref.PhantomReference;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService{

    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Double queryBidAllMoney() {

        redisTemplate.setKeySerializer(new StringRedisSerializer());

        BoundValueOperations<String, Object> boundValueOps = redisTemplate.boundValueOps(Constant.BID_ALL_MONEY);

        Double bidAllMoney = (Double) boundValueOps.get();
        if (bidAllMoney == null) {

            bidAllMoney= bidInfoMapper.selectBidAllMoney();

            boundValueOps.set(bidAllMoney);
            boundValueOps.expire(15, TimeUnit.SECONDS);

        }
        return bidAllMoney;
    }

    @Override
    public List<BidInfo> queryBidInfoListByLoanId(Integer id) {
        return bidInfoMapper.selectBidInfoListByLoanId(id);
    }

    @Override
    public List<BidInfo> queryBidinfoListByUid(Map<String, Object> map) {

        return bidInfoMapper.selectBidInfoListByPage(map);
    }

    @Override
    public PaginationVO<BidInfo> queryBidinfoListByPage(Map<String, Object> paramMap) {

        PaginationVO<BidInfo> paginationVO=new PaginationVO<>();
        paginationVO.setTotal(bidInfoMapper.selectBidinfoCount(paramMap));
        paginationVO.setDataList(bidInfoMapper.selectBidInfoListByPage(paramMap));
        return paginationVO;
    }

    @Override
    public ResultObject invest(Map<String, Object> paramMap) {

        ResultObject resultObject=new ResultObject();
        resultObject.setErrorCode(Constant.SUCCESS);

        Integer loanId= (Integer) paramMap.get("loanId");
        Double bidMoney= (Double) paramMap.get("bidMoney");
        Integer uid= (Integer) paramMap.get("uid");
        String phone= (String) paramMap.get("phone");
        //用了数据库的乐观锁,版本号信息
       LoanInfo loanInfo=  loanInfoMapper.selectByPrimaryKey(loanId);

        paramMap.put("version",loanInfo.getVersion());

       //商品剩余可投金额
         int updateLoanInfoCount= loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
         if(updateLoanInfoCount>0){
             //用户账户剩余金额
             int updateFinanceAccountCount= financeAccountMapper.updateFinanceAccountByUid(paramMap);
             if(updateFinanceAccountCount>0){
                 //更新投资记录
                 BidInfo bidInfo=new BidInfo();
                 bidInfo.setBidMoney(bidMoney);
                 bidInfo.setBidStatus(1);
                 bidInfo.setBidTime(new Date());
                 bidInfo.setLoanId(loanId);
                 bidInfo.setUid(uid);
                 int insertBidInfoCount= bidInfoMapper.insertSelective(bidInfo);
                 //判断商品是否满标
                 if(insertBidInfoCount>0){

                     LoanInfo loanInfoDetail= loanInfoMapper.selectByPrimaryKey(loanId);
                     if(loanInfoDetail.getLeftProductMoney()==0){
                         //更新商品满标时间
                         LoanInfo updateLoanInfo=new LoanInfo();
                         updateLoanInfo.setProductFullTime(new Date());
                         updateLoanInfo.setProductStatus(1);
                         updateLoanInfo.setId(loanInfoDetail.getId());

                         //更新
                         int updateLoanInfoCount1= loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
                         if(updateLoanInfoCount1<=0){
                             resultObject.setErrorCode(Constant.FAIL);
                         }

                     }


                    //存储用户投资信息利用redis数据库
                     redisTemplate.opsForZSet().incrementScore(Constant.INVEST_TOP,phone,bidMoney);


                 }else{
                     resultObject.setErrorCode(Constant.FAIL);
                 }


             }else{
                 resultObject.setErrorCode(Constant.FAIL);
             }

         }else{
             resultObject.setErrorCode(Constant.FAIL);
         }


        //更新用户的剩余金额
        //更新商品的可投金额
          //判断是否满标
        //更新投资记录

        return resultObject;
    }

    @Override
    public List<BidUserTop> queryBidUserTop() {

        //存放排行榜数据
        List<BidUserTop> bidUserTopList=new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constant.INVEST_TOP, 0, 9);

        Iterator<ZSetOperations.TypedTuple<Object>> typedTupleIterator = typedTuples.iterator();
        while (typedTupleIterator.hasNext()){
            ZSetOperations.TypedTuple<Object> typedTuple = typedTupleIterator.next();
            String phone= (String) typedTuple.getValue();
            Double score=typedTuple.getScore();

            BidUserTop bidUserTop=new BidUserTop();
            bidUserTop.setPhone(phone);
            bidUserTop.setScore(score);
            bidUserTopList.add(bidUserTop);

        }

        return bidUserTopList;
    }
}
