package com.home.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.home.reggie.dto.DishDto;
import com.home.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品 + 同时插入菜品对应的口味数据，需要操作 dish、dish_flavor 两张表
    void saveWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);
}
