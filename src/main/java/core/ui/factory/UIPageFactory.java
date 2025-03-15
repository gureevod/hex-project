package core.ui.factory;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;
import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import project.ui.components.BaseComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class UIPageFactory {

    /**
     * Initializes the elements in a page object or component using @FindBy annotations
     */
    public static <T> T initElements(T pageObjectOrComponent) {
        Class<?> currentClass = pageObjectOrComponent.getClass();

        // Process all fields in the class and its superclasses
        while (currentClass != null && !currentClass.equals(Object.class)) {
            processFields(pageObjectOrComponent, currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return pageObjectOrComponent;
    }

    private static <T> void processFields(T pageObjectOrComponent, Class<?> clazz) {
        // Process all fields in the class
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                // First check if field is a component
                if (BaseComponent.class.isAssignableFrom(field.getType())) {
                    // Get the component instance, or create one if null
                    Object component = field.get(pageObjectOrComponent);
                    if (component == null) {
                        try {
                            // Create a new instance of the component
                            component = field.getType().getDeclaredConstructor().newInstance();
                            field.set(pageObjectOrComponent, component);
                        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                            throw new RuntimeException("Failed to instantiate component: " + field.getName(), e);
                        }
                    }

                    // Recursively initialize the component's elements
                    initElements(component);
                }
                // Check if the field has a FindBy annotation
                else if (field.isAnnotationPresent(FindBy.class)) {
                    initElementWithAnnotation(pageObjectOrComponent, field);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
    }

    private static <T> void initElementWithAnnotation(T pageObjectOrComponent, Field field)
            throws IllegalAccessException {
        FindBy findBy = field.getAnnotation(FindBy.class);
        SelenideElement element = null;

        // Create the appropriate SelenideElement based on the annotation
        if (!findBy.xpath().isEmpty()) {
            if (BaseComponent.class.isAssignableFrom(pageObjectOrComponent.getClass()) && findBy.xpath().startsWith(".")) {
                SelenideElement root = ((BaseComponent) pageObjectOrComponent).getRoot();
                element = root.$(By.xpath(findBy.xpath()));
            } else {
                element = Selenide.$(By.xpath(findBy.xpath()));
            }
        } else if (!findBy.id().isEmpty()) {
            element = Selenide.$(By.id(findBy.id()));
        } else if (!findBy.css().isEmpty()) {
            element = Selenide.$(By.cssSelector(findBy.css()));
        } // Add other locator types as needed

        if (element != null) {
            // Check if there's a custom implementation specified
            if (field.isAnnotationPresent(ElementImpl.class)) {
                ElementImpl implAnnotation = field.getAnnotation(ElementImpl.class);
                Object elementWrapper = createCustomElementWrapper(element, implAnnotation);
                field.set(pageObjectOrComponent, elementWrapper);
            } else if (!field.isAnnotationPresent(ElementImpl.class)){
                // Use the default implementation based on type
                Object elementWrapper = createDefaultElementWrapper(element, field.getType());
                field.set(pageObjectOrComponent, elementWrapper);
            }
        }
    }

    private static Object createCustomElementWrapper(SelenideElement element, ElementImpl annotation) {
        Class<?> implClass = annotation.value();
        Class<?>[] constructorParamTypes = annotation.constructorParams();

        try {
            // Handle default constructor param type
            if (constructorParamTypes.length == 1 && constructorParamTypes[0] == Object.class) {
                constructorParamTypes = new Class<?>[] { SelenideElement.class };
            }

            // Get constructor and create instance
            Constructor<?> constructor = implClass.getConstructor(constructorParamTypes);
            return constructor.newInstance(element);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create custom implementation: " + implClass.getName(), e);
        }
    }

    private static Object createDefaultElementWrapper(SelenideElement element, Class<?> fieldType) {
        if (fieldType.isAssignableFrom(InputField.class)) {
            return new InputFieldImpl(element);
        } else if (fieldType.isAssignableFrom(Button.class)) {
            return new ButtonImpl(element);
        }
        // Add other UI element types as needed

        throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
    }
}
