package project.ui.components;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.Title;
import core.ui.interfaces.Button;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import project.ui.pages.FindOwnersPage;
import project.ui.pages.HomePage;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

@Getter
@Title(value = "Верхнее меню")
public class HeaderComponent extends BaseComponent {

    @FindBy(xpath = ".//*[@title='home page']")
    @Title(value = "Домой")
    private Button homeButton;
    @FindBy(xpath = ".//*[@title='find owners']")
    @Title(value = "Найти хозяев")
    private Button findOwnersButton;

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

    @Override
    public SelenideElement getRoot() {
        return $(xpath("//*[@id='main-navbar']"));
    }
}
