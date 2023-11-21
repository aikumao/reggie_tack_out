package com.wei.reggie_tack_out.dto;

import com.wei.reggie_tack_out.entity.Setmeal;
import com.wei.reggie_tack_out.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;  //具体套餐菜品

    private String categoryName;  // 套餐分类名
}
