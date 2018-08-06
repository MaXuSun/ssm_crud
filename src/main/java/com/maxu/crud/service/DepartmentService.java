package com.maxu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxu.crud.bean.Department;
import com.maxu.crud.dao.DepartmentMapper;

@Service
public class DepartmentService {

  @Autowired
  private DepartmentMapper departmentMapper;
  
  public List<Department> getDepts() {
    
    return departmentMapper.selectByExample(null);
  }

}
