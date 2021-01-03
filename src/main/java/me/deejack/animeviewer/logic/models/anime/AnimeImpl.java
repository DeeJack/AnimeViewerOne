package me.deejack.animeviewer.logic.models.anime;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;

/**
 * Represent an anime in the source
 */
public abstract class AnimeImpl extends ParsedHttpAnime {
  /**
   * A list containing some anime correlated at this one
   */
  private final List<AnimeImpl> correlatedAnime = new ArrayList<>();
  /**
   * {@link AnimeInformation}, it contains the main information about tha anime who sometimes are obtainable
   * in the list of the anime of the sites, without loading the anime page
   */
  @Expose
  private AnimeInformation animeInformation;
  /**
   * The page that contains the info and the createStreaming links of this anime, initialize this only when it is necessary,
   * otherwise it can be source of lag
   */
  /*protected Document animePage;*/
  /**
   * A list containing the seasons of this anime
   */
  private List<Episode> episodes = new ArrayList<>();
  private boolean hasBeenLoaded = false;

  /**
   * Main constructor for an anime
   *
   * @param animeInformation An instance of AnimeInformation that contains the basic infos about the anime, create an instance
   */
  public AnimeImpl(String url, AnimeInformation animeInformation) {
    super(url);
    this.animeInformation = animeInformation;
  }

  protected void addEpisode(Episode episode) {
    episodes.add(episode);
  }

  public List<Episode> getEpisodes() {
    if (episodes == null)
      episodes = new ArrayList<>();
    return episodes;
  }

  /**
   * Call the loadEpisodes and the setInfos in another Thread
   *
   * @param callback The callback to be called when the method has finished
   */
  @Override
  public void loadAsync(SuccessListener callback) {
    new Thread(() -> {
      animeInformation = fetchAnimeDetails();
      episodes = fetchAnimeEpisodes();
      hasBeenLoaded = true;
      callback.onSuccess();
    }).start();
  }

  @Override
  public void load() {
    animeInformation = fetchAnimeDetails();
    episodes = fetchAnimeEpisodes();
    hasBeenLoaded = true;
  }

  public void saveImageToFile(File output) {
    try {
      String link = getAnimeInformation().getImageUrl();
      Connection.Response response = ConnectionUtility.connect(link, true);
      Files.write(Paths.get(output.toURI()), response.bodyAsBytes());
    } catch (IOException e) {
      GeneralUtility.logError(e);
    }
  }

  public AnimeInformation getAnimeInformation() {
    return animeInformation;
  }

  /*public void setAnimePage(Document animePage) {
    this.animePage = animePage;
  }*/

  public boolean hasBeenLoaded() {
    return hasBeenLoaded;
  }
}
