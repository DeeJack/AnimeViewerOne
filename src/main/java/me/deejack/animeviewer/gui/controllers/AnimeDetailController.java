package me.deejack.animeviewer.gui.controllers;

import java.awt.Desktop;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.SiteElement;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;
import static me.deejack.animeviewer.gui.utils.SceneUtility.setRoot;

public class AnimeDetailController implements BaseScene {
  private final SiteElement element;
  private Pane root;
  private Pane content;
  private Button btnBack;
  private TextArea info;
  private ImageView imageView;
  private ListView<Node> lstViewEpisodes;

  public AnimeDetailController(SiteElement element) {
    this.element = element;
  }

  public void loadAsync() {
    new Thread(() -> {
      if (!element.hasLoadedEpisodes())
        element.loadEpisodes();
      element.setInfos();
      Platform.runLater(this::load);
    }).start();
  }

  // TODO cambiare nome
  private void load() {
    setupScene();
    hideWaitLoad();
    layout();
    setValues();
    loadScene();
    showWaitAndLoad("Loading element");
    loadEpisodes();
    registerEvents();
    hideWaitLoad();
  }

  private void setupScene() {
    root = (Pane) SceneUtility.loadParent("/scenes/animeDetailResp.fxml");
    content = (Pane) ((ScrollPane) root.lookup("#scrollPane")).getContent();
    ImageView imageFavorite = (ImageView) content.lookup("#imgFavorite");
    if (element.isFavorite())
      imageFavorite.setImage(new Image(App.class.getResourceAsStream("/assets/favorite.png")));
    imageFavorite.setOnMouseClicked((event) -> {
      element.toggleFavorite();
      if (element.isFavorite())
        imageFavorite.setImage(new Image(App.class.getResourceAsStream("/assets/favorite.png")));
      else
        imageFavorite.setImage(new Image(App.class.getResourceAsStream("/assets/non-favorite.png")));
      Favorite.getInstance().saveToFile();
    });
  }

  private void layout() {
    btnBack = (Button) root.lookup("#btnBack");
    imageView = (ImageView) content.lookup("#image");
    info = (TextArea) content.lookup("#description");
    lstViewEpisodes = (ListView<Node>) content.getChildren().get(2);
    lstViewEpisodes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void setValues() {
    ((ButtonBase) content.lookup("#hyperLink")).setOnAction((val) -> {
      try {
        Desktop.getDesktop().browse(new URI(element.getAnimeInformation().getUrl()));
      } catch (Exception e) {
        SceneUtility.handleException(e);
      }
    });

    Task<Image> task = SceneUtility.loadImage(element.getAnimeInformation().getImageUrl());
    Platform.runLater(() -> task.setOnSucceeded((value) -> imageView.setImage(task.getValue())));
    Platform.runLater(() -> {
      info.setText(element.getDescription());
    });
  }

  private void loadScene() {
    if (!Platform.isFxApplicationThread()) {
      Platform.runLater(this::loadScene);
      return;
    }
    setRoot(this);
    lstViewEpisodes.setPrefHeight(158 + (SceneUtility.getStage().getScene().getHeight() - 530));
  }

  private void loadEpisodes() {
    int i = 1;
    for (Season season : element.getSeasons()) {
      Label labelSeas = new Label("Stagione " + (i++) + ": " + season.getName());
      labelSeas.setStyle("font-weight: bold");
      labelSeas.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
      labelSeas.setOnContextMenuRequested((event) -> sagaRightClick(season, event.getScreenX(), event.getScreenY()));
      //labelSeas.setPrefWidth(lstViewEpisodes.getPrefWidth());
      HBox.setHgrow(labelSeas, Priority.ALWAYS);
      lstViewEpisodes.getItems().add(labelSeas);
      for (Episode episode : season.getEpisodes()) {
        addEpisode(episode);
      }
    }
  }

  private void addEpisode(Episode episode) {
    Label title = new Label(episode.getNumber() + " - " + episode.getTitle());
    if (episode.getTitle().isEmpty())
      title.setText("Episodio " + episode.getNumber());
    if (episode.getUrl().isEmpty()) {
      showNotReleased(episode, title);
      return;
    }
    Label download = new Label("Download");
    download.setOnMouseClicked((ex) -> download(episode));
    Label streaming = new Label("Streaming");
    streaming.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    download.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    Label releaseDate = new Label("");
    if (episode.getReleaseDate() != null)
      releaseDate.setText("[" + episode.getReleaseDate() + "]  -  ");
    streaming.setOnMouseClicked((ex) -> {
      showWaitAndLoad("Caricando link");
      new AnimePlayer(episode, element).streaming();
    });
    lstViewEpisodes.widthProperty().addListener((event, oldValue, newValue) -> title.setMaxWidth(newValue.doubleValue() - (250)));
    title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
    HBox box = new HBox(title, releaseDate, download, new Label(" - "), streaming);
    if (History.getHistory().contains(element) && History.getHistory().get(element).getEpisodesHistory().contains(episode)) {
      long totalSeconds = (long) History.getHistory().get(element).getEpisodesHistory().get(History.getHistory().get(element).getEpisodesHistory().indexOf(episode)).getSecondsWatched();
      int seconds = (int) Duration.ofSeconds(totalSeconds).getSeconds() % 60 % 60;
      int minutes = (int) Duration.ofSeconds(totalSeconds).toMinutes() % 60;
      int hours = (int) Duration.ofSeconds(totalSeconds).toHours();
      Label watched = new Label(String.format("Visto per: %02d:%02d:%02d", hours, minutes, seconds));
      box.getChildren().add(1, watched);
      box.getChildren().add(2, new Label(" - "));
    }
    HBox.setHgrow(title, Priority.ALWAYS);
    lstViewEpisodes.getItems().add(box);
  }

  private void showNotReleased(Episode episode, Label title) {
    Label notReleased = new Label("[Non ancora disponibile]");
    Label releaseDate = new Label("[" + episode.getReleaseDate() + "]  -  ");
    HBox box = new HBox(title, releaseDate, notReleased);
    //title.setPrefWidth(lstViewEpisodes.getPrefWidth() - (notReleased.getWidth() + releaseDate.getWidth() + 25));
    HBox.setHgrow(title, Priority.ALWAYS);
    lstViewEpisodes.getItems().add(box);
  }

  private void registerEvents() {
    btnBack.setOnMouseClicked((event -> SceneUtility.goToPreviousScene()));
    SceneUtility.getStage().getScene().heightProperty().addListener(((observable, oldValue, newValue) ->
            lstViewEpisodes.setPrefHeight(158 + (newValue.doubleValue() - 523))
    ));
  }

  private void sagaRightClick(Season season, double x, double y) {
    MenuItem downloadSaga = new MenuItem("Scarica intera saga");
    downloadSaga.setOnAction((event) -> downloadSaga(season));
    MenuItem downloadAll = new MenuItem("Scarica tutti gli episodi");
    downloadAll.setOnAction((event) -> downloadAll());
    ContextMenu menu = new ContextMenu(downloadSaga, downloadAll);
    menu.show(SceneUtility.getStage(), x, y);
  }

  private void downloadSaga(Season season) {
    List<Episode> episodes = new ArrayList<>(season.getEpisodes());
    downloadEpisodes(episodes);
  }

  private void downloadEpisodes(List<Episode> episodes) {
    DownloadController controller = DownloadController.getDownloadController();
    controller.addDownloads(episodes, element.getAnimeInformation().getName());
  }

  private void downloadAll() {
    List<Episode> episodes = new ArrayList<>();
    for (Season season : element.getSeasons()) {
      episodes.addAll(season.getEpisodes());
    }
    downloadEpisodes(episodes);
  }

  private void download(Episode episode) {
    DownloadController controller = DownloadController.getDownloadController();
    controller.singleDownload(episode, element.getAnimeInformation().getName());
  }

  @Override
  public Pane getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return "Dettaglio " + element.getAnimeInformation().getName();
  }

  @Override
  public String getName() {
    return "Detail";
  }
}
