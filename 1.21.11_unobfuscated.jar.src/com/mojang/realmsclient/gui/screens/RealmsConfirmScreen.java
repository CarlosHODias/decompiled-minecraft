/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import net.minecraft.client.GameNarrator;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ 
/*    */ public class RealmsConfirmScreen extends RealmsScreen {
/*    */   protected BooleanConsumer callback;
/*    */   private final Component title1;
/*    */   private final Component title2;
/*    */   
/*    */   public RealmsConfirmScreen(BooleanConsumer callback, Component title1, Component title2) {
/* 18 */     super(GameNarrator.NO_TITLE);
/* 19 */     this.callback = callback;
/* 20 */     this.title1 = title1;
/* 21 */     this.title2 = title2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 26 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_YES, button -> this.callback.accept(true)).bounds(this.width / 2 - 105, row(9), 100, 20).build());
/* 27 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_NO, button -> this.callback.accept(false)).bounds(this.width / 2 + 5, row(9), 100, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 32 */     super.render(graphics, xm, ym, a);
/*    */     
/* 34 */     graphics.drawCenteredString(this.font, this.title1, this.width / 2, row(3), -1);
/* 35 */     graphics.drawCenteredString(this.font, this.title2, this.width / 2, row(5), -1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsConfirmScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */