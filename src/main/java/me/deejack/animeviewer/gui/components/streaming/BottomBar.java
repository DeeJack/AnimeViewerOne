package me.deejack.animeviewer.gui.components.streaming;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import me.deejack.animeviewer.gui.components.general.ButtonBack;
import me.deejack.animeviewer.gui.components.streaming.bottombar.*;
import me.deejack.animeviewer.gui.controllers.streaming.ControlsLayerTask;
import me.deejack.animeviewer.gui.controllers.streaming.StreamingUtility;

public class BottomBar extends HBox {
  private final Pane root;
  private final MediaPlayer mediaPlayer;
  private final ControlsLayerTask cursorTask;
  private ButtonBack buttonBack;
  private final MediaViewStreaming mediaView;
  private FullScreenImage fullScreen;

  public BottomBar(Pane root, MediaPlayer mediaPlayer, ButtonNext buttonNext, String title, MediaViewStreaming mediaView) {
    this.root = root;
    this.mediaPlayer = mediaPlayer;
    this.mediaView = mediaView;
    cursorTask = new ControlsLayerTask((Pane) root.lookup("#paneLayer"), mediaView);
    setup(buttonNext, title);
  }

  private void setup(ButtonNext buttonNext, String title) {
    ButtonPause btnPause = new ButtonPause(mediaPlayer);
    buttonBack = (ButtonBack) root.lookup("#btnBack");
    Label lblTitle = (Label) root.lookup("#lblTitle");

    lblTitle.setText(title);

    StackPane stackPane = new StackPane(new ProgressBarBuffer(mediaPlayer), new SliderTime(mediaPlayer));
    stackPane.getStylesheets().add("/assets/streamingStyle.css");
    HBox.setHgrow(stackPane, Priority.ALWAYS);
    fullScreen = new FullScreenImage();

    Node[] nodes = {
            btnPause, buttonNext, stackPane, new LabelTime(mediaPlayer), new SliderVolume(mediaPlayer),
            fullScreen, new StretchVideoImage(mediaView, root), new AlwaysOnTopImage()
    };
    ((Pane) root.lookup("#bottomBar")).getChildren().addAll(nodes);
    registerEvents(btnPause, buttonNext, buttonBack);
  }

  private void registerEvents(ButtonPause btnPause, ButtonNext btnNext, ButtonBack btnBack) {
    root.lookup("#pauseLayer").setOnMouseClicked((event) -> btnPause.pause());
    //root.layoutBoundsProperty().addListener((event, oldValue, newValue) -> onSizeChange(mediaView));
    root.setOnKeyPressed((event) -> StreamingUtility.keyNavigation(event, mediaPlayer));
    mediaPlayer.statusProperty().addListener((event, oldValue, newValue) -> StreamingUtility.onChangeStatus(newValue, btnPause));
    mediaPlayer.setOnReady(() -> new Thread(cursorTask).start());
  }

  public MediaPlayer getMediaPlayer() {
    return mediaPlayer;
  }

  public Pane getRoot() {
    return root;
  }

  public ButtonBack getButtonBack() {
    return buttonBack;
  }

  public ControlsLayerTask getCursorTask() {
    return cursorTask;
  }

  public void clickFullScreen() {
    fullScreen.getOnMouseClicked().handle(null);
  }
}
