package com.gepardec.mega.rest;

import com.gepardec.mega.GoogleTokenVerifierMock;
import com.gepardec.mega.SessionUserMock;
import com.gepardec.mega.WorkerServiceMock;
import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.zep.service.impl.WorkerServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

@ExtendWith(MockitoExtension.class)
@QuarkusTest
public class WorkerResourceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdToken googleIdToken;

    @Mock
    private WorkerServiceImpl workerService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdTokenVerifier googleIdTokenVerifier;

    @Inject
    SessionUserMock sessionUserMock;

    @Inject
    WorkerServiceMock workerServiceMock;

    @Inject
    GoogleTokenVerifierMock googleTokenVerifierMock;

    @BeforeEach
    void beforeEach() throws Exception {
        final String userId = "1337-thomas.herzog";
        final String email = "thomas.herzog@gepardec.com";
        Mockito.when(googleIdTokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        googleTokenVerifierMock.setDelegate(googleIdTokenVerifier);
        sessionUserMock.init(userId, email, "", Role.ADMINISTRATOR.roleId);
        workerServiceMock.setDelegate(workerService);
    }



    @Test
    void employeeMonthendReport_withPOST_returnsMethodNotAllowed() {
        given().contentType(ContentType.JSON)
                .post("/worker/monthendreports")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void employeeMonthendReport_withNoReport_returnsNotFound() {
        given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void employeeMonthendReport_withReport_returnsReport() {
        final Employee employee = createEmployee(0);
        final com.gepardec.mega.monthlyreport.MonthlyReport expected = createZepMonthlyReport(employee);
        Mockito.when(workerService.getMonthendReportForUser(Mockito.anyString())).thenReturn(expected);

        final MonthlyReport actual = given().contentType(ContentType.JSON)
                .get("/worker/monthendreports")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(MonthlyReport.class);

        assertEmployee(actual.getEmployee(), employee);
        assertTimeWarnings(expected.getTimeWarnings(), actual.getTimeWarnings());
        assertJourneyWarnings(expected.getJourneyWarnings(), actual.getJourneyWarnings());
    }

    private com.gepardec.mega.monthlyreport.MonthlyReport createZepMonthlyReport(final Employee employee) {
        final List<TimeWarning> timeWarnings = Collections.singletonList(TimeWarning.of(LocalDate.now(), 0.0, 0.0, 0.0));
        final List<JourneyWarning> journeyWarnings = Collections.singletonList(new JourneyWarning(LocalDate.now(), Collections.singletonList("WARNING")));

        final MonthlyReport monthlyReport =  new MonthlyReport();
        monthlyReport.setTimeWarnings(timeWarnings);
        monthlyReport.setJourneyWarnings(journeyWarnings);
        monthlyReport.setEmployee(employee);
        return monthlyReport;
    }

    private Employee createEmployee(final int userId) {
        final Employee employee = new Employee();
        final String name = "Thomas_" + userId;

        employee.setEmail(name + "@gepardec.com");
        employee.setFirstName(name);
        employee.setSureName(name + "_Nachname");
        employee.setTitle("Ing.");
        employee.setUserId(String.valueOf(userId));
        employee.setSalutation("Herr");
        employee.setWorkDescription("ARCHITEKT");
        employee.setReleaseDate("2020-01-01");
        employee.setRole(Role.USER.roleId);

        return employee;
    }

    private void assertEmployee(final Employee actual, final Employee employee) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(employee.getRole(), actual.getRole(), "role"),
                () -> Assertions.assertEquals(employee.getUserId(), actual.getUserId(), "userId"),
                () -> Assertions.assertEquals(employee.getTitle(), actual.getTitle(), "title"),
                () -> Assertions.assertEquals(employee.getFirstName(), actual.getFirstName(), "firstName"),
                () -> Assertions.assertEquals(employee.getSureName(), actual.getSureName(), "sureName"),
                () -> Assertions.assertEquals(employee.getSalutation(), actual.getSalutation(), "salutation"),
                () -> Assertions.assertEquals(employee.getWorkDescription(), actual.getWorkDescription(), "workDescription"),
                () -> Assertions.assertEquals(employee.getReleaseDate(), actual.getReleaseDate()));
    }

    private void assertJourneyWarnings(List<com.gepardec.mega.monthlyreport.journey.JourneyWarning> expected, List<JourneyWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertJourneyWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertJourneyWarning(com.gepardec.mega.monthlyreport.journey.JourneyWarning expected, JourneyWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertIterableEquals(expected.getWarnings(), actual.getWarnings(), "warnings")
        );
    }

    private void assertTimeWarnings(List<com.gepardec.mega.monthlyreport.warning.TimeWarning> expected, List<TimeWarning> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTimeWarning(expected.get(i), actual.get(i));
        }
    }

    private void assertTimeWarning(com.gepardec.mega.monthlyreport.warning.TimeWarning expected, TimeWarning actual) {
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getDate(), actual.getDate(), "date"),
                () -> Assertions.assertEquals(expected.getExcessWorkTime(), actual.getExcessWorkTime(), "exessWorkTime"),
                () -> Assertions.assertEquals(expected.getMissingBreakTime(), actual.getMissingBreakTime(), "missingBreakTime"),
                () -> Assertions.assertEquals(expected.getMissingRestTime(), actual.getMissingRestTime(), "missingRestTime")
        );
    }
}
