package me.deejack.animeviewer.gui.components.animedetail;

import java.awt.Desktop;
import java.net.URI;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public class AnimeInfoBox extends HBox {
  private final Anime anime;
  private Hyperlink reloadLink;

  public AnimeInfoBox(Anime anime) {
    this.anime = anime;
    reload();
    setAlignment(Pos.CENTER);
    setFillHeight(true);
  }

  public void reload() {
    getChildren().clear();
    VBox vBox = new VBox(createOpenInBrowser(anime.getUrl()), createReload());
    ImageView imageFavorite = createImageFavorite(anime, vBox.heightProperty());
    vBox.getChildren().add(0, imageFavorite);
    heightProperty().addListener(((observable, oldValue, newValue) -> {
      vBox.setPrefHeight(newValue.doubleValue() * 0.95);
      vBox.setMinHeight(newValue.doubleValue() * 0.95);
      vBox.setMaxHeight(newValue.doubleValue() * 0.95);
    }));
    getChildren().addAll(createInformationArea(anime), vBox);
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

  private ImageFavorite createImageFavorite(Anime anime, ReadOnlyDoubleProperty heightProperty) {
    return new ImageFavorite(anime, heightProperty);
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

  private Hyperlink createReload() {
    reloadLink = new Hyperlink(LocalizedApp.getInstance().getString("Reload"));
    return reloadLink;
  }

  public void setOnReload(EventHandler<ActionEvent> onReload) {
    reloadLink.setOnAction(onReload);
  }
}
