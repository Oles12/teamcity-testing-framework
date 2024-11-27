package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage{
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";

    private final SelenideElement projectNameInput = $("#projectName");

    @Step("Create project page for project: {projectId}")
    public static CreateProjectPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    @Step
    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Step
    @Override
    public void setupCreateForm(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        super.setupCreateForm(projectName, buildTypeName);
    }
}
