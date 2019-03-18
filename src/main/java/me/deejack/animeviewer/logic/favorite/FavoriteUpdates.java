package me.deejack.animeviewer.logic.favorite;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.deejack.animeviewer.logic.models.episode.Episode;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;

import static me.deejack.animeviewer.gui.utils.SceneUtility.handleException;
import static me.deejack.animeviewer.logic.history.History.CONFIG_DIR;

public class FavoriteUpdates {
  @Expose
  private Map<LocalDateTime, List<AnimeUpdates>> updates = new HashMap<>();

  public static void main(String[] args) throws IOException {
    Favorite.getInstance().loadFromFile();
    FavoriteUpdates asd = new FavoriteUpdates();
    asd.readFromFile();
    asd.checkUpdates();
    asd.writeToFile();
  }

  public List<AnimeUpdates> checkUpdates() {
    List<AnimeUpdates> asd = new ArrayList<>();
    Favorite.getInstance().getFavorites().forEach((favorite) -> {
      AnimeUpdates anime = new AnimeUpdates(favorite.getId());
      List<Episode> newEpisodes = anime.checkUpdates();
      if(!newEpisodes.isEmpty())
        asd.add(anime);
    });
    if(!asd.isEmpty())
      updates.put(LocalDateTime.now(), asd);
    return asd;
  }

  public void writeToFile() {
    String json = new AnimeSerializer<FavoriteUpdates>(FavoriteUpdates.class).serialize(this);
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
    URI fileURI = new File(CONFIG_DIR + File.separator + "updates.json").toURI();
    try {
      String json = String.join("\n", Files.readAllLines(Paths.get(fileURI)));
      updates = Objects.requireNonNull(new AnimeSerializer<FavoriteUpdates>(FavoriteUpdates.class).deserializeObj(json)).updates;
    } catch (IOException e) {
      handleException(e);
    }
  }

  public Map<LocalDateTime, List<AnimeUpdates>> getUpdates() {
    return updates;
  }
}
