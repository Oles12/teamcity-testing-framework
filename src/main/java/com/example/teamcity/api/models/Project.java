package com.example.teamcity.api.models;

import com.example.teamcity.api.annotations.Optional;
import com.example.teamcity.api.annotations.Parameterizable;
import com.example.teamcity.api.annotations.Random;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project extends BaseModel{
    @Random
    @Parameterizable
    private String name;

    @Random
    @Parameterizable
    private String id;
    @Optional
    private String locator;


    @Optional
    private Project parentProject;

    @Optional
    private BuildTypes buildTypes;
}
