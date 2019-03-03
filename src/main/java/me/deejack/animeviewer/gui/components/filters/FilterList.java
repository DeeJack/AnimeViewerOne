package me.deejack.animeviewer.gui.components.filters;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FilterList extends VBox {
  private final Filter[] filters;

  public FilterList() {
    Map<String, String> items = new HashMap<>();
    items.put("asd", "sda");
    items.put("asd2", "sda2");
    filters = new Filter[]{
            new MultiSelectionFilter("asd", "Multi", items),
            new ComboBoxFilter("Test", "Generi", items),
            new TextBoxFilter("Test", "Test", "Sort"),
            new TextBoxFilter("Test", "Test", "Cerca"),
    };
    initialize();
  }

  public FilterList(Filter... filters) {
    this.filters = filters;
    addChildren();
  }

  private void initialize() {
    addChildren();
    setSpacing(30);
    setPadding(new Insets(20));
  }

  public void addChildren() {
    for (Filter filter : filters) {
      Label label = new Label(filter.getLabel());
      label.setLabelFor(filter.getNode());
      HBox box = new HBox(label, filter.getNode());
      box.setSpacing(50);

      getChildren().add(box);
    }
    Button button = new Button("Applica filtri");

    getChildren().add(button);
  }
}
