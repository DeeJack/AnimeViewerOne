package me.deejack.animeviewer.logic.models.episode;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.customexception.NoConnectionException;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

public abstract class HttpEpisode implements Episode {
  @Expose
  private final String url;

  public HttpEpisode(String url) {
    this.url = url;
  }

  public Connection.Response episodePageRequest() {
    return ConnectionUtility.connect(url, false);
  }

  @Override
  public void download(File output, String downloadLink) {
    DownloadAsync downloadAsync = new DownloadAsync(output, downloadLink);
    new Thread(downloadAsync).start();
  }

  @Override
  public List<StreamingLink> getStreamingLinks() {
    Connection.Response response = episodePageRequest();
    if (response == null)
      throw new NoConnectionException(getUrl(), new RuntimeException("Error while getting the streaming links for the episode"));
    if (response.statusCode() != 200)
      throw new RuntimeException("HTTP error while fetching streaming links. Error: " + response.statusCode() + " for link " + response.url());
    Document document;
    try {
      document = response.parse();
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
