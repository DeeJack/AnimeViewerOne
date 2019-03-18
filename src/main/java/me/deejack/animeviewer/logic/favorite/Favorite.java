package me.deejack.animeviewer.logic.favorite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.serialization.AnimeSerializer;

import static me.deejack.animeviewer.logic.history.History.CONFIG_DIR;

public final class Favorite {
  private static final Favorite instance = new Favorite();
  private final Set<FavoriteAnime> favorites = new HashSet<>();
  private final AnimeSerializer serializer = new AnimeSerializer<>(Anime.class);

  private Favorite() {
  }

  public static Favorite getInstance() {
    return instance;
  }

  public void addFavorite(Anime element) {
    favorites.add(new FavoriteAnime(element));
  }

  public void removeFavorite(Anime animeToRemove) {
    for (FavoriteAnime anime : favorites) {
      if (anime.getAnime().equals(animeToRemove)) {
        favorites.remove(anime);
        return;
      }
    }
  }

  public Set<FavoriteAnime> getFavorites() {
    return Collections.unmodifiableSet(favorites);
  }

  public boolean saveToFile() {
    if (!CONFIG_DIR.exists())
      CONFIG_DIR.mkdir();
    return saveToFile(new File(CONFIG_DIR.getPath() + File.separator + "favorites.json"));
  }

  public boolean saveToFile(File output) {
    if (output == null)
      return false;
    String json = serializer.serialize(new ArrayList<>(favorites));
    try {
      if (!output.exists())
        output.createNewFile();
      Files.write(Paths.get(output.toURI()), json.getBytes());
    } catch (IOException exception) {
      return false;
    }
    return true;
  }

  public boolean loadFromFile() throws IOException {
    return loadFromFile(new File(CONFIG_DIR + File.separator + "favorites.json"));
  }

  public boolean loadFromFile(File input) throws IOException {
    if (!input.exists())
      return false;
    String json = String.join("\n", Files.readAllLines(Paths.get(input.toURI())));
    List<FavoriteAnime> elements = serializer.deserializeList(json);

    favorites.addAll(elements);
    return true;
  }

  public boolean contains(Anime anime) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getAnime().getUrl().equals(anime.getUrl()))
        return true;
    return false;
  }

  public FavoriteAnime get(String url) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getAnime().getUrl().equals(url))
        return favorite;
    return null;
  }

  public FavoriteAnime get(int id) {
    for (FavoriteAnime favorite : favorites)
      if (favorite.getId() == id)
        return favorite;
    return null;
  }
}
