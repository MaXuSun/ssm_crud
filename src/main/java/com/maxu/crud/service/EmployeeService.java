package com.maxu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxu.crud.bean.Employee;
import com.maxu.crud.dao.EmployeeMapper;

@Service
public class EmployeeService {
  @Autowired
  EmployeeMapper employeeMapper;

  /**
   * 查询所有员工
   * @return
   */
  public List<Employee> getAll() {
    
    return employeeMapper.selectByExampleWithDept(null);
  }

  /**
   * 员工保存
   * @param employee
   */
  public void saveEmp(Employee employee) {
    employeeMapper.insertSelective(employee);
  }
  
}
