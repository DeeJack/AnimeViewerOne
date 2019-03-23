package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.annotations.SerializedName;

public class Asset {
  private String url;
  @SerializedName("browser_download_url")
  private String downloadLink;

  public String getUrl() {
    return url;
  }

  public String getDownloadLink() {
    return downloadLink;
  }
}
