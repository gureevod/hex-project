package core.ui.factory;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import project.ui.components.BaseComponent;

import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * Отвечает за поиск Selenide элементов с помощью аннотаций @FindBy
 */
public class ElementLocator {
    private static final Logger LOGGER = Logger.getLogger(ElementLocator.class.getName());

    /**
     * Ищет SelenideElement на основе аннотации @FindBy
     */
    public static SelenideElement findElement(Object context, Field field) {
        FindBy findBy = field.getAnnotation(FindBy.class);
        if (findBy == null) {
            return null;
        }

        try {
            // Поддержка различных типов локаторов
            if (!findBy.xpath().isEmpty()) {
                return findByXpath(context, findBy.xpath());
            } else if (!findBy.id().isEmpty()) {
                return Selenide.$(By.id(findBy.id()));
            } else if (!findBy.css().isEmpty()) {
                return Selenide.$(By.cssSelector(findBy.css()));
            } else if (!findBy.className().isEmpty()) {
                return Selenide.$(By.className(findBy.className()));
            } else if (!findBy.name().isEmpty()) {
                return Selenide.$(By.name(findBy.name()));
            }

            LOGGER.warning("Неподдерживаемый или пустой локатор в аннотации @FindBy у поля: " + field.getName());
            return null;
        } catch (Exception e) {
            LOGGER.severe("Ошибка поиска элемента: " + e.getMessage());
            return null;
        }
    }

    /**
     * Поиск по XPath с учётом контекста (поддержка относительных путей в компонентах)
     */
    private static SelenideElement findByXpath(Object context, String xpath) {
        if (BaseComponent.class.isAssignableFrom(context.getClass()) && xpath.startsWith(".")) {
            // Для относительных XPath в компонентах используем корневой элемент компонента
            SelenideElement root = ((BaseComponent) context).getRoot();
            return root.$(By.xpath(xpath));
        } else {
            return Selenide.$(By.xpath(xpath));
        }
    }
}