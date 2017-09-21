package com.example.subjecthub.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@ParametersAreNonnullByDefault
public class Tag {

    @Id
    @GeneratedValue
    @Column(nullable = false, name = "tag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Subject> subjects = new ArrayList<>();



    public Tag(String name) {
        this.name = name;
    }

    public Tag() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    @JsonIgnore
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
