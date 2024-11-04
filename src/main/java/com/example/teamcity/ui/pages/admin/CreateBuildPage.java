package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;

public class CreateBuildPage extends CreateBasePage{
    private static final String BUILD_SHOW_MODE = "createBuildTypeMenu";
    protected static SelenideElement errorBuildTypeNameMessage = $("span[id='error_buildTypeName']");


    public static CreateBuildPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_SHOW_MODE), CreateBuildPage.class);
    }

    public CreateBuildPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Override
    public void setupCreateForm(String projectName, String buildTypeName) {
        super.setupCreateForm(projectName, buildTypeName);
    }

    public static boolean ValidateBuildNameErrorMessage() {
        errorBuildTypeNameMessage.should(appear, BASE_WAITING);
       return errorBuildTypeNameMessage.text().contains("Build configuration name must not be empty");
    }
}
