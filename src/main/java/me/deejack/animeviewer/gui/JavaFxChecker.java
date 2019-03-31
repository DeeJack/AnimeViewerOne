package me.deejack.animeviewer.gui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javafx.application.Application;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import me.deejack.animeviewer.logic.internationalization.LocalizedApp;

public class JavaFxChecker {
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
    textArea.setEditable(false);

    frame.add(textArea);
    frame.setSize(200, 100);
    frame.pack();
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
    //frame.setVisible(true);
  }
}
