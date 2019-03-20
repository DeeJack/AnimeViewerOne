package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;

public class HistoryItem extends SingleFavorite {
  private final HistoryElement historyElement;

  public HistoryItem(HistoryElement historyElement) {
    super(historyElement.getViewedElement());
    this.historyElement = historyElement;
    setOnRemove(getOnRemove());
    setAnimeInfo();
  }

  private EventHandler<ActionEvent> getOnRemove() { // TODO: rimuovere un episodio o tutti, non l'anime intero.
    return (event) -> {
      History.getHistory().remove(historyElement.getViewedElement());
      FilesUtility.saveHistory();
      ((Pane) getParent()).getChildren().remove(this);
    };
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
