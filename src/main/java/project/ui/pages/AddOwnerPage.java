package project.ui.pages;

import core.ui.interfaces.InputField;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import project.ui.components.HeaderComponent;

@Getter
public class AddOwnerPage extends BasePage<AddOwnerPage> {
    public HeaderComponent header;
    @FindBy(xpath = "//*[@id='firstName']")
    private InputField firstNameField;

    public static AddOwnerPage get() {
        return new AddOwnerPage();
    }

    @Override
    public AddOwnerPage checkThatPageLoaded() {
        return this;
    }

    @Override
    public String getUrl() {
        return appConfig.getString("front.host") + "/owners/new";
    }

    @Override
    public String getTitle() {
        return "Add owner";
    }
}
