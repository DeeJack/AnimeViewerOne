package me.deejack.animeviewer.gui.components.streaming;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import me.deejack.animeviewer.gui.controllers.streaming.AnimePlayer;
import me.deejack.animeviewer.gui.controllers.streaming.ControlsLayerTask;
import me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.LoadingUtility.hideWaitLoad;
import static me.deejack.animeviewer.gui.utils.LoadingUtility.showWaitAndLoad;

public class BottomBar extends HBox {
  /*private final Pane root;
  private final MediaPlayer mediaPlayer;
  private final Anime anime;
  private final ControlsLayerTask cursorTask;

  public BottomBar(MediaPlayer mediaPlayer, Anime anime, Episode episode, Pane root, ControlsLayerTask cursorTask) {
    this.root = root;
    this.mediaPlayer = mediaPlayer;
    this.anime = anime;
    this.cursorTask = cursorTask;
  }

  public void createNodes() {

    Label lblTime = (Label) root.lookup("#lblTime");
    Label lblTitle = (Label) root.lookup("#lblTitle");
  }

  public void createButtons() {
    Button btnNext = (Button) root.lookup("#btnNext");
    Button btnPause = (Button) root.lookup("#btnPause");

    btnNext.setOnAction((event) -> StreamingUtility.onFinish(anime, mediaPlayer, cursorTask));
    btnPause.setOnAction((event) -> pause(btnPause));
  }

  public void createImages() {
    ImageView imageFullScreen = (ImageView) root.lookup("#imgFullScreen");
    ImageView imgStretchVideo = (ImageView) root.lookup("#stretchVideo");
    ImageView imgAlwaysOnTop = (ImageView) root.lookup("#alwaysOnTop");
  }

  public void createSlider() {
    Slider sliderTime = (Slider) root.lookup("#sliderTime");
    ProgressBar videoProgress = (ProgressBar) root.lookup("#videoProgress");
    Slider sliderVolume = (Slider) root.lookup("#sliderVolume");
  }

  public void registerEvents(ImageView imgAlwaysOnTop, ImageView imgStretchVideo) {
    imgAlwaysOnTop.setOnMouseClicked((event) -> SceneUtility.getStage().setAlwaysOnTop(!SceneUtility.getStage().isAlwaysOnTop()));

    stretchVideo.setOnMouseClicked((event) -> stretchVideo(stretchVideo, mediaView));
  }

  private void pause(Button btnPause) {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
      mediaPlayer.pause();
      btnPause.setText(">");
    }
    else {*//*if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)*//*
      mediaPlayer.play();
      btnPause.setText("| |");
    }
  }

  private void stretchVideo(ImageView stretchVideo, MediaView mediaView) {
    mediaView.setPreserveRatio(!mediaView.isPreserveRatio());
    stretchVideo.setImage(mediaView.isPreserveRatio() ? stretchVideoFull : stretchVideoSmall);
    onSizeChange(mediaView);
  }*/
}
