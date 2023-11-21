package com.wei.reggie_tack_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.reggie_tack_out.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
