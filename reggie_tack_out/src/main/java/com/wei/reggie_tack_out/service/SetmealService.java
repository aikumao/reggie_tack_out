package com.wei.reggie_tack_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.reggie_tack_out.dto.SetmealDto;
import com.wei.reggie_tack_out.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /* 套餐添加 */
    void saveWithDish(SetmealDto setmealDto);

    /* 删除添加 */
    void removeWithDish(List<Long> ids);
}
