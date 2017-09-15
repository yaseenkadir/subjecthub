package com.example.subjecthub.security;

import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Impl of UserDetailsService used by Spring to fetch UserDetails by username.
 */
@Service
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SubjectHubUser> subjectHubUser = subjectHubUserRepository.findByUsername(username);
        if (subjectHubUser.isPresent()) {
            SubjectHubUser user = subjectHubUser.get();
            return new SecurityUser(username, user.getPassword());
        }
        return null;
    }
}
