package project.ui.pages;

import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;
import lombok.Getter;
import org.openqa.selenium.By;
import project.ui.components.HeaderComponent;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class AddOwnerPage extends BasePage<AddOwnerPage> {
    public final HeaderComponent header;
    public final InputField firstNameField;
    public final InputField lastNameField;
    public final InputField addressField;
    public final InputField cityField;
    public final InputField telephoneField;
    public final Button addOwnerButton;

    private AddOwnerPage() {
        header = new HeaderComponent();
        firstNameField = new InputFieldImpl($(By.xpath("//*[@id='firstName']")));
        lastNameField = new InputFieldImpl($(By.xpath("//*[@id='lastName']")));
        addressField = new InputFieldImpl($(By.xpath("//*[@id='address']")));
        cityField = new InputFieldImpl($(By.xpath("//*[@id='city']")));
        telephoneField = new InputFieldImpl($(By.xpath("//*[@id='telephone']")));
        addOwnerButton = new ButtonImpl($(By.xpath("//*[@id='add-owner-form']/div[2]/div/button")));
    }

    public static AddOwnerPage get() {
        return new AddOwnerPage();
    }

    @Override
    public AddOwnerPage checkThatPageLoaded() {
        cityField.shouldBe(visible);
        addOwnerButton.shouldBe(visible);
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
