package core.ui.interfaces.table;



import core.ui.interfaces.UiElement;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface Table extends UiElement {
    // Core navigation
    List<Row> rows();
    Row row(int index);

    // Header-based operations
    Table withColumnMapping(Map<String, Integer> customColumnMapping);
    List<String> headers();
    int columnIndex(String headerName);

    // Row finding operations
    Row firstRowWhere(String columnHeader, String value);
    List<Row> allRowsWhere(String columnHeader, String value);
    Row firstRowWithText(String text);
    List<Row> rows(Predicate<Row> filterCondition);

    // Cell finding operations
    Cell findCell(int rowIndex, int columnIndex);
    Cell findCellByText(String text);
    List<Cell> findAllCellsByText(String text);
    List<Cell> findCells(Predicate<Cell> condition);
    List<Cell> getAllCells();

    // Bulk operations
    Table shouldHaveRows(int expectedCount);
    Table shouldContainRowWith(String columnHeader, String value);
}
