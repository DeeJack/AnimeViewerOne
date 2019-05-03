package me.deejack.animeviewer.gui.components.updates;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.updates.AnimeUpdates;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

/**
 * A box containing the updates of a day
 */
public class DailyUpdatesBox extends VBox {
  private VBox updatesBox;

  public DailyUpdatesBox(ChronoLocalDate dateTime, Collection<? extends AnimeUpdates> updates) {
    getChildren().addAll(createDayBox(dateTime), updates.isEmpty() ? createEmptyBox() : createUpdatesBox(updates));
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

  /**
   * Create a box containing a list of updates
   *
   * @param updates The updates of that day
   * @return a HBox containing the updates
   */
  private HBox createUpdatesBox(Iterable<? extends AnimeUpdates> updates) {
    AnchorPane pane = new AnchorPane();
    pane.setPrefWidth(30);
    updatesBox = new VBox();
    HBox box = new HBox(pane, updatesBox);
    updates.forEach((update) ->
            update.getEpisodes().forEach(episode ->
                    updatesBox.getChildren().add(new SingleAnimeUpdateBox(update.getAnime(), episode))));
    return box;
  }

  /**
   * Create a HBox without updates
   */
  private HBox createEmptyBox() {
    return new HBox(new Text(LocalizedApp.getInstance().getString("NoUpdates")));
  }
}
