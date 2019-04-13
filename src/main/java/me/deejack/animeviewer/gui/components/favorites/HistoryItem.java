package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
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

  private EventHandler<ActionEvent> getOnRemove() { // TODO: rimuovere un episodio o tutti, non l'anime intero.
    return (event) -> {
      /*History.getHistory().remove(historyElement.getViewedElement());
      FilesUtility.saveHistory();
      ((Pane) getParent()).getChildren().remove(this);*/
      requestRemoveAllEpisodes();
    };
  }

  private boolean requestRemoveAllEpisodes() {
    Dialog dialog = new Dialog();
    boolean result = false;
    Label label = new Label("Asd");
    CheckBox checkBox = new CheckBox("Delete?");
    VBox box = new VBox(label, checkBox);
    dialog.getDialogPane().setContent(box);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
    addListeners(dialog);
    dialog.showAndWait();
    return result;
  }

  private void addListeners(Dialog dialog) {
    addYesButtonListener(dialog);
    addNoButtonListener(dialog);
  }

  private void addYesButtonListener(Dialog dialog) {
    dialog.getDialogPane().lookupButton(ButtonType.YES).setOnMousePressed((event) -> {

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
            historyElement.getEpisodesHistory().get(historyElement.getEpisodesHistory().size() - 1).getNumber(),
            historyElement.getViewedElement().getAnimeInformation().getNumberOfEpisodes());
    Label labelEpisodes = new Label(episodes);
    TextArea textAreaPlot = new TextArea(historyElement.getViewedElement().getAnimeInformation().getPlot());
    textAreaPlot.setWrapText(true);
    textAreaPlot.setEditable(false);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelEpisodes, textAreaPlot);
  }
}
