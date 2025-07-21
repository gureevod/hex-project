package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;
import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Унифицированная фабрика для создания UI элементов
 */
public class ElementFactory {
    private static final Logger LOGGER = Logger.getLogger(ElementFactory.class.getName());

    // Реестр реализаций по умолчанию
    private static final Map<Class<?>, Function<SelenideElement, Object>> DEFAULT_IMPLEMENTATIONS = new HashMap<>();

    static {
        // Регистрация стандартных реализаций
        DEFAULT_IMPLEMENTATIONS.put(InputField.class, InputFieldImpl::new);
        DEFAULT_IMPLEMENTATIONS.put(Button.class, ButtonImpl::new);
    }

    /**
     * Создает элемент UI на основе Selenide-элемента и целевого типа
     */
    public static Object createWrapper(SelenideElement element, Field field) {
        // Проверяем наличие кастомной реализации через аннотацию
        ElementImpl customImpl = field.getAnnotation(ElementImpl.class);
        if (customImpl != null) {
            return createCustomImplementation(element, customImpl);
        }

        // Используем реализацию по умолчанию
        return createDefaultImplementation(element, field.getType());
    }

    private static Object createDefaultImplementation(SelenideElement element, Class<?> fieldType) {
        return DEFAULT_IMPLEMENTATIONS.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(fieldType))
                .findFirst()
                .map(entry -> entry.getValue().apply(element))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Не найдена реализация для типа: " + fieldType.getName()));
    }

    private static Object createCustomImplementation(SelenideElement element, ElementImpl annotation) {
        Class<?> implClass = annotation.value();

        try {
            Constructor<?> constructor = implClass.getConstructor(SelenideElement.class);
            return constructor.newInstance(element);
        } catch (Exception e) {
            LOGGER.severe("Ошибка создания кастомной реализации: " + implClass.getName());
            throw new RuntimeException("Ошибка создания кастомной реализации: " + implClass.getName(), e);
        }
    }
}
