package com.home.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.home.reggie.common.R;
import com.home.reggie.entity.Employee;
import com.home.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request 用于获取session
     * @param employee 接收前端传回来的JSON数据：name && password，自动封装到 employee 中
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
//        1. 将页面提交的密码 password 进行 md5 加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//        2. 根据页面提交的用户名 username 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

//        3. 如果没有查询到结果，返回登录失败结果
        if (null == emp) {
            return R.error("登录失败");
        }

//        4. 密码对比，如果不一致返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

//        5. 查看员工状态，如果为已经禁用状态，返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

//        6. 登录成功，将员工 id 存入 Session 并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出方法
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        // 清理 Session 中保存的当前员工的 ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    public R<String> save(@RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        return null;
    }
}















