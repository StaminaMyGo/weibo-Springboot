package com.wei.it.weibo.web;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wei.it.weibo.entity.User;

import com.wei.it.weibo.mapper.UserMapper;
import com.wei.it.weibo.web.dto.RespEntity;
import com.wei.it.weibo.web.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/users/login")
    public RespEntity login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_loginname", username);
        queryWrapper.eq("user_loginpwd", password);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            user.setLoginPwd(null);
            return new RespEntity(2000,"登录成功",user);

        }
        return new RespEntity(4001,"用户名或密码不对",null);
    }

    @PostMapping("/users/reg")
    @ResponseBody
    public RespEntity register(@RequestBody User user){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("user_loginname",user.getLoginName());
        User user1=userMapper.selectOne(queryWrapper);
        if(user1!=null){
            return new RespEntity(4000,"此用户名已存在，不允许注册",null);
        }
        user.setScore(50);
        user.setAttentionCount(0);
        userMapper.insert(user);
        user.setLoginPwd("");
        return new RespEntity(2002,"注册成功",user);
    }


//    @RequestMapping(value="/my/hello",method = RequestMethod.GET)
    @GetMapping(value = "/my/hello",produces = "application/json")
    @ResponseBody
    public String hello(){
//        return "hello";
        return """
                {
                "code": "200",
                "msg": "Hello World!",
                
                "data":
                {"name":"张三",
                "age":23}
                }
                """;
    }

    @GetMapping("/my/hello2")
    @ResponseBody
    public Map<String ,Object> hello2(){
        Map<String ,Object> map = new HashMap<>();
        map.put("code","2000");
        map.put("msg","Hello World!");
        map.put("data",Map.of("name","lisi","age",23));
        return map;
    }
    @GetMapping("/my/hello3")
    @ResponseBody
    public RespEntity hello3(){
        RespEntity tmp=new RespEntity(2000,"helloworld",Map.of("name","lisi","age",23));
        return tmp;
    }

    @GetMapping("/my/test0")
    @ResponseBody
    public RespEntity test0(@RequestParam (value = "name",defaultValue = "no name")String name , int age){
        System.out.println("name:"+name+" "+"age:"+age);
        RespEntity tmp=new RespEntity(2000,"success!",null);
        return tmp;
    }

    @GetMapping("/my/test02")
    @ResponseBody
    public RespEntity test02(
            @RequestParam (value = "page",defaultValue = "no page")int page ,
            @RequestParam(value = "find",defaultValue = "find no") String find){
//        System.out.println(name+age);
        return new RespEntity(2001,"successfully insert page!",
                "page:"+page+" "+"find:"+find);

    }

    @PostMapping("/my/test03")
    @ResponseBody
    public RespEntity test03(@RequestBody Map<String, Object> map) {
        String nickName = (String) map.get("nickName");
        String loginName = (String) map.get("loginName");
        String loginPwd = (String) map.get("loginPwd");

//        return new ResponseEntity(map, HttpStatus.OK);
        return new RespEntity(2000,
                "sign up!",
                nickName+" "+loginName+" "+loginPwd);
    }

    @PostMapping("/my/test04")
    @ResponseBody
    public RespEntity test04(@RequestBody UserDto user) {
        return new RespEntity(2000, "注册成功",
                 user.getNickName() + user.getLoginName() + user.getLoginPwd());
    }

    @PostMapping("/my/test05")
    @ResponseBody
    public RespEntity test05(String title, String content, MultipartFile imgFile) throws IOException {
        System.out.println("title:"+title+" "+"content:"+content+" imgFile:"+imgFile.getOriginalFilename());
        // sava img
        if(!imgFile.isEmpty()){
            imgFile.transferTo(new File("F:/"+imgFile.getOriginalFilename()));
        }
        return new RespEntity(2000,"test05 success!",
                "title:"+title+" "+"content:"+content+" imgFile:"+imgFile.getOriginalFilename());
    }


}
