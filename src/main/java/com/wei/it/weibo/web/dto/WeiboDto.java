package com.wei.it.weibo.web.dto;

import com.wei.it.weibo.entity.User;
import java.util.Date;

public class WeiboDto {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Date createTime;
    private Integer readCount;
    private String img;
    private User user;   // 按要求添加

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Integer getReadCount() { return readCount; }
    public void setReadCount(Integer readCount) { this.readCount = readCount; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}