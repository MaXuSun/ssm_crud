package com.maxu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
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
   * 单个 批量删除二合一
   * 批量中用-连接
   * @param id
   * @return
   */
  @RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
  @ResponseBody
  public Msg deleteEmp(@PathVariable("ids")String ids) {
    //批量删除
    if(ids.contains("-")) {
      List<Integer> delIds = new ArrayList<Integer>();
      String[] strIds = ids.split("-");
      //组装id的集合
      for(String string:strIds) {
        delIds.add(Integer.parseInt(string));
      }
      employeeService.deleteBatch(delIds);
    }else {
      //单个删除
      employeeService.deleteEmp(Integer.parseInt(ids));
    }
   
    return Msg.success();
  }
  
  
  /**
   * 如果直接发送ajax=PUT请求
   * 封装的数据除了路径上带的数据全是null
   * 
   * 问题：
   * 请求体中有数据，
   * 但是Employee对象封装不上
   * 
   * 原因：
   * tomcat:
   *    1.将请求体中的数据，封装成一个map,
   *    2.request.getParameter("emName")就会从这个map中取值
   *    3.SpringMVC封装POJO对象的时候，
   *      会把POJO中每个属性值，request.getParamter("email");
   * AJAX发送PUT请求引发的血案
   *    PUT请求，请求体中的数据，request.getParamter("email")拿不到
   *    Tomcat一看是put请求就不会封装请求体中的数据为mao,只有POST形式的请求才封装成map形式
   *    
   *    我们要能支持直接发送PUT之类的请求还要封装请求体中的数据
   *    在web.xml配置过滤器HttpPutFormContentFilter即可解决
   *    它的作用：将请求体中的数据解析包装成一个map
   *    request被重新包装，request.getParameter()被重写，就会从自己封装的map中请求数据
   *    
   * 员工更新方法
   * @param employee
   * @return
   */
  @RequestMapping(value="/emp/{id}",method=RequestMethod.PUT)
  @ResponseBody
  public Msg saveEmp(Employee employee,@PathVariable("id")Integer id) {
    employee.setEmpId(id);
    //System.out.print("将要更新的员工数据："+employee);
    employeeService.updateEmp(employee);
    return Msg.success();
  }
  
  /**
   * 根据id查询员工
   * @param id
   * @return
   */
  @RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
  @ResponseBody
  public Msg getEmp(@PathVariable("id")Integer id) {
    
    Employee employee = employeeService.getEmp(id);
    return Msg.success().add("emp", employee);
  }
  
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
        //System.out.println("错误字段名："+fieldError.getField());
        //System.out.println("错误字段信息："+fieldError.getDefaultMessage());
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
