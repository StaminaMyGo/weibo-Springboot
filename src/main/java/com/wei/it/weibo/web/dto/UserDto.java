package com.wei.it.weibo.web.dto;

public class UserDto {
    private String nickName;
    private String loginName;
    private String loginPwd;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
