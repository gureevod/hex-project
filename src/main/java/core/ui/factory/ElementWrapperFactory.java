package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;

import java.lang.reflect.Field;

public class ElementWrapperFactory {
    private static final DefaultElementWrapperStrategy defaultStrategy = new DefaultElementWrapperStrategy();
    private static final CustomElementWrapperStrategy customStrategy = new CustomElementWrapperStrategy();

    public static Object createElementWrapper(SelenideElement element, Field field) {
        ElementImpl elementImplAnnotation = field.getAnnotation(ElementImpl.class);
        return createElementWrapper(element, field.getType(), elementImplAnnotation);
    }

    public static Object createElementWrapper(SelenideElement element, Class<?> fieldType, ElementImpl elementImplAnnotation) {
        if (elementImplAnnotation != null) {
            return customStrategy.createElementWrapper(element, fieldType, elementImplAnnotation);
        } else {
            return defaultStrategy.createElementWrapper(element, fieldType);
        }
    }
}
