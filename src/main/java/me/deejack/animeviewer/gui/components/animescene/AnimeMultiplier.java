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

  public AnimeMultiplier(int startElement) {
    getChildren().addAll(new Label("Anime per pagina: "), createCombo(startElement));
  }

  private ComboBox<Integer> createCombo(int startElement) {
    List<Integer> multipliers = new ArrayList<>();
    for (int i = 1; i < 5; i++) {
      multipliers.add(i);
    }
    ComboBox<Integer> comboBox = new ComboBox<>(new ObservableListWrapper<>(multipliers));
    comboBox.setPrefWidth(150);
    comboBox.getSelectionModel().select((Integer) startElement);
    comboBox.getSelectionModel().selectedItemProperty().addListener((event, oldValue, newValue) ->
            selectionListeners.forEach(listener -> listener.changed(event, oldValue, newValue)));
    return comboBox;
  }

  public void addSelectionListener(ChangeListener<Integer> selectionListener) {
    selectionListeners.add(selectionListener);
  }
}
