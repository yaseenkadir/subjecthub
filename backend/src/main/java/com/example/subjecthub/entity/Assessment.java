package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;

@Entity
@Table(name = "assessments")
@ParametersAreNonnullByDefault
public class Assessment {

    public enum AssessmentType {
        REPORT,
        TEST,
        FINAL,
        PROJECT
    }

    @Id
    @GeneratedValue
    @Column(nullable = false, name = "assessment_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int weighting;

    @Column(nullable = false)
    private boolean groupWork;

    // Using a string here because length can differ e.g. could be "5000 words"
    // or "60 minutes" etc.
    @Column(nullable = false)
    private String length;

    @Column(nullable = false)
    private AssessmentType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public Assessment() {
    }

    public Assessment(String name, String description, int weighting,
                      boolean groupWork, String length, AssessmentType type) {
        this.name = name;
        this.description = description;
        this.weighting = weighting;
        this.groupWork = groupWork;
        this.length = length;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeighting() {
        return weighting;
    }

    public void setWeighting(int weighting) {
        this.weighting = weighting;
    }

    public boolean isGroupWork() {
        return groupWork;
    }

    public void setGroupWork(boolean groupWork) {
        this.groupWork = groupWork;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public AssessmentType getType() { return type; }

    public void setType(AssessmentType type) { this.type = type; }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Assessment{" +
            " id=" + id +
            ", name='" + name + '\'' +
            ", type=" + type +
            ", subject=" + subject.getName() +
            '}';
    }
}
