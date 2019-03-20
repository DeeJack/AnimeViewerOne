package me.deejack.animeviewer.gui.components.updates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.logic.favorite.AnimeUpdates;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class DailyUpdatesBox extends VBox {
  private VBox updatesBox;

  public DailyUpdatesBox(LocalDate dateTime, List<AnimeUpdates> updates) {
    getChildren().addAll(createDayBox(dateTime), createUpdatesBox(updates));
  }

  private Label createDayBox(LocalDate dateTime) {
    return new Label(dateTime.format(DateTimeFormatter.ofPattern("eeee d MMMM uuuu")));
  }

  private HBox createUpdatesBox(List<AnimeUpdates> updates) {
    AnchorPane pane = new AnchorPane();
    pane.setPrefWidth(30);
    updatesBox = new VBox();
    HBox box = new HBox(pane, updatesBox);
    updates.forEach((update) -> {
      update.getEpisodes().forEach(episode ->
              updatesBox.getChildren().add(new SingleAnimeUpdate(update.getAnime(), episode)));
    });
    return box;
  }

  public void addUpdate(Anime anime, Episode episode) {
    updatesBox.getChildren().add(new SingleAnimeUpdate(anime, episode));
  }
}
