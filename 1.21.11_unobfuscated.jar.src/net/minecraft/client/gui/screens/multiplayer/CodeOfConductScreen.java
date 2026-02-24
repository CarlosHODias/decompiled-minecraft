/*    */ package net.minecraft.client.gui.screens.multiplayer;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.layouts.Layout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.multiplayer.ServerList;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class CodeOfConductScreen
/*    */   extends WarningScreen {
/* 17 */   private static final Component TITLE = (Component)Component.translatable("multiplayer.codeOfConduct.title").withStyle(ChatFormatting.BOLD);
/* 18 */   private static final Component CHECK = (Component)Component.translatable("multiplayer.codeOfConduct.check");
/*    */   
/*    */   private final ServerData serverData;
/*    */   private final String codeOfConductText;
/*    */   private final BooleanConsumer resultConsumer;
/*    */   private final Screen parent;
/*    */   
/*    */   private CodeOfConductScreen(ServerData serverData, Screen parent, Component contents, String codeOfConductText, BooleanConsumer resultConsumer) {
/* 26 */     super(TITLE, contents, CHECK, (Component)TITLE.copy().append("\n").append(contents));
/* 27 */     this.serverData = serverData;
/* 28 */     this.parent = parent;
/* 29 */     this.codeOfConductText = codeOfConductText;
/* 30 */     this.resultConsumer = resultConsumer;
/*    */   }
/*    */   
/*    */   public CodeOfConductScreen(ServerData serverData, Screen parent, String codeOfConductText, BooleanConsumer resultConsumer) {
/* 34 */     this(serverData, parent, (Component)Component.literal(codeOfConductText), codeOfConductText, resultConsumer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Layout addFooterButtons() {
/* 39 */     LinearLayout footer = LinearLayout.horizontal().spacing(8);
/* 40 */     footer.addChild(
/* 41 */         (LayoutElement)Button.builder(CommonComponents.GUI_ACKNOWLEDGE, button -> onResult(true)).build());
/*    */     
/* 43 */     footer.addChild(
/* 44 */         (LayoutElement)Button.builder(CommonComponents.GUI_DISCONNECT, button -> onResult(false)).build());
/*    */     
/* 46 */     return (Layout)footer;
/*    */   }
/*    */   
/*    */   private void onResult(boolean accepted) {
/* 50 */     this.resultConsumer.accept(accepted);
/*    */     
/* 52 */     if (this.serverData != null) {
/* 53 */       if (accepted && this.stopShowing.selected()) {
/* 54 */         this.serverData.acceptCodeOfConduct(this.codeOfConductText);
/*    */       } else {
/* 56 */         this.serverData.clearCodeOfConduct();
/*    */       } 
/* 58 */       ServerList.saveSingleServer(this.serverData);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 69 */     super.tick();
/*    */     
/* 71 */     if (this.parent instanceof net.minecraft.client.gui.screens.ConnectScreen || this.parent instanceof ServerReconfigScreen)
/*    */     {
/* 73 */       this.parent.tick();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/multiplayer/CodeOfConductScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */