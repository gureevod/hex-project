package core.ui.factory;

/**
 * Фабрика для создания и инициализации Page Objects
 */
public class PageFactory {

    /**
     * Инициализирует все UI элементы и компоненты в Page Object
     */
    public static <T> T initElements(T pageObject) {
        return PageInitializer.initialize(pageObject);
    }

    /**
     * Создает и инициализирует новый экземпляр указанного класса Page Object
     */
    public static <T> T initElements(Class<T> pageClass) {
        try {
            T page = pageClass.getDeclaredConstructor().newInstance();
            return initElements(page);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать экземпляр Page Object: " + pageClass.getName(), e);
        }
    }
}
