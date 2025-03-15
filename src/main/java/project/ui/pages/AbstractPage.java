package project.ui.pages;

public interface AbstractPage<T extends AbstractPage<T>> {
    T checkThatPageLoaded();
    String getUrl();
    String getTitle();
}
