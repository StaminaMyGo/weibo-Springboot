package com.wei.it.weibo.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.it.weibo.entity.Weibo;
import com.wei.it.weibo.service.WeiboService;
import com.wei.it.weibo.web.dto.WeiboDto;
import com.wei.it.weibo.web.dto.RespEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/weibo")
public class WeiboController {

    @Autowired
    private WeiboService weiboService;

    // 分页查询微博（自动关联用户）
    @GetMapping("/page")
    public RespEntity page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Weibo> page = new Page<>(current, size);
        IPage<WeiboDto> dtoPage = weiboService.pageWithUser(page);
        return RespEntity.success(dtoPage);
    }

    // 根据ID查询单条微博（关联用户）
    @GetMapping("/{id}")
    public RespEntity getById(@PathVariable Integer id) {
        Weibo weibo = weiboService.getById(id);
        if (weibo == null) {
            return RespEntity.error(404, "微博不存在", null);
        }
        WeiboDto dto = new WeiboDto();
        BeanUtils.copyProperties(weibo, dto);
        // 查询关联的用户（实际建议在 service 中完成，此处简化演示）
        if (weibo.getUserId() != null) {
            // 需要注入 UserService，暂不扩展，保持与之前逻辑一致
            // 推荐使用 weiboService.getWeiboWithUser(id) 方法
        }
        return RespEntity.success(dto);
    }

    // 新增微博
    @PostMapping
    public RespEntity add(@RequestBody Weibo weibo) {
        boolean save = weiboService.save(weibo);
        if (save) {
            return RespEntity.success(true);
        } else {
            return RespEntity.error(500, "添加失败", null);
        }
    }

    // 更新微博
    @PutMapping
    public RespEntity update(@RequestBody Weibo weibo) {
        boolean update = weiboService.updateById(weibo);
        if (update) {
            return RespEntity.success(true);
        } else {
            return RespEntity.error(500, "更新失败", null);
        }
    }

    // 删除微博
    @DeleteMapping("/{id}")
    public RespEntity delete(@PathVariable Integer id) {
        boolean remove = weiboService.removeById(id);
        if (remove) {
            return  RespEntity.success(true);
        } else {
            return RespEntity.error(500, "删除失败", null);
        }
    }
}