package com.example.teamcity.api.ProjectTest;

import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.ResponseValidator.ProjectResponseValidator;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseApiTest {
    ProjectResponseValidator responseValidator = new ProjectResponseValidator();

    @Test(description = "Super user should be able to create project", groups = {"Positive", "CRUD"})
    public void CreateProject() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project by the user
        var createProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // get createdProject by id and check the created Project data
        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).read("id:" + createProject.getId());

        responseValidator.ValidateProjectNameAndId(createdProject, testData.getProject());
    }

    @Test(description = "Super user should be able to create project with name and without Id", groups = {"Positive", "Smoke"})
    public void CreateProjectWithNameOnly() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // set project with id=null
        testData.getProject().setId(null);

        //create project by the user
        var createdProjectData = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        softy.assertEquals(createdProjectData.getName(), testData.getProject().getName(),"Project name is not correct");
        softy.assertNotEquals(createdProjectData.getId(), testData.getProject().getId(),"Project ID is not correct");

        // get createdProject by id and check the created Project data
        var getProjectData = userCheckRequests.<Project>getRequest(PROJECTS).read("id:" + createdProjectData.getId());
        softy.assertEquals( getProjectData.getName(), testData.getProject().getName(),"Project name is not correct");
        // we expect that absent projectId will be automatically set upon Project creation
        softy.assertNotEquals(getProjectData.getId(), testData.getProject().getId(),"Project ID is not correct");

        String expectedProjectId = convertNameToId(getProjectData.getName());
        softy.assertEquals(getProjectData.getId(), expectedProjectId,"Project ID is not correct");
    }

    @Test(description = "Super user should be able to sub create project for existing project", groups = {"Positive", "Smoke"})
    public void CreateSubProjectForSpecificParentProject() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // create Parent project by the user as precondition
        var parentProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        var sb = generate();
        sb.getProject().setParentProject((new Project(parentProject.getName(), parentProject.getId(), null, null)));
        // create SubProject with the existing parent project
        var subProject = userCheckRequests.<Project>getRequest(PROJECTS).create(sb.getProject());

        // get created SubProject by id and check the created Project data
        var getCreatedSubProjectData = userCheckRequests.<Project>getRequest(PROJECTS).read("id:" + subProject.getId());

        // validate created SubProject data
        responseValidator.ValidateProjectNameAndId(getCreatedSubProjectData,  subProject);

        // validate parent project data in created SubProject data
        responseValidator.ValidateProjectNameAndId(getCreatedSubProjectData.getParentProject(),  subProject.getParentProject());
    }

    @Test(description = "Super user should not be able to create subproject for non-existing project", groups = {"Negative", "Smoke"})
    public void CreateSubProjectForNonExistingParentProject() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // Precondition: generate data for Parent project that won't be created
        var NonExistingParentProject = generate();

        var subProject = generate();
        subProject.getProject().setParentProject((new Project(NonExistingParentProject.getProject().getName(), NonExistingParentProject.getProject().getId(), null, null)));

        // create subproject for non-existing parent project
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(subProject.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator 'count:1,id:" + NonExistingParentProject.getProject().getId()+"'."
                        + " Project cannot be found by external id '%s'."
                    .formatted(NonExistingParentProject.getProject().getId())));
    }

    @Test(description = "Super user should not be able to create project with same name and id", groups = {"Negative", "CRUD"})
    public void CreateProjectDuplicate() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project by the user
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(testData.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: %s"
                .formatted( testData.getProject().getName())));
    }

    @Test(description = "Super user should not be able to create project with same name and unique id", groups = {"Negative", "CRUD"})
    public void CreateProjectWithExistingName() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project by the user
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        //create project with the same name
        var createWithSameNameProject = generate(Arrays.asList(testData.getProject()), Project.class, testData.getProject().getName());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(createWithSameNameProject)
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project with this name already exists: %s"
                        .formatted( testData.getProject().getName())));
    }

    @Test(description = "Super user should not be able to create new project with same id of existing project and unique name of existing project", groups = {"Negative", "CRUD"})
    public void CreateProjectWithExistingId() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        //create project by the user
        var project1 = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        //set project with the same id and unique name
        testData.getProject().setId(project1.getId());
        testData.getProject().setName(RandomData.getString());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(testData.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project ID \"%s\" is already used by another project"
                        .formatted( project1.getId())));
    }

    @Test(description = "Super user should not be able to create new project without name", groups = {"Negative", "CRUD"})
    public void CreateProjectWithoutName() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        testData.getProject().setName(null);

        //create project without name by the user
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(testData.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));
    }

    @Test(description = "Super user should Not be able to create project with id started with digits", groups = {"Negative", "CRUD"})
    public void CreateProjectWithWrongIdFormat() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // set project with id contains digits
        testData.getProject().setId(1+RandomStringUtils.randomAlphanumeric(10));

        //create project by the user
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(testData.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Here wrong status code returned - should be 400
                .body(Matchers.containsString(("Project ID \"%s\" is invalid: starts with non-letter character '1'. " +
                        "ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).")
                        .formatted(testData.getProject().getId())));
    }

    @Test(description = "Super user should Not be able to create project with id > 225 characters", groups = {"Negative", "CRUD"})
    public void CreateProjectWithLongId() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        // set project with id contains characters
        testData.getProject().setId(RandomData.getString(226));

        //create project by the user
        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(testData.getProject())
                .then()
                .assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR) // Here wrong status code returned - should be 400
                .body(Matchers.containsString(("Project ID \"%s\" is invalid: " +
                        "it is 226 characters long while the maximum length is 225. " +
                        "ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters).")
                        .formatted( testData.getProject().getId())));
    }

    @Test(description = "Super user should not be able to create new project with empty request body", groups = {"Negative", "CRUD"})
    public void CreateProjectWithoutPayload() {
        // create superUser
        superUserCheckedRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .createWithoutBody()
                .then()
                .assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty"));
        }


   /** Converts 'name' value to 'id' value using special format string
    Example:
    * Input: test_rJject_000 → Output: TestRJject
    * Input: TestnYTYkHxmbv → Output: TestNYTYkHxmbvv
    */
    private static String convertNameToId(String name) {
        // Remove underscores and capitalize the character immediately following each underscore
        String id = name.replaceAll("_(.)", "$1");

        // Capitalize the first letter of the string
        Pattern pattern = Pattern.compile("^[a-z]");
        Matcher matcher = pattern.matcher(id);
        if (matcher.find()) {
            id = matcher.replaceFirst(matcher.group(0).toUpperCase());
        }

        // Manually loop through characters and capitalize letters after lowercase sequences
        StringBuilder result = new StringBuilder();
        char[] chars = id.toCharArray();
        boolean capitalizeNext = false;

        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];

            if (i == 0) {
                // Capitalize first character
                result.append(Character.toUpperCase(currentChar));
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(currentChar));
                capitalizeNext = false;
            } else {
                // Append as-is, if not uppercase already
                if (Character.isLowerCase(currentChar)) {
                    capitalizeNext = (i < chars.length - 1 && Character.isUpperCase(chars[i + 1]));
                }
                result.append(currentChar);
            }
        }

        // Remove any leading zeros after the last underscore
        id = result.toString().replaceAll("0+", "");

        return id;
    }
}
