package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuItem {
  void selectItem(SpectatorMenu paramSpectatorMenu);
  
  Component getName();
  
  void renderIcon(GuiGraphics paramGuiGraphics, float paramFloat1, float paramFloat2);
  
  boolean isEnabled();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/SpectatorMenuItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */