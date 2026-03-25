package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {
    @Select("select setmeal_id from setmeal_dish where id = #{id}")
    Long querySetmealId(Long id);
}
