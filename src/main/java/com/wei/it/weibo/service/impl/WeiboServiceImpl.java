package com.wei.it.weibo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.it.weibo.entity.User;
import com.wei.it.weibo.entity.Weibo;
import com.wei.it.weibo.mapper.WeiboMapper;
import com.wei.it.weibo.service.UserService;
import com.wei.it.weibo.service.WeiboService;
import com.wei.it.weibo.web.dto.WeiboDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeiboServiceImpl extends ServiceImpl<WeiboMapper, Weibo> implements WeiboService {

    @Autowired
    private UserService userService;

    @Override
    public IPage<WeiboDto> pageWithUser(Page<Weibo> page) {
        // 分页查询微博
        IPage<Weibo> weiboPage = this.page(page);
        List<Weibo> records = weiboPage.getRecords();
        if (records.isEmpty()) {
            // 返回空的分页结果，转换为 WeiboDto 分页
            Page<WeiboDto> dtoPage = new Page<>(weiboPage.getCurrent(), weiboPage.getSize(), weiboPage.getTotal());
            return dtoPage;
        }

        // 收集所有 userId
        List<Integer> userIds = records.stream().map(Weibo::getUserId).distinct().collect(Collectors.toList());
        // 批量查询用户
        List<User> users = userService.listByIds(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

        // 转换为 DTO 并填充 user
        List<WeiboDto> dtoList = records.stream().map(weibo -> {
            WeiboDto dto = new WeiboDto();
            BeanUtils.copyProperties(weibo, dto);
            User user = userMap.get(weibo.getUserId());
            dto.setUser(user);
            return dto;
        }).collect(Collectors.toList());

        // 构建分页结果
        Page<WeiboDto> dtoPage = new Page<>(weiboPage.getCurrent(), weiboPage.getSize(), weiboPage.getTotal());
        dtoPage.setRecords(dtoList);
        return dtoPage;
    }
}