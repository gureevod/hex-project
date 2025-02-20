package project.ui.pages;

import core.ui.implementation.table.TableImpl;
import core.ui.interfaces.table.Table;
import lombok.Getter;
import org.openqa.selenium.By;
import project.ui.components.HeaderComponent;

import static com.codeborne.selenide.Selenide.$;

@Getter
public class OwnerInfoPage extends BasePage<OwnerInfoPage> {
    public HeaderComponent header;
    public Table ownerInfoTable;

    private OwnerInfoPage() {
        header = new HeaderComponent();
        ownerInfoTable = new TableImpl($(By.xpath("/html/body/div/div/table[1]")));
    }

    public static OwnerInfoPage get() {
        return new OwnerInfoPage();
    }

    @Override
    public OwnerInfoPage checkThatPageLoaded() {
        return this;
    }

    @Override
    public String getUrl() {
        return appConfig.getString("front.host") + "/owners/%s";
    }

    @Override
    public String getTitle() {
        return "OwnerInfoPage";
    }
}
