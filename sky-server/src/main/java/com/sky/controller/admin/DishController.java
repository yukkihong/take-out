package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Api("菜品相关接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);

        String key = "dish_" + dishDTO.getCategoryId();
        redisTemplate.delete(key);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询“{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("批量删除菜品:{}",ids);
        dishService.deleteBatch(ids);

        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> get(@PathVariable Long id){
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    @CacheEvict(cacheNames = "dishName",allEntries = true)
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        cleanCache("dish_*");

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售、停售")
    @CacheEvict(cacheNames = "dishName",key = "#id")
    public Result updateStatus(@PathVariable("status") Integer status,@RequestParam Long id){
        log.info("菜品起售、停售:{}",status);
        dishService.updateStatus(status, id);

        cleanCache("dish_*");

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(@RequestParam Long categoryId){
        log.info("根据分类id查询菜品:{}",categoryId);
        List<Dish> dishList = dishService.getByCategordId(categoryId);
        return Result.success(dishList);
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
