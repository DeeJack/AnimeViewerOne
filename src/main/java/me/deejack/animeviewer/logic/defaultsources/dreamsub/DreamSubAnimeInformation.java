package me.deejack.animeviewer.logic.defaultsources.dreamsub;

import java.util.List;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Status;
import org.jsoup.nodes.Element;

public final class DreamSubAnimeInformation extends AnimeInformation {
  private DreamSubAnimeInformation(short releaseYear, String name, int episodes, double rating,
                                   String url, List<Genre> genres, boolean isFilm, String imageUrl,
                                   Status status) {
    super(releaseYear, name, episodes, rating, url, genres, isFilm, imageUrl, status);
  }

  public static DreamSubAnimeInformation getPreviewByElement(Element element) {
    String name = element.getElementsByClass("title").first().text(); // Name of the anime
    Element description = element.getElementsByClass("desc").first(); // The description element which contains some info
    List<Genre> genres = description.getElementsByTag("a").eachText().stream()
            .map(Genre::new).collect(Collectors.toList());
    int indEp = description.text().indexOf("Episodi: ") + "Episodi: ".length(); // Start index for the episodes' number
    int endEp = description.text().substring(indEp).trim().indexOf(" ");
    String numEpisodes = description.text().substring(indEp, indEp + endEp).trim();
    Status status = numEpisodes.contains("+") ? DreamSubSite.DreamSubAnimeStatus.CORSO :
            DreamSubSite.DreamSubAnimeStatus.COMPLETATE;
    int indRate = description.text().indexOf("Rating: ") + "Rating: ".length();
    int endRate = description.text().substring(indRate).indexOf("/");
    double rating = Double.parseDouble(description.text().substring(indRate, indRate + endRate)
            .replace(",", ""));
    int indYear = description.text().indexOf("Anno di inizio: ") + "Anno di inizio: ".length();
    int endYear = description.text().substring(indYear).indexOf(" ");
    short year = Short.parseShort(description.text().substring(indYear, indYear + endYear));

    String baseUrl = DreamSubSite.BASE_URL;
    String imageUrl = baseUrl.substring(0, baseUrl.length() - 1) +
            element.getElementsByClass("cover").get(0).attr("style")
                    .replaceAll("background: url\\(", "").replaceAll("\\) no-repeat center", "");
    String animeUrl = baseUrl.substring(0, baseUrl.length() - 1) + element.getElementsByTag("a").first().attr("href");
    return new DreamSubAnimeInformation(year, name, Integer.parseInt(numEpisodes.replaceAll("\\+", "")),
            rating, animeUrl, genres, animeUrl.contains("/movie/"), imageUrl, status);
  }

  /*@Override
  public Anime getAnime() {
    return anime == null ? (anime = new DreamSubAnime(this)) : anime;
  }*/
}
