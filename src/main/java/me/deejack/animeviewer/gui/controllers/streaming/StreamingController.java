package me.deejack.animeviewer.gui.controllers.streaming;

import java.util.Optional;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.App;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.SiteElement;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class StreamingController implements BaseScene {
  private final MediaPlayer mediaPlayer;
  private final Episode episode;
  private final SiteElement element;
  private Image fullScreenImage;
  private Image smallScreenImage;
  private Image stretchVideoFull;
  private Image stretchVideoSmall;
  private String title = "";
  private Pane root;
  private Button btnPause;
  private ControlsLayerTask cursorTask;

  public StreamingController(MediaPlayer mediaPlayer, Episode episode, SiteElement anime) {
    this.episode = episode;
    element = anime;
    this.mediaPlayer = mediaPlayer;
  }

  public void setUpPlayer() {
    initialize();
    loadScene();
    hideWaitLoad();
    mediaPlayer.play();
  }

  private void initialize() {
    fullScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-full.png"));
    smallScreenImage = new Image(App.class.getResourceAsStream("/assets/resize-small.png"));
    stretchVideoFull = new Image(App.class.getResourceAsStream("/assets/stretch-full.png"));
    stretchVideoSmall = new Image(App.class.getResourceAsStream("/assets/stretch-small.png"));

    if (SceneUtility.getStage().getHeight() < 720)
      SceneUtility.getStage().setHeight(720);
    if (SceneUtility.getStage().getWidth() < 1280)
      SceneUtility.getStage().setWidth(1280);
    if (element != null)
      title = element.getAnimeInformation().getName() + " - " + "Episodio " + episode.getNumber() + " - " + episode.getTitle();
  }

  public void afterInitialization() {
    if (History.getHistory().contains(element)) {
      HistoryElement historyElement = History.getHistory().get(element);
      if (historyElement.getEpisodesHistory().contains(episode))
        episode.setSecondsWatched(
                historyElement.getEpisodesHistory().get(
                        historyElement.getEpisodesHistory().indexOf(episode)).getSecondsWatched());
      else historyElement.addEpisode(episode);
    } else
      History.getHistory().add(new HistoryElement(element, episode));
    if (episode.getSecondsWatched() > 0)
      mediaPlayer.seek(Duration.seconds(episode.getSecondsWatched()));
  }

  private void loadScene() {
    root = (Pane) SceneUtility.loadParent("/scenes/streaming.fxml");
    Button btnBack = (Button) root.lookup("#btnBack");
    Button btnNext = (Button) root.lookup("#btnNext");
    btnPause = (Button) root.lookup("#btnPause");
    MediaView mediaView = (MediaView) root.lookup("#mediaView");
    Slider sliderTime = (Slider) root.lookup("#sliderTime");
    Slider sliderVolume = (Slider) root.lookup("#sliderVolume");
    Label lblTime = (Label) root.lookup("#lblTime");
    Label lblTitle = (Label) root.lookup("#lblTitle");
    ImageView imageFullScreen = (ImageView) root.lookup("#imgFullScreen");
    ImageView stretchVideo = (ImageView) root.lookup("#stretchVideo");
    ProgressBar videoProgress = (ProgressBar) root.lookup("#videoProgress");
    ImageView alwaysOnTop = (ImageView) root.lookup("#alwaysOnTop");
    alwaysOnTop.setOnMouseClicked((event) -> SceneUtility.getStage().setAlwaysOnTop(!SceneUtility.getStage().isAlwaysOnTop()));
    mediaView.setMediaPlayer(mediaPlayer);
    registerEvents(btnBack, btnNext, btnPause, sliderTime, sliderVolume, lblTime, lblTitle, mediaView, videoProgress, imageFullScreen, stretchVideo);
  }

  private void setValues(MediaView mediaView, Slider sliderTime, Slider sliderVolume, Label lblTitle) {
    lblTitle.setText(title + String.format(" [%dx%d]", mediaPlayer.getMedia().getWidth(), mediaPlayer.getMedia().getHeight()));
    sliderTime.setMax(mediaPlayer.getTotalDuration().toSeconds());
    sliderVolume.setMax(0.5);
    sliderVolume.setValue(0.5);
  }

  private void setChangeListener(Label lblTime, Slider sliderTime, ProgressBar videoLoaded) {
    mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
      int hours = (int) newValue.toHours();
      int minutes = (int) newValue.toMinutes() % 60;
      int seconds = (int) newValue.toSeconds() % 60 % 60;
      int endHours = (int) mediaPlayer.getTotalDuration().toHours();
      int endMinutes = (int) mediaPlayer.getTotalDuration().toMinutes() % 60;
      int endSeconds = (int) mediaPlayer.getTotalDuration().toSeconds() % 60 % 60;
      lblTime.setText(String.format("%01d:%02d:%02d/%02d:%02d:%02d", hours, minutes, seconds, endHours, endMinutes, endSeconds));
      if (episode != null)
        episode.setSecondsWatched(mediaPlayer.getCurrentTime().toSeconds());
      if (!sliderTime.isValueChanging())
        sliderTime.setValue(mediaPlayer.getCurrentTime().toSeconds());
    });
    mediaPlayer.bufferProgressTimeProperty().addListener(((observable, oldValue, newValue) -> {
      double bufferedProgressPercent = (mediaPlayer.bufferProgressTimeProperty().get().toSeconds() * 100) / mediaPlayer.getTotalDuration().toSeconds();
      videoLoaded.setProgress(bufferedProgressPercent / 100);
    }));
  }

  private void registerEvents(Button btnBack, Button btnNext, Button btnPause, Slider sliderTime, Slider sliderVolume,
                              Label lblTime, Label lblTitle, MediaView mediaView, ProgressBar videoProgress,
                              ImageView imageFullScreen, ImageView stretchVideo) {
    root.layoutBoundsProperty().addListener((obs, oldValue, newValue) -> onSizeChange(mediaView));
    btnBack.setOnAction((event) -> goBack());
    btnNext.setOnAction((event) -> onFinish());
    btnPause.setOnAction((event) -> pause());
    sliderTime.valueProperty().addListener((listener) -> onSliderTimeChange(sliderTime));
    sliderVolume.valueProperty().addListener((listener) -> mediaPlayer.setVolume(sliderVolume.getValue()));
    lblTime.setText(mediaPlayer.getTotalDuration().toMinutes() + " min");
    mediaPlayer.statusProperty().addListener((obs) -> onChangeStatus(mediaPlayer.getStatus()));
    mediaPlayer.setOnEndOfMedia(this::onFinish);
    mediaPlayer.setOnError(() -> SceneUtility.handleException(mediaPlayer.getError()));
    mediaPlayer.volumeProperty().addListener((event) -> sliderVolume.setValue(mediaPlayer.getVolume()));
    setChangeListener(lblTime, sliderTime, videoProgress);
    cursorTask = new ControlsLayerTask((Pane) root.lookup("#paneLayer"), root);
    new Thread(cursorTask).start();
    SceneUtility.getStage().getScene().setOnKeyPressed(this::keyNavigation);
    btnBack.setOnKeyPressed(this::keyNavigation);
    sliderVolume.setOnKeyPressed(this::keyNavigation);
    root.lookup("#pauseLayer").setOnMouseClicked((event) -> pause());
    imageFullScreen.setOnMouseClicked((event) -> SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen()));
    stretchVideo.setOnMouseClicked((event) -> stretchVideo(stretchVideo, mediaView));
    mediaPlayer.setOnReady(() -> {
      setValues(mediaView, sliderTime, sliderVolume, lblTitle);
      if (element != null)
        afterInitialization();
    });
    SceneUtility.getStage().fullScreenProperty().addListener((change) -> imageFullScreen.setImage(SceneUtility.getStage().isFullScreen() ? smallScreenImage : fullScreenImage));
  }

  private void stretchVideo(ImageView stretchVideo, MediaView mediaView) {
    mediaView.setPreserveRatio(!mediaView.isPreserveRatio());
    stretchVideo.setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    onSizeChange(mediaView);
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
    if (element != null) {
      History.getHistory().saveToFile();
      Favorite.getInstance().saveToFile();
    }
    cursorTask.setInterrupted(true);
    mediaPlayer.dispose();
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
    SceneUtility.goToPreviousScene();
  }

  private void pause() {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
      mediaPlayer.pause();
    else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
      mediaPlayer.play();
  }

  private void onChangeStatus(MediaPlayer.Status status) {
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
  }

  private void onFinish() {
    Optional<Episode> nextEpisode = findNextEpisode();
    if (!nextEpisode.isPresent()) {
      new Alert(Alert.AlertType.NONE, "Hai raggiunto la fine, non è presente un prossimo episodio", ButtonType.OK).show();
      return;
    }
    Alert alert = new Alert(Alert.AlertType.NONE, "Vuoi guardare il prossimo episodio?", ButtonType.YES, ButtonType.NO);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.YES) {
      cursorTask.setInterrupted(true);
      mediaPlayer.dispose();
      showWaitAndLoad("Loading next episode...");
      boolean selected = new AnimePlayer(nextEpisode.get(), element).streaming();
      if (!selected) {
        hideWaitLoad();
        goBack();
      }
    }
  }

  private Optional<Episode> findNextEpisode() {
    if (element == null)
      return Optional.empty();
    if (!element.hasLoadedEpisodes())
      element.loadEpisodes();
    Season episodeSeason = element.getSeasons().stream()
            .filter(season -> season.getEpisodes().contains(episode)).findFirst().get();
    int episodeIndex = episodeSeason.getEpisodes().indexOf(episode);
    return episodeSeason.getEpisodes().size() > episodeIndex ? Optional.of(episodeSeason.getEpisodes().get(episodeIndex + 1)) : Optional.empty();
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
