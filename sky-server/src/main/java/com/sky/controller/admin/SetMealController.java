package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api("套餐相关接口")
public class SetMealController {

    @Autowired
    SetMealService setMealService;

    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key="#setmealDTO.categoryId")
    public Result setMealDish(@RequestBody SetmealDTO  setmealDTO){
        setMealService.insert(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result getSetmealDishPage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询“{}",setmealPageQueryDTO);
        PageResult setmealVOPageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(setmealVOPageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    @CacheEvict(cacheNames = "setmealCache" ,allEntries = true)
    public Result updateMealStatus(@PathVariable("status") Integer status,@RequestParam Long id){
        log.info("套餐起售、停售：{}",status);
        setMealService.updateMealStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result getSetmealDishById(@PathVariable("id") Long id){
        log.info("根据id查询套餐：{}",id);
        SetmealVO byIdWithDishes = setMealService.getByIdWithDishes(id);
        return Result.success(byIdWithDishes);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache" ,allEntries = true)
    public Result updateSetmealDish(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache" ,allEntries = true)
    public Result deleteSetmealDish(@RequestParam List<Long> ids){
        log.info("批量删套餐:{}",ids);
        setMealService.deleteByIds(ids);
        return Result.success();
    }
}
