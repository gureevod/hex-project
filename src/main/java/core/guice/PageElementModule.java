package core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;
import org.openqa.selenium.By;

import java.lang.reflect.Field;

import static com.codeborne.selenide.Selenide.$;

public class PageElementModule extends AbstractModule {

    @Override
    protected void configure() {
        // Bindings for your UI components
    }

    @Provides
    public <T> T providePageInstance(Class<T> pageClass) {
        try {
            T pageInstance = pageClass.getDeclaredConstructor().newInstance();
            initializePageElements(pageInstance);
            return pageInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize page: " + pageClass.getName(), e);
        }
    }

    private void initializePageElements(Object page) {

    }
}
