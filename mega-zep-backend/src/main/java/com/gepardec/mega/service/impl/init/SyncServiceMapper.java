package com.gepardec.mega.service.impl.init;

import com.gepardec.mega.application.configuration.NotificationConfig;
import com.gepardec.mega.db.entity.Role;
import com.gepardec.mega.db.entity.User;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class SyncServiceMapper {

    @Inject
    Logger log;

    @Inject
    NotificationConfig notificationConfig;

    public User mapEmployeeToUser(final Employee employee, List<Project> projects, Locale defaultLocale) {
        return mapEmployeeToUser(new User(), employee, projects, defaultLocale);
    }

    public User mapEmployeeToUser(User user, Employee employee, List<Project> projects, Locale defaultLocale) {
        user.setZepId(employee.userId());
        user.setEmail(employee.email());
        user.setFirstname(employee.firstName());
        user.setLastname(employee.sureName());
        user.setActive(employee.active());
        user.setRoles(determineRoles(employee, projects));
        setUserLocaleFromEmployeeLanguage(user, employee, defaultLocale);

        return user;
    }

    private Set<Role> determineRoles(final Employee employee, final List<Project> projects) {
        final boolean projectLead = projects.stream()
                .anyMatch(project -> project.leads().contains(employee.userId()));
        final boolean omEmployee = Arrays.stream(notificationConfig.getOmMailAddresses().trim().split(","))
                .anyMatch(omEmail -> omEmail.equals(employee.email()));

        final Set<Role> roles = new HashSet<>();
        // Everyone if employee
        roles.add(Role.EMPLOYEE);
        if (projectLead) {
            roles.add(Role.PROJECT_LEAD);
        }
        if (omEmployee) {
            roles.add(Role.OFFICE_MANAGEMENT);
        }

        return roles;
    }

    private void setUserLocaleFromEmployeeLanguage(final User user, final Employee employee, final Locale defaultLocale) {
        if (employee.language() == null) {
            user.setLocale(defaultLocale);
        } else {
            try {
                user.setLocale(Locale.forLanguageTag(employee.language()));
            } catch (Exception e) {
                log.warn("Employee '" + employee.email()
                        + "' has an invalid language '" + employee.language()
                        + "' set, therefore set default Locale '" + defaultLocale.toString() + "'");
                user.setLocale(defaultLocale);
            }
        }
    }
}
