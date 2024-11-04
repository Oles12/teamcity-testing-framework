package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class CreateBasePage extends BasePage {
    protected static final String CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=%s";

    protected SelenideElement urlInput = $("#url");
    protected SelenideElement submitButton = $(Selectors.byAttribute("value", "Proceed"));
    protected SelenideElement buildTypeNameInput = $("#buildTypeName");
    protected SelenideElement connectionSuccessfulMessage = $(".connectionSuccessful");

    protected void baseCreateForm(String url) {
        urlInput.should(visible);
        urlInput.val(url);
        submitButton.click();
        connectionSuccessfulMessage.should(appear, BASE_WAITING);
    }

    protected void setupCreateForm(String projectName, String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        submitButton.click();
    }
}
