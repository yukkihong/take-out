package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.v3.oas.annotations.servers.Server;

import java.util.List;


public interface SetMealService {
    void insert(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateMealStatus(Integer status, long id);

    SetmealVO  getByIdWithDishes(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void updateSetMeal(SetmealDTO setmealDTO);
    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
    /**
     * 批量根据ids批量删除套餐
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);
}
