package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id获取菜品
     * @return
     */
    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from sky_take_out.dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据ids菜品id集合删除菜品
     * @param ids
     */
    void deteleByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    @Select("select * from sky_take_out.dish where category_id = #{categoryId}")
    List<Dish> getListByCategoryId(Long categoryId);

    @Select("select a.* from sky_take_out.dish a left join sky_take_out.setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(long setmealId);

    List<Dish> list(Dish dish);
}
