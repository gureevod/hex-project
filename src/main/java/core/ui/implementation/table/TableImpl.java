package core.ui.implementation.table;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;

import core.ui.implementation.UiElementImpl;
import core.ui.interfaces.table.Cell;
import core.ui.interfaces.table.Row;
import core.ui.interfaces.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;

public class TableImpl extends UiElementImpl implements Table {
    private static final Logger logger = LoggerFactory.getLogger(TableImpl.class);
    private final SelenideElement element;
    private final Map<String, Integer> columnMapping;
    private final List<String> headers;

    public TableImpl(SelenideElement element) {
        super(element);
        this.element = element;
        this.headers = parseHeaders();
        this.columnMapping = createDefaultColumnMapping();
    }

    private TableImpl(SelenideElement element, Map<String, Integer> columnMapping, List<String> headers) {
        super(element);
        this.element = element;
        this.columnMapping = columnMapping;
        this.headers = headers;
    }

    private List<String> parseHeaders() {
        return element.$$("thead th").texts();
    }

    private Map<String, Integer> createDefaultColumnMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            mapping.put(headers.get(i), i);
        }
        return mapping;
    }

    @Override
    public List<Row> rows() {
        logger.info("Getting all table rows");
        return element.$$("tbody tr").stream()
                .map(rowElement -> new RowImpl(rowElement, this))
                .collect(Collectors.toList());
    }

    @Override
    public Row row(int index) {
        logger.info("Getting row at index {}", index);
        SelenideElement rowElement = element.$$("tbody tr").get(index);
        return new RowImpl(rowElement, this);
    }

    @Override
    public Table withColumnMapping(Map<String, Integer> customColumnMapping) {
        logger.info("Applying custom column mapping: {}", customColumnMapping);
        Map<String, Integer> newMapping = new HashMap<>(columnMapping);
        newMapping.putAll(customColumnMapping);
        return new TableImpl(element, newMapping, headers);
    }

    @Override
    public List<String> headers() {
        logger.info("Getting table headers");
        return new ArrayList<>(headers);
    }

    @Override
    public int columnIndex(String headerName) {
        logger.info("Getting column index for header '{}'", headerName);
        return columnMapping.getOrDefault(headerName, -1);
    }

    @Override
    public Row firstRowWhere(String columnHeader, String value) {
        logger.info("Finding first row where column '{}' equals '{}'", columnHeader, value);
        return rows().stream()
                .filter(row -> row.cell(columnHeader).text().equals(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Row not found"));
    }

    @Override
    public List<Row> allRowsWhere(String columnHeader, String value) {
        logger.info("Finding all rows where column '{}' equals '{}'", columnHeader, value);
        return rows().stream()
                .filter(row -> row.cell(columnHeader).text().equals(value))
                .collect(Collectors.toList());
    }

    @Override
    public Row firstRowWithText(String text) {
        logger.info("Finding first row containing text '{}'", text);
        return rows().stream()
                .filter(row -> row.getText().contains(text))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Row not found"));
    }

    @Override
    public List<Row> rows(Predicate<Row> filterCondition) {
        logger.info("Filtering rows with custom condition");
        return rows().stream()
                .filter(filterCondition)
                .collect(Collectors.toList());
    }

    @Override
    public Table shouldHaveRows(int expectedCount) {
        logger.info("Verifying table has {} rows", expectedCount);
        element.$$("tbody tr").shouldHave(CollectionCondition.size(expectedCount));
        return this;
    }

    @Override
    public Table shouldContainRowWith(String columnHeader, String value) {
        logger.info("Verifying table contains row with '{}' = '{}'", columnHeader, value);
        int colIndex = columnIndex(columnHeader);
        element.$$("tbody tr").shouldHave(CollectionCondition.anyMatch(
                "Row with value " + value,
                row -> $(row).$$("td").get(colIndex).text().equals(value)
        ));
        return this;
    }

    @Override
    public Cell findCell(int rowIndex, int columnIndex) {
        logger.info("Finding cell at row {}, column {}", rowIndex, columnIndex);
        return row(rowIndex).cell(columnIndex);
    }

    @Override
    public Cell findCellByText(String text) {
        logger.info("Finding first cell with text '{}'", text);
        return getAllCells().stream()
                .filter(cell -> cell.text().contains(text))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cell with text '" + text + "' not found"));
    }

    @Override
    public List<Cell> findAllCellsByText(String text) {
        logger.info("Finding all cells with text '{}'", text);
        return getAllCells().stream()
                .filter(cell -> cell.text().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cell> findCells(Predicate<Cell> condition) {
        logger.info("Finding cells matching custom condition");
        return getAllCells().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cell> getAllCells() {
        logger.info("Getting all cells in table");
        return rows().stream()
                .flatMap(row -> row.cells().stream())
                .collect(Collectors.toList());
    }
}
