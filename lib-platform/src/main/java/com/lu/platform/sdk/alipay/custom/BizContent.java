package com.lu.platform.sdk.alipay.custom;

/**
 * @Author: luqihua
 * @Time: 2018/4/18
 * @Description: BizContent
 */

public class BizContent {

    /**
     * 必填：product_code : QUICK_MSECURITY_PAY
     * 必填：total_amount : 0.01
     * 必填：subject : 商品的标题/交易标题/订单标题/订单关键字等
     * 必填：out_trade_no : 70501111111S001111119
     * body : 我是测试数据
     * timeout_express : 30m
     */

    private String timeout_express;
    private String product_code = "QUICK_MSECURITY_PAY";
    private String total_amount;
    private String subject;
    private String body;
    private String out_trade_no;

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
}
