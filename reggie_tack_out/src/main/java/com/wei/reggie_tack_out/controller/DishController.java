package com.wei.reggie_tack_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.reggie_tack_out.common.CustomException;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.dto.DishDto;
import com.wei.reggie_tack_out.entity.Category;
import com.wei.reggie_tack_out.entity.Dish;
import com.wei.reggie_tack_out.entity.DishFlavor;
import com.wei.reggie_tack_out.service.CategoryService;
import com.wei.reggie_tack_out.service.DishFlavorService;
import com.wei.reggie_tack_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /* 添加菜品 */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        log.info("接收到的数据为：{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功");
    }

    /* 菜品分页 */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        // 1. 构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 2. 构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 3. 添加排序条件
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //4. 执行分页查询
        dishService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /* 根据 id 查询菜品 */
    @GetMapping("/{id}")
    public Result<DishDto> getByIdWithFlavor(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        log.info("查询到的数据为：{}", dishDto);
        return Result.success(dishDto);
    }
    /* 修改菜品 */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        log.info("接收到的数据为：{}", dishDto);
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改菜品成功");
    }

    /* 查询菜品数据 */
    @GetMapping("/list")
    public Result<List<DishDto>> get(Dish dish) {
        //条件查询器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //根据传进来的categoryId查询
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //只查询状态为1的菜品（在售菜品）
        queryWrapper.eq(Dish::getStatus, 1);
        //简单排下序，其实也没啥太大作用
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //获取查询到的结果作为返回值
        List<Dish> list = dishService.list(queryWrapper);
        log.info("查询到的菜品信息list:{}",list);
        //item就是list中的每一条数据，相当于遍历了
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            //创建一个dishDto对象
            DishDto dishDto = new DishDto();
            //将item的属性全都copy到dishDto里
            BeanUtils.copyProperties(item, dishDto);
            //由于dish表中没有categoryName属性，只存了categoryId
            Long categoryId = item.getCategoryId();
            //所以我们要根据categoryId查询对应的category
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                //然后取出categoryName，赋值给dishDto
                dishDto.setCategoryName(category.getName());
            }
            //然后获取一下菜品id，根据菜品id去dishFlavor表中查询对应的口味，并赋值给dishDto
            Long itemId = item.getId();
            //条件构造器
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            //条件就是菜品id
            lambdaQueryWrapper.eq(itemId != null, DishFlavor::getDishId, itemId);
            //根据菜品id，查询到菜品口味
            List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
            //赋给dishDto的对应属性
            dishDto.setFlavors(flavors);
            //并将dishDto作为结果返回
            return dishDto;
            //将所有返回结果收集起来，封装成List
        }).collect(Collectors.toList());
        return Result.success(dishDtoList);
    }

    /* 起售 与 停售 */
//    @PostMapping("/status/{status}")
//    public Result<String> updateStatusSales(@PathVariable Integer status, Long ids) {
//        log.info("status:{}, ids:{}", status, ids);
//        //1. 根据 id 查询菜品
//        Dish dish = dishService.getById(ids);
//        //2. 如果查到的数据不为空，根据前端传递过来的 status 修改菜品状态, status == 1为起售， status == 0 为停售
//        if (dish != null) {
//            dish.setStatus(status);
//            dishService.updateById(dish);
//            return Result.success("菜品状态修改成功");
//        }
//        return Result.success("系统繁忙，请稍后再试");
//    }

    /* 起售与停售 || 批量起售与停售 */
    @PostMapping("/status/{status}")
    public Result<String> updateStatusSales(@PathVariable Integer status, @RequestParam List<Long> ids) {
        log.info("status:{}, ids:{}", status, ids);
        //1. 根据 id 查询菜品
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids != null, Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, status);
        dishService.update(updateWrapper);
        return Result.success("菜品状态修改成功");
    }

    /* 删除 || 批量删除 */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("删除的ids：{}", ids);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        // 起售状态的菜品不可以删除（status == 1 起售，0 停售）
        queryWrapper.eq(Dish::getStatus, 1);
        long count = dishService.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("删除列表中存在启售状态商品，无法删除");
        }
        dishService.removeByIds(ids);
        return Result.success("删除成功");
    }
}
