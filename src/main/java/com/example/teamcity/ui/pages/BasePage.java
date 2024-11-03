package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.elements.BasePageElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage {
    protected static final Duration BASE_WAITING = Duration.ofSeconds(30);

    private static final String LOGIN_URL = "/login.html";
    private SelenideElement inputUsername = $("#username");
    private SelenideElement inputPassword = $("#password");
    private SelenideElement inputSubmitLogin = $(".loginButton");


    public static LoginPage open(){
        return Selenide.open(LOGIN_URL, LoginPage.class);
    }

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
