package com.example.teamcity.api.ResponseValidator;

import com.example.teamcity.api.models.Project;
import org.testng.asserts.SoftAssert;

public class ProjectResponseValidator {
    private final SoftAssert softy = new SoftAssert();

    // validates created project name and id in response are equals to the request data
    public void ValidateProjectNameAndId(Project actualData, Project expectedData) {
        softy.assertEquals(actualData.getName(), expectedData.getName(), "Project name is not correct");
        softy.assertEquals(actualData.getId(), expectedData.getId(), "Project ID is not correct");
    }
}
