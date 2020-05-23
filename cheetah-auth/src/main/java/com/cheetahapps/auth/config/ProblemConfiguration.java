package com.cheetahapps.auth.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ProblemConfiguration {

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.registerModules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }
}