package com.example.qingyun.bean;

public class Browser {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String visitTime;
    private String productName;
    private String imagePath;
    private Double price;

    public Browser(Integer id,Integer productId,String productName,Double price,String imagePath,String visitTime){
        this.id=id;
        this.productId=productId;
        this.productName=productName;
        this.price=price;
        this.imagePath=imagePath;
        this.visitTime=visitTime;
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

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
