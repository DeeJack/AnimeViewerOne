package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.components.general.ButtonBack;
import me.deejack.animeviewer.gui.components.streaming.AlwaysOnTopImage;
import me.deejack.animeviewer.gui.components.streaming.ButtonNext;
import me.deejack.animeviewer.gui.components.streaming.ButtonPause;
import me.deejack.animeviewer.gui.components.streaming.FullScreenImage;
import me.deejack.animeviewer.gui.components.streaming.LabelTime;
import me.deejack.animeviewer.gui.components.streaming.ProgressBarBuffer;
import me.deejack.animeviewer.gui.components.streaming.SliderTime;
import me.deejack.animeviewer.gui.components.streaming.SliderVolume;
import me.deejack.animeviewer.gui.components.streaming.StretchVideoImage;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
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
    setupEpisode();
    hideWaitLoad();
    mediaPlayer.play();
  }

  public void setupEpisode() {
    if (anime == null)
      return;
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
      mediaPlayer.setStartTime(Duration.seconds(episode.getSecondsWatched()));
  }

  private void setupNodes() {
    ButtonPause btnPause = new ButtonPause(mediaPlayer);
    ButtonNext btnNext = new ButtonNext(anime, episode);
    root = (Pane) SceneUtility.loadParent("/scenes/streaming.fxml");
    ButtonBack btnBack = (ButtonBack) root.lookup("#btnBack");
    MediaView mediaView = (MediaView) root.lookup("#mediaView");
    Label lblTitle = (Label) root.lookup("#lblTitle");

    cursorTask = new ControlsLayerTask((Pane) root.lookup("#paneLayer"), mediaView);
    new Thread(cursorTask).start();
    lblTitle.setText(title);
    mediaView.setMediaPlayer(mediaPlayer);

    StackPane stackPane = new StackPane(new ProgressBarBuffer(mediaPlayer), new SliderTime(mediaPlayer));
    stackPane.getStylesheets().add("/assets/style.css");
    HBox.setHgrow(stackPane, Priority.ALWAYS);

    Node[] nodes = {
            btnPause, btnNext, stackPane, new LabelTime(mediaPlayer), new SliderVolume(mediaPlayer),
            new FullScreenImage(), new StretchVideoImage(mediaView, root), new AlwaysOnTopImage()
    };
    ((Pane) root.lookup("#bottomBar")).getChildren().addAll(nodes);
    registerEvents(btnPause, btnNext, btnBack, mediaView);
  }

  private void registerEvents(ButtonPause btnPause, ButtonNext btnNext, ButtonBack btnBack, MediaView mediaView) {
    root.lookup("#pauseLayer").setOnMouseClicked((event) -> btnPause.pause());
    root.layoutBoundsProperty().addListener((event, oldValue, newValue) -> onSizeChange(mediaView));
    btnNext.setOnNextEpisode(this::onFinish);
    btnBack.setOnAction((event) -> {
      onFinish();
      SceneUtility.goToPreviousScene();
    });
    root.setOnKeyPressed((event) -> StreamingUtility.keyNavigation(event, mediaPlayer));
    mediaPlayer.statusProperty().addListener((event, oldValue, newValue) -> StreamingUtility.onChangeStatus(newValue, btnPause));
    if (episode != null)
      mediaPlayer.currentTimeProperty().addListener((event, oldValue, newValue) -> episode.setSecondsWatched(newValue.toSeconds()));
  }

  private void onFinish() {
    if (anime != null) {
      FilesUtility.saveHistory();
      FilesUtility.saveFavorite();
    }
    cursorTask.setInterrupted(true);
    mediaPlayer.dispose();
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
  }

  private void onSizeChange(MediaView view) {
    if (root.getWidth() > root.getHeight())
      view.setFitHeight(root.getHeight());
    else view.setFitWidth(root.getWidth());
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