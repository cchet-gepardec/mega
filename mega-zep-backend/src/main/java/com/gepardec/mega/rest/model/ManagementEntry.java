package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.State;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.List;

@AutoValue
@JsonSerialize(as = ManagementEntry.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_ManagementEntry.Builder.class)
public abstract class ManagementEntry {

    public static Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_ManagementEntry.Builder();
    }

    @JsonProperty
    public abstract Employee employee();

    @JsonProperty
    public abstract State employeeCheckState();

    @JsonProperty
    public abstract State customerCheckState();

    @JsonProperty
    public abstract State internalCheckState();

    @JsonProperty
    public abstract State projectCheckState();

    @JsonProperty
    @Nullable
    public abstract List<PmProgress> employeeProgresses();

    @JsonProperty
    public abstract long totalComments();

    @JsonProperty
    public abstract long finishedComments();

    @JsonProperty
    public abstract String entryDate();
    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Builder employee(Employee employee);

        @JsonProperty
        public abstract Builder employeeCheckState(State state);

        @JsonProperty
        public abstract Builder customerCheckState(State state);

        @JsonProperty
        public abstract Builder internalCheckState(State state);

        @JsonProperty
        public abstract Builder projectCheckState(State state);

        @JsonProperty
        public abstract Builder employeeProgresses(List<PmProgress> pmProgresses);

        @JsonProperty
        public abstract Builder totalComments(long totalComments);

        @JsonProperty
        public abstract Builder finishedComments(long totalComments);

        @JsonProperty
        public abstract Builder entryDate(String entryDate);

        @JsonProperty
        public abstract ManagementEntry build();
    }
}
