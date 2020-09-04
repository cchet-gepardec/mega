package com.gepardec.mega.application.security;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.UserContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesAllowedInterceptorTest {

    @RolesAllowed(allowedRoles = Role.USER)
    private static class TargetWithAnnotation {

    }

    private static class TargetNoAnnotation {

    }

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private UserContext userContext;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InvocationContext invocationContext;

    @InjectMocks
    private RolesAllowedInterceptor rolesAllowedInterceptor;

    @Test
    void invoke_whenAnnotationOnClassLevel_thenUsesClassLevelAnnotation() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(null);
        when(invocationContext.getTarget()).thenReturn(new TargetWithAnnotation());
        when(userContext.user().role()).thenReturn(Role.USER);

        rolesAllowedInterceptor.intercept(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }

    @Test
    void invoke_whenNoAnnotationOnMethodAndClassLevel_thenThrowsNullpointerException() {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(null);
        when(invocationContext.getTarget()).thenReturn(new TargetNoAnnotation());

        Assertions.assertThrows(NullPointerException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void intercept_whenNotLogged_thenThrowsForbiddenException() {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.USER}));

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_whenLoggedAndNotInRole_thenThrowsForbiddenException() {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(new Role[]{Role.ADMINISTRATOR}));
        when(userContext.user().role()).thenReturn(Role.USER);

        assertThrows(ForbiddenException.class, () -> rolesAllowedInterceptor.intercept(invocationContext));
    }

    @Test
    void invoke_whenLoggedAndInRoleMethodAnnotated_thenThrowsForbiddenException() throws Exception {
        when(invocationContext.getMethod().getAnnotation(any())).thenReturn(createAnnotation(Role.values()));
        when(userContext.user().role()).thenReturn(Role.USER);

        rolesAllowedInterceptor.intercept(invocationContext);

        verify(invocationContext, times(1)).proceed();
    }

    private RolesAllowed createAnnotation(final Role[] roles) {
        return new RolesAllowed() {
            @Override
            public Role[] allowedRoles() {
                return roles;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return RolesAllowed.class;
            }
        };
    }
}
