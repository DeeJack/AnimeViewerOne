package me.deejack.animeviewer.gui.components.animescene;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AnimeMultiplier extends HBox {
  private final List<ChangeListener<Integer>> selectionListeners = new ArrayList<>();
  ComboBox<Integer> comboBox;

  public AnimeMultiplier() {
    getChildren().addAll(new Label("Anime per pagina: "), createCombo());
  }

  private ComboBox<Integer> createCombo() {
    List<Integer> multipliers = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      multipliers.add(i);
    }
    comboBox = new ComboBox<>(new ObservableListWrapper<>(multipliers));
    comboBox.setPrefWidth(150);
    comboBox.getSelectionModel().selectedItemProperty().addListener((event, oldValue, newValue) ->
            selectionListeners.forEach(listener -> listener.changed(event, oldValue, newValue)));
    return comboBox;
  }

  public void select(int item) {
    comboBox.getSelectionModel().select((Integer) item);
  }

  public void addSelectionListener(ChangeListener<Integer> selectionListener) {
    selectionListeners.add(selectionListener);
  }
}
