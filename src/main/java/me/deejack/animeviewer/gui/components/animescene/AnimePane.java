package me.deejack.animeviewer.gui.components.animescene;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.layout.FlowPane;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimePane extends FlowPane {
  private final Listener<Anime> onRequestAnimeTab;

  public AnimePane(Iterable<? extends Anime> animeList, Listener<Anime> onRequestAnimeTab) {
    //setPrefWidth(600);
    this.onRequestAnimeTab = onRequestAnimeTab;
    setMinWidth(Double.NEGATIVE_INFINITY);
    setMinHeight(Double.NEGATIVE_INFINITY);
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);
    setColumnHalignment(HPos.CENTER);
    addElements(animeList);
  }

  public void addElements(Iterable<? extends Anime> animeList) {
    for (Anime anime : animeList) {
      AnimeBox box = new AnimeBox(anime, onRequestAnimeTab);
      Platform.runLater(() -> getChildren().add(box));
    }
    /*animeList.stream().
            map(AnimeBox::new).
            forEach(getChildren()::add);*/
  }
}
