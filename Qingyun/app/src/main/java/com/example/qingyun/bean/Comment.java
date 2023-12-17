package com.example.qingyun.bean;

public class Comment {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String content;
    private String issueTime;
    private Integer deleteState;
    private Integer isOwner;
    private String username;
    public Comment(Integer id,String username,String content,String issueTime,Integer deleteState,Integer isOwner){
        this.id=id;
        this.username=username;
        this.content=content;
        this.issueTime=issueTime;
        this.deleteState=deleteState;
        this.isOwner=isOwner;
    }
    public Comment(Integer id,Integer productId,String content,String issueTime,Integer deleteState,Integer isOwner){
        this.id=id;
        this.productId=productId;
        this.content=content;
        this.issueTime=issueTime;
        this.deleteState=deleteState;
        this.isOwner=isOwner;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public Integer getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(Integer deleteState) {
        this.deleteState = deleteState;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
