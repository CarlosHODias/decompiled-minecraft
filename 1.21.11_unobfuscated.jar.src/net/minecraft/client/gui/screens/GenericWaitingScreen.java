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
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericWaitingScreen
/*    */   extends Screen
/*    */ {
/*    */   private static final int TITLE_Y = 80;
/*    */   private static final int MESSAGE_Y = 120;
/*    */   private static final int MESSAGE_MAX_WIDTH = 360;
/*    */   private final Component messageText;
/*    */   private final Component buttonLabel;
/*    */   private final Runnable buttonCallback;
/*    */   private MultiLineLabel message;
/*    */   private Button button;
/*    */   private int disableButtonTicks;
/*    */   
/*    */   public static GenericWaitingScreen createWaiting(Component title, Component buttonLabel, Runnable buttonCallback) {
/* 29 */     return new GenericWaitingScreen(title, null, buttonLabel, buttonCallback, 0);
/*    */   }
/*    */   
/*    */   public static GenericWaitingScreen createCompleted(Component title, Component messageText, Component buttonLabel, Runnable buttonCallback) {
/* 33 */     return new GenericWaitingScreen(title, messageText, buttonLabel, buttonCallback, 20);
/*    */   }
/*    */   
/*    */   protected GenericWaitingScreen(Component title, Component messageText, Component buttonLabel, Runnable buttonCallback, int disableButtonTicks) {
/* 37 */     super(title);
/* 38 */     this.messageText = messageText;
/* 39 */     this.buttonLabel = buttonLabel;
/* 40 */     this.buttonCallback = buttonCallback;
/* 41 */     this.disableButtonTicks = disableButtonTicks;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 46 */     super.init();
/*    */     
/* 48 */     if (this.messageText != null) {
/* 49 */       this.message = MultiLineLabel.create(this.font, this.messageText, 360);
/*    */     }
/*    */     
/* 52 */     int buttonWidth = 150;
/* 53 */     int buttonHeight = 20;
/*    */     
/* 55 */     int lineCount = (this.message != null) ? this.message.getLineCount() : 1;
/* 56 */     Objects.requireNonNull(this.font); int messageButtonSpacing = Math.max(lineCount, 5) * 9;
/* 57 */     int buttonY = Math.min(120 + messageButtonSpacing, this.height - 40);
/*    */     
/* 59 */     this.button = addRenderableWidget(Button.builder(this.buttonLabel, b -> onClose()).bounds((this.width - 150) / 2, buttonY, 150, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 64 */     if (this.disableButtonTicks > 0) {
/* 65 */       this.disableButtonTicks--;
/*    */     }
/* 67 */     this.button.active = (this.disableButtonTicks == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 72 */     super.render(graphics, mouseX, mouseY, a);
/* 73 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 74 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 80, -1);
/*    */     
/* 76 */     if (this.message == null) {
/* 77 */       String loadingDots = LoadingDotsText.get(Util.getMillis());
/* 78 */       graphics.drawCenteredString(this.font, loadingDots, this.width / 2, 120, -6250336);
/*    */     } else {
/* 80 */       Objects.requireNonNull(this.font); this.message.visitLines(TextAlignment.CENTER, this.width / 2, 120, 9, textRenderer);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 86 */     return (this.message != null && this.button.active);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onClose() {
/* 91 */     this.buttonCallback.run();
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 96 */     return (Component)CommonComponents.joinForNarration(new Component[] { this.title, (this.messageText != null) ? this.messageText : CommonComponents.EMPTY });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/GenericWaitingScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */