package me.deejack.animeviewer.gui.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.sound.midi.Soundbank;

import static me.deejack.animeviewer.gui.App.getSite;

public final class LocalizedApp {
  private static final LocalizedApp localizedApp = new LocalizedApp();
  private ResourceBundle resourceBundle = ResourceBundle.getBundle("languages/messages", Locale.getDefault());

  private LocalizedApp() {
  }

  public void loadResourceBundle(Locale newLocale) {
    resourceBundle = ResourceBundle.getBundle("messages", newLocale);
  }

  public String getString(String key) {
    try {
      return replacePlaceholders(resourceBundle.getString(key));
    } catch (MissingResourceException missingExc) {
      String message = String.format("Key %s wasn't found for the locale %s-%s",
              key,
              resourceBundle.getLocale().getLanguage(),
              resourceBundle.getLocale().getCountry());
      SceneUtility.handleException(new RuntimeException(message));
      return "Error";
    }
  }

  private String replacePlaceholders(String message) {
    return message.contains("{SiteName}") ?
            message.replaceAll("\\{SiteName}", getSite().getName()) :
            message;
  }

  public static LocalizedApp getInstance() {
    return localizedApp;
  }
}