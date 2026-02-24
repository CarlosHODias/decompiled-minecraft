/*    */ package net.minecraft.client.gui.screens.multiplayer;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class SafetyScreen extends WarningScreen {
/* 13 */   private static final Component TITLE = (Component)Component.translatable("multiplayerWarning.header").withStyle(ChatFormatting.BOLD);
/* 14 */   private static final Component CONTENT = (Component)Component.translatable("multiplayerWarning.message");
/* 15 */   private static final Component CHECK = (Component)Component.translatable("multiplayerWarning.check").withColor(-2039584);
/* 16 */   private static final Component NARRATION = (Component)TITLE.copy().append("\n").append(CONTENT);
/*    */   
/*    */   private final Screen previous;
/*    */   
/*    */   public SafetyScreen(Screen previous) {
/* 21 */     super(TITLE, CONTENT, CHECK, NARRATION);
/* 22 */     this.previous = previous;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Layout addFooterButtons() {
/* 27 */     LinearLayout footer = LinearLayout.horizontal().spacing(8);
/* 28 */     footer.addChild(
/* 29 */         (LayoutElement)Button.builder(CommonComponents.GUI_PROCEED, button -> {
/*    */             if (this.stopShowing.selected()) {
/*    */               this.minecraft.options.skipMultiplayerWarning = true;
/*    */               this.minecraft.options.save();
/*    */             } 
/*    */             this.minecraft.setScreen(new JoinMultiplayerScreen(this.previous));
/* 35 */           }).build());
/*    */     
/* 37 */     footer.addChild(
/* 38 */         (LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/*    */     
/* 40 */     return (Layout)footer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 45 */     this.minecraft.setScreen(this.previous);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/SafetyScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */