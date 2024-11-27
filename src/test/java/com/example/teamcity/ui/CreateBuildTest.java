package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.LoginPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateBuildPage;
import io.qameta.allure.Step;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.ui.pages.admin.CreateBuildPage.validateBuildNameErrorMessage;

@Test(groups = {"Regression"})
public class CreateBuildTest extends BaseUiTest{
    @Test(description = "User should be able to create build", groups = {"Positive", "UI"})
    public void createBuild() {
        // Precondition:
        var createdProject = createProjectAPI();
        LoginPage.open().login(testData.getUser());

        // Create Build for the project
        CreateBuildPage.open(createdProject.getId())
                .createForm(REPO_URL)
                .setupCreateForm(testData.getProject().getName(), testData.getBuildType().getName());

        // check state on API: data is transferred correctly from UI to API
        var createdBuildType = superUserCheckedRequests.<BuildType>getRequest(BUILD_TYPES)
                .read("project:id:" + createdProject.getId());
        softy.assertNotNull(createdBuildType);

        var projectExists = ProjectsPage.openProjectsPage().expandAllProjects().getBuilds().stream()
                .anyMatch(build -> build.getName().text()
                        .equals(testData.getBuildType().getName()));

        softy.assertTrue(projectExists);
    }

    @Test(description = "User should NOT be able to create build without name", groups = {"Negative", "UI"})
    public void createBuildWithoutName() {
        // Precondition:
        var createdProject = createProjectAPI();
        LoginPage.open().login(testData.getUser());

        // Create Build for the project
        CreateBuildPage.open(createdProject.getId())
                .createForm(REPO_URL)
                .setupCreateForm(testData.getProject().getName(), "");
        softy.assertTrue(validateBuildNameErrorMessage());

        // check state on API: data is transferred correctly from UI to API
        var getProjectData = superUserCheckedRequests.<Project>getRequest(Endpoint.PROJECTS)
                .read("name:" + testData.getProject().getName());
        softy.assertTrue(getProjectData.getBuildTypes().getBuildType().isEmpty());
        softy.assertTrue(getProjectData.getBuildTypes().getCount() == 0);
    }

    @Test(description = "User should be able to cancel build creation and return to the initial build creation form",
            groups = {"Positive", "UI"})
    public void cancelCreateBuild() {
        // Precondition:
        var createdProject = createProjectAPI();
        LoginPage.open().login(testData.getUser());

        // Create Build for the project
        CreateBuildPage.open(createdProject.getId())
                .createForm(REPO_URL)
                .clickCancelBuildCreation()
                .validateCancelBuildCreation();
    }

    private Project createProjectAPI() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create Project by the user
        return userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
    }
}
