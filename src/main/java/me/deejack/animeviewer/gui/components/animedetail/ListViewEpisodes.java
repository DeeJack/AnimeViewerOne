package me.deejack.animeviewer.gui.components.animedetail;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import me.deejack.animeviewer.logic.async.events.Listener;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class ListViewEpisodes extends ListView<ItemEpisode> {
  private final Anime anime;
  private final OptionMenu optionMenu = new OptionMenu(this);
  private final Listener<Episode> onStreamingRequested;

  public ListViewEpisodes(Anime anime, Listener<Episode> onStreamingRequested) {
    this.anime = anime;
    this.onStreamingRequested = onStreamingRequested;
    registerEvents();
    initialize();
    setMinHeight(Double.MIN_VALUE);
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
    BorderPane.setAlignment(this, Pos.CENTER);
    addChildren();
    getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void addChildren() {
    anime.getEpisodes().stream()
            .filter(Objects::nonNull)
            .map((episode) -> new ItemEpisode(episode, anime, this, onStreamingRequested))
            .forEach(getItems()::add);
  }
}
