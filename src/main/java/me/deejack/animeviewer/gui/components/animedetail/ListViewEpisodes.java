package me.deejack.animeviewer.gui.components.animedetail;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class ListViewEpisodes extends ListView<ItemEpisode> {
  private final Anime anime;
  private final OptionMenu optionMenu = new OptionMenu(this);

  public ListViewEpisodes(Anime anime) {
    this.anime = anime;
    registerEvents();
    initialize();
  }

  public void reload() {
    getItems().clear();
    addChildren();
    setWidth(getWidth() + 1);
  }

  private void registerEvents() {
    setOnMouseReleased((event) -> {
      if (event.getButton() == MouseButton.SECONDARY)
        optionMenu.open(event.getScreenX(), event.getScreenY());
    });
  }

  private void initialize() {
    setMinHeight(100);
    setHeight(100);
    BorderPane.setAlignment(this, Pos.CENTER);
    addChildren();
    getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void addChildren() {
    anime.getEpisodes().stream()
            .filter(Objects::nonNull)
            .map((episode) -> new ItemEpisode(episode, anime, this))
            .forEach(getItems()::add);
  }
}
