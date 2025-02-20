package core.ui.implementation;

import com.codeborne.selenide.SelenideElement;
import core.ui.interfaces.InputField;
import org.jspecify.annotations.NonNull;


public class InputFieldImpl extends UiElementImpl implements InputField {
    public InputFieldImpl(@NonNull SelenideElement element) {
        super(element);
    }

    @Override
    public void clearField() {
        element.clear();
    }

    @Override
    public void setText(String text) {
        element.sendKeys(text);
    }
}
