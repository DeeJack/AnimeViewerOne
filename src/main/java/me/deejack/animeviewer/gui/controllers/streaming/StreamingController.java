package me.deejack.animeviewer.gui.controllers.streaming;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import me.deejack.animeviewer.gui.components.general.ButtonBack;
import me.deejack.animeviewer.gui.components.streaming.BottomBar;
import me.deejack.animeviewer.gui.components.streaming.MediaViewStreaming;
import me.deejack.animeviewer.gui.components.streaming.bottombar.ButtonNext;
import me.deejack.animeviewer.gui.scenes.BaseScene;
import me.deejack.animeviewer.gui.utils.FilesUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;
import me.deejack.animeviewer.logic.history.HistoryEpisode;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import java.time.LocalDateTime;
import java.util.Optional;

import static me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility.findNextEpisode;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;

public class StreamingController implements BaseScene {
  private final MediaPlayer mediaPlayer;
  private final Episode episode;
  private final Anime anime;
  private final Pane root;
  private String title = "";
  private ControlsLayerTask cursorTask;
  private ButtonBack buttonBack;

  public StreamingController(MediaPlayer mediaPlayer, Episode episode, Anime anime) {
    this.mediaPlayer = mediaPlayer;
    this.episode = episode;
    this.anime = anime;
    root = (Pane) SceneUtility.loadParent("/scenes/streaming.fxml");
  }

  public void setUpPlayer(boolean isNewTab, Tab currentTab) {
    if (anime != null)
      title = String.format("%s - %s %d - %s",
              anime.getAnimeInformation().getName(), LocalizedApp.getInstance().getString("Episode"),
              episode.getNumber(), episode.getTitle());
    setupNodes(isNewTab, currentTab);
    setupEpisode();
    hideWaitLoad();
    mediaPlayer.play();
  }

  public void setupEpisode() {
    if (anime == null)
      return;
    Optional<HistoryElement> historyElement = History.getHistory().get(anime);
    if (historyElement.isPresent()) {
      if (historyElement.get().getEpisodesHistory().contains(episode))
        History.getHistory().getHistoryEpisode(historyElement.get(), episode)
                .ifPresent((historyEpisode -> episode.setSecondsWatched(historyEpisode.getEpisode().getSecondsWatched())));
      else historyElement.get().addEpisode(new HistoryEpisode(episode, LocalDateTime.now()));
    } else
      History.getHistory().add(new HistoryElement(anime, new HistoryEpisode(episode, LocalDateTime.now())));
    if (episode.getSecondsWatched() > 0)
      mediaPlayer.setStartTime(Duration.seconds(episode.getSecondsWatched()));
  }

  private void setupNodes(boolean isNewTab, Tab currentTab) {
    ButtonNext buttonNext = new ButtonNext(anime, episode, isNewTab, currentTab);
    BottomBar bottomBar = new BottomBar(root, mediaPlayer, buttonNext, title);
    MediaViewStreaming mediaView = new MediaViewStreaming(mediaPlayer, root);
    mediaView.setMediaPlayer(mediaPlayer);
    cursorTask = new ControlsLayerTask((Pane) root.lookup("#paneLayer"), mediaView);
    root.getChildren().add(0, mediaView);
    cursorTask = bottomBar.getCursorTask();
    buttonBack = bottomBar.getButtonBack();

    registerEvents(buttonNext, bottomBar.getButtonBack());
  }

  private void registerEvents(ButtonNext buttonNext, ButtonBack buttonBack) {
    buttonNext.setOnNextEpisode(this::onFinish);
    buttonBack.setOnAction((event) -> {
      onFinish();
      SceneUtility.goToPreviousScene();
    });
    if (episode != null)
      mediaPlayer.currentTimeProperty().addListener((event, oldValue, newValue) -> episode.setSecondsWatched(newValue.toSeconds()));
    root.lookup("#paneLayer").setOnMouseClicked(this::checkDoubleClick);
  }

  public void setOnBack(EventHandler<ActionEvent> onBack) {
    buttonBack.setOnAction(onBack);
  }

  private void checkDoubleClick(MouseEvent mouseEvent) {
    if (mouseEvent.getButton() == MouseButton.PRIMARY &&
            mouseEvent.getClickCount() == 2)
      SceneUtility.getStage().setFullScreen(!SceneUtility.getStage().isFullScreen());
  }

  public void onFinish() {
    if (mediaPlayer.getTotalDuration().subtract(mediaPlayer.getCurrentTime()).lessThan(Duration.minutes(1)))
      addNextEpisodeToHistory();
    if (anime != null) {
      FilesUtility.saveHistory();
      FilesUtility.saveFavorite();
    }
    cursorTask.setInterrupted(true);
    mediaPlayer.dispose();
    SceneUtility.getStage().getScene().setCursor(Cursor.DEFAULT);
    SceneUtility.getStage().setAlwaysOnTop(false);
  }

  private void addNextEpisodeToHistory() {
    Optional<Episode> nextEpisode = findNextEpisode(anime, episode);
    nextEpisode.ifPresent((episode) ->
            History.getHistory().get(anime).ifPresent((anime) ->
                    anime.addEpisode(new HistoryEpisode(episode, LocalDateTime.now()))));
  }

  @Override
  public void onBackFromOtherScene() {
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