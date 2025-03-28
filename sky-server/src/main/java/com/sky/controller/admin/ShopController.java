package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    @Autowired
    RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    @CacheEvict(cacheNames = KEY ,allEntries = true)
    public Result setStatus(@PathVariable("status") Integer status) {
        log.info("设置店铺的营业状态为：{}", status==1?"营业中":"打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }


    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    @Cacheable(cacheNames = KEY )
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态为：{}",  status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
