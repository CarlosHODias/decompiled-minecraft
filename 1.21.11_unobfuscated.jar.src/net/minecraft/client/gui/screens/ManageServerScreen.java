/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.CycleButton;
/*    */ import net.minecraft.client.gui.components.EditBox;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class ManageServerScreen extends Screen {
/* 15 */   private static final Component NAME_LABEL = (Component)Component.translatable("manageServer.enterName");
/* 16 */   private static final Component IP_LABEL = (Component)Component.translatable("manageServer.enterIp");
/* 17 */   private static final Component DEFAULT_SERVER_NAME = (Component)Component.translatable("selectServer.defaultName");
/*    */   
/*    */   private Button addButton;
/*    */   private final BooleanConsumer callback;
/*    */   private final ServerData serverData;
/*    */   private EditBox ipEdit;
/*    */   private EditBox nameEdit;
/*    */   private final Screen lastScreen;
/*    */   
/*    */   public ManageServerScreen(Screen lastScreen, Component title, BooleanConsumer callback, ServerData serverData) {
/* 27 */     super(title);
/* 28 */     this.lastScreen = lastScreen;
/* 29 */     this.callback = callback;
/* 30 */     this.serverData = serverData;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 35 */     this.nameEdit = new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, NAME_LABEL);
/* 36 */     this.nameEdit.setValue(this.serverData.name);
/* 37 */     this.nameEdit.setHint(DEFAULT_SERVER_NAME);
/* 38 */     this.nameEdit.setResponder(v -> updateAddButtonStatus());
/* 39 */     addWidget(this.nameEdit);
/*    */     
/* 41 */     this.ipEdit = new EditBox(this.font, this.width / 2 - 100, 106, 200, 20, IP_LABEL);
/* 42 */     this.ipEdit.setMaxLength(128);
/* 43 */     this.ipEdit.setValue(this.serverData.ip);
/* 44 */     this.ipEdit.setResponder(v -> updateAddButtonStatus());
/* 45 */     addWidget(this.ipEdit);
/*    */     
/* 47 */     addRenderableWidget(CycleButton.builder(ServerData.ServerPackStatus::getName, this.serverData.getResourcePackStatus())
/* 48 */         .withValues((Object[])ServerData.ServerPackStatus.values())
/* 49 */         .create(this.width / 2 - 100, this.height / 4 + 72, 200, 20, (Component)Component.translatable("manageServer.resourcePack"), (button, value) -> this.serverData.setResourcePackStatus(value)));
/*    */     
/* 51 */     this.addButton = addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onAdd()).bounds(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20).build());
/* 52 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.callback.accept(false)).bounds(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20).build());
/*    */     
/* 54 */     updateAddButtonStatus();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setInitialFocus() {
/* 59 */     setInitialFocus((GuiEventListener)this.nameEdit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resize(int width, int height) {
/* 64 */     String oldIpEdit = this.ipEdit.getValue();
/* 65 */     String oldNameEdit = this.nameEdit.getValue();
/* 66 */     init(width, height);
/* 67 */     this.ipEdit.setValue(oldIpEdit);
/* 68 */     this.nameEdit.setValue(oldNameEdit);
/*    */   }
/*    */   
/*    */   private void onAdd() {
/* 72 */     String name = this.nameEdit.getValue();
/* 73 */     this.serverData.name = name.isEmpty() ? DEFAULT_SERVER_NAME.getString() : name;
/* 74 */     this.serverData.ip = this.ipEdit.getValue();
/* 75 */     this.callback.accept(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 80 */     this.minecraft.setScreen(this.lastScreen);
/*    */   }
/*    */   
/*    */   private void updateAddButtonStatus() {
/* 84 */     this.addButton.active = ServerAddress.isValidAddress(this.ipEdit.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 89 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/* 91 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 17, -1);
/* 92 */     graphics.drawString(this.font, NAME_LABEL, this.width / 2 - 100 + 1, 53, -6250336);
/* 93 */     graphics.drawString(this.font, IP_LABEL, this.width / 2 - 100 + 1, 94, -6250336);
/*    */     
/* 95 */     this.nameEdit.render(graphics, mouseX, mouseY, a);
/* 96 */     this.ipEdit.render(graphics, mouseX, mouseY, a);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ManageServerScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */