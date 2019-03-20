package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;
import me.deejack.animeviewer.logic.serialization.JsonValidator;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.logic.history.History.CONFIG_DIR;

public final class FavoriteUpdates {
  private static final FavoriteUpdates instance = new FavoriteUpdates();
  @Expose
  private Map<LocalDate, List<AnimeUpdates>> updates = new HashMap<>();

  private FavoriteUpdates() {
  }

  public static FavoriteUpdates getInstance() {
    return instance;
  }

  public List<AnimeUpdates> checkUpdates() {
    List<AnimeUpdates> newEpisodes = new ArrayList<>();
    LocalDate today = LocalDate.now();
    Favorite.getInstance().getFavorites().forEach((favorite) -> {
      AnimeUpdates anime = new AnimeUpdates(favorite.getId());
      List<Episode> episodes = anime.checkUpdates();
      if (!episodes.isEmpty())
        newEpisodes.add(anime);
    });
    if(updates.containsKey(today)) {
      updates.get(today).forEach((anime) -> {
        if (newEpisodes.contains(anime)) {
          anime.getEpisodes().addAll(newEpisodes.get(newEpisodes.indexOf(anime)).getEpisodes());
          newEpisodes.remove(anime);
        }
      });
    }
    if (!newEpisodes.isEmpty()) {
      if (updates.containsKey(today))
        newEpisodes.addAll(updates.get(today));
      updates.put(today, newEpisodes);
    }
    return updates.getOrDefault(today, newEpisodes);
  }

  public void writeToFile() {
    String json = new AnimeSerializer<>(FavoriteUpdates.class).serialize(this);
    File output = new File(CONFIG_DIR + File.separator + "updates.json");
    try {
      if (!output.exists())
        output.createNewFile();
      Files.write(Paths.get(output.toURI()), json.getBytes());
    } catch (IOException exception) {
      handleException(exception);
    }
  }

  public void readFromFile() {
    File fileUpdates = new File(CONFIG_DIR + File.separator + "updates.json");
    if (!fileUpdates.exists())
      return;
    try {
      String json = String.join("\n", Files.readAllLines(Paths.get(fileUpdates.toURI())));
      if (json.isEmpty())
        return;
      if (!JsonValidator.isValid(json))
        throw new IOException("Json invalid!");
      updates = Objects.requireNonNull(new AnimeSerializer<>(FavoriteUpdates.class).deserializeObj(json)).updates;
    } catch (IOException e) {
      handleException(e);
    }
  }

  public Map<LocalDate, List<AnimeUpdates>> getUpdates() {
    return updates;
  }
}
