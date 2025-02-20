package core.ui.implementation.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import core.ui.implementation.UiElementImpl;
import core.ui.interfaces.table.Cell;
import core.ui.interfaces.table.Row;


public class CellImpl extends UiElementImpl implements Cell {
    private final SelenideElement element;
    private final int columnIndex;
    private final Row parentRow;

    public CellImpl(SelenideElement element, int columnIndex, Row parentRow) {
        super(element);
        this.element = element;
        this.columnIndex = columnIndex;
        this.parentRow = parentRow;
    }

    @Override
    public String text() {
        logger.info("Getting cell text");
        return element.getText();
    }

    @Override
    public String header() {
        logger.info("Getting cell header");
        return parentRow.parentTable().headers().get(columnIndex);
    }

    @Override
    public Cell shouldContain(String expectedText) {
        logger.info("Verifying cell contains text '{}'", expectedText);
        element.shouldHave(Condition.text(expectedText));
        return this;
    }

    @Override
    public Cell shouldMatch(String regex) {
        logger.info("Verifying cell matches regex '{}'", regex);
        element.shouldHave(Condition.matchText(regex));
        return this;
    }

    @Override
    public Row parentRow() {
        return parentRow;
    }

    @Override
    public int columnIndex() {
        return columnIndex;
    }
}
