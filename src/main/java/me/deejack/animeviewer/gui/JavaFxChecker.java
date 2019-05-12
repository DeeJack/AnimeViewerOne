package me.deejack.animeviewer.gui;

import javafx.application.Application;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class JavaFxChecker {
  private static final String JAVA_LINK = "https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html";

  public static void main(String[] args) {
    try {
      //if (args.length > 0 && args[0].equals("-u"))
      //LocalAppUpdate.update(args[1], args[2]);
      Class.forName("javafx.application.Application");
      Application.launch(App.class, args);
    } catch (ClassNotFoundException e) {
      showJFXNotFound();
      e.printStackTrace();
    }
  }

  private static void showJFXNotFound() {
    JFrame frame = new JFrame("JavaFx not found");
    JTextArea textArea = new JTextArea(LocalizedApp.getInstance().getString("ErrorJFXNotFound"));
    textArea.setLineWrap(true);
    textArea.setSize(600, 20);
    textArea.setEditable(false);
    JButton button = new JButton(LocalizedApp.getInstance().getString("OpenJavaSite"));
    button.setBounds(70, 70, 200, 30);
    button.addActionListener(e -> {
      try {
        Desktop.getDesktop().browse(new URI(JAVA_LINK));
      } catch (IOException | URISyntaxException e1) {
        e1.printStackTrace();
      }
    });

    frame.add(textArea);
    frame.add(button);
    frame.setSize(600, 200);
    frame.setLayout(null);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
