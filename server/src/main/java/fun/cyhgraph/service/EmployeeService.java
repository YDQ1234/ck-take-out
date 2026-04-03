package fun.cyhgraph.service;

import fun.cyhgraph.dto.EmployeeDTO;
import fun.cyhgraph.dto.EmployeeFixPwdDTO;
import fun.cyhgraph.dto.EmployeeLoginDTO;
import fun.cyhgraph.dto.PageDTO;
import fun.cyhgraph.entity.Employee;
import fun.cyhgraph.result.PageResult;
import fun.cyhgraph.result.Result;

public interface EmployeeService {
    Employee getEmployeeById(Integer id);

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void register(EmployeeLoginDTO employeeLoginDTO);

    PageResult employeePageList(PageDTO pageDTO);

    void update(EmployeeDTO employeeDTO);

    void delete(Integer id);

    void onOff(Integer id);

    void addEmployee(EmployeeDTO employeeDTO);

    void fixPwd(EmployeeFixPwdDTO employeeFixPwdDTO);

    Result<String> sendMsg(EmployeeLoginDTO employeeLoginDTO);

    Employee loginByPhone(EmployeeLoginDTO dto);
}
