package me.deejack.animeviewer.logic.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class FilesManager {
  public static final File EXTENSION_FOLDER = new File(GeneralUtility.CONFIG_PATH + File.separator + "extensions");
  private static final File CONFIG_FOLDER = new File(GeneralUtility.CONFIG_PATH);
  private static final File FAVORITE_IMAGE_FOLDER = new File(GeneralUtility.CONFIG_PATH + File.separator + "favoritesImages");

  private FilesManager() {
  }

  public static void createDirsIfNotExists() {
    if (!CONFIG_FOLDER.exists())
      CONFIG_FOLDER.mkdir();
    if (!FAVORITE_IMAGE_FOLDER.exists())
      FAVORITE_IMAGE_FOLDER.mkdir();
    if (!EXTENSION_FOLDER.exists())
      EXTENSION_FOLDER.mkdir();
  }

  public static void writeToFile(File output, String text) {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(output.toURI()), Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      writer.write(text);
    } catch (IOException e) {
      GeneralUtility.logError(e);
    }
  }

  public static String readFromFile(File input) {
    String text = "";
    try {
      if (input.exists())
        text = String.join("\n", Files.readAllLines(Paths.get(input.toURI())));
    } catch (IOException e) {
      GeneralUtility.logError(e);
    }
    return text;
  }
}
