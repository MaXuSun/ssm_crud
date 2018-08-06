package com.maxu.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
   * 检查用户名是否可用
   * @param empName
   * @return
   */
  @RequestMapping(value="/checkuser",method=RequestMethod.POST)
  @ResponseBody
  public Msg checkuse(@RequestParam("empName") String empName) {
    //先判断用户名是否是合法的（可以放在前端，也可以放在后端）
    String regx = "(^[A-Za-z0-9]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)";
    if(!empName.matches(regx)) {
      return Msg.fail().add("va_msg", "用户名必须是2-5位中文或者6-16位英文和数字组合");
    }
   
    //数据库用户名重复校验
    boolean b = employeeService.checkUser(empName);
    if(b) {
      return Msg.success().add("va_msg", "用户名可用");
    }else {
      return Msg.fail().add("va_msg", "用户名重复,不可用");
    }
  }

  /**
   * 员工保存
   * 1.支持JSP#)#校验
   * 2.导入hibernate validator
   * 
   * 
   * @return
   */
  @RequestMapping(value="/emp",method=RequestMethod.POST)
  @ResponseBody
  public Msg saveEmp(@Valid Employee employee,BindingResult result) {
    if(result.hasErrors()) {
      //校验失败，返回失败，在模态框中显示校验失败的错误信息
      Map<String, Object> map = new HashMap<String, Object>();
      List<FieldError> errors =  result.getFieldErrors();
      for(FieldError fieldError:errors) {
        System.out.println("错误字段名："+fieldError.getField());
        System.out.println("错误字段信息："+fieldError.getDefaultMessage());
        map.put(fieldError.getField(),fieldError.getDefaultMessage());
      }
      return Msg.fail().add("errorFields", map);
    }else {
      employeeService.saveEmp(employee);
      return Msg.success();
    }
  }
  
  
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
