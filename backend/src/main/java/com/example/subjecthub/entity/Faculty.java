package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    public Faculty() {
    }

    public Faculty(String name, String code, University university) {
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

    @Override
    public String toString() {
        return "Faculty{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", university=" + university.getName() +
            '}';
    }
}
