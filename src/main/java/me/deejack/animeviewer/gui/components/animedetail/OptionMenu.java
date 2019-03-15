package me.deejack.animeviewer.gui.components.animedetail;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import me.deejack.animeviewer.gui.controllers.download.DownloadController;
import me.deejack.animeviewer.gui.utils.SceneUtility;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class OptionMenu extends ContextMenu {
  private final ListViewEpisodes listViewEpisodes;

  public OptionMenu(ListViewEpisodes listViewEpisodes) {
    this.listViewEpisodes = listViewEpisodes;
    initialize();
  }

  private void initialize() {
    MenuItem thisEpisode = new MenuItem("Download");
    MenuItem selectedEpisodes = new MenuItem("Download episodi selezionati");
    MenuItem allEpisodes = new MenuItem("Download tutti gli episodi");
    registerEvents(thisEpisode, selectedEpisodes, allEpisodes);
    getItems().addAll(thisEpisode, selectedEpisodes, allEpisodes);
  }

  private void registerEvents(MenuItem thisEpisode, MenuItem selectedEpisode, MenuItem allEpisodes) {
    thisEpisode.setOnAction((event) -> listViewEpisodes.getSelectionModel().getSelectedItem().download());
    selectedEpisode.setOnAction((event) -> downloadEpisodes(listViewEpisodes.getSelectionModel().getSelectedItems()));
    allEpisodes.setOnAction((event) -> downloadAll());
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
