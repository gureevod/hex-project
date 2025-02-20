package core.ui.implementation;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import core.ui.interfaces.UiElement;
import groovy.util.logging.Slf4j;
import io.qameta.allure.Step;
import org.jspecify.annotations.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@Slf4j
public class UiElementImpl implements UiElement {

    protected static final Logger logger = LoggerFactory.getLogger(UiElementImpl.class);

    @NonNull
    protected final SelenideElement element;

    public UiElementImpl(@NonNull SelenideElement element) {
        this.element = element;
    }

    @Override
    @Step("Проверка, отображается ли элемент")
    public boolean isDisplayed() {
        logger.info("Проверка, отображается ли элемент: {}", element);
        return element.isDisplayed();
    }

    @Override
    @Step("Проверка, существует ли элемент")
    public boolean exists() {
        logger.info("Проверка, существует ли элемент: {}", element);
        return element.exists();
    }

    @Override
    @Step("Проверка, активен ли элемент")
    public boolean isEnabled() {
        logger.info("Проверка, активен ли элемент: {}", element);
        return element.isEnabled();
    }

    @Override
    @NonNull
    public SelenideElement getElement() {
        logger.info("Получение элемента: {}", element);
        return element;
    }

    @Override
    @Step("Клик по элементу")
    public void click() {
        logger.info("Клик по элементу: {}", element);
        element.click();
    }

    @Override
    @Step("Наведение на элемент")
    public void hover() {
        logger.info("Наведение на элемент: {}", element);
        element.hover();
    }

    @Override
    @Step("Двойной клик по элементу")
    public void doubleClick() {
        logger.info("Двойной клик по элементу: {}", element);
        element.doubleClick();
    }

    @Override
    @Step("Получение текста элемента")
    public String getText() {
        logger.info("Получение текста элемента: {}", element);
        return element.getText();
    }

    @Override
    @Step("Получение атрибута '{name}' элемента")
    public String getAttribute(String name) {
        logger.info("Получение атрибута '{}' элемента: {}", name, element);
        return element.getAttribute(name);
    }

    @Override
    @Step("Прокрутка к элементу")
    public void scrollIntoView() {
        logger.info("Прокрутка к элементу: {}", element);
        element.scrollIntoView(true);
    }

    @Override
    @Step("Ожидание видимости элемента в течение {timeout}")
    public void waitUntilVisible(Duration timeout) {
        logger.info("Ожидание видимости элемента в течение {}: {}", timeout, element);
        element.shouldBe(Condition.visible, timeout);
    }

    @Override
    @Step("Ожидание исчезновения элемента в течение {timeout}")
    public void waitUntilDisappear(Duration timeout) {
        logger.info("Ожидание исчезновения элемента в течение {}: {}", timeout, element);
        element.shouldBe(Condition.disappear, timeout);
    }

    @Override
    @Step("Ожидание выполнения условия '{condition}' в течение {timeout}")
    public void waitUntil(WebElementCondition condition, Duration timeout) {
        logger.info("Ожидание выполнения условия '{}' в течение {}: {}", condition, timeout, element);
        element.should(condition, timeout);
    }

    @Override
    @Step("Фокусировка на элементе")
    public void focus() {
        logger.info("Фокусировка на элементе: {}", element);
        element.sendKeys("\t");
    }

    @Override
    @Step("Снятие фокуса с элемента")
    public void blur() {
        logger.info("Снятие фокуса с элемента: {}", element);
        element.sendKeys("\t");
    }

    @Override
    @Step("Проверка, что элемент соответствует условию '{condition}'")
    public void shouldBe(WebElementCondition condition) {
        logger.info("Проверка, что элемент соответствует условию '{}': {}", condition, element);
        element.shouldBe(condition);
    }

    @Override
    @Step("Проверка, что элемент имеет условие '{condition}'")
    public void shouldHave(WebElementCondition condition) {
        logger.info("Проверка, что элемент имеет условие '{}': {}", condition, element);
        element.shouldHave(condition);
    }

    @Override
    @Step("Проверка, что элемент не имеет условия '{condition}'")
    public void shouldNotHave(WebElementCondition condition) {
        logger.info("Проверка, что элемент не имеет условия '{}': {}", condition, element);
        element.shouldNotHave(condition);
    }
}
