package com.flashpoint.p2p.model.vo;

import java.io.Serializable;

public class BidUserTop implements Serializable {

    private String phone;
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
