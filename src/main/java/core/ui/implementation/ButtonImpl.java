package core.ui.implementation;

import com.codeborne.selenide.SelenideElement;
import core.ui.interfaces.Button;
import io.qameta.allure.Step;
import org.jspecify.annotations.NonNull;


public class ButtonImpl extends UiElementImpl implements Button {

    public ButtonImpl(@NonNull SelenideElement element) {
        super(element);
    }

    @Override
    @Step("Клик по кнопке")
    public void click() {
        logger.info("Клик по кнопке: {}", element);
        element.click();
    }
}
