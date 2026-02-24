/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.input.CharacterEvent;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class InBedChatScreen
/*    */   extends ChatScreen
/*    */ {
/*    */   private Button leaveBedButton;
/*    */   
/*    */   public InBedChatScreen(String initial, boolean isDraft) {
/* 20 */     super(initial, isDraft);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 25 */     super.init();
/*    */     
/* 27 */     this.leaveBedButton = Button.builder((Component)Component.translatable("multiplayer.stopSleeping"), button -> sendWakeUp()).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build();
/* 28 */     addRenderableWidget(this.leaveBedButton);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 33 */     if (!this.minecraft.getChatStatus().isChatAllowed(this.minecraft.isLocalServer())) {
/* 34 */       this.leaveBedButton.render(graphics, mouseX, mouseY, a);
/*    */       return;
/*    */     } 
/* 37 */     super.render(graphics, mouseX, mouseY, a);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 42 */     sendWakeUp();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean charTyped(CharacterEvent event) {
/* 47 */     if (!this.minecraft.getChatStatus().isChatAllowed(this.minecraft.isLocalServer())) {
/* 48 */       return true;
/*    */     }
/*    */     
/* 51 */     return super.charTyped(event);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 57 */     if (event.isEscape()) {
/* 58 */       sendWakeUp();
/*    */     }
/* 60 */     if (!this.minecraft.getChatStatus().isChatAllowed(this.minecraft.isLocalServer())) {
/* 61 */       return true;
/*    */     }
/*    */     
/* 64 */     if (event.isConfirmation()) {
/* 65 */       handleChatInput(this.input.getValue(), true);
/* 66 */       this.input.setValue("");
/* 67 */       this.minecraft.gui.getChat().resetChatScroll();
/* 68 */       return true;
/*    */     } 
/* 70 */     return super.keyPressed(event);
/*    */   }
/*    */   
/*    */   private void sendWakeUp() {
/* 74 */     ClientPacketListener connection = this.minecraft.player.connection;
/* 75 */     connection.send((Packet)new ServerboundPlayerCommandPacket((Entity)this.minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
/*    */   }
/*    */   
/*    */   public void onPlayerWokeUp() {
/* 79 */     String text = this.input.getValue();
/*    */     
/* 81 */     if (this.isDraft || text.isEmpty()) {
/* 82 */       this.exitReason = ChatScreen.ExitReason.INTERRUPTED;
/* 83 */       this.minecraft.setScreen(null);
/*    */     } else {
/* 85 */       this.exitReason = ChatScreen.ExitReason.DONE;
/* 86 */       this.minecraft.setScreen(new ChatScreen(text, false));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/InBedChatScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */