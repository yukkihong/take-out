package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 插入新套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 套餐分页分类查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal getById(Long id);


    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from sky_take_out.setmeal_dish sd left join sky_take_out.dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据ids获取对应的套餐数据
     * @param ids
     * @return
     */
    List<Setmeal> getByIds(List<Long> ids);

    /**
     * 根据ids删除对应的套餐数据
     * @param ids
     */
    void deleteByIds(List<Long> ids);
}
