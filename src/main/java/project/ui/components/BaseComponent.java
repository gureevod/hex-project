package project.ui.components;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseComponent {
    private String title;
    public abstract SelenideElement getRoot();
}
