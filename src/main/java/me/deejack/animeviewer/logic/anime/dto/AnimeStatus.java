package me.deejack.animeviewer.logic.anime.dto;

/**
 * Implement this interface on an enum to set a list of statuses
 * For example an anime can be "on going" or "Completed"
 */
public enum AnimeStatus implements Status {
  ONGOING("On going", "ONGOING"),
  COMPLETED("Completed", "Completed"),
  FUTURE("Future", "Future"),
  UNKNOWN("Unknown", "Unknown");

  private final String name;
  private final String value;

  AnimeStatus(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }
}