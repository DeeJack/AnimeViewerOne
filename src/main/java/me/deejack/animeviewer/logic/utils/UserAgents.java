package me.deejack.animeviewer.logic.utils;

import java.util.Random;

/**
 * Some user agents that can be used for the connections
 */
public enum UserAgents {
  WIN10_EDGE("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246"),
  MACOS_SAFARI("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/601.3.9 (KHTML, like Gecko) Version/9.0.2 Safari/601.3.9"),
  WIN10_CHROME("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36"),
  MACOS_CHROME("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36"),
  WIN10_FIREFOX("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0"),
  LINUX_FIREFOX("Mozilla/5.0 (X11; Linux i686; rv:64.0) Gecko/20100101 Firefox/64.0");

  private final String value;

  UserAgents(String value) {
    this.value = value;
  }

  /**
   * Get A random user agent
   *
   * @return a random user agent
   */
  public static UserAgents getRandom() {
    return UserAgents.values()[new Random().nextInt(UserAgents.values().length)];
  }

  public String getValue() {
    return value;
  }
}
