package com.flashpoint.pay.config;

/**
 * ClassName:PayConfig
 * Package:com.bjpowernode.pay.config
 * Description:
 *
 * @date:2018/11/12 14:57
 * @author:guoxin@bjpowernode.com
 */
public class PayConfig {

    private String gateWayUrl;
    private String alipaAppId;
    private String merchant_private_key;
    private String format;
    private String charset;
    private String alipay_public_key;
    private String sign_type;
    private String alipay_return_url;
    private String alipay_notify_url;

    public String getAlipay_return_url() {
        return alipay_return_url;
    }

    public void setAlipay_return_url(String alipay_return_url) {
        this.alipay_return_url = alipay_return_url;
    }

    public String getAlipay_notify_url() {
        return alipay_notify_url;
    }

    public void setAlipay_notify_url(String alipay_notify_url) {
        this.alipay_notify_url = alipay_notify_url;
    }

    public String getGateWayUrl() {
        return gateWayUrl;
    }

    public void setGateWayUrl(String gateWayUrl) {
        this.gateWayUrl = gateWayUrl;
    }

    public String getAlipaAppId() {
        return alipaAppId;
    }

    public void setAlipaAppId(String alipaAppId) {
        this.alipaAppId = alipaAppId;
    }

    public String getMerchant_private_key() {
        return merchant_private_key;
    }

    public void setMerchant_private_key(String merchant_private_key) {
        this.merchant_private_key = merchant_private_key;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}
