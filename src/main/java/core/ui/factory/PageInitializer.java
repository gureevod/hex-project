package core.ui.factory;

import com.codeborne.selenide.SelenideElement;
import core.ui.annotations.Title;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import project.ui.components.BaseComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * Отвечает за инициализацию Page Objects и компонентов
 */
@Slf4j
public class PageInitializer {
    private static final Logger LOGGER = Logger.getLogger(PageInitializer.class.getName());

    /**
     * Инициализирует все поля в Page Object или компоненте
     */
    public static <T> T initialize(T pageObjectOrComponent) {
        Class<?> currentClass = pageObjectOrComponent.getClass();

        // Обрабатываем иерархию классов
        while (currentClass != null && !currentClass.equals(Object.class)) {
            initializeFields(pageObjectOrComponent, currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return pageObjectOrComponent;
    }

    /**
     * Инициализирует поля конкретного класса
     */
    private static <T> void initializeFields(T pageObjectOrComponent, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            try {
                // Инициализация компонентов
                if (BaseComponent.class.isAssignableFrom(field.getType())) {
                    initializeComponent(pageObjectOrComponent, field);
                }
                // Инициализация элементов с аннотацией @FindBy
                else if (field.isAnnotationPresent(FindBy.class)) {
                    initializeElement(pageObjectOrComponent, field);
                }

                // Обработка аннотации @Title если есть
                if (field.isAnnotationPresent(Title.class)) {
                    applyTitle(pageObjectOrComponent, field);
                }
            } catch (Exception e) {
                LOGGER.warning("Ошибка инициализации поля: " + field.getName() + " - " + e.getMessage());
            }
        }
    }

    /**
     * Инициализирует компонент
     */
    private static <T> void initializeComponent(T pageObjectOrComponent, Field field) throws Exception {
        Object component = field.get(pageObjectOrComponent);

        // Создаем экземпляр компонента если null
        if (component == null) {
            component = field.getType().getDeclaredConstructor().newInstance();
            field.set(pageObjectOrComponent, component);
        }

        // Рекурсивная инициализация компонента
        initialize(component);
    }

    /**
     * Инициализирует UI элемент
     */
    private static <T> void initializeElement(T pageObjectOrComponent, Field field) throws Exception {
        // Находим элемент с помощью @FindBy
        SelenideElement element = ElementLocator.findElement(pageObjectOrComponent, field);

        if (element != null) {
            // Создаем подходящую обертку для элемента
            Object wrapper = ElementFactory.createWrapper(element, field);
            field.set(pageObjectOrComponent, wrapper);
        }
    }

    /**
     * Применяет заголовок к элементу или компоненту
     */
    private static <T> void applyTitle(T pageObjectOrComponent, Field field) throws Exception {
        Title titleAnnotation = field.getAnnotation(Title.class);
        String titleValue = titleAnnotation.value();

        Object fieldValue = field.get(pageObjectOrComponent);
        if (fieldValue == null) return;

        try {
            Method setTitleMethod = fieldValue.getClass().getMethod("setTitle", String.class);
            setTitleMethod.invoke(fieldValue, titleValue);
        } catch (NoSuchMethodException e) {
            LOGGER.fine("Метод 'setTitle' не найден в " + field.getType().getName());
        }
    }
}
