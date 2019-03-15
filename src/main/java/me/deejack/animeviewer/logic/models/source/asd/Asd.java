package me.deejack.animeviewer.logic.models.source.asd;

import java.util.List;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;
import me.deejack.animeviewer.logic.history.HistoryElement;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public class Asd {
  public static void main(String[] args) {
    DreamSubSource source = new DreamSubSource();
    List<Anime> animeList = source.searchAnime("", 1);
    animeList.get(0).load();
    Favorite.getInstance().addFavorite(animeList.get(0));
    Favorite.getInstance().saveToFile();
    Episode episode = animeList.get(0).getEpisodes().get(0);
    episode.setSecondsWatched(10);
    History.getHistory().add(new HistoryElement(animeList.get(0), episode));
    History.getHistory().saveToFile();
  }
}
