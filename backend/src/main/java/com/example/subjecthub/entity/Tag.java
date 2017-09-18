package com.example.subjecthub.entity;


import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;
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


    private List<Subject> subjects;

    public Tag() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    @ManyToMany(mappedBy = "subjects")
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
