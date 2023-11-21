package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.entity.OrderDetail;
import com.wei.reggie_tack_out.mapper.OrderDetailMapper;
import com.wei.reggie_tack_out.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}