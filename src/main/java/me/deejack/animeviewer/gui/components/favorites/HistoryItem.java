package me.deejack.animeviewer.gui.components.favorites;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
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

  private EventHandler<ActionEvent> getOnRemove() {
    return (event) -> {
      History.getHistory().remove(historyElement.getViewedElement());
      History.getHistory().saveToFile();
      ((Pane) getParent()).getChildren().remove(this);
    };
  }

  private void setAnimeInfo() {
    Label labelTitle = new Label("Titolo: " + historyElement.getViewedElement().getAnimeInformation().getName());
    String episodes = String.format("Ultimo episodio visto: %d su %d",
            historyElement.getEpisodesHistory().get(historyElement.getEpisodesHistory().size() - 1).getNumber(),
            historyElement.getViewedElement().getAnimeInformation().getNumberOfEpisodes());
    Label labelEpisodes = new Label(episodes);
    TextArea textAreaPlot = new TextArea(historyElement.getViewedElement().getAnimeInformation().getPlot());
    textAreaPlot.setWrapText(true);
    textAreaPlot.setEditable(false);
    getAnimeInfoBox().getChildren().addAll(labelTitle, labelEpisodes, textAreaPlot);
  }
}
