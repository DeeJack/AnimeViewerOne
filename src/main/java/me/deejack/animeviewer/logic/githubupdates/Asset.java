package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.annotations.SerializedName;

public class Asset {
  private final String url;
  @SerializedName("browser_download_url")
  private final String downloadLink;

  public Asset(String url, String downloadLink) {
    this.url = url;
    this.downloadLink = downloadLink;
  }

  public String getUrl() {
    return url;
  }

  public String getDownloadLink() {
    return downloadLink;
  }
}
