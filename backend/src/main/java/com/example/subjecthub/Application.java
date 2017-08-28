package com.example.subjecthub;

import com.example.subjecthub.entity.Student;
import com.example.subjecthub.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
public class Application {

    public static final Logger log = LoggerFactory.getLogger( Application.class );

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/", "/**").permitAll().and()
                .csrf()
                    // disabling csrf so that we can access the h2-console webpage
                    .ignoringAntMatchers("/h2-console/**").and()
                .headers()
                    .frameOptions().sameOrigin();
        }
    }

    @Bean
    public CommandLineRunner demo(StudentRepository repository) {
        // Temporary hack to save students to the database on app start
        return (args) -> {
            repository.save(new Student(12020525L, "Yaseen", "Kadir", "C1000", "FDAB"));
            repository.save(new Student(75048524L, "Miles", "Johnson", "C1234", "FEIT"));
            repository.save(new Student(98084294L, "Andre", "Farah", "C1234", "FEITChanged"));
            repository.save(new Student(12544110L, "Simran", "Bagga", "C1234", "FEIT"));
            repository.save(new Student(12564011L, "Anuj", "Paudel", "C1234", "FEIT"));
            repository.save(new Student(11338896L, "Jet", "Zhu", "C1234", "FEIT"));
        };
    }
}
