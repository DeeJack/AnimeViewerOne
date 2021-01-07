package me.deejack.animeviewer.logic.models.episode;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class HttpEpisode implements Episode {
  @Expose
  private final String url;

  public HttpEpisode(String url) {
    this.url = url;
  }

  public Optional<Connection.Response> episodePageRequest() {
    return ConnectionUtility.connect(url, true);
  }

  @Override
  public void download(File output, String downloadLink) {
    DownloadAsync downloadAsync = new DownloadAsync(output, downloadLink);
    new Thread(downloadAsync).start();
  }

  @Override
  public List<StreamingLink> getStreamingLinks() {
    var response = episodePageRequest();
    if (response.isEmpty())
      throw new NoConnectionException(getUrl(), new RuntimeException("Error while getting the streaming links for the episode"));
    if (response.get().statusCode() != 200)
      throw new RuntimeException("HTTP error while fetching streaming links. Error: " + response.get().statusCode() + " for link " + response.get().url());
    Document document;
    try {
      document = response.get().parse();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
    return getStreamingLinks(document);
  }

  protected abstract List<StreamingLink> getStreamingLinks(Document document);

  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object otherEpisode) {
    return otherEpisode instanceof Episode && ((Episode) otherEpisode).getUrl().equalsIgnoreCase(getUrl());
  }
}
