package me.deejack.animeviewer.gui.components.animescene;

import javafx.geometry.HPos;
import javafx.scene.control.Tab;
import javafx.scene.layout.FlowPane;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.models.anime.Anime;

import java.util.ArrayList;
import java.util.List;

public class AnimePane extends FlowPane {
  private final List<AnimeBox> animeBoxSet = new ArrayList<>();

  public AnimePane(Iterable<? extends Anime> animeList, Listener<Anime> onRequestAnimeTab, Tab firstTab) {
    //setPrefWidth(600);
    /*setMinWidth(Double.NEGATIVE_INFINITY); IN CASO NON FUNZIONARE MI SA CHE ERA QUESTO
    setMinHeight(Double.NEGATIVE_INFINITY);
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);*/
    setColumnHalignment(HPos.CENTER);
    addElements(animeList, onRequestAnimeTab, firstTab);
  }

  public void addElements(Iterable<? extends Anime> animeList, Listener<Anime> onRequestAnimeTab, Tab firstTab) {
    for (Anime anime : animeList) {
      AnimeBox box = new AnimeBox(anime, onRequestAnimeTab, firstTab);
      getChildren().add(box);
      animeBoxSet.add(box);
    }
    /*animeList.stream().
            map(AnimeBox::new).
            forEach(getChildren()::add);*/
  }

  public void reload() {
    animeBoxSet.forEach(AnimeBox::reload);
  }
}
