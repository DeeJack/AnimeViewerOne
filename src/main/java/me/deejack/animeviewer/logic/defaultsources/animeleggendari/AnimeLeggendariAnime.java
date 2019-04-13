package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.models.anime.AnimeImpl;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AnimeLeggendariAnime extends AnimeImpl {

  public AnimeLeggendariAnime(String url, AnimeInformation animeInformation) {
    super(url, animeInformation);
  }

  @Override
  protected Connection.Response animePageRequest() {
    return ConnectionUtility.connect(getUrl(), true);
  }

  @Override
  protected AnimeInformation parseAnimeDetails(Document document) {
    String infos = document.getElementsByClass("entry-content").first().wholeText();
    Pattern genresPattern = Pattern.compile("Genere:\\s?(.*)", Pattern.CASE_INSENSITIVE);
    Matcher genresMatcher = genresPattern.matcher(infos);
    if (getAnimeInformation().getGenres() == null)
      getAnimeInformation().setGenres(new ArrayList<>());
    if (genresMatcher.find()) {
      for (String genere : genresMatcher.group(1).split(", "))
        getAnimeInformation().getGenres().add(new Genre(genere.trim()));
    }
    getAnimeInformation().setReleaseYear(getYear(infos));
    setPlot(infos);
    return getAnimeInformation();
  }

  private void setPlot(String info) {
    Pattern plotPattern = Pattern.compile("Trama:?\n{1,2}(.*)", Pattern.CASE_INSENSITIVE);
    Matcher plotMatcher = plotPattern.matcher(info);
    if (plotMatcher.find())
      getAnimeInformation().setPlot(plotMatcher.group(1));
  }

  public String getYear(String info) {
    String releaseYear = "Not released";
    if (info.contains("Data di Uscita")) {
      Pattern pattern = Pattern.compile("Data di Uscita:.*(\\d{4})", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(info);
      if (matcher.find())
        releaseYear = matcher.group(1);
    } else if (info.contains("Anno")) {
      Pattern pattern = Pattern.compile("Anno:.+(\\d{4})", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(info);
      if (matcher.find())
        releaseYear = matcher.group(1);
    }
    return releaseYear;
  }

  @Override
  protected String episodeSelector() {
    return "div.pagination > *, div#main-content";
  }

  @Override
  protected Episode parseEpisode(Element element) {
    if (element.tagName().equalsIgnoreCase("div")) {
      return new AnimeLeggendariEpisode("1", 1, getUrl(), null, getAnimeInformation().getName());
    }
    String title = element.text();
    String link = element.attr("href");
    int number = GeneralUtility.tryParse(title).orElse(-1);
    if (title.equalsIgnoreCase("1"))
      return null;
    return new AnimeLeggendariEpisode(title, number, link, null, getAnimeInformation().getName());
  }
}
