package me.deejack.animeviewer.gui.components.filters;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import me.deejack.animeviewer.logic.filters.Filter;

import java.util.Map;

public class ComboBoxFilter extends ComboBox<String> implements Filter {
  private final String label;
  private final Map<String, String> items;
  private final String filterId;

  public ComboBoxFilter(String filterId, String label, Map<String, String> items) {
    this.label = label;
    this.items = items;
    this.filterId = filterId;
    initialize();
  }

  public ComboBoxFilter(String filterId, String label, Map<String, String> items, String selectedKey) {
    this(filterId, label, items);
    getSelectionModel().select(selectedKey);
  }

  private void initialize() {
    getItems().addAll(items.keySet());
    getSelectionModel().select(0);
  }

  @Override
  public String getFilterValue() {
    return items.get(getSelectionModel().getSelectedItem());
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public String getFilterId() {
    return filterId;
  }

  @Override
  public Filter cloneFilter() {
    return new ComboBoxFilter(filterId, label, items, getSelectionModel().getSelectedItem());
  }
}
