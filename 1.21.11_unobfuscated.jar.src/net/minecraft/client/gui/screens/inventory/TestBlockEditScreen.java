/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundSetTestBlockPacket;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*     */ 
/*     */ public class TestBlockEditScreen
/*     */   extends Screen {
/*  21 */   private static final List<TestBlockMode> MODES = List.of(TestBlockMode.values());
/*  22 */   private static final Component TITLE = (Component)Component.translatable(Blocks.TEST_BLOCK.getDescriptionId());
/*  23 */   private static final Component MESSAGE_LABEL = (Component)Component.translatable("test_block.message");
/*     */   
/*     */   private final BlockPos position;
/*     */   private TestBlockMode mode;
/*     */   private String message;
/*     */   private EditBox messageEdit;
/*     */   
/*     */   public TestBlockEditScreen(TestBlockEntity block) {
/*  31 */     super(TITLE);
/*  32 */     this.position = block.getBlockPos();
/*  33 */     this.mode = block.getMode();
/*  34 */     this.message = block.getMessage();
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  39 */     this.messageEdit = new EditBox(this.font, this.width / 2 - 152, 80, 240, 20, (Component)Component.translatable("test_block.message"));
/*  40 */     this.messageEdit.setMaxLength(128);
/*  41 */     this.messageEdit.setValue(this.message);
/*  42 */     addRenderableWidget((GuiEventListener)this.messageEdit);
/*     */     
/*  44 */     updateMode(this.mode);
/*     */     
/*  46 */     addRenderableWidget((GuiEventListener)CycleButton.builder(TestBlockMode::getDisplayName, this.mode)
/*  47 */         .withValues(MODES)
/*  48 */         .displayOnlyValue()
/*  49 */         .create(this.width / 2 - 4 - 150, 185, 50, 20, TITLE, (button, value) -> updateMode(value)));
/*     */ 
/*     */     
/*  52 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(this.width / 2 - 4 - 150, 210, 150, 20).build());
/*  53 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).bounds(this.width / 2 + 4, 210, 150, 20).build());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  58 */     if (this.messageEdit != null) {
/*  59 */       setInitialFocus((GuiEventListener)this.messageEdit);
/*     */     } else {
/*  61 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  67 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/*  69 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 10, -1);
/*  70 */     if (this.mode != TestBlockMode.START) {
/*  71 */       graphics.drawString(this.font, MESSAGE_LABEL, this.width / 2 - 153, 70, -6250336);
/*     */     }
/*     */     
/*  74 */     graphics.drawString(this.font, this.mode.getDetailedMessage(), this.width / 2 - 153, 174, -6250336);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInGameUi() {
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  88 */     this.message = this.messageEdit.getValue();
/*  89 */     this.minecraft.getConnection().send((Packet)new ServerboundSetTestBlockPacket(this.position, this.mode, this.message));
/*  90 */     onClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  95 */     onCancel();
/*     */   }
/*     */   
/*     */   private void onCancel() {
/*  99 */     this.minecraft.setScreen(null);
/*     */   }
/*     */   
/*     */   private void updateMode(TestBlockMode value) {
/* 103 */     this.mode = value;
/* 104 */     this.messageEdit.visible = (value != TestBlockMode.START);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/TestBlockEditScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */