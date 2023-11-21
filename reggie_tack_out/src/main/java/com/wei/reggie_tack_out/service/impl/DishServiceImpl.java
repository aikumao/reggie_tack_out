package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.dto.DishDto;
import com.wei.reggie_tack_out.entity.Dish;
import com.wei.reggie_tack_out.entity.DishFlavor;
import com.wei.reggie_tack_out.mapper.DishMapper;
import com.wei.reggie_tack_out.service.DishFlavorService;
import com.wei.reggie_tack_out.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /* 添加菜品 */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 将菜品保存到 dish 表
        this.save(dishDto);
        //获取dishId
        Long dishId = dishDto.getId();
        //将获取到的dishId赋值给dishFlavor的dishId属性
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dishId);
        }
        //同时将菜品口味数据保存到dish_flavor表
        dishFlavorService.saveBatch(flavors);
    }

    /* 查询菜品 */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 根据 id 查询菜品
        Dish dish = this.getById(id);
        // 创建 dto 对象
        DishDto dishDto = new DishDto();
        // 拷贝对象
        BeanUtils.copyProperties(dish, dishDto);
        // 构造条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        // 根据 dish_id 查询对应的菜品口味数据
        queryWrapper.eq(DishFlavor::getDishId, id);
        // 查询结果
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        // 赋值
        dishDto.setFlavors(dishFlavors);
        // 返回结果给前端
        return dishDto;
    }

    /* 修改菜品 */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 修改当前菜品数据（dish 表）
        this.updateById(dishDto);
        //下面是更新当前菜品的口味数据
        //条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //条件是当前菜品id
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        //将其删除掉
        dishFlavorService.remove(queryWrapper);
        //获取传入的新的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //这些口味数据还是没有dish_id，所以需要赋予其dishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //再重新加入到表中
        dishFlavorService.saveBatch(flavors);
    }

}
