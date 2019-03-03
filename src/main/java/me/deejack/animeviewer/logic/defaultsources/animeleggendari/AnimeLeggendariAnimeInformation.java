package me.deejack.animeviewer.logic.defaultsources.animeleggendari;

import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.anime.AnimeInformation;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import me.deejack.animeviewer.logic.anime.dto.Status;
import org.jsoup.nodes.Element;

public final class AnimeLeggendariAnimeInformation extends AnimeInformation {
  private AnimeLeggendariAnimeInformation(short releaseYear, String name, int episodes, double rating,
                                          String url, List<Genre> genres, boolean isFilm, String imageUrl,
                                          Status status) {
    super(releaseYear, name, episodes, rating, url, genres, isFilm, imageUrl, status);
  }

  // SiteElement sarà l'elemento <article>
  public static AnimeInformation getPreviewByElement(Element element) {
    Element aTag = element.children().first().getElementsByTag("a").first();
    String link = aTag.attr("href");
    String title = aTag.attr("title");
    String imgUrl = aTag.children().first().attr("src");
    return new AnimeLeggendariAnimeInformation((short) 0, title, 0, 0, link, new ArrayList<>(),
            false, imgUrl, AnimeLeggendariSite.AnimeLeggendariStatus.TUTTO);
  }

 /* @Override
  public Anime getAnime() {
    return anime == null ? (anime = new AnimeLeggendariAnime(this)) : anime;
  }*/
}
