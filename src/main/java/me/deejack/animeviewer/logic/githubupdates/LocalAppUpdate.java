package me.deejack.animeviewer.logic.githubupdates;

import javafx.application.Platform;
import me.deejack.animeviewer.gui.utils.SceneUtility;

import java.io.File;
import java.io.IOException;

import static me.deejack.animeviewer.logic.utils.GeneralUtility.logError;

public final class LocalAppUpdate {
  private LocalAppUpdate() {
  }

  private static void deleteOldFile(String oldPath) {
    File oldFile = new File(oldPath);
    if (!oldFile.exists())
      throw new IllegalArgumentException("WTF? Old file not exists: " + oldPath);
    oldFile.delete();
  }

  private static void renameNewFile(String oldPath, String newPath) {
    File newFile = new File(newPath);
    if (!newFile.exists())
      throw new IllegalArgumentException("WTF? New file not exists: " + newPath);
    newFile.renameTo(new File(oldPath));
  }

  public static void update(String oldPath, String newPath) {
    deleteOldFile(oldPath);
    renameNewFile(oldPath, newPath);
  }

  public static void executeNewVersion(String newPath, String oldPath) {
/*
    newPath = "D://Cartelle//Desktop//AnimeViewer.jar";
    JarClassLoader jcl = new JarClassLoader();
    jcl.add(newPath);
    ProxyProviderFactory.setDefaultProxyProvider( new CglibProxyProvider() );

    JclObjectFactory factory = JclObjectFactory.getInstance(true);
    JavaFxChecker mainClass = (JavaFxChecker) factory.create(jcl, "me.deejack.animeviewer.gui.JavaFxChecker");
    try {
      mainClass.getClass().getMethod("main", String[].class).invoke(null, new Object[] {new String[] {"-u", newPath, oldPath}});
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }*/
    System.err.println(newPath + "\n" + oldPath);
    String updatePath = "D://Cartelle//Desktop//appupdater.jar";
    newPath = "D://Cartelle//Desktop//AnimeViewerOld.jar";
      /*ClassLoader classLoader = new URLClassLoader(new URL[]{new URL("jar:file:" + updatePath + "!/")});
      Class mainClass = classLoader.loadClass("me.deejack.appupdater.AppUpdater");
      System.out.println("Class name: " + mainClass.getName());
      Method mainMethod = mainClass.getMethod("main", String[].class);

      System.out.println("Main: " + mainMethod.getName() + "Parameters: " + Arrays.asList(mainMethod.getParameters()));
      String finalNewPath = newPath;
      new Thread(() -> {
        try {
          mainMethod.invoke(null, new Object[] { new String[] {oldPath, finalNewPath}});
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }).start();*/

    String finalNewPath = newPath;
    Platform.runLater(() -> {
      SceneUtility.getStage().close();
      try {
        Runtime.getRuntime().exec("java -jar " + updatePath + " " + oldPath + " " + finalNewPath);
      } catch (IOException e) {
        e.printStackTrace();
        logError(new RuntimeException("Exception while executing the updater app", e));
      }
      Runtime.getRuntime().exit(0);
    });
   /*catch (ClassNotFoundException e) {
      logError(new RuntimeException("Class me.deejack.animeviewer.gui.App not found in the new version, wtf???", e));
    } catch (NoSuchMethodException e) {
      logError(new RuntimeException("Method main not found in the App class, WTF???", e));
    }*/

  }
}
