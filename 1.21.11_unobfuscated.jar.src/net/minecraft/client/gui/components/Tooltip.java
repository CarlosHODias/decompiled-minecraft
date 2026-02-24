/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.narration.NarratedElementType;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.narration.NarrationSupplier;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public class Tooltip
/*    */   implements NarrationSupplier
/*    */ {
/*    */   private static final int MAX_WIDTH = 170;
/*    */   private final Component message;
/*    */   private List<FormattedCharSequence> cachedTooltip;
/*    */   private Language splitWithLanguage;
/*    */   private final Component narration;
/*    */   
/*    */   private Tooltip(Component message, Component narration) {
/* 23 */     this.message = message;
/* 24 */     this.narration = narration;
/*    */   }
/*    */   
/*    */   public static Tooltip create(Component message, Component narration) {
/* 28 */     return new Tooltip(message, narration);
/*    */   }
/*    */   
/*    */   public static Tooltip create(Component message) {
/* 32 */     return new Tooltip(message, message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateNarration(NarrationElementOutput output) {
/* 37 */     if (this.narration != null) {
/* 38 */       output.add(NarratedElementType.HINT, this.narration);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<FormattedCharSequence> toCharSequence(Minecraft minecraft) {
/* 43 */     Language currentLanguage = Language.getInstance();
/* 44 */     if (this.cachedTooltip == null || currentLanguage != this.splitWithLanguage) {
/* 45 */       this.cachedTooltip = splitTooltip(minecraft, this.message);
/* 46 */       this.splitWithLanguage = currentLanguage;
/*    */     } 
/* 48 */     return this.cachedTooltip;
/*    */   }
/*    */   
/*    */   public static List<FormattedCharSequence> splitTooltip(Minecraft minecraft, Component message) {
/* 52 */     return minecraft.font.split((FormattedText)message, 170);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/Tooltip.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */