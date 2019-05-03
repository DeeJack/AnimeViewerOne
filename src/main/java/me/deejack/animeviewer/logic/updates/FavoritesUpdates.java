package me.deejack.animeviewer.logic.updates;

import com.google.gson.annotations.Expose;
import me.deejack.animeviewer.logic.favorite.Favorite;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;
import me.deejack.animeviewer.logic.serialization.JsonValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.logic.history.History.CONFIG_DIR;

/**
 * Checks the updates for every anime in the favorite list
 * The updates can be saved to file and then loaded from the file
 */
public final class FavoritesUpdates {
  private static final FavoritesUpdates instance = new FavoritesUpdates();
  @Expose
  private Map<LocalDate, List<AnimeUpdates>> updatesByDay = new HashMap<>();

  private FavoritesUpdates() {
  }

  public static FavoritesUpdates getInstance() {
    return instance;
  }

  public List<AnimeUpdates> checkUpdates() {
    List<AnimeUpdates> animeUpdates = new ArrayList<>();
    LocalDate today = LocalDate.now();
    Favorite.getInstance().getFavorites().forEach((favorite) -> {
      AnimeUpdates animeUpdate = new AnimeUpdates(favorite.getId());
      List<Episode> newEpisodes = animeUpdate.checkUpdates();
      if (!newEpisodes.isEmpty())
        animeUpdates.add(animeUpdate);
    });
    if (updatesByDay.containsKey(today)) {
      updatesByDay.get(today).forEach((anime) -> {
        if (animeUpdates.contains(anime)) {
          anime.getEpisodes().addAll(animeUpdates.get(animeUpdates.indexOf(anime)).getEpisodes());
          animeUpdates.remove(anime);
        }
      });
    }
    if (!animeUpdates.isEmpty()) {
      if (updatesByDay.containsKey(today))
        animeUpdates.addAll(updatesByDay.get(today));
      updatesByDay.put(today, animeUpdates);
    }
    return updatesByDay.getOrDefault(today, animeUpdates);
  }

  public void writeToFile() {
    String json = new AnimeSerializer<>(FavoritesUpdates.class).serialize(this);
    File output = new File(CONFIG_DIR + File.separator + "updatesByDay.json");
    try {
      if (!output.exists())
        output.createNewFile();
      Files.write(Paths.get(output.toURI()), json.getBytes());
    } catch (IOException exception) {
      handleException(exception);
    }
  }

  public void readFromFile() {
    File fileUpdates = new File(CONFIG_DIR + File.separator + "updatesByDay.json");
    if (!fileUpdates.exists())
      return;
    try {
      String json = String.join("\n", Files.readAllLines(Paths.get(fileUpdates.toURI())));
      if (json.isEmpty())
        return;
      if (!JsonValidator.isValid(json))
        throw new IOException("Json invalid!");
      updatesByDay = Objects.requireNonNull(new AnimeSerializer<>(FavoritesUpdates.class).deserializeObj(json)).updatesByDay;
    } catch (IOException e) {
      handleException(e);
    }
  }

  public Map<LocalDate, List<AnimeUpdates>> getUpdatesByDay() {
    return updatesByDay;
  }
}
