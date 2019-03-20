package me.deejack.animeviewer.logic.extentions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.deejack.animeviewer.logic.models.source.FilteredSource;

public final class ExtensionLoader {

  private ExtensionLoader() {
  }

  public static void main(String[] args) {
    loadExtension();
  }

  public static List<FilteredSource> loadExtension() {
    List<FilteredSource> sources = new ArrayList<>();
    File extensionsPath = new File(System.getProperty("user.home") + File.separator + ".animeviewer" + File.separator + "extensions");
    if (!extensionsPath.exists())
      extensionsPath.mkdir();
    for (File file : Objects.requireNonNull(extensionsPath.listFiles())) {
      if (file.isFile() && file.getName().endsWith(".jar")) {
        FilteredSource source = loadJar(file);
        if (source != null)
          sources.add(source);
      }
    }
    return sources;
  }

  private static FilteredSource loadJar(File file) {
    FilteredSource animeSource = null;
    try (JarFile jarFile = new JarFile(file)) {
      ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + file.getPath() + "!/")});
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
          Class<?> loadedClass = classLoader.loadClass(entry.getName().replaceAll(".class", "").replaceAll("/", "."));
          try {
            animeSource = loadedClass.asSubclass(FilteredSource.class).newInstance();
          } catch (ClassCastException ignored) {
          } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    if (animeSource == null)
      return null;
    return animeSource;
  }
}