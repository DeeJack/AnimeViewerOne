package me.deejack.animeviewer.logic.anime;

import com.google.gson.annotations.Expose;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Status;

/**
 * A class that contains the main information about a anime, that sometimes (like in dreamsub) can be obtained without
 * connecting to the anime page, but directly in the list of the anime
 */
public class AnimeInformation {
  /**
   * The name of the anime
   */
  @Expose
  private final String name;
  /**
   * The rating, if present, -1 if not present
   */
  private final double rating;
  @Expose
  private final String imageUrl;
  /**
   * When it was released
   */
  @Expose
  private String releaseYear;
  /**
   * The number of the episodes
   */
  @Expose
  private int episodes;
  /**
   * The list of genres of this anime
   */
  @Expose
  private List<Genre> genres;
  /**
   * The plot for this anime
   */
  @Expose
  private String plot = "";
  /**
   * The animeStatus of the anime, like COMPLETE or ON_GOING
   */
  private Status animeStatus;

  public AnimeInformation(String releaseYear, String name, int episodes, double rating,
                          List<Genre> genres, String imageUrl,
                          Status animeStatus) {
    this.releaseYear = releaseYear;
    this.name = name;
    this.episodes = episodes;
    this.rating = rating;
    this.genres = genres;
    this.imageUrl = imageUrl;
    this.animeStatus = animeStatus;
  }

  /*public abstract AnimeImpl getAnime();*/

  // Getters
  public String getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  public String getName() {
    return name;
  }

  public int getNumberOfEpisodes() {
    return episodes;
  }

  public double getRating() {
    return rating;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setEpisodes(int episodes) {
    this.episodes = episodes;
  }

  public void setAnimeStatus(Status animeStatus) {
    this.animeStatus = animeStatus;
  }

  public String getPlot() {
    return plot;
  }

  public void setPlot(String plot) {
    this.plot = plot;
  }
}
