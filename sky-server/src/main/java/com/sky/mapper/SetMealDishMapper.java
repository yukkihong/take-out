package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealIdByDishIds(List<Long> ids);


    /**
     * 插入套餐对应的菜品列表信息
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
