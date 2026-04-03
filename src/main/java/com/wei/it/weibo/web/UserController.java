package com.wei.it.weibo.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.it.weibo.entity.User;
import com.wei.it.weibo.mapper.UserMapper;
import com.wei.it.weibo.web.dto.RespEntity;
import com.wei.it.weibo.web.dto.UserDto;
import com.wei.it.weibo.service.UserService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController  // 改为 @RestController，这样所有方法默认都有 @ResponseBody
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

//    @Autowired
//    private UserMapper userMapper;
    @GetMapping(value = "/g/users",params = {"pageNum"})
    public RespEntity usrPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "" ) String find
    ){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!find.isEmpty(),"user_nickname",find);
        queryWrapper.orderByDesc("user_nickname");
        Page<User> pageInfo = new Page<>(page,2);
        userService.page(pageInfo,queryWrapper);
        pageInfo.setRecords(UserDto.dtoList(pageInfo.getRecords()));

        return RespEntity.success(2003,"查询成功",pageInfo);
    }


    @GetMapping("/g/users")
    public RespEntity getUserList() {
        List<User> list = userService.list(new QueryWrapper<>());
        return RespEntity.success( 2000, "查询成功", list);
    }

    /**
     * 处理所有未匹配的请求，返回404错误
     */
    @RequestMapping("/**")
    public RespEntity catchAll(HttpServletRequest request) {
        System.out.println("捕获到未知请求: " + request.getRequestURI());
        return RespEntity.error(404, "接口不存在: " + request.getRequestURI(), null);
    }
    @Value("${my.jwt_pwd}")
    private String jwtPwd="";

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public RespEntity login(@RequestParam String username, @RequestParam String password) {
        // 1. 参数校验
        System.out.println("========== 登录请求到达 ==========");
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (username == null || username.trim().isEmpty()) {
            return RespEntity.success(4001, "用户名不能为空",null);
        }
        if (password == null || password.trim().isEmpty()) {
            return RespEntity.error(4001, "密码不能为空", null);
        }

        System.out.println("开始查询数据库...");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_loginname", username);
        queryWrapper.eq("user_loginpwd", password);
        // userMapper.SelectOne(queryWrapper)
        User user = userService.getOne(queryWrapper);

        System.out.println("查询结果: " + (user != null ? "找到用户" : "未找到用户"));

        if (user != null) {
            //向浏览器派发一个Jwt凭证，日后凭此凭证证明身份
            JwtBuilder builder = Jwts.builder();
            builder.setId(UUID.randomUUID().toString());
            builder.setIssuedAt(new Date());//凭证派发的时间
            builder.setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24));
            builder.signWith(SignatureAlgorithm.HS256,this.jwtPwd);
            builder.setSubject(user.getId()+"");
            builder.setClaims(Map.of(
                    "id", user.getId(),
                    "nickName", user.getNickName() != null ? user.getNickName() : "",
                    "loginName", user.getLoginName() != null ? user.getLoginName() : "",
                    "photo", user.getPhoto() != null ? user.getPhoto() : "",
                    "score", user.getScore(),
                    "attentionCount", user.getAttentionCount()
            ));

            String jwttoken=builder.compact();
            System.out.println( jwttoken);

            UserDto dto = new UserDto(user);
            dto.setToken(jwttoken);
            return RespEntity.success(2004,"登录成功",dto);
        }

        return RespEntity.error(4001, "用户名或密码错误", null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/reg")
    public RespEntity register(@RequestBody User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_loginname", user.getLoginName());
//        User existingUser = userMapper.SelectOne(queryWrapper);

        User existingUser = userService.getOne(queryWrapper);

        if (existingUser != null) {
            return RespEntity.error(4000, "此用户名已存在，不允许注册", null);
        }

        // 设置默认值
        user.setScore(50);
        user.setAttentionCount(0);

        // 插入用户
//        userMapper.insert(user);
        userService.save(user);

        // 清除密码后返回
        user.setLoginPwd("");
        return RespEntity.success(2002, "注册成功", user);
    }




}