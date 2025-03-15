package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.ElementImpl;

public interface ElementWrapperStrategy {
    //If all wrappers needed only selenide element
    default Object createElementWrapper(SelenideElement element, Class<?> fieldType) {
        throw new UnsupportedOperationException("This method should not be called directly.Implement it in Strategy if you have to");
    }

    default Object createElementWrapper(SelenideElement element, Class<?> fieldtype, ElementImpl elementImplAnnotation) {
        throw new UnsupportedOperationException("This method should not be called directly.Implement it in Strategy if you have to");
    }

}
