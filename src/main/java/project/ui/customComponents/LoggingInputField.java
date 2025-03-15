package project.ui.customComponents;

import com.codeborne.selenide.SelenideElement;
import core.ui.implementation.InputFieldImpl;
import groovy.util.logging.Slf4j;
import org.jspecify.annotations.NonNull;

@Slf4j
public class LoggingInputField extends InputFieldImpl {
    public LoggingInputField(@NonNull SelenideElement element) {
        super(element);
        System.out.println("Я логирующий инпут!");
    }

    @Override
    public void setText(String text) {
        element.sendKeys("skibidi always!");
    }
}
