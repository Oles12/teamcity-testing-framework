package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CreateBuildPage extends CreateBasePage{
    private static final String BUILD_SHOW_MODE = "createBuildTypeMenu";
    protected static SelenideElement errorBuildTypeNameMessage = $("span[id='error_buildTypeName']");
    protected SelenideElement cancelButton = $(".saveButtonsBlock [class='btn cancel']");
    protected static SelenideElement createFromUrlBtn = $(Selectors.byAttribute("href", "#createFromUrl"));

    @Step("Open create Build page")
    public static CreateBuildPage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_SHOW_MODE), CreateBuildPage.class);
    }

    @Step
    public CreateBuildPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Step("Setup create Build form with projectName {projectName} and buildType {buildTypeName}")
    @Override
    public void setupCreateForm(String projectName, String buildTypeName) {
        super.setupCreateForm(projectName, buildTypeName);
    }

    public CreateBuildPage clickCancelBuildCreation(){
        cancelButton.click();
        return this;
    }

    public CreateBuildPage validateCancelBuildCreation() {
        createFromUrlBtn.should(visible, BASE_WAITING);
        urlInput.should(visible).shouldBe(Condition.empty);
        return this;
    }
    public static boolean validateBuildNameErrorMessage() {
        errorBuildTypeNameMessage.should(appear, BASE_WAITING);
       return errorBuildTypeNameMessage.text().contains("Build configuration name must not be empty");
    }
}
