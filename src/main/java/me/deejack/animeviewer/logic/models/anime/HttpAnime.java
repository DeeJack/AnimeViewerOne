package me.deejack.animeviewer.logic.models.anime;

import org.jsoup.Connection;

public abstract class HttpAnime implements Anime {
  public abstract Connection.Response episodeRequest()
}
