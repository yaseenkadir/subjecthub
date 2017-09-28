package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByName(String name);

    Tag findById(Long tagId);
}
