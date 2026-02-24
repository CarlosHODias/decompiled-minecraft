package net.minecraft.client.gui.screens.dialog.input;

import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.action.Action;

@FunctionalInterface
public interface InputControlHandler<T extends net.minecraft.server.dialog.input.InputControl> {
  void addControl(T paramT, Screen paramScreen, Output paramOutput);
  
  @FunctionalInterface
  public static interface Output {
    void accept(LayoutElement param1LayoutElement, Action.ValueGetter param1ValueGetter);
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/input/InputControlHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */