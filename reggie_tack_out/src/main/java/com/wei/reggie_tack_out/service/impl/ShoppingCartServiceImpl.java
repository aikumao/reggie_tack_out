package com.wei.reggie_tack_out.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.entity.ShoppingCart;
import com.wei.reggie_tack_out.mapper.ShoppingCartMapper;
import com.wei.reggie_tack_out.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
