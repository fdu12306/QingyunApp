package com.example.qingyun.bean;

import java.time.LocalDateTime;

public class Product {
    private Integer id;
    private Integer userId;
    private String productName;
    private String campus;
    private String category;
    private Double price;
    private String description;
    private String issueTime;
    private String soldTime;
    private String imagePath;
    private Integer soldState;
    private Double priceThreshold;

    public Product(Integer id,String productName,Double price,String imagePath){
        this.id=id;
        this.productName=productName;
        this.price=price;
        this.imagePath=imagePath;
    }

    public Product(Integer id,String productName,Double price,String imagePath,String soldTime){
        this.id=id;
        this.productName=productName;
        this.price=price;
        this.imagePath=imagePath;
        this.soldTime=soldTime;
    }

    public Product(Integer id,String productName,Double price,String imagePath,Double priceThreshold){
        this.id=id;
        this.productName=productName;
        this.price=price;
        this.imagePath=imagePath;
        this.priceThreshold=priceThreshold;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public String getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(String soldTime) {
        this.soldTime = soldTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getSoldState() {
        return soldState;
    }

    public void setSoldState(Integer soldState) {
        this.soldState = soldState;
    }

    public Double getPriceThreshold() {
        return priceThreshold;
    }

    public void setPriceThreshold(Double priceThreshold) {
        this.priceThreshold = priceThreshold;
    }
}
