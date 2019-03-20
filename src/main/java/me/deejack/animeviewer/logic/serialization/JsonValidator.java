package me.deejack.animeviewer.logic.serialization;

import com.google.gson.JsonParser;
import java.io.IOException;
import me.deejack.animeviewer.logic.history.History;

public class JsonValidator {
  public static void main(String[] args) {
    try {
      History.getHistory().loadFromFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isValid(String json) {
    if(json.trim().isEmpty())
      return true;
    try {
      new JsonParser().parse(json).getAsJsonObject();
      return true;
    } catch (Exception exc) {
      try {
        new JsonParser().parse(json).getAsJsonArray();
        return true;
      } catch (Exception exc2) {
        return false;
      }
    }
  }
}
