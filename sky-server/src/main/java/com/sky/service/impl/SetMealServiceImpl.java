package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.exception.SetmealEnableFailedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        Long id = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();

        if(setmealDishes != null && setmealDishes.size() > 0) {

            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
            setMealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> setmealVOS = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(setmealVOS.getTotal(), setmealVOS.getResult());
    }

    @Override
    public void updateMealStatus(Integer status, long id) {
        if(status == StatusConstant.ENABLE){
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if(dishList != null && dishList.size() > 0){
                dishList.forEach(dish -> {
                    if(StatusConstant.DISABLE == dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
        setmealMapper.update(setmeal);
    }

    @Override
    public SetmealVO getByIdWithDishes(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        List<SetmealDish> dishes = setMealDishMapper.getDishesById(id);

        setmealVO.setSetmealDishes(dishes);

        return setmealVO;

    }

    @Override
    public void updateSetMeal(SetmealDTO setmealDTO) {
        //更新setmeal行
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //根据id查找相关所有setmealdish行删除，然后写入新的数据
        setMealDishMapper.deleteBySetMealId(setmealDTO.getId());

        //批量写入对应的setmealdish
        List<SetmealDish> dishes = setmealDTO.getSetmealDishes();
        if(dishes != null && dishes.size() > 0) {
            dishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
            });
            setMealDishMapper.insertBatch(dishes);
        }

    }

    @Override
    public void deleteByIds(List<Long> ids) {
        //同样需要检查是否在售，在售抛出异常处理
        setmealMapper.getByIds(ids).forEach(setmeal -> {
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        //删除套餐
        setmealMapper.deleteByIds(ids);

        //删除套餐对应套餐菜品数据
        setMealDishMapper.deleteBySetMealIds(ids);

    }


}
