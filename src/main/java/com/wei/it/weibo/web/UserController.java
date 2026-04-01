package com.wei.it.weibo.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wei.it.weibo.entity.User;
import com.wei.it.weibo.mapper.UserMapper;
import com.wei.it.weibo.web.dto.RespEntity;
import com.wei.it.weibo.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController  // 改为 @RestController，这样所有方法默认都有 @ResponseBody
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 处理所有未匹配的请求，返回404错误
     */
    @RequestMapping("/**")
    public RespEntity catchAll(HttpServletRequest request) {
        System.out.println("捕获到未知请求: " + request.getRequestURI());
        return new RespEntity(404, "接口不存在: " + request.getRequestURI(), null);
    }

    /**
     * 用户登录
     */
    @PostMapping("/users/login")
    public RespEntity login(@RequestParam String username, @RequestParam String password) {
        // 1. 参数校验
        System.out.println("========== 登录请求到达 ==========");
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        if (username == null || username.trim().isEmpty()) {
            return new RespEntity(4001, "用户名不能为空", null);
        }
        if (password == null || password.trim().isEmpty()) {
            return new RespEntity(4001, "密码不能为空", null);
        }

        System.out.println("开始查询数据库...");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_loginname", username);
        queryWrapper.eq("user_loginpwd", password);
        User user = userMapper.selectOne(queryWrapper);

        System.out.println("查询结果: " + (user != null ? "找到用户" : "未找到用户"));

        if (user != null) {
            user.setLoginPwd(null);  // 清除密码，安全考虑
            RespEntity successResp = new RespEntity(2000, "登录成功", user);
            System.out.println("准备返回成功响应: code=" + successResp.getCode() + ", msg=" + successResp.getMsg());
            System.out.println("响应对象: " + successResp);
            return successResp;
        }

        return new RespEntity(4001, "用户名或密码错误", null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/users/reg")
    public RespEntity register(@RequestBody User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_loginname", user.getLoginName());
        User existingUser = userMapper.selectOne(queryWrapper);

        if (existingUser != null) {
            return new RespEntity(4000, "此用户名已存在，不允许注册", null);
        }

        // 设置默认值
        user.setScore(50);
        user.setAttentionCount(0);

        // 插入用户
        userMapper.insert(user);

        // 清除密码后返回
        user.setLoginPwd("");
        return new RespEntity(2002, "注册成功", user);
    }

    /**
     * 测试接口 - 返回JSON字符串
     */
    @GetMapping(value = "/my/hello", produces = "application/json")
    public String hello() {
        return """
                {
                    "code": "200",
                    "msg": "Hello World!",
                    "data": {
                        "name": "张三",
                        "age": 23
                    }
                }
                """;
    }

    /**
     * 测试接口 - 返回Map
     */
    @GetMapping("/my/hello2")
    public Map<String, Object> hello2() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "2000");
        map.put("msg", "Hello World!");
        map.put("data", Map.of("name", "lisi", "age", 23));
        return map;
    }

    /**
     * 测试接口 - 返回RespEntity
     */
    @GetMapping("/my/hello3")
    public RespEntity hello3() {
        return new RespEntity(2000, "helloworld", Map.of("name", "lisi", "age", 23));
    }

    /**
     * 测试接口 - 带默认值的参数
     */
    @GetMapping("/my/test0")
    public RespEntity test0(@RequestParam(value = "name", defaultValue = "no name") String name,
                            @RequestParam int age) {
        System.out.println("name:" + name + " " + "age:" + age);
        return new RespEntity(2000, "success!", null);
    }

    /**
     * 测试接口 - 多个参数
     */
    @GetMapping("/my/test02")
    public RespEntity test02(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "find", defaultValue = "") String find) {
        return new RespEntity(2001, "successfully insert page!",
                "page:" + page + " " + "find:" + find);
    }

    /**
     * 测试接口 - 接收Map
     */
    @PostMapping("/my/test03")
    public RespEntity test03(@RequestBody Map<String, Object> map) {
        String nickName = (String) map.get("nickName");
        String loginName = (String) map.get("loginName");
        String loginPwd = (String) map.get("loginPwd");

        return new RespEntity(2000, "sign up!",
                nickName + " " + loginName + " " + loginPwd);
    }

    /**
     * 测试接口 - 接收UserDto对象
     */
    @PostMapping("/my/test04")
    public RespEntity test04(@RequestBody UserDto user) {
        return new RespEntity(2000, "注册成功",
                user.getNickName() + user.getLoginName() + user.getLoginPwd());
    }

    /**
     * 测试接口 - 文件上传
     */
    @PostMapping("/my/test05")
    public RespEntity test05(String title,
                             String content,
                             @RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        System.out.println("title:" + title + " " + "content:" + content +
                " imgFile:" + imgFile.getOriginalFilename());

        // 保存图片
        if (!imgFile.isEmpty()) {
            imgFile.transferTo(new File("F:/" + imgFile.getOriginalFilename()));
        }

        return new RespEntity(2000, "test05 success!",
                "title:" + title + " " + "content:" + content +
                        " imgFile:" + imgFile.getOriginalFilename());
    }
}