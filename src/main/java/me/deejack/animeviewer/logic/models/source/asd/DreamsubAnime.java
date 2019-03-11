package me.deejack.animeviewer.logic.models.source.asd;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.AnimeStatus;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.models.anime.AnimeImpl;
import me.deejack.animeviewer.logic.models.episode.Episode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DreamsubAnime extends AnimeImpl {

  public DreamsubAnime(String name, String url, String imageUrl) {
    super(new AnimeInformation((short) -1, name, -1, -1, url, new ArrayList<>(), imageUrl, AnimeStatus.UNKNOWN));
  }

  @Override
  public AnimeInformation parseAnimeDetails(Document document) {
    String info = document.getElementsByClass("innerText").get(0).wholeText();
    String subInfo = info.substring(info.indexOf("Genere:"));
    subInfo = subInfo.substring(0, subInfo.indexOf("\n"));
    getAnimeInformation().setGenres(Arrays.stream(subInfo.split(", ")).map(Genre::new).collect(Collectors.toList()));
    String status = info.substring(info.indexOf("Episodi:"));
    status = status.substring(0, status.indexOf("\n"));
    getAnimeInformation().setAnimeStatus(status.contains("+") ? AnimeStatus.ONGOING : AnimeStatus.COMPLETED);
    return getAnimeInformation();
  }

  @Override
  public String episodeSelector() {
    return "ul.seasEpisode > li";
  }

  @Override
  public Episode parseEpisode(Element element) {
    String num = element.text().split(" ")[1].split(" ")[0];
    String releaseDate = element.text().split("-")[1].trim();
    String title = element.getElementsByTag("i").first().text();
    boolean alreadyReleased = !element.getElementsByTag("a").isEmpty() &&
            !element.getElementsByTag("a").get(0).attr("href").isEmpty();
    String url = alreadyReleased ? "https://www.dreamsub.stream/" + element.getElementsByTag("a").get(0).attr("href") : "";
    return new DreamsubEpisode(title, Integer.parseInt(num), url,
            LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("d/M/y")));
  }
}
