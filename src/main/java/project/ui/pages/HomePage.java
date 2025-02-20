package project.ui.pages;

import lombok.Getter;
import project.ui.components.HeaderComponent;


@Getter
public class HomePage extends BasePage<HomePage> {

    public final HeaderComponent header;

    private HomePage() {
        header = new HeaderComponent();
    }

    public static HomePage get() {
        return new HomePage();
    }

    @Override
    public HomePage checkThatPageLoaded() {
        System.out.println("waiting...");
        return this;
    }

    @Override
    public String getUrl() {
        return appConfig.getString("front.host");
    }

    @Override
    public String getTitle() {
        return "HomePage";
    }
}
