package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.deejack.animeviewer.logic.anime.Anime;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.nodes.Element;

public final class AnimeLeggendariAnime extends Anime {

  public AnimeLeggendariAnime(AnimeInformation animeInformation) {
    super(animeInformation);
  }

  @Override
  public void loadEpisodes() {
    if (animePage == null)
      animePage = ConnectionUtility.getPage(getAnimeInformation().getUrl(), false);

    List<Episode> episodes = new ArrayList<>();
    episodes.add(new AnimeLeggendariEpisode("", 1, getAnimeInformation().getUrl()));
    Element pageDiv = animePage.getElementsByClass("pagination clearfix").first();
    if (pageDiv != null) {
      for (Element element : pageDiv.children()) {
        if (!element.tagName().equals("a"))
          continue;
        Optional<Integer> number = GeneralUtility.tryParse(element.text());
        getAnimeInformation().setEpisodes(number.orElse(-1));
        episodes.add(new AnimeLeggendariEpisode("", number.orElse(-1), element.attr("href")));
      }
    }
    addSeason(new Season("", episodes));
  }

  @Override
  public String getPlot() {
    if (super.getPlot() == null) {
      if (animePage == null)
        animePage = ConnectionUtility.getPage(getAnimeInformation().getUrl(), false);
      String infos = animePage.getElementsByClass("entry-content").first().wholeText();
      Pattern plotPattern = Pattern.compile("Trama:?\n{1,2}(.*)", Pattern.CASE_INSENSITIVE);
      Matcher plotMatcher = plotPattern.matcher(infos);
      if (plotMatcher.find())
        setPlot(plotMatcher.group(1));
    }
    return super.getPlot();
  }

  @Override
  public void setInfos() {
    if (animePage == null)
      animePage = ConnectionUtility.getPage(getAnimeInformation().getUrl(), true);
    String infos = animePage.getElementsByClass("entry-content").first().wholeText();
    Pattern genresPattern = Pattern.compile("Genere:\\s?(.*)", Pattern.CASE_INSENSITIVE);
    Matcher genresMatcher = genresPattern.matcher(infos);
    if (getAnimeInformation().getGenres() == null)
      getAnimeInformation().setGenres(new ArrayList<>());
    if (genresMatcher.find()) {
      for (String genere : genresMatcher.group(1).split(", "))
        getAnimeInformation().getGenres().add(new Genre(genere.trim()));
    }
    getAnimeInformation().setStatus(AnimeLeggendariSite.AnimeLeggendariStatus.TUTTO);
  }

  public short getYear() {
    String infos = animePage.getElementsByClass("entry-content").first().wholeText();
    short releaseYear = 0;
    if (infos.contains("Data di Uscita")) {
      Pattern pattern = Pattern.compile("Data di Uscita:.*(\\d{4})", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(infos);
      if (matcher.find())
        releaseYear = Short.parseShort(matcher.group(1));
    } else if (infos.contains("Anno")) {
      Pattern pattern = Pattern.compile("Anno:.+(\\d{4})", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(infos);
      if (matcher.find())
        releaseYear = Short.parseShort(matcher.group(1));
    }
    return releaseYear;
  }
}
