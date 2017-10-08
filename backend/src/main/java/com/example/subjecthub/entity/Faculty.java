package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faculties")
@ParametersAreNonnullByDefault
public class Faculty {

    @Id
    @Column(nullable = false, name = "faculty_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String code;

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonIgnore
    private University university;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faculty", orphanRemoval = true)
    @JsonIgnore
    private List<Subject> subjects;

    public Faculty() {
        this.subjects = new ArrayList<>();
    }

    public Faculty(String name, String code, University university) {
        this();
        this.name = name;
        this.code = code;
        this.university = university;
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

    @Nullable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Faculty{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", university=" + university.getName() +
            '}';
    }
}
