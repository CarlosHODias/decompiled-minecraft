/*    */ package net.minecraft.client.gui.screens;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.TextAlignment;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.MultiLineLabel;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class DatapackLoadFailureScreen extends Screen {
/* 12 */   private MultiLineLabel message = MultiLineLabel.EMPTY;
/*    */   
/*    */   private final Runnable cancelCallback;
/*    */   private final Runnable safeModeCallback;
/*    */   
/*    */   public DatapackLoadFailureScreen(Runnable cancelCallback, Runnable safeModeCallback) {
/* 18 */     super((Component)Component.translatable("datapackFailure.title"));
/* 19 */     this.cancelCallback = cancelCallback;
/* 20 */     this.safeModeCallback = safeModeCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void init() {
/* 25 */     super.init();
/*    */     
/* 27 */     this.message = MultiLineLabel.create(this.font, getTitle(), this.width - 50);
/* 28 */     addRenderableWidget(Button.builder((Component)Component.translatable("datapackFailure.safeMode"), button -> this.safeModeCallback.run()).bounds(this.width / 2 - 155, this.height / 6 + 96, 150, 20).build());
/* 29 */     addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, button -> this.cancelCallback.run()).bounds(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 34 */     super.render(graphics, mouseX, mouseY, a);
/* 35 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 36 */     Objects.requireNonNull(this.font); this.message.visitLines(TextAlignment.CENTER, this.width / 2, 70, 9, textRenderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 41 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/DatapackLoadFailureScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */