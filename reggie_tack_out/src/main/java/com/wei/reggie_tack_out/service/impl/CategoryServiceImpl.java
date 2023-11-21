package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.common.CustomException;
import com.wei.reggie_tack_out.entity.Category;
import com.wei.reggie_tack_out.entity.Dish;
import com.wei.reggie_tack_out.entity.Setmeal;
import com.wei.reggie_tack_out.mapper.CategoryMapper;
import com.wei.reggie_tack_out.service.CategoryService;
import com.wei.reggie_tack_out.service.DishService;
import com.wei.reggie_tack_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /*
    *  我们需要在删除数据之前，根据id值，去Dish表和Setmeal表中查询是否关联了数据
    *  如果存在关联数据，则不能删除，并抛一个异常
    *  如果不存在关联数据（也就是查询到的数据条数为0），正常删除即可
    */
    @Override
    public void remove(Long id) {
        // 1. 构造 菜品 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2. 添加 Dish 查询条件，根据分类 id 进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        // 方便 Debug 用
        long count1 = dishService.count(dishLambdaQueryWrapper);
        log.info("dish查询条件，查询到的条数为{}", count1);
        //3. 如果count1 结果大于0，说明关联了数据，不能直接删除
        if (count1 > 0) {
            // 4. 已关联 菜品，抛出一个异常
            throw  new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 5. 构造 套餐 条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //6. 添加 Setmeal 查询条件，根据分类 id 进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        // 方便 Debug 用
        Long count2 = setmealService.count(setmealLambdaQueryWrapper);
        log.info("Setmeal查询条件，查询到的条数为{}", count2);
        //7. 如果count2 结果大于0，说明关联了数据，不能直接删除
        if (count2 > 0) {
            // 8. 已关联 套餐，抛出一个异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //9. 正常删除
        super.removeById(id);
    }
}
