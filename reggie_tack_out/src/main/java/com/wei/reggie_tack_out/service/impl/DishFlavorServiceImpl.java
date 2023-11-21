package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.entity.DishFlavor;
import com.wei.reggie_tack_out.mapper.DishFlavorMapper;
import com.wei.reggie_tack_out.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
