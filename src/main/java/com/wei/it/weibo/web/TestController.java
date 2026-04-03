package com.wei.it.weibo.web;

import com.wei.it.weibo.web.dto.RespEntity;
import com.wei.it.weibo.web.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Hello Weibo!";
    }

    @GetMapping("/api/test")
    public String test() {
        return "API is working";
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
        return RespEntity.success(2000, "helloworld", Map.of("name", "lisi", "age", 23));
    }

    /**
     * 测试接口 - 带默认值的参数
     */
    @GetMapping("/my/test0")
    public RespEntity test0(@RequestParam(value = "name", defaultValue = "no name") String name,
                            @RequestParam int age) {
        System.out.println("name:" + name + " " + "age:" + age);
        return RespEntity.success(2000, "success!", null);
    }

    /**
     * 测试接口 - 多个参数
     */
    @GetMapping("/my/test02")
    public RespEntity test02(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "find", defaultValue = "") String find) {
        return RespEntity.success(2001, "successfully insert page!",
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

        return RespEntity.success(2000, "sign up!",
                nickName + " " + loginName + " " + loginPwd);
    }

    /**
     * 测试接口 - 接收UserDto对象
     */
    @PostMapping("/my/test04")
    public RespEntity test04(@RequestBody UserDto user) {
        return RespEntity.success(2000, "注册成功",
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

        return RespEntity.success(2000, "test05 success!",
                "title:" + title + " " + "content:" + content +
                        " imgFile:" + imgFile.getOriginalFilename());
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
}
