package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.Anime;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.Episode;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Season;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DreamSubAnime extends Anime {
  private final boolean isFilm;

  public DreamSubAnime(AnimeInformation animeInformation) {
    super(animeInformation);
    isFilm = animeInformation.getUrl().contains("/movie/");// || animeInformation.getUrl().contains("/oav/");
  }

  // TODO
  public static Anime parsePageToAnime(Document page) throws Exception {
    throw new Exception("TODO");
  }

  @Override
  public void loadEpisodes() {
    if (animePage == null)
      setAnimePage(ConnectionUtility.getPage(getAnimeInformation().getUrl(), false));
    if (isFilm) { // if is a film, the video is directly in the page
      List<Episode> episode = new ArrayList<>();
      /*String releaseDate = animePage.getElementById("episodeInfo").wholeText().substring(
              animePage.getElementById("episodeInfo").wholeText().indexOf("Data di Uscita:"));
      releaseDate = releaseDate.substring(releaseDate.indexOf("\n"));*/
      episode.add(new DreamSubEpisode(
              getAnimeInformation().getName(), getAnimeInformation().getNumberOfEpisodes(),
              getAnimeInformation().getUrl(), LocalDate.now()));//LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("d/M/y"))));
      addSeason(new Season("Film", episode));
      return;
    }
    Elements seasEpisode = animePage.getElementsByClass("seasEpisode");
    Elements seasName = animePage.getElementsByClass("seasonEp");
    for (Element seasElement : seasName) {
      addSeason(new Season(seasElement.text(),
              getEpisodeBySeason(seasEpisode.get(seasName.indexOf(seasElement)))));
    }
  }

  private List<Episode> getEpisodeBySeason(Element season) {
    List<Episode> episodes = new ArrayList<>();
    for (Element episode : season.getElementsByTag("li")) {
      // Ex: Episodio 1 - 15/02/2007 - Ritorno a casa (ITA e Sub ITA Streaming & Download HD)
      String num = episode.text().split(" ")[1].split(" ")[0];
      String releaseDate = episode.text().split("-")[1].trim();
      String title = episode.getElementsByTag("i").first().text();
      boolean alreadyReleased = !episode.getElementsByTag("a").isEmpty() &&
              !episode.getElementsByTag("a").get(0).attr("href").isEmpty();
      String url = alreadyReleased ? DreamSubSite.BASE_URL + episode.getElementsByTag("a").get(0).attr("href") : "";
      episodes.add(new DreamSubEpisode(title, Integer.parseInt(num), url,
              LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("d/M/y"))));
    }
    return episodes;
  }

  private void verifyUrl() {
    // TODO
  }

  @Override
  public void setInfos() {
    getPlot();
    if (animePage == null)
      setAnimePage(ConnectionUtility.getPage(getAnimeInformation().getUrl(), false));
    if (animePage.getElementsByClass("innerText").isEmpty())
      return;
    if (getAnimeInformation().getGenres() == null || getAnimeInformation().getGenres().isEmpty()) {
      String info = animePage.getElementsByClass("innerText").get(0).wholeText();
      String subInfo = info.substring(info.indexOf("Genere:"));
      subInfo = subInfo.substring(0, subInfo.indexOf("\n"));
      getAnimeInformation().setGenres(Arrays.stream(subInfo.split(", ")).map(Genre::new).collect(Collectors.toList()));
    }
    String info = animePage.getElementsByClass("innerText").get(0).wholeText();
    String status = info.substring(info.indexOf("Episodi:"));
    status = status.substring(0, status.indexOf("\n"));
    getAnimeInformation().setStatus(status.contains("+") ? DreamSubSite.DreamSubAnimeStatus.CORSO : DreamSubSite.DreamSubAnimeStatus.COMPLETATE);
  }

  @Override
  public String getPlot() {
    if (super.getPlot() == null) {
      if (animePage == null)
        setAnimePage(ConnectionUtility.getPage(getAnimeInformation().getUrl(), false));
      if (isFilm) {
        Element element = animePage.getElementById("episodeInfo");
        String description = element.wholeText().substring(element.wholeText().indexOf("Trama Anime") + "Trama Anime".length());
        description = description.substring(0, description.indexOf("\n"));
        setPlot(description);
      } else
        setPlot(animePage.getElementById("actContTrama").text());
    }
    return super.getPlot();
  }
}
