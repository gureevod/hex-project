package core.ui.factory;

public class PageFactory {
    public static <T> T initElements(T pageObjectOrComponent) {
        ComponentInitializer.initialize(pageObjectOrComponent);
        return pageObjectOrComponent;
    }
}
