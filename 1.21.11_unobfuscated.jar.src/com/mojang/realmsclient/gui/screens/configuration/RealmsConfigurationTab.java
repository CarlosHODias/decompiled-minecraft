package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsServer;

public interface RealmsConfigurationTab {
  void updateData(RealmsServer paramRealmsServer);
  
  default void onSelected(RealmsServer serverData) {}
  
  default void onDeselected(RealmsServer serverData) {}
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsConfigurationTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */