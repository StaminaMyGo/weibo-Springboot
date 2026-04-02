package com.wei.it.weibo.service.impl;
import com.wei.it.weibo.mapper.UserMapper;
import com.wei.it.weibo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.wei.it.weibo.entity.User;

@Service
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {

}

