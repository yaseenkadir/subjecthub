package com.example.subjecthub.repository;

import com.example.subjecthub.entity.SubjectHubUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubjectHubUserRepository extends CrudRepository<SubjectHubUser, Long> {

    Optional<SubjectHubUser> findByUsername(String username);

    Optional<SubjectHubUser> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
