package com.wei.reggie_tack_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.entity.Employee;
import com.wei.reggie_tack_out.mapper.EmployeeMapper;
import com.wei.reggie_tack_out.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    /* 登录
    * 1. 将页面提交过来的密码进行 md5 加密
    * 2. 将页面传递过来的 用户名 在数据库中查找
    * 3. 如果数据库中没有结果则返回错误信息
    * 4. 数据库中查找到了用户名，则进行密码验证，密码不一致则返回错误信息
    * 5. 查看员工状态，如果为 禁用状态，则返回员工禁用的结果
    * 6. 登录成功，把员工信息存入 session 中并返回登录成功的结果
    * */
    @Override
    public Result<Employee> login(HttpServletRequest request, Employee employee) {
        //1. 将页面提交过来的密码进行 md5 加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2. 将页面传递过来的 用户名 在数据库中查找
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3. 如果数据库中没有结果则返回错误信息
        if (emp == null) {
            return Result.error("查无此人");
        }
        //4. 数据库中查找到了用户名，则进行密码验证，密码不一致则返回错误信息
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        //5. 查看员工状态，如果为 禁用状态，则返回员工禁用的结果,0表示禁用，1表示可用
        if (emp.getStatus() == 0) {
            return Result.error("您的账号被禁用，暂无法登录");
        }
        //6. 登录成功，把员工信息存入 session 中并返回登录成功的结果
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /* 退出登录 */
    @Override
    public Result<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }
}
