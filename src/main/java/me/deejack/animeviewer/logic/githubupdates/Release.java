package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.annotations.SerializedName;

public class Release {
  private String url;
  @SerializedName("tag_name")
  private String version;
  private Asset[] assets;

  public String getUrl() {
    return url;
  }

  public Asset[] getAssets() {
    return assets;
  }

  public String getVersion() {
    return version;
  }
}
