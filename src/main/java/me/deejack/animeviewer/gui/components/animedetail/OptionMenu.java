package me.deejack.animeviewer.gui.components.animedetail;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;
import me.deejack.animeviewer.logic.models.episode.Episode;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class OptionMenu extends ContextMenu {
  private final ListViewEpisodes listViewEpisodes;

  public OptionMenu(ListViewEpisodes listViewEpisodes) {
    this.listViewEpisodes = listViewEpisodes;
    initialize();
  }

  private void initialize() {
    MenuItem thisEpisode = new MenuItem(LocalizedApp.getInstance().getString("Download"));
    MenuItem selectedEpisodes = new MenuItem(LocalizedApp.getInstance().getString("MenuItemDownloadSelected"));
    MenuItem allEpisodes = new MenuItem(LocalizedApp.getInstance().getString("MenuItemDownloadAll"));
    MenuItem openOnBrowser = new MenuItem(LocalizedApp.getInstance().getString("OpenOnBrowser"));
    registerEvents(thisEpisode, selectedEpisodes, allEpisodes, openOnBrowser);
    getItems().addAll(thisEpisode, selectedEpisodes, allEpisodes, openOnBrowser);
  }

  private void registerEvents(MenuItem thisEpisode, MenuItem selectedEpisode, MenuItem allEpisodes, MenuItem openOnBrowser) {
    thisEpisode.setOnAction((event) -> listViewEpisodes.getSelectionModel().getSelectedItem().download());
    selectedEpisode.setOnAction((event) -> downloadEpisodes(listViewEpisodes.getSelectionModel().getSelectedItems()));
    allEpisodes.setOnAction((event) -> downloadAll());
    openOnBrowser.setOnAction((action) -> {
      try {
        Desktop.getDesktop().browse(new URI(listViewEpisodes.getSelectionModel().getSelectedItem().getEpisode().getUrl()));
      } catch (URISyntaxException | IOException e) {
        handleException(e);
      }
    });
  }

  private void downloadAll() {
    List<ItemEpisode> itemEpisodes = listViewEpisodes.getItems();
    downloadEpisodes(itemEpisodes);
  }

  private void downloadEpisodes(List<ItemEpisode> itemEpisodes) {
    List<Episode> episodes = new ArrayList<>();
    itemEpisodes.stream().map(ItemEpisode::getEpisode).forEach(episodes::add);
    DownloadController controller = DownloadController.getDownloadController();
    controller.addDownloads(episodes, itemEpisodes.get(0).getAnime().getAnimeInformation().getName());
  }

  public void open(double x, double y) {
    show(SceneUtility.getStage(), x, y);
  }
}
