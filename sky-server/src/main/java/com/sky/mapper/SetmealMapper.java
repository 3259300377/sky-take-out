package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @CacheEvict(cacheNames = "setmealCache", key = "#setmeal.categoryId")
    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);

    //分页查询
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal selectById(Long id);

    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @Delete("delete from setmeal where id = #{setmealId}")
    void delete(Long setmealId);

    @Select("select s.*,c.name categoryName from setmeal s left join category c on c.id = s.category_id where s.id = #{id}")
    SetmealVO getById(Long id);

    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @Update("update setmeal set status = #{status} where id = #{id}")
    void changeStatus(Integer status, Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
