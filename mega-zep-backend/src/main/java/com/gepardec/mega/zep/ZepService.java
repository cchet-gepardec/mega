package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;

import java.util.List;

public interface ZepService {

    Employee getEmployee(String userId);

    List<Employee> getEmployees();

    void updateEmployeesReleaseDate(String userId, String releaseDate);

    List<ProjectEntry> getProjectTimes(Employee employee);
}
