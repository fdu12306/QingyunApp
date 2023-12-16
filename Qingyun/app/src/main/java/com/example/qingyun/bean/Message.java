package com.example.qingyun.bean;

public class Message {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String time;
    private String remark;
    private Integer isRead;
    public Message(Integer id,String time,String remark,Integer isRead){
        this.id=id;
        this.time=time;
        this.remark=remark;
        this.isRead=isRead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
