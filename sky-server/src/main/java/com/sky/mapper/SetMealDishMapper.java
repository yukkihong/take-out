package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealIdByDishIds(List<Long> ids);


    /**
     * 插入套餐对应的菜品列表信息
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);


    /**
     * 通过setmealId查询菜品
     * @param setmealId
     * @return
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id =#{setmealId}")
    List<SetmealDish> getDishesById(Long setmealId);

    /**
     * 根据setMealId删除
     * @param setMealId
     */
    @Delete("delete from sky_take_out.setmeal_dish where setmeal_id =#{setMealId}")
    void deleteBySetMealId(Long setMealId);

    /**
     * 根据setmealIds批量删除套餐菜品数据
     * @param setMealIds
     */
    void deleteBySetMealIds(List<Long> setMealIds);
}
