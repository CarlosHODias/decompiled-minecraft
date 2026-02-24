/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.EditBox;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class DirectJoinServerScreen extends Screen {
/* 15 */   private static final Component ENTER_IP_LABEL = (Component)Component.translatable("manageServer.enterIp");
/*    */   
/*    */   private Button selectButton;
/*    */   private final ServerData serverData;
/*    */   private EditBox ipEdit;
/*    */   private final BooleanConsumer callback;
/*    */   private final Screen lastScreen;
/*    */   
/*    */   public DirectJoinServerScreen(Screen lastScreen, BooleanConsumer callback, ServerData serverData) {
/* 24 */     super((Component)Component.translatable("selectServer.direct"));
/* 25 */     this.lastScreen = lastScreen;
/* 26 */     this.serverData = serverData;
/* 27 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 32 */     if (this.selectButton.active && getFocused() == this.ipEdit && event.isConfirmation()) {
/* 33 */       onSelect();
/* 34 */       return true;
/*    */     } 
/* 36 */     return super.keyPressed(event);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 41 */     this.ipEdit = new EditBox(this.font, this.width / 2 - 100, 116, 200, 20, ENTER_IP_LABEL);
/* 42 */     this.ipEdit.setMaxLength(128);
/* 43 */     this.ipEdit.setValue(this.minecraft.options.lastMpIp);
/* 44 */     this.ipEdit.setResponder(value -> updateSelectButtonStatus());
/* 45 */     addWidget(this.ipEdit);
/* 46 */     this.selectButton = addRenderableWidget(Button.builder((Component)Component.translatable("selectServer.select"), button -> onSelect()).bounds(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20).build());
/* 47 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.callback.accept(false)).bounds(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20).build());
/*    */     
/* 49 */     updateSelectButtonStatus();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setInitialFocus() {
/* 54 */     setInitialFocus((GuiEventListener)this.ipEdit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resize(int width, int height) {
/* 59 */     String oldEdit = this.ipEdit.getValue();
/* 60 */     init(width, height);
/* 61 */     this.ipEdit.setValue(oldEdit);
/*    */   }
/*    */   
/*    */   private void onSelect() {
/* 65 */     this.serverData.ip = this.ipEdit.getValue();
/* 66 */     this.callback.accept(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 71 */     this.minecraft.setScreen(this.lastScreen);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed() {
/* 76 */     this.minecraft.options.lastMpIp = this.ipEdit.getValue();
/* 77 */     this.minecraft.options.save();
/*    */   }
/*    */   
/*    */   private void updateSelectButtonStatus() {
/* 81 */     this.selectButton.active = ServerAddress.isValidAddress(this.ipEdit.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 86 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/* 88 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
/* 89 */     graphics.drawString(this.font, ENTER_IP_LABEL, this.width / 2 - 100 + 1, 100, -6250336);
/*    */     
/* 91 */     this.ipEdit.render(graphics, mouseX, mouseY, a);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/DirectJoinServerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */