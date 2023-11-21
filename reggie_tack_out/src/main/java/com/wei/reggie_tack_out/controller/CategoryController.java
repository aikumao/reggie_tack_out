package com.wei.reggie_tack_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.entity.Category;
import com.wei.reggie_tack_out.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /* 新增分类 */
    @PostMapping
    public Result<String> save(@RequestBody  Category category) {
        log.info("category{}", category);
        // 调用mybatisPlus 的添加方法
        categoryService.save(category);
        return Result.success("分类添加成功");
    }

    /* 分页 */
    @GetMapping("page")
    public Result<Page> page(int page, int pageSize) {
        // 构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件
        queryWrapper.orderByDesc(Category::getSort);
        // 分页查询
        categoryService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 删除分类
     * 在分类管理列表页面，可以对某个分类进行删除操作
     * 需要注意的是：当分类关联了菜品或者套餐时，此分类将不允许被删除
     * */

    @DeleteMapping
    private Result<String> delete(Long ids) {
        log.info("将被删除的id：{}", ids);
        categoryService.remove(ids);
        return Result.success("分类信息删除成功");
    }

    /* 修改分类 */
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息为{}", category);
        categoryService.updateById(category);
        return Result.success("修改分类信息成功");
    }

    /* 菜品列表显示（下拉框）*/
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        // 1.构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 2.添加条件，判断是否为菜品，（type == 1 为菜品， type == 2 为套餐）
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 3. 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        //4. 查询数据
        List<Category> list = categoryService.list(queryWrapper);
        //5. 返回数据
        return Result.success(list);
    }
}
