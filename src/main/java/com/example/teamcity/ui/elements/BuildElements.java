package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

 @Getter
    public class BuildElements extends BasePageElement {
        private SelenideElement name;
        private SelenideElement link;
        private SelenideElement button;

        public BuildElements(SelenideElement element) {
            super(element);
            this.name = find("span[class*='MiddleEllipsis__middleEllipsis--Ei BuildType']");
            this.link = find("a");
            this.button = find("button");
        }
    }
