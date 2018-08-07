package com.maxu.crud.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.pagehelper.PageInfo;
import com.maxu.crud.bean.Employee;

/**
 * 使用Spring测试模块提供的测试请求功能，测试curd请求的正确性
 * Spring4测试的时候，需要servlet3.0的支持
 * @author MaXU
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
    "classpath:spring-mvc.xml"})
public class MvcTest {
  // 传入springmvc的ioc
  @Autowired
  WebApplicationContext context;

  // 虚拟的mvc请求，获取处理结果
  MockMvc mockMvc;
  @org.junit.Before
  public void initMokcMvc() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }
  @Test
  //模拟请求拿到返回值
  public void testPage() throws Exception {
   MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "1"))
        .andReturn();
    
   //请求成功后，请求域中会有pageInfo,我们可以取出pageInfo进行验证
    MockHttpServletRequest request = result.getRequest();
    PageInfo<Employee> pi = (PageInfo<Employee>)request.getAttribute("pageInfo");
    System.out.println("当前页码："+pi.getPageNum());
    System.out.println("总页码："+pi.getPages());
    System.out.println("总记录数："+pi.getTotal());
    System.out.print("在页面需要连续显示的页码：");
    int[] nums = pi.getNavigatepageNums();
    for(int i :nums) {
      System.out.println("  "+i);
    }
    
    //获取员工数据
    List<Employee> list = pi.getList();
    for (Employee e:list) {
      System.out.println("Id:"+e.getEmpId()+" name:"+e.getEmName()+" email"+e.getEmail()+" departmentName:"+e.getDepartment().getDeptName());
    }
  }

}
