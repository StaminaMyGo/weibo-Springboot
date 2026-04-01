package com.wei.it.weibo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import org.apache.catalina.User;
import com.wei.it.weibo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
