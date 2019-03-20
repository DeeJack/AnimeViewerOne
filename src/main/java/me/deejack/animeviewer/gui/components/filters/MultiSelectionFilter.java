package me.deejack.animeviewer.gui.components.filters;

import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class MultiSelectionFilter extends ListView<MultiComboItem> implements Filter {
  private final Map<String, String> items;
  private final String label;
  private final String filterId;

  public MultiSelectionFilter(String filterId, String label, Map<String, String> items) {
    this.filterId = filterId;
    this.label = label;
    this.items = items;
    setPrefWidth(150);
    getItems().addAll(
            items.keySet().stream().map(MultiComboItem::new).collect(Collectors.toList()));
    setCellFactory(itemListView -> createListCell());
  }

  private ListCell<MultiComboItem> createListCell() {
    ListCell<MultiComboItem> cell = new ListCell<MultiComboItem>() {
      @Override
      protected void updateItem(MultiComboItem item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && item != null) {
          CheckBox checkBox = new CheckBox(item.getText());
          checkBox.selectedProperty().bind(item.getBooleanProperty());
          checkBox.setDisable(true);
          checkBox.setOpacity(1);
          setGraphic(checkBox);
        }
      }
    };
    cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event ->
            cell.getItem().getBooleanProperty().set(!cell.getItem().getBooleanProperty().get()));
    return cell;
  }

  @Override
  public String getFilterValue() {
    return null;
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
    return null;
  }
}
