package com.wei.reggie_tack_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.entity.Employee;
import com.wei.reggie_tack_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
    * 员工登录
    *     这里需要给 login 一个参数 （HttpServletRequest request)，因为登录成功之后需要把用户信息存在 session 中，
    *  后续要使用该用户的信息就可以通过 request 对象 get 一个 session 。
    *     加 @RequestBody 注解是因为前端在发送请求的时候还带了两个参数，并且是以 Json 字符串的形式传递过来的，
    *   所以需要加一个注解
    * */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.login(request, employee);
    }

    /* 员工退出 */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        return employeeService.loginOut(request);
    }

    /* 员工添加 */
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //1. 设置初始密码为123456, 但不是明文存入数据库，使用 md5 加密之后再存入数据库中
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //保存到数据库中
        employeeService.save(employee);
        return Result.success("添加员工成功");
    }

    /* 员工分页 */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        // 构造分页构造器
        Page pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件（当我们没有输入name时，就相当于查询所有了）
        queryWrapper.like(!(name == null || "".equals(name)), Employee::getName, name);
        //并对查询的结果进行降序排序，根据更新时间
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /* 根据 id 修改员工信息 */
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("员工信息{}", employee.toString());
        // 在这里获取线程 id
        Long id = Thread.currentThread().getId();
        log.info("doFilter的线程id为：{}", id);
        employeeService.updateById(employee);
        return Result.success("修改信息成功");
    }

    /* 根据 id 查询员工信息，并把信息以 json 形式响应给页面 */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据 id 查询员工信息");
        Employee emp = employeeService.getById(id);
        return Result.success(emp);
    }

}
