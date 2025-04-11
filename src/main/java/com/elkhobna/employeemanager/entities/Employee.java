package com.elkhobna.employeemanager.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity(name = "employee_")
public class Employee {
    @Id
    @Column(length = 8)
    @Size(min = 8, max = 8)
    private String cin;

    @Column(unique = true, nullable = false)
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String familyName;

    @Column(nullable = false)
    @NotNull
    private LocalDate joiningDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Company company;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private Set<Document> documents;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public @Size(min = 8, max = 8) String getCin() {
        return cin;
    }

    public void setCin(@Size(min = 8, max = 8) String cin) {
        this.cin = cin;
    }

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(@NotBlank String familyName) {
        this.familyName = familyName;
    }

    public @NotNull LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(@NotNull LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public @NotNull Company getCompany() {
        return company;
    }

    public void setCompany(@NotNull Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
