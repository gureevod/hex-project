package core.ui.factory;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import project.ui.components.BaseComponent;

import java.lang.reflect.Field;

public class FindByAnnotationProcessor {

    public static SelenideElement findSelenideElement(Object pageObjectOrComponent, Field field) {
        FindBy findBy = field.getAnnotation(FindBy.class);

        SelenideElement element = null;

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
        }
        return element;
    }
}
