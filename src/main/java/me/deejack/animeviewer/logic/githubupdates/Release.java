package me.deejack.animeviewer.logic.githubupdates;

import com.google.gson.annotations.SerializedName;

public class Release {
  private final String url;
  @SerializedName("tag_name")
  private final String version;
  private final Asset[] assets;
  @SerializedName("body")
  private final String changelog;

  public Release(String url, String version, Asset[] assets, String changelog) {
    this.url = url;
    this.version = version;
    this.assets = assets;
    this.changelog = changelog;
  }

  public String getUrl() {
    return url;
  }

  public Asset[] getAssets() {
    return assets;
  }

  public String getVersion() {
    return version;
  }

  public String getChangelog() {
    return changelog;
  }
}
