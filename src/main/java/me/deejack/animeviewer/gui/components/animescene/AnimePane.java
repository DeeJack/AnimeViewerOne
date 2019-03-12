package me.deejack.animeviewer.gui.components.animescene;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimePane extends FlowPane {
  private final List<Anime> animeList;

  public AnimePane(List<Anime> animeList) {
    this.animeList = new ArrayList<>(animeList);
    //setPrefWidth(600);
    setMinWidth(Double.NEGATIVE_INFINITY);
    setMinHeight(Double.NEGATIVE_INFINITY);
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);
    setColumnHalignment(HPos.CENTER);
  }

  public void load() {
    animeList.stream().
            map(AnimeBox::new).
            forEach(getChildren()::add);
  }
}
