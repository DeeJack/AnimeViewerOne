package me.deejack.animeviewer.logic.extensions;

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
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public final class ExtensionLoader {
  public static List<Class> loadedClasses = new ArrayList<>();

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
          loadedClasses.add(loadedClass);
          animeSource = tryLoadAnimeSource(loadedClass);
          if (animeSource != null)
            return animeSource;
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      GeneralUtility.logError(e);
    }
    return null;
  }

  private static FilteredSource tryLoadAnimeSource(Class<?> classz) {
    try {
      return classz.asSubclass(FilteredSource.class).newInstance();
    } catch (IllegalAccessException | InstantiationException | ClassCastException ignored) {
    }
    return null;
  }
}