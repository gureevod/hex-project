package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.implementation.ButtonImpl;
import core.ui.implementation.InputFieldImpl;
import core.ui.interfaces.Button;
import core.ui.interfaces.InputField;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultElementWrapperStrategy implements ElementWrapperStrategy {

    private final Map<Class<?>, Function<SelenideElement, Object>> wrapperMap = new HashMap<>();

    public DefaultElementWrapperStrategy() {
        wrapperMap.put(InputField.class, InputFieldImpl::new);
        wrapperMap.put(Button.class, ButtonImpl::new);
    }

    @Override
    public Object createElementWrapper(SelenideElement element, Class<?> fieldType) {
        return wrapperMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(fieldType))
                .findFirst()
                .map(entry -> entry.getValue().apply(element))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported field type: " + fieldType.getName()));
    }
}