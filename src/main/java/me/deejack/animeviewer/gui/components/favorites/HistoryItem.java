package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class HistoryItem extends SingleFavorite {
  private final HistoryElement historyElement;

  public HistoryItem(HistoryElement historyElement) {
    super(historyElement.getViewedElement(), historyElement.getViewedElement().getAnimeInformation().getImageUrl(), null);
    this.historyElement = historyElement;
    setOnRemove(getOnRemove());
    setAnimeInfo();
  }

  public EventHandler<ActionEvent> getOnRemove() {
    return (event) -> requestRemoveAllEpisodes();
  }

  private void requestRemoveAllEpisodes() {
    Dialog dialog = new Dialog();
    Label label = new Label(LocalizedApp.getInstance().getString("DeleteOneHistory"));
    CheckBox checkBox = new CheckBox(LocalizedApp.getInstance().getString("DeleteAllHistory"));
    VBox box = new VBox(label, checkBox);
    dialog.getDialogPane().setContent(box);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
    addListeners(dialog, checkBox);
    dialog.showAndWait();
  }

  private void addListeners(Dialog dialog, CheckBox checkBox) {
    addYesButtonListener(dialog, checkBox);
    addNoButtonListener(dialog);
  }

  private void addYesButtonListener(Dialog dialog, CheckBox checkBox) {
    dialog.getDialogPane().lookupButton(ButtonType.YES).setOnMousePressed((event) -> {
      if (checkBox.isSelected() || historyElement.getEpisodesHistory().size() - 1 == 0) {
        History.getHistory().remove(historyElement.getViewedElement());
        ((Pane) getParent()).getChildren().remove(this);
      } else {
        historyElement.removeEpisodeFromHistory(historyElement.getEpisodesHistory().size() - 1);
      }
      FilesUtility.saveHistory();
    });
  }

  private void addNoButtonListener(Dialog dialog) {
    dialog.getDialogPane().lookupButton(ButtonType.NO).setOnMousePressed((event) -> {
    });
  }

  private void setAnimeInfo() {
    String title = LocalizedApp.getInstance().getString("Title") + ": " + historyElement.getViewedElement().getAnimeInformation().getName();
    Label labelTitle = new Label(title);
    String episodes = String.format(LocalizedApp.getInstance().getString("LastEpisodeSeen") + ": %d su %d",
            historyElement.getEpisodesHistory().get(historyElement.getEpisodesHistory().size() - 1).getEpisode().getNumber(),
            historyElement.getViewedElement().getAnimeInformation().getNumberOfEpisodes());
    Label labelEpisodes = new Label(episodes);
    TextArea textAreaPlot = new TextArea(historyElement.getViewedElement().getAnimeInformation().getPlot());
    textAreaPlot.setWrapText(true);
    textAreaPlot.setEditable(false);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelEpisodes, textAreaPlot);
  }
}
