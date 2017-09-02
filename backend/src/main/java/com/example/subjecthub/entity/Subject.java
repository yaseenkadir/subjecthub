package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "subjects")
@ParametersAreNonnullByDefault
public class Subject {

    // TODO: Add coordinator

    // id refers to the id of the subject in our database, not the subject's code
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "subject_id")
    private Long id;

    // the subject's id within the university's domain, not the subject hub
    // database
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    // Ignoring the university field inside faculty. Unnecessary details since uni is known.
    @JsonIgnoreProperties(value = {"university"})
    private Faculty faculty;

    @Column(nullable = false)
    private int creditPoints;

    @Column(nullable = false)
    private String description;

    @Column
    private String minRequirements;

    @Column(nullable = false)
    private boolean undergrad;

    @Column(nullable = false)
    private boolean postgrad;

    @Column(nullable = false)
    private boolean autumn;

    @Column(nullable = false)
    private boolean spring;

    @Column(nullable = false)
    private boolean summer;

    @Column
    private double rating;

    @Column
    private int numRatings;

    @OneToMany
    @JoinColumn(name = "assessment_id")
    private List<Assessment> assessments;

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public String getMinRequirements() {
        return minRequirements;
    }

    public void setMinRequirements(String minRequirements) {
        this.minRequirements = minRequirements;
    }

    public boolean isUndergrad() {
        return undergrad;
    }

    public void setUndergrad(boolean undergrad) {
        this.undergrad = undergrad;
    }

    public boolean isPostgrad() {
        return postgrad;
    }

    public void setPostgrad(boolean postgrad) {
        this.postgrad = postgrad;
    }

    public boolean isAutumn() {
        return autumn;
    }

    public void setAutumn(boolean autumn) {
        this.autumn = autumn;
    }

    public boolean isSpring() {
        return spring;
    }

    public void setSpring(boolean spring) {
        this.spring = spring;
    }

    public boolean isSummer() {
        return summer;
    }

    public void setSummer(boolean summer) {
        this.summer = summer;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }
}
