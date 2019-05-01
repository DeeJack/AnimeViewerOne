package me.deejack.animeviewer.logic.defaultsources.otakustream;

import com.google.gson.annotations.SerializedName;

public class OtakuStreamJsonObj {
  private String text;
  @SerializedName("description")
  private String url;

  public String getText() {
    return text;
  }

  public String getUrl() {
    return url;
  }
}
