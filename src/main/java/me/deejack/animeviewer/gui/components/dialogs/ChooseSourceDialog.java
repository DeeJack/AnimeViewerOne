package me.deejack.animeviewer.gui.components.dialogs;

import com.sun.javafx.collections.ObservableListWrapper;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.events.Listener;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;

public class ChooseSourceDialog extends Dialog {
  private Listener<StreamingLink> onEvent;
  private ListView<StreamingLink> linkListView;

  public ChooseSourceDialog(List<StreamingLink> links) {
    initialize(new ObservableListWrapper<>(links));
    registerEvents();
  }

  private void initialize(ObservableList<StreamingLink> links) {
    linkListView = new ListView<>(links);
    linkListView.setMinWidth(ListView.USE_COMPUTED_SIZE);
    linkListView.setPrefWidth(ListView.USE_COMPUTED_SIZE);
    //getDialogPane().setMaxWidth(DialogPane.USE_COMPUTED_SIZE);
    getDialogPane().setMaxWidth(400);
    StackPane root = new StackPane(linkListView);
    root.setMinWidth(StackPane.USE_COMPUTED_SIZE);
    root.prefWidthProperty().bind(getDialogPane().widthProperty().subtract(20));
    //root.setMaxWidth(getDialogPane().getMaxWidth());
    getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    getDialogPane().setGraphic(root);
  }

  private void registerEvents() {
    getDialogPane().lookupButton(ButtonType.OK).setOnMouseClicked((event) -> {
      onEvent.onChange(linkListView.getSelectionModel().getSelectedItem());
    });
    getDialogPane().lookupButton(ButtonType.OK).setOnMouseClicked((event) -> {
      onEvent.onChange(null);
    });
    setOnCloseRequest((event) -> {
      onEvent.onChange(linkListView.getSelectionModel().getSelectedItem());
      hideWaitLoad();
    });
  }

  public void setOnEvent(Listener<StreamingLink> onEvent) {
    this.onEvent = onEvent;
  }
}
