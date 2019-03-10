package me.deejack.animeviewer.logic.models.episode;

import java.io.File;
import java.util.List;
import me.deejack.animeviewer.logic.anime.dto.StreamingLink;
import me.deejack.animeviewer.logic.async.DownloadAsync;
import me.deejack.animeviewer.logic.utils.ConnectionUtility;
import org.jsoup.Connection;

public abstract class HttpEpisode implements Episode {
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
    return getStreamingLinks(episodePageRequest());
  }

  protected abstract List<StreamingLink> getStreamingLinks(Connection.Response response);

  @Override
  public String getUrl() {
    return url;
  }
}
