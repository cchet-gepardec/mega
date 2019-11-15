package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.monthendreport.MonthendReport;
import de.provantis.zep.MitarbeiterType;

import java.util.List;

public interface WorkerService {

    MitarbeiterType getEmployee(GoogleUser user);

    List<MitarbeiterType> getAllEmployees();

    Integer updateEmployee(MitarbeiterType employee);

    Integer updateEmployees(List<MitarbeiterType> employees);

    MonthendReport getMonthendReport(GoogleUser user);
}