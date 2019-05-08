package me.deejack.animeviewer.gui.components.streaming.bottombar;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class BoxExternalStreaming extends GridPane {
  public BoxExternalStreaming() {
    TextField textField = new TextField();
    Button button = new ButtonExternalStreaming(textField);
    createConstraints();
    textField.setPromptText(LocalizedApp.getInstance().getString("OpenExternalVidPromptText"));
    add(textField, 1, 0);
    add(button, 3, 0);
  }

  private void createConstraints() {
    ColumnConstraints first = new ColumnConstraints();
    first.setPercentWidth(30);
    ColumnConstraints second = new ColumnConstraints();
    first.setPercentWidth(70);
    getColumnConstraints().addAll(new ColumnConstraints(10), first, new ColumnConstraints(5),
            second, new ColumnConstraints(10));
  }

  class ButtonExternalStreaming extends Button {
    ButtonExternalStreaming(TextField textField) {
      super(LocalizedApp.getInstance().getString("OpenExternalVidButtonText"));
      setOnAction((event) -> {
        showWaitAndLoad();
        new AnimePlayer(textField.getText());
      });
      setTooltip(new Tooltip(LocalizedApp.getInstance().getString("OpenExtVideo")));
    }
  }
}

