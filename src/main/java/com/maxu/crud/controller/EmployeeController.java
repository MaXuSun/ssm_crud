package com.maxu.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.maxu.crud.bean.Employee;
import com.maxu.crud.bean.Msg;
import com.maxu.crud.service.EmployeeService;

/**
 * 处理员工CRUD请求
 * 
 * @author MaXU
 *
 */
@Controller
public class EmployeeController {

  @Autowired
  EmployeeService employeeService;

  /**
   * 需要导入jsckon包
   * 
   * @param pn
   * @return
   */
  @RequestMapping("/emps")
  @ResponseBody // 可以将返回值直接变成json数据
  public Msg getEmpsWithJson(
      @RequestParam(value = "pn", defaultValue = "1") Integer pn) {
    // 这不是分页查询
    // 引入PageHelper分页插件
    // 在查询之前只需要调用,传入页码，以及分页每页大小
    PageHelper.startPage(pn, 5);
    // startPage后面紧跟的这个查询就是分页查询
    List<Employee> emps = employeeService.getAll();
    // 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
    // 封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
    PageInfo<Employee> page = new PageInfo<Employee>(emps, 5);

    return Msg.success().add("pageInfo", page);
  }

  /**
   * 查询员工数据（分页查询） 这个方法是通过主页跳转到/emps然后再控制器再跳转到list页面
   * 下面注销（注销成普通方法就行）这个方法直接返回json数据
   * 
   * @return
   */
  // @RequestMapping("/emps")
  public String getEmps(
      @RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
    // 这不是分页查询
    // 引入PageHelper分页插件
    // 在查询之前只需要调用,传入页码，以及分页每页大小
    PageHelper.startPage(pn, 5);
    // startPage后面紧跟的这个查询就是分页查询
    List<Employee> emps = employeeService.getAll();
    // 使用pageInfo包装查询后的结果，只需要将pageInfo交给页面就行了
    // 封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
    PageInfo<Employee> page = new PageInfo<Employee>(emps, 5);
    model.addAttribute("pageInfo", page);

    return "list";
  }
}
