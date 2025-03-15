package core.jupiter.extensions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import core.guice.PageElementModule;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import project.ui.pages.AbstractPage;

import java.lang.reflect.Field;

public class PageInitializerExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Injector injector = Guice.createInjector(new PageElementModule());

        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (AbstractPage.class.isAssignableFrom(field.getType())) {
                boolean wasAccessible = field.canAccess(testInstance);
                try {
                    field.setAccessible(true);
                    Object page = injector.getInstance(field.getType());
                    field.set(testInstance, page);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        }
    }
}
