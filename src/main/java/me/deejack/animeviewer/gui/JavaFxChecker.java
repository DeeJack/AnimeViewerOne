package me.deejack.animeviewer.gui;

import java.awt.Desktop;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.application.Application;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class JavaFxChecker {
  private static final String JAVA_LINK = "https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html";

  public static void main(String[] args) {
    try {
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
    frame.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {
      }

      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }

      @Override
      public void windowClosed(WindowEvent e) {

      }

      @Override
      public void windowIconified(WindowEvent e) {

      }

      @Override
      public void windowDeiconified(WindowEvent e) {

      }

      @Override
      public void windowActivated(WindowEvent e) {

      }

      @Override
      public void windowDeactivated(WindowEvent e) {

      }
    });
    frame.setVisible(true);
  }
}
