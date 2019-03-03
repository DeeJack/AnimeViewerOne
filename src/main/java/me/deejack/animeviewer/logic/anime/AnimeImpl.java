package me.deejack.animeviewer.logic.anime;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.async.events.SuccessListener;
import org.jsoup.nodes.Document;

/**
 * Represent an anime in the source
 */
public abstract class AnimeImpl implements Anime {
  /**
   * A list containing some anime correlated at this one
   */
  private final List<AnimeImpl> correlatedAnime = new ArrayList<>();
  /**
   * {@link AnimeInformation}, it contains the main information about tha anime who sometimes are obtainable
   * in the list of the anime of the sites, without loading the anime page
   */
  @Expose
  private final AnimeInformation animeInformation;
  /**
   * The page that contains the info and the streaming links of this anime, initialize this only when it is necessary,
   * otherwise it can be source of lag
   */
  protected Document animePage;
  /**
   * A list containing the seasons of this anime
   */
  private List<Season> seasons = new ArrayList<>();
  private boolean hasLoadedEpisodes = false;

  /**
   * Main constructor for an anime
   *
   * @param animeInformation An instance of AnimeInformation that contains the basic infos about the anime, create an instance
   */
  public AnimeImpl(AnimeInformation animeInformation) {
    this.animeInformation = animeInformation;
  }

  protected void addSeason(Season season) {
    if (seasons == null)
      seasons = new ArrayList<>();
    seasons.add(season);
    hasLoadedEpisodes = true;
  }

  @Override
  public List<Season> getSeasons() {
    return seasons == null ? Collections.unmodifiableList(new ArrayList<>()) : Collections.unmodifiableList(seasons);
  }

  /**
   * Call the loadEpisodes and the setInfos in another Thread
   *
   * @param callback The callback to be called when the method has finished
   */
  public void loadAsync(SuccessListener callback) {
    new Thread(() -> {
      setInfos();
      loadEpisodes();
      callback.onSuccess();
    }).start();
  }

  public AnimeInformation getAnimeInformation() {
    return animeInformation;
  }

  public void setAnimePage(Document animePage) {
    this.animePage = animePage;
  }

  @Override
  public boolean hasLoadedEpisodes() {
    return hasLoadedEpisodes;
  }
}
