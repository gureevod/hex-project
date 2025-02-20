package project.ui.pages;

import com.codeborne.selenide.Selenide;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config appConfig = ConfigFactory.load();

    public abstract T checkThatPageLoaded();
    public abstract String getUrl();
    public abstract String getTitle();
    public T open(String... dynamicPart) {
        Selenide.open(getUrl().formatted(dynamicPart));
        return checkThatPageLoaded();
    }
}
