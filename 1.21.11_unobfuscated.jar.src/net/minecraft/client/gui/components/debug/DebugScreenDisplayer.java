package net.minecraft.client.gui.components.debug;

import java.util.Collection;
import net.minecraft.resources.Identifier;

public interface DebugScreenDisplayer {
  void addPriorityLine(String paramString);
  
  void addLine(String paramString);
  
  void addToGroup(Identifier paramIdentifier, Collection<String> paramCollection);
  
  void addToGroup(Identifier paramIdentifier, String paramString);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugScreenDisplayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */