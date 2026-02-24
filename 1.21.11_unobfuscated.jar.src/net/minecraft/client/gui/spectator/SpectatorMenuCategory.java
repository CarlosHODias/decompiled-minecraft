package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuCategory {
  List<SpectatorMenuItem> getItems();
  
  Component getPrompt();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/spectator/SpectatorMenuCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */