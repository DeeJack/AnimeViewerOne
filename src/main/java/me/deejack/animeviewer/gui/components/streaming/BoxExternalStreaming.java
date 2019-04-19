package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class BoxExternalStreaming extends HBox {
  public BoxExternalStreaming() {
    setSpacing(20);
    TextField textField = new TextField();
    textField.setPrefWidth(350);
    textField.setPromptText(LocalizedApp.getInstance().getString("OpenExternalVidPromptText"));
    getChildren().addAll(textField, new ButtonExternalStreaming(textField));
  }

  class ButtonExternalStreaming extends Button {
    ButtonExternalStreaming(TextField textField) {
      super(LocalizedApp.getInstance().getString("OpenExternalVidButtonText"));
      setOnAction((event) -> {
        showWaitAndLoad();
        new AnimePlayer(textField.getText());
      });
      setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenExtVideo")));
      setWidth(Button.USE_COMPUTED_SIZE);
    }
  }
}

