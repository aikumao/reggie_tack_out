package com.wei.reggie_tack_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.reggie_tack_out.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
