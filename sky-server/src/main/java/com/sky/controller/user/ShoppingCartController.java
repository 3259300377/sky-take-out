package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.beancontext.BeanContext;
import java.util.List;

@RequestMapping("/user/shoppingCart")
@RestController
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加商品至购物车，商品数据为：{}",shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> queryShoppingCart(){
        log.info("查看购物车商品，并回显在客户端");
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartService.queryShoppingCart(userId);
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result cleanShoppingCart(){
        log.info("正在清空购物车........");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
}
