package com.home.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.home.reggie.common.CustomException;
import com.home.reggie.entity.Category;
import com.home.reggie.entity.Dish;
import com.home.reggie.entity.Setmeal;
import com.home.reggie.mapper.CategoryMapper;
import com.home.reggie.service.CategoryService;
import com.home.reggie.service.DishService;
import com.home.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据 id 删除分类，删除之前需要进行判断，是否关联了其他库中的字段
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        // 添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        // 查询当前分类是否已经关联菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0) {  // 已经关联了菜品，需要抛出一个异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        // 查询当前分类是否已经关联了套餐，如果已经关联，抛出一个业务异常
        if (count2 > 0) {  // 已经关联套餐，需要抛出一个异常
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }

        // 正常删除分类
        super.removeById(id);
    }
}


















