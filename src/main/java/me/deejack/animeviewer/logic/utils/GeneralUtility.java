package me.deejack.animeviewer.logic.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.dto.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GeneralUtility {
  private static final Logger logger = LogManager.getLogger();
  public static final String TMP_PATH = System.getProperty("java.io.tmpdir") +
          File.separator + "AnimeViewer" + File.separator;
  public static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".animeviewer";
  public static final String version = "0.000001";

  private GeneralUtility() {
  }

  public static String genreListToString(Collection<? extends Genre> genres, CharSequence delimiter) {
    if (genres == null || genres.isEmpty())
      return "";
    return genres.stream().filter(Objects::nonNull).map(Genre::getName).collect(Collectors.joining(delimiter));
  }

  /**
   * Try to parse an integer
   *
   * @param text the text to be parsed
   * @return An optional containing the integer if the parse works
   */
  public static Optional<Integer> tryParse(String text) {
    try {
      return Optional.of(Integer.parseInt(text));
    } catch (NumberFormatException exception) {
      return Optional.empty();
    }
  }

  public static void logError(Throwable throwable) {
    logger.error(throwable);
    logger.error("Cause: " + throwable.getCause());
    logger.error("Stack trace: " + Arrays.stream(throwable.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n\t")));
  }
}