package me.deejack.animeviewer.gui.components.updates;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.updates.AnimeUpdates;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A box containing the updates of a day
 */
public class DailyUpdatesBox extends VBox {
  private final VBox updatesBox = new VBox();

  public DailyUpdatesBox(ChronoLocalDate dateTime) {
    getChildren().addAll(createDayBox(dateTime), updatesBox);
    setMargin(updatesBox, new Insets(0, 0, 0, 30));
  }

  /**
   * Create a label with a date time pattern like: Thursday 18 April 2019
   *
   * @param dateTime The date of the update
   * @return a label with the formatted date
   */
  private Label createDayBox(ChronoLocalDate dateTime) {
    return new Label(dateTime.format(DateTimeFormatter.ofPattern("eeee d MMMM uuuu")));
  }

  public void addAnimeUpdates(AnimeUpdates animeUpdates) {
    animeUpdates.getEpisodes().forEach(episode ->
            updatesBox.getChildren().add(new SingleAnimeUpdateBox(animeUpdates.getAnime(), episode)));
  }

  /**
   * Create a HBox without updates
   */
  private HBox createEmptyBox() {
    return new HBox(new Text(LocalizedApp.getInstance().getString("NoUpdates")));
  }
}
