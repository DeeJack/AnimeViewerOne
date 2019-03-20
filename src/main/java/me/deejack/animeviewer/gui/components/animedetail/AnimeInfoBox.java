package me.deejack.animeviewer.gui.components.animedetail;

import java.awt.Desktop;
import java.net.URI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.utils.LocalizedApp;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public class AnimeInfoBox extends HBox {

  public AnimeInfoBox(Anime anime, EventHandler<ActionEvent> onReload) {
    getChildren().addAll(createInformationArea(anime),
            new VBox(createImageFavorite(anime), createOpenInBrowser(anime.getUrl()), createReload(onReload)));
  }

  private TextArea createInformationArea(Anime anime) {
    TextArea textArea = new TextArea(getInfoText(anime));
    textArea.setEditable(false);
    textArea.setWrapText(true);
    HBox.setHgrow(textArea, Priority.ALWAYS);
    return textArea;
  }

  private String getInfoText(Anime anime) {
    LocalizedApp app = LocalizedApp.getInstance();
    String message = String.format("%s: %s\n%s: %d\n%s: %s\n%s: %s\nUrl: %s\n%s: %s",
            app.getString("Title"), anime.getAnimeInformation().getName(),
            app.getString("EpisodesNumber"), anime.getEpisodes().size(),
            app.getString("ReleaseYear"), anime.getAnimeInformation().getReleaseYear(),
            app.getString("Genres"), GeneralUtility.genreListToString(anime.getAnimeInformation().getGenres(), ", "),
            anime.getUrl(),
            app.getString("Plot"), anime.getAnimeInformation().getPlot());
    return message;
  }

  private ImageFavorite createImageFavorite(Anime anime) {
    return new ImageFavorite(anime);
  }

  private Hyperlink createOpenInBrowser(String url) {
    Hyperlink openInBrowser = new Hyperlink(LocalizedApp.getInstance().getString("OpenOnBrowser"));
    openInBrowser.setOnAction((event) -> {
      try {
        Desktop.getDesktop().browse(new URI(url));
      } catch (Exception e) {
        SceneUtility.handleException(e);
      }
    });
    return openInBrowser;
  }

  private Hyperlink createReload(EventHandler<ActionEvent> onReload) {
    Hyperlink reload = new Hyperlink(LocalizedApp.getInstance().getString("Reload"));
    reload.setOnAction(onReload);
    return reload;
  }
}
