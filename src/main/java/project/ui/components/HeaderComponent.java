package project.ui.components;

import core.ui.implementation.ButtonImpl;
import core.ui.interfaces.Button;
import io.qameta.allure.Step;
import project.ui.pages.FindOwnersPage;
import project.ui.pages.HomePage;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class HeaderComponent extends BaseComponent {

    public final Button homeButton;
    public final Button findOwnersButton;

    public HeaderComponent() {
        super($(xpath("//*[@id='main-navbar']")));
        homeButton = new ButtonImpl(root.$(xpath(".//*[@title='home page']")));
        findOwnersButton = new ButtonImpl(root.$(xpath(".//*[@title='find owners']")));
    }

    @Step("Кликнуть на кнопку домой")
    public HomePage homeButtonClick() {
        homeButton.click();
        return HomePage.get();
    }

    @Step("Кликнуть на кнопку Найти владельца")
    public FindOwnersPage findOwnersButtonClick() {
        findOwnersButton.click();
        return FindOwnersPage.get();
    }
}
