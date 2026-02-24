/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class AlertScreen
/*    */   extends Screen {
/*    */   private static final int LABEL_Y = 90;
/*    */   private final Component messageText;
/* 17 */   private MultiLineLabel message = MultiLineLabel.EMPTY;
/*    */   
/*    */   private final Runnable callback;
/*    */   private final Component okButton;
/*    */   private final boolean shouldCloseOnEsc;
/*    */   
/*    */   public AlertScreen(Runnable callback, Component title, Component messageText) {
/* 24 */     this(callback, title, messageText, CommonComponents.GUI_BACK, true);
/*    */   }
/*    */   
/*    */   public AlertScreen(Runnable callback, Component title, Component messageText, Component okButton, boolean shouldCloseOnEsc) {
/* 28 */     super(title);
/* 29 */     this.callback = callback;
/* 30 */     this.messageText = messageText;
/* 31 */     this.okButton = okButton;
/* 32 */     this.shouldCloseOnEsc = shouldCloseOnEsc;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 37 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.messageText });
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 42 */     super.init();
/*    */     
/* 44 */     this.message = MultiLineLabel.create(this.font, this.messageText, this.width - 50);
/*    */     
/* 46 */     Objects.requireNonNull(this.font); int textHeight = this.message.getLineCount() * 9;
/* 47 */     int buttonY = Mth.clamp(90 + textHeight + 12, this.height / 6 + 96, this.height - 24);
/* 48 */     int buttonWidth = 150;
/*    */     
/* 50 */     addRenderableWidget(Button.builder(this.okButton, button -> this.callback.run()).bounds((this.width - 150) / 2, buttonY, 150, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 55 */     super.render(graphics, mouseX, mouseY, a);
/* 56 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 57 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 70, -1);
/* 58 */     Objects.requireNonNull(this.font); this.message.visitLines(TextAlignment.CENTER, this.width / 2, 90, 9, textRenderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 63 */     return this.shouldCloseOnEsc;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/AlertScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */