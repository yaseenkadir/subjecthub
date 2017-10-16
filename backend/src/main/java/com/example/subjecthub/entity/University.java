package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "universities")
@ParametersAreNonnullByDefault
public class University {

    @Id
    @Column(nullable = false, name = "university_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // e.g. UOW for University of Wollongong
    @Column(nullable = false)
    private String abbreviation;

    // Apparently spring can map (snake_case to camelCase for columns)
    @Column(nullable = false)
    private String imageUrl;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "university", orphanRemoval = true)
    private List<Faculty> faculties;

    public University() {
        this.faculties = new ArrayList<>();
    }

    public University(String name, String abbreviation) {
        this();
        this.name = name;
        this.abbreviation = abbreviation;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "University{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", abbreviation='" + abbreviation + '\'' +
            '}';
    }
}
