package net.minecraft.client.gui.screens.dialog.body;

import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.dialog.DialogScreen;

public interface DialogBodyHandler<T extends net.minecraft.server.dialog.body.DialogBody> {
  LayoutElement createControls(DialogScreen<?> paramDialogScreen, T paramT);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/body/DialogBodyHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */