/*    */ package net.minecraft.client.gui.screens.options;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Options;
/*    */ import net.minecraft.client.gui.components.AbstractWidget;
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.player.PlayerModelPart;
/*    */ 
/*    */ public class SkinCustomizationScreen
/*    */   extends OptionsSubScreen {
/* 14 */   private static final Component TITLE = (Component)Component.translatable("options.skinCustomisation.title");
/*    */   
/*    */   public SkinCustomizationScreen(Screen lastScreen, Options options) {
/* 17 */     super(lastScreen, options, TITLE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addOptions() {
/* 22 */     List<AbstractWidget> widgets = new ArrayList<>();
/* 23 */     for (PlayerModelPart part : PlayerModelPart.values()) {
/* 24 */       widgets.add(CycleButton.onOffBuilder(this.options.isModelPartEnabled(part)).create(part.getName(), (button, value) -> this.options.setModelPart(part, value)));
/*    */     }
/*    */ 
/*    */     
/* 28 */     widgets.add(this.options.mainHand().createButton(this.options));
/* 29 */     this.list.addSmall(widgets);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/SkinCustomizationScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */