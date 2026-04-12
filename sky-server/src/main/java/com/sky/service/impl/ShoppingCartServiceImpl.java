package com.sky.service.impl;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前菜品或套餐是否在购物车中
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = shoppingCartMapper.selectByDishOrSetmealId(shoppingCartDTO,userId);
        //若在，则数量加一
        if(shoppingCart != null){
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.update(shoppingCart);
        }
        //若不在，则插入数据
        //判断添加至购物车的为菜品还是套餐
        else {
            ShoppingCart shoppingCart1 = new ShoppingCart();
            shoppingCart1.setUserId(BaseContext.getCurrentId());
            shoppingCart1.setNumber(1);
            shoppingCart1.setCreateTime(LocalDateTime.now());
            //若为菜品
            if(shoppingCartDTO.getDishId() != null){
                Dish dish = dishMapper.selectById(shoppingCartDTO.getDishId());
                BeanUtils.copyProperties(shoppingCartDTO,shoppingCart1);
                shoppingCart1.setAmount(dish.getPrice());
                shoppingCart1.setName(dish.getName());
                shoppingCart1.setImage(dish.getImage());
                shoppingCartMapper.add(shoppingCart1);
            }
            //若为套餐
            else{
                shoppingCart1.setSetmealId(shoppingCartDTO.getSetmealId());
                Setmeal setmeal = setmealMapper.selectById(shoppingCart1.getSetmealId());
                shoppingCart1.setName(setmeal.getName());
                shoppingCart1.setImage(setmeal.getImage());
                shoppingCart1.setAmount(setmeal.getPrice());
                shoppingCartMapper.add(shoppingCart1);
            }
        }
    }

    @Override
    public List<ShoppingCart> queryShoppingCart(Long userId) {
        List<ShoppingCart> list = shoppingCartMapper.queryShoppingCart(userId);
        return list;
    }

    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.cleanShoppingCart(userId);
    }

}
