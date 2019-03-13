package me.deejack.animeviewer.gui.components.animedetail;

import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ListViewEpisodes extends ListView {
  private final Anime anime;

  public ListViewEpisodes(Anime anime) {
    this.anime = anime;
    initialize();
  }

  private void initialize() {
    setMinHeight(158);
    setHeight(158);
    setPrefWidth(530);
    BorderPane.setAlignment(this, Pos.CENTER);
    addChildren();
  }

  private void addChildren() {
    anime.getEpisodes().stream()
            .map(ItemEpisode::new)
            .forEach(getChildren()::add);
  }
}
