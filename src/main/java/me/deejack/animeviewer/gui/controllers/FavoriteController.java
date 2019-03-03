package me.deejack.animeviewer.gui.controllers;

import java.util.concurrent.CountDownLatch;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.SiteElement;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class FavoriteController implements BaseScene {
  private final boolean ishistory;
  private VBox boxFavorite;
  private StackPane root;

  public FavoriteController(boolean history) {
    ishistory = history;
    initialize();
  }

  public void initialize() {
    root = (StackPane) SceneUtility.loadParent("/scenes/favorite/favorite.fxml");
    boxFavorite = (VBox) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    Button btnBack = (Button) root.lookup("#btnBack");
    btnBack.setOnAction((event) -> SceneUtility.goToPreviousScene());
    if (ishistory) {
      for (HistoryElement element : History.getHistory().getViewedElements())
        createHistoryElement(element);
    } else {
      for (SiteElement element : Favorite.getInstance().getFavorites())
        createFavorite(element);
    }
  }

  private void createFavorite(SiteElement element) {
    Pane singleFav = (Pane) SceneUtility.loadParent("/scenes/favorite/singleFavorite.fxml");
    ImageView image = (ImageView) singleFav.lookup("#imgAnime");
    registerEvents(image, singleFav, element);
    Task<Image> loadImage = SceneUtility.loadImage(element.getAnimeInformation().getImageUrl());
    loadImage.setOnSucceeded((value) -> image.setImage(loadImage.getValue()));
    Label lblTitle = (Label) singleFav.lookup("#lblTitle");
    lblTitle.setText("Titolo: " + element.getAnimeInformation().getName());
    Label lblEpisodes = (Label) singleFav.lookup("#lblEpisodes");
    lblEpisodes.setText("Episodi: " + element.getAnimeInformation().getNumberOfEpisodes());
    TextArea lblPlot = (TextArea) singleFav.lookup("#lblPlot");
    lblPlot.setText("Trama: " + element.getPlot());
    Button btnResume = (Button) singleFav.lookup("#btnResume");
    btnResume.setOnAction((event) -> resume(element));
    Button btnRemove = (Button) root.lookup("#btnRemove");
    btnRemove.setOnAction((event) -> Favorite.getInstance().removeFavorite(element));
    singleFav.setPrefWidth(boxFavorite.getPrefWidth());
    boxFavorite.getChildren().add(singleFav);
  }

  private void createHistoryElement(HistoryElement historyElement) {
    Pane singleFav = (Pane) SceneUtility.loadParent("/scenes/favorite/singleFavorite.fxml");
    ImageView image = (ImageView) singleFav.lookup("#imgAnime");
    registerEvents(image, singleFav, historyElement.getViewedElement());
    Label lblTitle = (Label) singleFav.lookup("#lblTitle");
    lblTitle.setText("Titolo: " + historyElement.getViewedElement().getAnimeInformation().getName());
    Task<Image> loadImage = SceneUtility.loadImage(historyElement.getViewedElement().getAnimeInformation().getImageUrl());
    loadImage.setOnSucceeded((value) -> image.setImage(loadImage.getValue()));
    Label lblEpisodes = (Label) singleFav.lookup("#lblEpisodes");
    TextArea lblPlot = (TextArea) singleFav.lookup("#lblPlot");
    lblPlot.setText("Trama: " + (historyElement.getViewedElement()).getPlot());
    Button btnResume = (Button) singleFav.lookup("#btnResume");
    btnResume.setOnAction((event) -> resume(historyElement.getViewedElement()));
    singleFav.setPrefWidth(boxFavorite.getPrefWidth());
    boxFavorite.getChildren().add(singleFav);
    Button btnRemove = (Button) singleFav.lookup("#btnRemove");
    btnRemove.setOnAction((event) -> removeHistory(historyElement, lblEpisodes, singleFav));
    setValues(lblEpisodes, historyElement);
  }

  private void setValues(Label lblEpisodes, HistoryElement historyElement) {
    Episode lastEpisode = historyElement.getEpisodesHistory().get(historyElement.getEpisodesHistory().size() - 1);
    lblEpisodes.setText(String.format("Ultmo episodio visto: %d su %d",
            lastEpisode.getNumber(),
            historyElement.getViewedElement().getAnimeInformation().getNumberOfEpisodes()));
  }

  private void removeHistory(HistoryElement element, Label lblEpisodiVisti, Node nodeHistory) {
    Alert alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK, ButtonType.CANCEL);
    Label text = new Label("Vuoi rimuovere l'ultimo episodio visto?");
    CheckBox checkBox = new CheckBox("Elimina tutti gli episodi di questo hentai");
    VBox vBox = new VBox(text, checkBox);
    alert.setGraphic(vBox);
    alert.showAndWait();
    if (alert.getResult() != ButtonType.OK)
      return;
    if (checkBox.isSelected())
      History.getHistory().remove(element.getViewedElement());
    else {
      element.removeEpisodeFromHistory(
              element.getEpisodesHistory().indexOf(
                      element.getEpisodesHistory().get(element.getEpisodesHistory().size() - 1)));
    }
    if (element.getEpisodesHistory().isEmpty() || checkBox.isSelected())
      boxFavorite.getChildren().remove(nodeHistory);
    else
      setValues(lblEpisodiVisti, element);
  }

  private void resume(SiteElement element) {
    Episode episode;
    if (History.getHistory().contains(element))
      episode = History.getHistory().get(element).getEpisodesHistory().get(History.getHistory().get(element).getEpisodesHistory().size() - 1);
    else {
      CountDownLatch countDownLatch = new CountDownLatch(1);
      if (!element.hasLoadedEpisodes()) {
        showWaitAndLoad("Loading episodes....");
        new Thread(() -> {
          element.loadEpisodes();
          countDownLatch.countDown();
        }).start();
      }
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        handleException(e);
      }
      hideWaitLoad();
      episode = element.getSeasons().get(0).getEpisodes().get(0);
    }
    showWaitAndLoad("Caricando link");
    new AnimePlayer(episode, element).streaming();
  }

  private void registerEvents(Node imageNode, Parent root, SiteElement element) {
    root.setOnMouseClicked((event) -> {
      if (event.isPrimaryButtonDown())
        new AnimeDetailController(element).loadAsync();
    });
    imageNode.setOnMousePressed((event) -> {
      if (event.isPrimaryButtonDown())
        new AnimeDetailController(element).loadAsync();
    });
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return ishistory ? "Cronologia" : "Preferiti";
  }

  @Override
  public String getName() {
    return "Favorite";
  }
}
