package me.deejack.animeviewer.gui.bypasser;

import me.deejack.animeviewer.gui.utils.WebBypassUtility;

public interface StreamingSiteBypasser {

  /**
   * Return the direct link to the video passing the link to the page of the streaming link
   *
   * @param unresolvedLink The link of the page of the desired streaming video
   * @return The direct link to the video
   */
  void getDirectLink(String unresolvedLink, WebBypassUtility.Callback<String> callback);

  /**
   * Returns the links of the sites compatible with this bypass method
   * Insert link like this: www.google.com
   *
   * @return the list of the links compatible with this bypass method
   */
  String[] getCompatibleLinks();
}
