package core.ui.interfaces;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import org.jspecify.annotations.NonNull;

import java.time.Duration;

public interface UiElement {

    // Fundamental state checks
    boolean isDisplayed();

    boolean exists();

    boolean isEnabled();

    // Core element access
    @NonNull
    SelenideElement getElement();

    // Basic interactions
    void click();

    void doubleClick();

    void hover();

    // Content/attribute retrieval
    String getText();

    String getAttribute(String name);

    // Visibility control
    void scrollIntoView();

    // Waiting mechanisms
    void waitUntilVisible(Duration timeout);

    void waitUntilDisappear(Duration timeout);

    void waitUntil(WebElementCondition condition, Duration timeout);

    // Focus/blur handling
    void focus();

    void blur();

    // Fluent validations (common Conditions)
    void shouldBe(WebElementCondition condition);

    void shouldHave(WebElementCondition condition);

    void shouldNotHave(WebElementCondition condition);

    void setTitle(String title);

    String getTitle();
}
