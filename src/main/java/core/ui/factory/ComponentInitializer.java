package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;
import core.ui.annotations.Title;
import org.openqa.selenium.support.FindBy;
import project.ui.components.BaseComponent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ComponentInitializer {

    public static <T> void initialize(T pageObjectOrComponent) {
        Class<?> currentClass = pageObjectOrComponent.getClass();

        while (currentClass != null && !currentClass.equals(Object.class)) {
            processFields(pageObjectOrComponent, currentClass);
            currentClass = currentClass.getSuperclass();
        }
    }

    private static <T> void processFields(T pageObjectOrComponent, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                if (BaseComponent.class.isAssignableFrom(field.getType())) {
                    initializeComponent(pageObjectOrComponent, field);
                } else if (field.isAnnotationPresent(FindBy.class)) {
                    initializeElement(pageObjectOrComponent, field);
                }
                processTitleAnnotation(pageObjectOrComponent, field);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }

    private static <T> void initializeComponent(T pageObjectOrComponent, Field field) throws IllegalAccessException {
        Object component = field.get(pageObjectOrComponent);
        if (component == null) {
            try {
                component = field.getType().getDeclaredConstructor().newInstance();
                field.set(pageObjectOrComponent, component);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate component: " + field.getName(), e);
            }
        }
        initialize(component);
    }

    private static <T> void initializeElement(T pageObjectOrComponent, Field field) throws IllegalAccessException {
        SelenideElement element = FindByAnnotationProcessor.findSelenideElement(pageObjectOrComponent, field);
        if (element != null) {
            Object elementWrapper = ElementWrapperFactory.createElementWrapper(element, field);
            field.set(pageObjectOrComponent, elementWrapper);
        }
    }

    private static <T> void processTitleAnnotation(T pageObjectOrComponent, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Title.class)) {
            Title titleAnnotation = field.getAnnotation(Title.class);
            String titleValue = titleAnnotation.value();

            try {
                // Attempt to find a 'setTitle' method on the element or component
                Method setTitleMethod = field.getType().getMethod("setTitle", String.class);
                Object fieldValue = field.get(pageObjectOrComponent);
                if (fieldValue != null) {
                    setTitleMethod.invoke(fieldValue, titleValue);
                }
            } catch (NoSuchMethodException e) {
                System.out.println("Warning: No 'setTitle' method found on class " + field.getType().getName());
            } catch (InvocationTargetException | IllegalArgumentException e) {
                System.err.println("Error setting title on field " + field.getName() + ": " + e.getMessage());
            }
        }
    }
}
