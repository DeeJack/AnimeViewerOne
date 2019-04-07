package me.deejack.animeviewer.logic.extensions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.utils.FilesManager;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

public final class ExtensionLoader {
  public static List<Class> loadedClasses = new ArrayList<>();

  private ExtensionLoader() {
  }

  public static List<FilteredSource> loadExtension() {
    List<FilteredSource> sources = new ArrayList<>();
    for (File file : Objects.requireNonNull(FilesManager.EXTENSION_FOLDER.listFiles())) {
      if (file.isFile() && file.getName().endsWith(".jar")) {
        addExtToClasspath(file);
        FilteredSource source = getFilteredSource(file);
        if (source != null)
          sources.add(source);
      }
    }
    return sources;
  }

  private static FilteredSource getFilteredSource(File file) {
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

  private static void addExtToClasspath(File file) {
    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    Class sysClass = URLClassLoader.class;

    try {
      Method method = sysClass.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(urlClassLoader, new URL("jar:file:" + file.getPath() + "!/"));
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private static FilteredSource tryLoadAnimeSource(Class<?> classz) {
    try {
      return classz.asSubclass(FilteredSource.class).newInstance();
    } catch (IllegalAccessException | InstantiationException | ClassCastException ignored) {
    }
    return null;
  }
}