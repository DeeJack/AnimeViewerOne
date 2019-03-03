package me.deejack.animeviewer.logic.utils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import me.deejack.animeviewer.logic.anime.dto.Genre;

public final class GeneralUtility {

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
}