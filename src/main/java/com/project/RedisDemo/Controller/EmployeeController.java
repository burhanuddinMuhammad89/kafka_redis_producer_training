package com.project.RedisDemo.Controller;
import java.util.Set;

import com.project.RedisDemo.Service.EmployeeService;
import com.project.RedisDemo.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "/requestEmp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void newEmployee(@RequestBody Employee newEmployee) {
       employeeService.redisSend(newEmployee.getName());
    }

    @GetMapping("/employees")
    Set<String> getEmployee(@RequestBody Employee newEmployee) {
        return employeeService.getRedisData();
    }

    @RequestMapping(value = "/sendToKafka", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void newEmployeeKafka(@RequestBody Employee newEmployee) {
        employeeService.sendToKafka(newEmployee);
    }

    @GetMapping("/getEmployeeKafka")
    void getEmployeeKafka(@RequestBody Employee newEmployee) {
        employeeService.getEmployeeKafka();
    }

}
