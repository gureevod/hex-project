package core.ui.implementation.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import core.ui.implementation.UiElementImpl;
import core.ui.interfaces.table.Cell;
import core.ui.interfaces.table.Row;
import core.ui.interfaces.table.Table;
import org.openqa.selenium.NoSuchElementException;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RowImpl extends UiElementImpl implements Row {
    private final SelenideElement element;
    private final Table parentTable;

    public RowImpl(SelenideElement element, Table parentTable) {
        super(element);
        this.element = element;
        this.parentTable = parentTable;
    }

    @Override
    public Cell cell(int columnIndex) {
        logger.info("Getting cell at index {}", columnIndex);
        SelenideElement cellElement = element.$$("td").get(columnIndex);
        return new CellImpl(cellElement, columnIndex, this);
    }

    @Override
    public Cell cell(String columnHeader) {
        logger.info("Getting cell for column '{}'", columnHeader);
        int columnIndex = parentTable.columnIndex(columnHeader);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Invalid column header: " + columnHeader);
        }
        return cell(columnIndex);
    }

    @Override
    public List<Cell> cells() {
        logger.info("Getting all cells in row");
        ElementsCollection cellElements = element.$$("td");
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < cellElements.size(); i++) {
            cells.add(new CellImpl(cellElements.get(i), i, this));
        }
        return cells;
    }

    @Override
    public Row shouldContainText(String expectedText) {
        logger.info("Verifying row contains text '{}'", expectedText);
        element.shouldHave(Condition.text(expectedText));
        return this;
    }

    @Override
    public Row shouldHaveValue(String columnHeader, String expectedValue) {
        logger.info("Verifying column '{}' has value '{}'", columnHeader, expectedValue);
        cell(columnHeader).shouldContain(expectedValue);
        return this;
    }

    @Override
    public int index() {
        ElementsCollection rows = parentTable.getElement().$$("tbody tr");
        return IntStream.range(0, rows.size())
                .filter(i -> rows.get(i).equals(element))
                .findFirst()
                .orElse(-1); // Return -1 if the element is not found
    }

    @Override
    public Table parentTable() {
        return parentTable;
    }

    @Override
    public Cell findCellByText(String text) {
        logger.info("Finding first cell with text '{}' in row", text);
        return cells().stream()
                .filter(cell -> cell.text().contains(text))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cell with text '" + text + "' not found in row"));
    }

    @Override
    public List<Cell> findCells(Predicate<Cell> condition) {
        logger.info("Finding cells in row matching custom condition");
        return cells().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}
