package core.ui.interfaces.table;



import core.ui.interfaces.UiElement;

import java.util.List;
import java.util.function.Predicate;

public interface Row extends UiElement {
    // Cell access
    Cell cell(int columnIndex);

    Cell cell(String columnHeader);

    List<Cell> cells();

    // Row verification
    Row shouldContainText(String expectedText);

    Row shouldHaveValue(String columnHeader, String expectedValue);

    // Cell finding methods
    Cell findCellByText(String text);

    List<Cell> findCells(Predicate<Cell> condition);

    // Row metadata
    int index();

    Table parentTable();
}
