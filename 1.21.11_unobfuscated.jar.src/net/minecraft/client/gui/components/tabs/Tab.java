package net.minecraft.client.gui.components.tabs;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

public interface Tab {
  Component getTabTitle();
  
  Component getTabExtraNarration();
  
  void visitChildren(Consumer<AbstractWidget> paramConsumer);
  
  void doLayout(ScreenRectangle paramScreenRectangle);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/tabs/Tab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */