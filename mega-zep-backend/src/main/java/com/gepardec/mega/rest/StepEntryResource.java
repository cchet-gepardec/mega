package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.rest.model.EmployeeStep;
import com.gepardec.mega.rest.model.ProjectStep;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Secured
@RequestScoped
@Path("/stepentry")
public class StepEntryResource {

    @Inject
    StepEntryService stepEntryService;

    @Inject
    UserContext userContext;

    @PUT
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean close(@NotNull(message = "{stepEntryResource.parameter.notNull}") final EmployeeStep employeeStep) {
        return stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStep.employee(), employeeStep.stepId());
    }

    @PUT
    @Path("/closeforproject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean close(@NotNull(message = "{stepEntryResource.parameter.notNull}") final ProjectStep projectStep) {
        return stepEntryService.closeStepEntryForEmployeeInProject(
                projectStep.employee(),
                projectStep.stepId(),
                projectStep.projectName(),
                userContext.user().email()
        );
    }
}
