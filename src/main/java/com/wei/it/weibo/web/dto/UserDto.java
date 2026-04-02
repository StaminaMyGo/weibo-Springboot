package com.wei.it.weibo.web.dto;
import com.wei.it.weibo.entity.User;
public class UserDto extends User {
    private String token="";


    public UserDto(){}
    public UserDto(User user){
        this.setId(user.getId());
        this.setLoginName(user.getLoginName());
        this.setLoginPwd(user.getLoginPwd());
        this.setScore(user.getScore());
        this.setAttentionCount(user.getAttentionCount());
        this.setPhoto(user.getPhoto());
        this.setNickName(user.getNickName());

    }
    public User toEntity(){
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
