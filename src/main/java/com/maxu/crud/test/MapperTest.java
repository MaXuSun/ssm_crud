package com.maxu.crud.test;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maxu.crud.bean.Employee;
import com.maxu.crud.dao.DepartmentMapper;
import com.maxu.crud.dao.EmployeeMapper;

/**
 * 测试dao层的工作
 * 
 * @author MaXU 推荐使用Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要组件 1.导入SpringTest模块
 *         2.@ContextConfiguration指定Spring配置文件的位置 3.直接autowired要使用的组件
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {
  @Autowired
  DepartmentMapper departmentMapper;
  @Autowired
  EmployeeMapper employeeMapper;
  @Autowired
  SqlSession session;
  /**
   * 测试DepartmentMapper
   */
  @Test
  public void testCRUD() {

    System.out.println(departmentMapper);

    // 1.插入删除部门
    // departmentMapper.insertSelective(new Department(1,"开发部"));
    // departmentMapper.deleteByPrimaryKey(1);

    // 2.生成员工数据，测试员工插入
    // employeeMapper.insertSelective(new
    // Employee(1,"Jerry","M","Jerry@maxu.com",1));
    // 3.批量插入多个员工名；使用可以执行批量操作的SqlSession

     EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
     for (int i = 2; i < 1000; i++) {
     mapper.insertSelective(new Employee(null, i+"", "M", i+"@qq.com",1));
     }
  }
}
