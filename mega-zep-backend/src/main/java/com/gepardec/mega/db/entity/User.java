package com.gepardec.mega.db.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UIDX_EMAIL", columnNames = {"EMAIL"})
        })
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "select e from User e where e.email = :email")
})
public class User {

    @Id
    @Column(name = "ID", insertable = false, updatable = false)
    @GeneratedValue(generator = "employeeIdGenerator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "employeeIdGenerator", sequenceName = "SEQUENCE_USER_ID", allocationSize = 1)
    private Long id;

    /**
     * The creation date of the user
     */
    @NotNull
    @Column(name = "CREATION_DATE", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * The updated date of the user
     */
    @NotNull
    @Column(name = "UPDATE_DATE", updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    /**
     * The unique email of the user
     */
    @NotNull
    @Email
    @Length(min = 1, max = 255)
    @Column(name = "EMAIL")
    private String email;

    public User() {
    }

    private User(final String email) {
        this.email = email;
    }

    public static User of(final String email) {
        return new User(email);
    }

    /**
     * The step entries the user is assigned to
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignee")
    private Set<StepEntry> assignedStepEntries = new HashSet<>(0);

    /**
     * The step entries the user owns
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<StepEntry> ownedStepEntries = new HashSet<>(0);

    @PrePersist
    void onPersist() {
        creationDate = updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<StepEntry> getAssignedStepEntries() {
        return assignedStepEntries;
    }

    public void setAssignedStepEntries(Set<StepEntry> assignedStepEntries) {
        this.assignedStepEntries = assignedStepEntries;
    }

    public Set<StepEntry> getOwnedStepEntries() {
        return ownedStepEntries;
    }

    public void setOwnedStepEntries(Set<StepEntry> ownedStepEntries) {
        this.ownedStepEntries = ownedStepEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return (id != null) ? Objects.equals(id, user.id) : super.equals(o);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }
}
