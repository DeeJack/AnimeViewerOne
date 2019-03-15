package me.deejack.animeviewer.gui.components.animedetail;

import java.awt.Desktop;
import java.net.URI;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;

public class AnimeInfoBox extends HBox {

  public AnimeInfoBox(Anime anime) {
    setMaxHeight(200);
    setBackground(new Background(new BackgroundFill(Paint.valueOf("red"), CornerRadii.EMPTY, null)));
    getChildren().addAll(createInformationArea(anime.getAnimeInformation().toString()),
            new VBox(createImageFavorite(anime), createOpenInBrowser(anime.getUrl())));
  }

  private TextArea createInformationArea(String text) {
    TextArea textArea = new TextArea(text);
    /*textArea.setMaxWidth(600);
    textArea.setPrefHeight(200);*/
    textArea.setMaxHeight(200);
    textArea.setEditable(false);
    textArea.setWrapText(true);
    HBox.setHgrow(textArea, Priority.ALWAYS);
    return textArea;
  }

  private ImageFavorite createImageFavorite(Anime anime) {
    return new ImageFavorite(anime);
  }

  private Hyperlink createOpenInBrowser(String url) {
    Hyperlink openInBrowser = new Hyperlink("Apri nel browser");
    openInBrowser.setOnAction((event) -> {
      try {
        Desktop.getDesktop().browse(new URI(url));
      } catch (Exception e) {
        SceneUtility.handleException(e);
      }
    });
    return openInBrowser;
  }
}
