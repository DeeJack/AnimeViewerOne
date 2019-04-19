package me.deejack.animeviewer.gui.components.updates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.deejack.animeviewer.logic.favorite.AnimeUpdates;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class DailyUpdatesBox extends VBox {
  private VBox updatesBox;

  public DailyUpdatesBox(LocalDate dateTime, List<AnimeUpdates> updates) {
    getChildren().addAll(createDayBox(dateTime), updates.isEmpty() ? createEmptyBox() : createUpdatesBox(updates));
  }

  /**
   * Create a label with a date time pattern like: Thursday 18 April 2019
   *
   * @param dateTime The date of the update
   * @return a label with the formatted date
   */
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

  private HBox createEmptyBox() {
    HBox emptyBox = new HBox(new Text(LocalizedApp.getInstance().getString("NoUpdates")));
    return emptyBox;
  }
}
