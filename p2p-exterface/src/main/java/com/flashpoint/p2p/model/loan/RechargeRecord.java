package com.flashpoint.p2p.model.loan;

import java.util.Date;

public class RechargeRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.id
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.uid
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private Integer uid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.recharge_no
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private String rechargeNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.recharge_status
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private String rechargeStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.recharge_money
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private Double rechargeMoney;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.recharge_time
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private Date rechargeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column b_recharge_record.recharge_desc
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    private String rechargeDesc;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.id
     *
     * @return the value of b_recharge_record.id
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.id
     *
     * @param id the value for b_recharge_record.id
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.uid
     *
     * @return the value of b_recharge_record.uid
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.uid
     *
     * @param uid the value for b_recharge_record.uid
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.recharge_no
     *
     * @return the value of b_recharge_record.recharge_no
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public String getRechargeNo() {
        return rechargeNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.recharge_no
     *
     * @param rechargeNo the value for b_recharge_record.recharge_no
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo == null ? null : rechargeNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.recharge_status
     *
     * @return the value of b_recharge_record.recharge_status
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public String getRechargeStatus() {
        return rechargeStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.recharge_status
     *
     * @param rechargeStatus the value for b_recharge_record.recharge_status
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus == null ? null : rechargeStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.recharge_money
     *
     * @return the value of b_recharge_record.recharge_money
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public Double getRechargeMoney() {
        return rechargeMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.recharge_money
     *
     * @param rechargeMoney the value for b_recharge_record.recharge_money
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setRechargeMoney(Double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.recharge_time
     *
     * @return the value of b_recharge_record.recharge_time
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public Date getRechargeTime() {
        return rechargeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.recharge_time
     *
     * @param rechargeTime the value for b_recharge_record.recharge_time
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column b_recharge_record.recharge_desc
     *
     * @return the value of b_recharge_record.recharge_desc
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public String getRechargeDesc() {
        return rechargeDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column b_recharge_record.recharge_desc
     *
     * @param rechargeDesc the value for b_recharge_record.recharge_desc
     *
     * @mbggenerated Mon Nov 05 17:44:36 CST 2018
     */
    public void setRechargeDesc(String rechargeDesc) {
        this.rechargeDesc = rechargeDesc == null ? null : rechargeDesc.trim();
    }
}