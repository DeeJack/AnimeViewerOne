package me.deejack.animeviewer.gui.components.animescene;

import javafx.geometry.HPos;
import javafx.scene.layout.FlowPane;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimePane extends FlowPane {

  public AnimePane(Iterable<? extends Anime> animeList, Listener<Anime> onRequestAnimeTab) {
    //setPrefWidth(600);
    /*setMinWidth(Double.NEGATIVE_INFINITY); IN CASO NON FUNZIONARE MI SA CHE ERA QUESTO
    setMinHeight(Double.NEGATIVE_INFINITY);
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);*/
    setColumnHalignment(HPos.CENTER);
    addElements(animeList, onRequestAnimeTab);
  }

  public void addElements(Iterable<? extends Anime> animeList, Listener<Anime> onRequestAnimeTab) {
    for (Anime anime : animeList) {
      AnimeBox box = new AnimeBox(anime, onRequestAnimeTab);
      getChildren().add(box);
    }
    /*animeList.stream().
            map(AnimeBox::new).
            forEach(getChildren()::add);*/
  }
}
