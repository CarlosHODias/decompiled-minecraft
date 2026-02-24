/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import net.minecraft.client.GameNarrator;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ProgressListener;
/*    */ 
/*    */ public class ProgressScreen
/*    */   extends Screen
/*    */   implements ProgressListener
/*    */ {
/*    */   private Component header;
/*    */   private Component stage;
/*    */   private int progress;
/*    */   private boolean stop;
/*    */   private final boolean clearScreenAfterStop;
/*    */   
/*    */   public ProgressScreen(boolean clearScreenAfterStop) {
/* 19 */     super(GameNarrator.NO_TITLE);
/* 20 */     this.clearScreenAfterStop = clearScreenAfterStop;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldNarrateNavigation() {
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void progressStartNoAbort(Component string) {
/* 35 */     progressStart(string);
/*    */   }
/*    */ 
/*    */   
/*    */   public void progressStart(Component string) {
/* 40 */     this.header = string;
/* 41 */     progressStage((Component)Component.translatable("menu.working"));
/*    */   }
/*    */ 
/*    */   
/*    */   public void progressStage(Component string) {
/* 46 */     this.stage = string;
/* 47 */     progressStagePercentage(0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void progressStagePercentage(int i) {
/* 52 */     this.progress = i;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 57 */     this.stop = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 62 */     if (this.stop) {
/* 63 */       if (this.clearScreenAfterStop) {
/* 64 */         this.minecraft.setScreen(null);
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 70 */     super.render(graphics, mouseX, mouseY, a);
/*    */     
/* 72 */     if (this.header != null) {
/* 73 */       graphics.drawCenteredString(this.font, this.header, this.width / 2, 70, -1);
/*    */     }
/* 75 */     if (this.stage != null && this.progress != 0)
/* 76 */       graphics.drawCenteredString(this.font, (Component)Component.empty().append(this.stage).append(" " + this.progress + "%"), this.width / 2, 90, -1); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ProgressScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */