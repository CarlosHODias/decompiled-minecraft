package net.minecraft.client.gui.screens.dialog;

import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerLinks;
import net.minecraft.server.dialog.Dialog;

public interface DialogConnectionAccess {
  void disconnect(Component paramComponent);
  
  void runCommand(String paramString, Screen paramScreen);
  
  void openDialog(Holder<Dialog> paramHolder, Screen paramScreen);
  
  void sendCustomAction(Identifier paramIdentifier, Optional<Tag> paramOptional);
  
  ServerLinks serverLinks();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/DialogConnectionAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */