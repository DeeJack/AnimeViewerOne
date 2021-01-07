package me.deejack.animeviewer.logic.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GeneralListTypeAdapter<T> implements JsonSerializer, JsonDeserializer {
  private static final String CLASS_NAME_FIELD = "CLASSNAME";
  private static final String DATA = "DATA";

  @Override
  public List<T> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    List<T> elements = new ArrayList<>();
    if (jsonElement.isJsonArray()) {
      JsonArray jsonArray = jsonElement.getAsJsonArray();
      jsonArray.forEach(element -> elements.add(addAnimeByJson(context, element)));
    } else {
      elements.add(addAnimeByJson(context, jsonElement));
    }
    return elements;
  }

  /**
   * Add the deserialized object to a list given a jsonElement and a jsonDeserializationContext
   *
   * @param context     The context of the deserialization
   * @param jsonElement The json element
   */
  private T addAnimeByJson(JsonDeserializationContext context, JsonElement jsonElement) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive classNameField = (JsonPrimitive) jsonObject.get(CLASS_NAME_FIELD);
    String className = classNameField.getAsString();
    Class classFound = getObjectClass(className);
    return context.deserialize(jsonObject.get(DATA), classFound);
  }

  @Override
  public JsonElement serialize(Object object, Type jsonType, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(CLASS_NAME_FIELD, object.getClass().getName());
    jsonObject.add(DATA, context.serialize(object));
    return jsonObject;
  }

  private Class getObjectClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException("Class Not Found! " + className);
    }
  }
}
