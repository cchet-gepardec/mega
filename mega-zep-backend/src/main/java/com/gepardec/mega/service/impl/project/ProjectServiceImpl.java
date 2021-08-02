package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.entity.project.ProjectEntry;
import com.gepardec.mega.db.repository.ProjectRepository;
import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.ProjectFilter;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private static final String INTERN_PROJECT_CATEGORY = "INT";

    @Inject
    ZepService zepService;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    @Override
    public List<Project> getProjectsForMonthYear(LocalDate monthYear) {
        return this.getProjectsForMonthYear(monthYear, List.of());
    }

    @Override
    public List<Project> getProjectsForMonthYear(final LocalDate monthYear, final List<ProjectFilter> projectFilters) {
        return zepService.getProjectsForMonthYear(monthYear)
                .stream()
                .filter(project -> filterProject(project, Optional.ofNullable(projectFilters).orElse(List.of())))
                .collect(Collectors.toList());
    }

    @Override
    public void addProject(com.gepardec.mega.db.entity.project.Project project) {

        com.gepardec.mega.db.entity.project.Project projectEntity = projectRepository.findByName(project.getName());

        if (projectEntity == null) {
            projectEntity = new com.gepardec.mega.db.entity.project.Project();
        }

        com.gepardec.mega.db.entity.project.Project finalProjectEntity = projectEntity;
        project.getProjectLeads().forEach(lead -> {
            User user = userRepository.findById(lead.getId());
            if(finalProjectEntity.getProjectLeads() == null){
                finalProjectEntity.setProjectLeads(new HashSet<>());
            }
            finalProjectEntity.getProjectLeads().add(user);
        });

        com.gepardec.mega.db.entity.project.Project finalProjectEntity1 = projectEntity;

        LocalDate currentMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

        boolean noProjectEntriesExist = projectEntity.getProjectEntries().stream().noneMatch(pe -> pe.getDate().equals(currentMonth));

        if (noProjectEntriesExist) {
            project.getProjectEntries().forEach(projectEntry -> {

                User owner = userRepository.findById(projectEntry.getOwner().getId());
                User assignee = userRepository.findById(projectEntry.getAssignee().getId());

                ProjectEntry pe = new ProjectEntry();
                pe.setPreset(projectEntry.isPreset());
                pe.setProject(projectEntry.getProject());
                pe.setStep(projectEntry.getStep());
                pe.setState(projectEntry.getState());
                pe.setUpdatedDate(projectEntry.getUpdatedDate());
                pe.setCreationDate(projectEntry.getCreationDate());
                pe.setDate(projectEntry.getDate());
                pe.setName(projectEntry.getName());
                pe.setOwner(owner);
                pe.setAssignee(assignee);

                finalProjectEntity1.addProjectEntry(pe);
            });
        }

        projectEntity.setName(project.getName());
        projectEntity.setStartDate(project.getStartDate());
        projectEntity.setEndDate(project.getEndDate());

        projectRepository.merge(projectEntity);
    }

    private boolean filterProject(final Project project, final List<ProjectFilter> projectFilters) {
        return projectFilters.stream()
                .allMatch(projectFilter -> filterProject(project, projectFilter));
    }

    private boolean filterProject(final Project project, final ProjectFilter projectFilter) {
        switch (projectFilter) {
            case IS_LEADS_AVAILABLE:
                return !project.leads().isEmpty();
            case IS_CUSTOMER_PROJECT:
                return !project.categories().contains(INTERN_PROJECT_CATEGORY);
            default:
                throw new IllegalStateException(String.format("projectFilter %s not implemented", projectFilter));
        }
    }
}
