package com.wei.it.weibo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.it.weibo.entity.Weibo;
import com.wei.it.weibo.web.dto.WeiboDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface WeiboService extends IService<Weibo> {
    IPage<WeiboDto> pageWithUser(Page<Weibo> page);
}