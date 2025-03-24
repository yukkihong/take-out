package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api("套餐相关接口")
public class SetMealDishController {

    @Autowired
    SetMealService setMealService;

    @PutMapping
    @ApiOperation("修改套餐")
    public Result setMealDish(){
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增套餐")
    public Result setMealDish(@RequestBody SetmealDTO  setmealDTO){
        setMealService.insert(setmealDTO);
        return Result.success();
    }
}
