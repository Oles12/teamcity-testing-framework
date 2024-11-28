package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.BuildElements;
import com.example.teamcity.ui.elements.ProjectElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class ProjectsPage extends BasePage{
    private static final String PROJECTS_URL = "/favorite/projects";

    private final ElementsCollection projectElements = $$("div[class*='Subproject__container']");

    private final ElementsCollection buildElements = $$("div[class*='BuildsByBuildType__container']");

    private SelenideElement spanFavoriteProjects = $("span[class='ProjectPageHeader__title--ih']");

    private final SelenideElement header = $(".MainPanel__router--gF > div");
    private final SelenideElement expandAllButton = $x("//div[@data-test='ring-button-set']//button[.//span[text()='Expand All']]");
    private final SelenideElement expandElement = $("div[class*='Subproject__container'] button[aria-expanded='true']");

    // ElementCollection -> List<ProjectElement>
    // UI elements -> List<Object>
    // ElementCollection -> List<BasePageElement>

    @Step
    public static ProjectsPage openProjectsPage() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    // wait until Projects page is fully loaded
    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    public ProjectsPage expandAllProjects() {
        expandAllButton.click();
        expandElement.shouldBe(Condition.exist, BASE_WAITING);
        return this;
    }

    public List<BuildElements> getBuilds() {
        return generatePageElements(buildElements, BuildElements::new);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectElements, ProjectElement::new);
    }
}