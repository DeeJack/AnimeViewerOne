package me.deejack.animeviewer.logic.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import me.deejack.animeviewer.logic.models.anime.Anime;
import me.deejack.animeviewer.logic.models.episode.Episode;

public final class AnimeSerializer<T> {
  private static final Gson gson;

  static {
    gson = new GsonBuilder()
            .registerTypeAdapter(Anime.class, new GeneralTypeAdapter<Anime>())
            .registerTypeAdapter(Episode.class, new GeneralTypeAdapter<Episode>())
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                    LocalDateTime.parse(json.getAsJsonPrimitive().getAsString()))
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
  }

  private final Class<T> tClass;

  public AnimeSerializer(Class<T> tClass) {
    this.tClass = tClass;
  }

  public String serialize(Iterable<? extends T> elements) {
    StringBuilder json = new StringBuilder("[\n");
    for (T element : elements) {
      if (element == null)
        continue;
      String serialized = serialize(element);
      if (serialized != null)
        json.append(serialized).append(",");
    }
    json.deleteCharAt(json.length() - 1).append("\n]");
    return json.toString();
  }

  public String serialize(T element) {
    return gson.toJson(element, tClass);
  }

  public List<T> deserializeList(String json) {
    if (json.isEmpty())
      return new ArrayList<>();
    return gson.fromJson(json, TypeToken.getParameterized(List.class, tClass).getType());
  }

  public T deserializeObj(String json) {
    if (json.isEmpty())
      return null;
    return gson.fromJson(json, tClass);
  }
}
