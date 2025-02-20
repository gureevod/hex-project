package project.ui.components;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponent {
    protected final SelenideElement root;

    protected BaseComponent(SelenideElement root) {
        this.root = root;
    }
}
