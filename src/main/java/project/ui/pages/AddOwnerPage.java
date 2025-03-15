package project.ui.pages;

import core.ui.annotations.ElementImpl;
import core.ui.factory.PageFactory;
import core.ui.factory.UIPageFactory;
import core.ui.interfaces.InputField;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import project.ui.components.HeaderComponent;
import project.ui.customComponents.LoggingInputField;

@Getter
public class AddOwnerPage extends BasePage<AddOwnerPage> {
    public HeaderComponent header;
    @FindBy(xpath = "//*[@id='firstName']")
    @ElementImpl(LoggingInputField.class)
    private InputField firstNameField;

    public AddOwnerPage() {
        PageFactory.initElements(this);
    }

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
