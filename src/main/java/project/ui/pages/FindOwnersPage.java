package project.ui.pages;

import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;
import lombok.Getter;
import org.openqa.selenium.By;
import project.ui.components.HeaderComponent;


import static com.codeborne.selenide.Selenide.$;

@Getter
public class FindOwnersPage extends BasePage<FindOwnersPage>{

    public final HeaderComponent header;
    public final InputField lastName;
    public final Button findOwnerButton;
    public final Button addOwnerButton;

    private FindOwnersPage() {
        header = new HeaderComponent();
        lastName = new InputFieldImpl($(By.xpath("//*[@id='lastName']")));
        findOwnerButton = new ButtonImpl($(By.xpath("//*[@id='search-owner-form']/div[2]/div/button")));
        addOwnerButton = new ButtonImpl($(By.xpath("//*[@id='search-owner-form']/a")));
    }

    public AddOwnerPage clickAddOwnerButton() {
        addOwnerButton.click();
        return AddOwnerPage.get();
    }

    public static FindOwnersPage get() {
        return new FindOwnersPage();
    }

    @Override
    public FindOwnersPage checkThatPageLoaded() {
        return this;
    }

    @Override
    public String getUrl() {
        return appConfig.getString("front.host") + "/owners/find";
    }

    @Override
    public String getTitle() {
        return "Owners Page";
    }
}
