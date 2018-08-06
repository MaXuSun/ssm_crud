package com.maxu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxu.crud.bean.Employee;
import com.maxu.crud.bean.EmployeeExample;
import com.maxu.crud.bean.EmployeeExample.Criteria;
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

  /**
   * 检验用户名是否可用
   * @param empName
   * @return true:代表当前姓名可用     false:不可用
   */
  public boolean checkUser(String emName) {
    //byExample是按照条件
    EmployeeExample example = new EmployeeExample();
    Criteria criteria = example.createCriteria();
    criteria.andEmNameEqualTo(emName);
    long count = employeeMapper.countByExample(example);
    return count == 0;
  }
  
}
