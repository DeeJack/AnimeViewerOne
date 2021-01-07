package me.deejack.animeviewer.gui.utils;

import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.history.History;

import java.io.File;
import java.io.IOException;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;

public class FilesUtility {
  public static final File tempHistory = new File(System.getProperty("user.home") + File.separator + ".animeviewer" + File.separator +
          "tempHistory.json");

  public static void saveHistory() {
    try {
      History.getHistory().saveToFile();
    } catch (IOException e) {
      handleException(e);
    }
  }

  public static void saveTempHistory() {
    try {
      if (!tempHistory.exists())
        tempHistory.createNewFile();
      System.out.println("Saving temp history");
      History.getHistory().saveToFile(tempHistory);
    } catch (IOException e) {
      handleException(e);
    }
  }

  public static void saveFavorite() {
    try {
      Favorite.getInstance().saveToFile();
    } catch (IOException e) {
      handleException(e);
    }
  }

  public static void loadHistory() {
    try {
      History.getHistory().loadFromFile();
    } catch (IOException e) {
      handleException(e);
    }
  }

  public static void loadFavorite() {
    try {
      Favorite.getInstance().loadFromFile();
    } catch (IOException e) {
      handleException(e);
    }
  }
}
