package com.project.RedisDemo.Service;

import com.project.RedisDemo.model.Employee;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EmployeeService {

    public void redisSend(String employeName);
    public Set<String> getRedisData();

    public void sendToKafka(Employee employee);
    public void getEmployeeKafka();
}
