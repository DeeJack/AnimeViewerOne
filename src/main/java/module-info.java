open module AnimeViewer {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires org.jsoup;
    requires java.desktop;
    requires com.google.gson;
    requires fontawesomefx;
    requires java.instrument;
    requires TrayNotification;

    exports me.deejack.animeviewer.gui.components.general;
}
