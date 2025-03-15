package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CustomElementWrapperStrategy implements ElementWrapperStrategy {

    @Override
    public Object createElementWrapper(SelenideElement element, Class<?> fieldType, ElementImpl elementImplAnnotation) {
        if (elementImplAnnotation == null) {
            throw new IllegalArgumentException("ElementImpl annotation is required for custom wrapper strategy.");
        }

        Class<?> implClass = elementImplAnnotation.value();
        Class<?>[] constructorParamTypes = elementImplAnnotation.constructorParams();

        try {
            if (constructorParamTypes.length == 1 && constructorParamTypes[0] == Object.class) {
                constructorParamTypes = new Class<?>[] { SelenideElement.class };
            }

            Constructor<?> constructor = implClass.getConstructor(constructorParamTypes);
            return constructor.newInstance(element);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Failed to create custom implementation: " + implClass.getName(), e);
        }
    }
}
