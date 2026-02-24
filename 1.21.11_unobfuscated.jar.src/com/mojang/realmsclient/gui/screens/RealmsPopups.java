/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.gui.components.PopupScreen;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ public class RealmsPopups
/*    */ {
/*    */   private static final int COLOR_INFO = 8226750;
/* 13 */   private static final Component INFO = (Component)Component.translatable("mco.info").withColor(8226750);
/* 14 */   private static final Component WARNING = (Component)Component.translatable("mco.warning").withColor(-65536);
/*    */   
/*    */   public static PopupScreen customPopupScreen(Screen backgroundScreen, Component popupTitle, Component popupMessage, Consumer<PopupScreen> onContinue) {
/* 17 */     return new PopupScreen.Builder(backgroundScreen, popupTitle)
/* 18 */       .setMessage(popupMessage)
/* 19 */       .addButton(CommonComponents.GUI_CONTINUE, onContinue)
/* 20 */       .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 21 */       .build();
/*    */   }
/*    */   
/*    */   public static PopupScreen infoPopupScreen(Screen backgroundScreen, Component popupMessage, Consumer<PopupScreen> onContinue) {
/* 25 */     return new PopupScreen.Builder(backgroundScreen, INFO)
/* 26 */       .setMessage(popupMessage)
/* 27 */       .addButton(CommonComponents.GUI_CONTINUE, onContinue)
/* 28 */       .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 29 */       .build();
/*    */   }
/*    */   
/*    */   public static PopupScreen warningPopupScreen(Screen backgroundScreen, Component popupMessage, Consumer<PopupScreen> onContinue) {
/* 33 */     return new PopupScreen.Builder(backgroundScreen, WARNING)
/* 34 */       .setMessage(popupMessage)
/* 35 */       .addButton(CommonComponents.GUI_CONTINUE, onContinue)
/* 36 */       .addButton(CommonComponents.GUI_CANCEL, PopupScreen::onClose)
/* 37 */       .build();
/*    */   }
/*    */   
/*    */   public static PopupScreen warningAcknowledgePopupScreen(Screen backgroundScreen, Component popupMessage, Consumer<PopupScreen> onContinue) {
/* 41 */     return new PopupScreen.Builder(backgroundScreen, WARNING)
/* 42 */       .setMessage(popupMessage)
/* 43 */       .addButton(CommonComponents.GUI_OK, onContinue)
/* 44 */       .build();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsPopups.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */