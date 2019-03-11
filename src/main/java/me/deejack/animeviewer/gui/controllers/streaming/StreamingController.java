package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.controllers.streaming.components.AlwaysOnTopImage;
import me.deejack.animeviewer.gui.controllers.streaming.components.ButtonNext;
import me.deejack.animeviewer.gui.controllers.streaming.components.ButtonPause;
import me.deejack.animeviewer.gui.controllers.streaming.components.FullScreenImage;
import me.deejack.animeviewer.gui.controllers.streaming.components.LabelTime;
import me.deejack.animeviewer.gui.controllers.streaming.components.ProgressBarBuffer;
import me.deejack.animeviewer.gui.controllers.streaming.components.SliderTime;
import me.deejack.animeviewer.gui.controllers.streaming.components.SliderVolume;
import me.deejack.animeviewer.gui.controllers.streaming.components.StretchVideoImage;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;

public class StreamingController implements BaseScene {
  private final MediaPlayer mediaPlayer;
  private final Episode episode;
  private final Anime anime;
  private String title = "";
  private Pane root;
  private ControlsLayerTask cursorTask;

  public StreamingController(MediaPlayer mediaPlayer, Episode episode, Anime anime) {
    this.mediaPlayer = mediaPlayer;
    this.episode = episode;
    this.anime = anime;
  }

  public void setUpPlayer() {
    if (anime != null)
      title = anime.getAnimeInformation().getName() + " - " + "Episodio " + episode.getNumber() + " - " + episode.getTitle();
    setupNodes();
    hideWaitLoad();
    mediaPlayer.play();
  }

  public void afterInitialization() {
    HistoryElement historyElement = History.getHistory().get(anime);
    if (historyElement != null) {
      if (historyElement.getEpisodesHistory().contains(episode))
        episode.setSecondsWatched(
                historyElement.getEpisodesHistory().get(
                        historyElement.getEpisodesHistory().indexOf(episode)).getSecondsWatched());
      else historyElement.addEpisode(episode);
    } else
      History.getHistory().add(new HistoryElement(anime, episode));
    if (episode.getSecondsWatched() > 0)
      mediaPlayer.seek(Duration.seconds(episode.getSecondsWatched()));
  }

  private void setupNodes() {
    ButtonPause btnPause = new ButtonPause(mediaPlayer);
    ButtonNext btnNext = new ButtonNext(anime, episode);
    root = (Pane) SceneUtility.loadParent("/scenes/streaming.fxml");
    Button btnBack = (Button) root.lookup("#btnBack");
    MediaView mediaView = (MediaView) root.lookup("#mediaView");
    Label lblTitle = (Label) root.lookup("#lblTitle");
    lblTitle.setText(title);
    mediaView.setMediaPlayer(mediaPlayer);

    StackPane stackPane = new StackPane(new ProgressBarBuffer(mediaPlayer), new SliderTime(mediaPlayer));
    stackPane.getStylesheets().add("/assets/style.css");
    HBox.setHgrow(stackPane, Priority.ALWAYS);

    Node[] nodes = {
            btnPause, btnNext, stackPane, new LabelTime(mediaPlayer), new SliderVolume(mediaPlayer),
            new FullScreenImage(), new StretchVideoImage(mediaView), new AlwaysOnTopImage()
    };
    ((Pane) root.lookup("#bottomBar")).getChildren().addAll(nodes);
    registerEvents(btnPause, btnNext, btnBack);
  }

  private void registerEvents(ButtonPause btnPause, ButtonNext btnNext, Button btnBack) {
    root.lookup("#pauseLayer").setOnMouseClicked((event) -> btnPause.pause());
    btnNext.setOnNextEpisode(this::onFinish);
    if (episode != null)
      mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) ->
              episode.setSecondsWatched(mediaPlayer.getCurrentTime().toSeconds()));
    btnBack.setOnAction((event) -> {
      onFinish();
      SceneUtility.goToPreviousScene();
    });
  }

  private void onFinish() {
    if (anime != null) {
      History.getHistory().saveToFile();
      Favorite.getInstance().saveToFile();
    }
    cursorTask.setInterrupted(true);
    mediaPlayer.dispose();
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
    SceneUtility.goToPreviousScene();
  }

  @Override
  public Pane getRoot() {
    return root;
  }

  @Override
  public String getTitle() {
    return "Streaming " + title;
  }

  @Override
  public String getName() {
    return "Streaming";
  }
}

















 /* private void setValues(MediaView mediaView, Slider sliderTime, Slider sliderVolume, Label lblTitle) {
    lblTitle.setText(title + String.format(" [%dx%d]", mediaPlayer.getMedia().getWidth(), mediaPlayer.getMedia().getHeight()));
    sliderTime.setMax(mediaPlayer.getTotalDuration().toSeconds());
    sliderVolume.setMax(0.5);
    sliderVolume.setValue(0.5);
  }

  private void registerEvents(Button btnBack, Button btnNext, Button btnPause, Slider sliderTime, Slider sliderVolume,
                              Label lblTime, Label lblTitle, MediaView mediaView, ProgressBar videoProgress,
                              ImageView imageFullScreen, ImageView stretchVideo) {
    root.layoutBoundsProperty().addListener((obs, oldValue, newValue) -> onSizeChange(mediaView));
    btnBack.setOnAction((event) -> goBack());
    btnNext.setOnAction((event) -> onFinish());
    *//*btnPause.setOnAction((event) -> pause());*//*
    sliderTime.valueProperty().addListener((listener) -> onSliderTimeChange(sliderTime));
    lblTime.setText(mediaPlayer.getTotalDuration().toMinutes() + " min");
    mediaPlayer.statusProperty().addListener((obs) -> onChangeStatus(mediaPlayer.getStatus(), btnPause));
    mediaPlayer.setOnEndOfMedia(this::onFinish);
    mediaPlayer.setOnError(() -> SceneUtility.handleException(mediaPlayer.getError()));
    cursorTask = new ControlsLayerTask((Pane) root.lookup("#paneLayer"), root);
    new Thread(cursorTask).start();
    SceneUtility.getStage().getScene().setOnKeyPressed(this::keyNavigation);
    btnBack.setOnKeyPressed(this::keyNavigation);
    sliderVolume.setOnKeyPressed(this::keyNavigation);
    root.lookup("#pauseLayer").setOnMouseClicked((event) -> pause());
    mediaPlayer.setOnReady(() -> {
      setValues(mediaView, sliderTime, sliderVolume, lblTitle);
      if (anime != null)
        afterInitialization();
    });
  }

  private void keyNavigation(KeyEvent keyEvent) {
    keyEvent.consume();
    switch (keyEvent.getCode()) {
      case UP:
        if (mediaPlayer.getVolume() < 0.55)
          mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.05);
        break;
      case DOWN:
        if (mediaPlayer.getVolume() > 0.05)
          mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.05);
        break;
      case RIGHT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(4)));
        break;
      case LEFT:
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(4)));
        break;
      case F11:
        SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
        break;
      case SPACE:
        pause();
        break;
    }
  }

  private void onSizeChange(MediaView view) {
    view.setFitHeight(SceneUtility.getStage().getHeight());
    view.setFitWidth(SceneUtility.getStage().getWidth());
  }

  private void onSliderTimeChange(Slider sliderTime) {
    if (sliderTime.isValueChanging())
      mediaPlayer.seek(Duration.seconds(sliderTime.getValue()));
  }

  private void goBack() {
    if (anime != null) {
      History.getHistory().saveToFile();
      Favorite.getInstance().saveToFile();
    }
    cursorTask.setInterrupted(true);
    mediaPlayer.dispose();
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
    SceneUtility.goToPreviousScene();
  }

  *//*private void pause() {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
      mediaPlayer.pause();
    else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
      mediaPlayer.play();
  }*//*

  private void onChangeStatus(MediaPlayer.Status status, Button btnPause) {
    switch (status) {
      case PLAYING:
        btnPause.setText("| |");
        break;
      case UNKNOWN:
      case STALLED:
        btnPause.setText(">");
        showWaitAndLoad("Caricando video...");
        break;
      case READY:
        hideWaitLoad();
        break;
      case PAUSED:
      case STOPPED:
        btnPause.setText(">");
        break;
      case HALTED:
        Alert alert = new Alert(Alert.AlertType.ERROR,
                "Errore critico, non so cosa sia sucesso ma lo streaming si è interrotto. Prova a riaprire lo streaming.");
        alert.showAndWait();
        goBack();
    }
  }*/