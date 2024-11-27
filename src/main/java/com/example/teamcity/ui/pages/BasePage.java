package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.elements.BasePageElement;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage {
    protected static final Duration BASE_WAITING = Duration.ofSeconds(30);
    protected static final Duration LONG_WAITING = Duration.ofMinutes(5);

    private static final String LOGIN_URL = "/login.html";
    private final SelenideElement inputUsername = $("#username");
    private final SelenideElement inputPassword = $("#password");
    private final SelenideElement inputSubmitLogin = $(".loginButton");

    @Step("Open login page")
    public static LoginPage open(){
        return Selenide.open(LOGIN_URL, LoginPage.class);
    }

    @Step("Login as {user.username}")
    public ProjectsPage login(User user){
        // Method .val - contains clear + sendKeys
        inputUsername.val(user.getUsername());
        inputPassword.val(user.getPassword());
        inputSubmitLogin.click();

        return Selenide.page(ProjectsPage.class);
    }

    // ElementCollection: Selenide Element 1, Selenide Element 2 и тд
    // collection.stream() -> Конвеер: Selenide Element 1, Selenide Element 2 и тд
    // creator(Selenide Element 1) -> T -> add to list
    // creator(Selenide Element 2) -> T -> add to list
    protected <T extends BasePageElement> List<T> generatePageElements(
            ElementsCollection collection, Function<SelenideElement, T> creator)
    {
        return collection.stream().map(creator).toList();
    }

}
