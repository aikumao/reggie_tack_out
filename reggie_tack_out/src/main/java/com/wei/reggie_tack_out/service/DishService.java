package com.wei.reggie_tack_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.reggie_tack_out.dto.DishDto;
import com.wei.reggie_tack_out.entity.Dish;

public interface DishService extends IService<Dish> {
    /* 添加菜品 */
    void saveWithFlavor(DishDto dishDto);

    /* 查询菜品 */
    DishDto getByIdWithFlavor(Long id);

    /* 修改菜品 */
    void updateWithFlavor(DishDto dishDto);
}
