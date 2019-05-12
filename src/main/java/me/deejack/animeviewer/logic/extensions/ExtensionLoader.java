package me.deejack.animeviewer.logic.extensions;

import me.deejack.animeviewer.gui.bypasser.SiteBypasser;
import me.deejack.animeviewer.logic.models.source.FilteredSource;
import me.deejack.animeviewer.logic.utils.FilesManager;
import me.deejack.animeviewer.logic.utils.GeneralUtility;

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

public final class ExtensionLoader<T> {
  //public static List<Class> loadedClasses = new ArrayList<>();

  private ExtensionLoader() {
  }

  public static List<FilteredSource> loadExtension() {
    return loadExtension(FilesManager.EXTENSION_FOLDER, FilteredSource.class);
  }

  public static List<SiteBypasser> loadBypassers() {
    return loadExtension(FilesManager.BYPASSER_FOLDER, SiteBypasser.class);
  }

  private static <T> List<T> loadExtension(File folder, Class<T> superClass) {
    List<T> subclasses = new ArrayList<>();
    for (File file : Objects.requireNonNull(folder.listFiles())) {
      if (file.isFile() && file.getName().endsWith(".jar")) {
        addExtToClasspath(file);
        T subclass = getSubclass(file, superClass);
        if (subclass != null)
          subclasses.add(subclass);
      }
    }
    return subclasses;
  }

  private static <T> T getSubclass(File file, Class<T> superClass) {
    T animeSource = null;
    try (JarFile jarFile = new JarFile(file)) {
      ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + file.getPath() + "!/")});
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
          Class<?> loadedClass = classLoader.loadClass(entry.getName().replaceAll(".class", "").replaceAll("/", "."));
          //loadedClasses.add(loadedClass);
          animeSource = tryLoadAnimeSource(loadedClass, superClass);
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

  private static <T> T tryLoadAnimeSource(Class<?> subClass, Class<T> superClass) {
    try {
      return subClass.asSubclass(superClass).newInstance();
    } catch (IllegalAccessException | InstantiationException | ClassCastException ignored) {
    }
    return null;
  }
}