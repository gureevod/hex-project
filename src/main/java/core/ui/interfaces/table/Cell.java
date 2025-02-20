package core.ui.interfaces.table;


import core.ui.interfaces.UiElement;

public interface Cell extends UiElement {
    // Content access
    String text();
    String header();

    // Value verification
    Cell shouldContain(String expectedText);
    Cell shouldMatch(String regex);

    // Relationship
    Row parentRow();
    int columnIndex();
}
