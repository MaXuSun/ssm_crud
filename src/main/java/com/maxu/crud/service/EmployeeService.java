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

  /**
   * 按照员工id查询员工
   * @param id
   * @return
   */
  public Employee getEmp(Integer id) {
    return employeeMapper.selectByPrimaryKey(id);
  }

  /**
   * 员工更新
   * @param employee
   */
  public void updateEmp(Employee employee) {
    employeeMapper.updateByPrimaryKeySelective(employee);
  }

  /**
   * 员工单个删除
   * @param id
   */
  public void deleteEmp(Integer id) {
    employeeMapper.deleteByPrimaryKey(id);
  }

  /**
   * 员工批量删除
   * @param ids
   */
  public void deleteBatch(List<Integer> ids) {
    EmployeeExample example = new EmployeeExample();
    Criteria criteria = example.createCriteria();
    criteria.andEmpIdIn(ids);
    employeeMapper.deleteByExample(example);
  }
  
}
