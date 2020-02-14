package com.gepardec.mega.service.impl;

import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.aplication.security.RolesAllowed;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.zep.exception.ZepServiceException;
import com.gepardec.mega.zep.service.ZepServiceImpl;
import com.google.common.collect.Iterables;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeServiceImpl implements EmployeeService {

    @Inject
    Logger logger;

    @Inject
    ZepServiceImpl zepService;

    @Inject
    ManagedExecutor managedExecutor;

    // TODO: find better way to unittest this, at the moment we use setter injection of ConfigProperty @runtime and call setter @testing
    Integer employeeUpdateParallelExecutions;

    @Inject
    public void setEmployeeUpdateParallelExecutions(@ConfigProperty(name = "mega.employee.update.parallel.executions", defaultValue = "10") Integer employeeUpdateParallelExecutions) {
        this.employeeUpdateParallelExecutions = employeeUpdateParallelExecutions;
    }

    @Override
    public Employee getEmployee(String userId) {
        return zepService.getEmployee(userId);
    }

    @Override
    public List<Employee> getAllActiveEmployees() {
        return zepService.getActiveEmployees();
    }

    @Override
    public void updateEmployeeReleaseDate(String userId, String releaseDate) {
        zepService.updateEmployeesReleaseDate(userId, releaseDate);
    }

    @Override
    public List<String> updateEmployeesReleaseDate(List<Employee> employees) {
        final List<String> failedUserIds = new LinkedList<>();

        /*
        workaround until we can configure the managed executor in quarkus environment.
        at the moment, employees list is partitioned by 10 and therefore 10 requests to zep are started at a time.
         */
        Iterables.partition(Optional.ofNullable(employees).orElseThrow(() -> new ZepServiceException("no employees to update")), employeeUpdateParallelExecutions).forEach((partition) -> {
            try {
                CompletableFuture.allOf(partition.stream().map((employee) -> CompletableFuture.runAsync(() -> updateEmployeeReleaseDate(employee.getUserId(), employee.getReleaseDate()), managedExecutor)
                        .handle((aVoid, throwable) -> {
                            Optional.ofNullable(throwable).ifPresent((t) -> failedUserIds.add(employee.getUserId()));
                            return null;
                        })).toArray(CompletableFuture[]::new)).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("error updating employees", e);
                failedUserIds.addAll(partition.stream().map(Employee::getUserId).collect(Collectors.toList()));
            }
        });

        return failedUserIds;
    }
}
