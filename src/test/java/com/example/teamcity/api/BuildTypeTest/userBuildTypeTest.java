package com.example.teamcity.api.BuildTypeTest;
import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class userBuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project
         userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // create buildType based on userCheckRequests (getting buildType endpoint)
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // get buildTypeId and check the created buildType
        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());
        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "Build type name is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project by the user
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // generate buildType by created project
        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        // create buildType1 for project by the user based on userCheckRequests (getting buildType endpoint)
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // Create buildType2 with same id as buildType1 for project by the user
        // Check buildType2 was not created with bad request code
        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template"
                        .formatted( testData.getBuildType().getId())));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreateBuildTypeTest() {
        step("Create user");
        step("Create project by the user");
        step("Grant user PROJECT_ADMIN role in project");

        step("Create buildType for project by the user (PROJECT_ADMIN)");
        step("Check buildType was created successfully");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreateBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project by the user1");
        step("Grant user1 PROJECT_ADMIN role in project1");

        step("Create user2");
        step("Create project by the user2");
        step("Grant user2 PROJECT_ADMIN role in project2");

        step("Create buildType for project1 by the user2");
        step("Check buildType was not created with forbidden request code");
    }

}
