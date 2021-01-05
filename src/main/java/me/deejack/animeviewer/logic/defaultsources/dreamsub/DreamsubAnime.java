package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.AnimeStatus;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.models.anime.AnimeImpl;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.utils.GeneralUtility;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DreamsubAnime extends AnimeImpl {

  public DreamsubAnime(String name, String url, String imageUrl) {
    super(url, new AnimeInformation("Not released", name, -1, -1, new ArrayList<>(), imageUrl, AnimeStatus.UNKNOWN));
  }

  @Override
  public AnimeInformation parseAnimeDetails(Document document) {
    if (document.getElementsByClass("detail-content").isEmpty())
      return getAnimeInformation();
    Element container = document.getElementsByClass("detail-content").first();

    String elementInfo = container.getElementsByClass("dci-spe").first().text();
    String dateSubString = elementInfo.substring(elementInfo.indexOf("Data: ") + "Data: ".length());
    String episodesSubString = elementInfo.substring(elementInfo.indexOf("Episodi: ") + "Episodi: ".length());
    episodesSubString = episodesSubString.substring(0, episodesSubString.indexOf(' '));
    String releaseDate = dateSubString.substring(0, dateSubString.indexOf(','));
    var genresText = container.getElementsByTag("a").stream().filter(el -> el.attr("href")
            .startsWith("/genere/")).map(Element::text);
    String plot = container.getElementsByClass("dci-desc").first().text();
    List<Genre> genres = genresText.map(Genre::new).collect(Collectors.toList());
    getAnimeInformation().setGenres(genres);
    getAnimeInformation().setEpisodes(GeneralUtility.tryParse(episodesSubString.replaceAll("\\+", "")).orElse(-1));
    getAnimeInformation().setReleaseYear(releaseDate);
    getAnimeInformation().setPlot(plot);
    return getAnimeInformation();
  }

  @Override
  public String episodeSelector() {
    return "ul#episodes-sv > ul.innerSeas > li, div.main-content > a.dwButton";
  }

  @Override
  public Episode parseEpisode(Element element) {
    if (element.tagName().equalsIgnoreCase("div"))
      return new DreamsubEpisode(getAnimeInformation().getName(), 1, getUrl(),
              LocalDate.parse(getAnimeInformation().getReleaseYear(), DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ITALY)));
    String num = element.text().split(" ")[1].split(" ")[0].replaceAll(":", "");
    String releaseDate = element.children().get(0).children().get(1).text().trim();
    String title = element.children().get(0).children().get(0).text();
    boolean alreadyReleased = !element.getElementsByTag("a").isEmpty() &&
            !element.getElementsByTag("a").get(0).attr("href").isEmpty();
    String url = alreadyReleased ? "https://www.dreamsub.stream" + element.getElementsByTag("a").get(0).attr("href") : "";
    return new DreamsubEpisode(title, Integer.parseInt(num), url,
            LocalDate.parse(releaseDate.toLowerCase(), DateTimeFormatter.ofPattern("dd MMMM uuuu", Locale.ITALY)));
  }

  @Override
  public void afterEpisodeLoaded(List<Episode> episodes) {

  }
}

    /*if (document.getElementsByClass("detail-content").isEmpty()) {// MOVIE / OAV !!! {
      String elementInfo = document.getElementById("episodeInfo").wholeText();
      String releaseDate = elementInfo.substring(elementInfo.indexOf("Data di Uscita: ") + "Data di Uscita: ".length(),
              elementInfo.indexOf("Data di Uscita: ") + elementInfo.substring(elementInfo.indexOf("Data di Uscita: ")).indexOf("\n"));
      String plot = elementInfo.substring(elementInfo.indexOf("Trama Anime: ") + "Trama Anime: ".length(),
              elementInfo.indexOf("Trama Anime: ") + elementInfo.substring(elementInfo.indexOf("Trama Anime: ")).indexOf("\n"));
      String genresText = elementInfo.substring(elementInfo.indexOf("Genere: ") + "Genere: ".length(),
              elementInfo.indexOf("Genere: ") + elementInfo.substring(elementInfo.indexOf("Genere: ")).indexOf("\n"));
      List<Genre> genres = Arrays.stream(genresText.split(", ")).map(Genre::new).collect(Collectors.toList());
      getAnimeInformation().setGenres(genres);
      getAnimeInformation().setReleaseYear(releaseDate);
      getAnimeInformation().setPlot(plot);
      return getAnimeInformation();
    }
    String info = document.getElementsByClass("innerText").get(0).wholeText();
    String subInfo = info.substring(info.indexOf("Genere: ") + "Genere: ".length());
    subInfo = subInfo.substring(0, subInfo.indexOf("\n"));
    getAnimeInformation().setGenres(Arrays.stream(subInfo.split(", ")).map(Genre::new).collect(Collectors.toList()));
    String status = info.substring(info.indexOf("Episodi:") + "Episodi: ".length());
    status = status.substring(0, status.indexOf("\n")).trim();
    getAnimeInformation().setEpisodes(GeneralUtility.tryParse(status.replaceAll("\\+", "")).orElse(-2));
    getAnimeInformation().setAnimeStatus(status.contains("+") ? AnimeStatus.ONGOING : AnimeStatus.COMPLETED);
    getAnimeInformation().setPlot(document.getElementById("actContTrama").text());
    int startYear = info.indexOf("Anno: ") + "Anno: ".length();
    getAnimeInformation().setReleaseYear(info.substring(startYear, startYear + info.substring(info.indexOf("Anno: ")).indexOf("\n") - 1).trim());*/