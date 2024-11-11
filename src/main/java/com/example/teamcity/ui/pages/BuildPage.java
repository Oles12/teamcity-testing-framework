package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.example.teamcity.ui.pages.BasePage.BASE_WAITING;

public class BuildPage {
    private static final String build_URL = "/buildConfiguration/%s";
    private final SelenideElement header = $(".BuildTypePageHeader__links--TF");

    public SelenideElement title = $("h1[class*='ring-heading-heading']>span");

    // wait until Build page is fully loaded
    public BuildPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    public static BuildPage open(String buildName){
        return Selenide.open(build_URL.formatted(buildName), BuildPage.class);
    }
}

