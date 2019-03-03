package me.deejack.animeviewer.logic.models.source;

public interface LoginSource {
  boolean isLogged();

  void login(String username, String password) throws IllegalArgumentException;
}
