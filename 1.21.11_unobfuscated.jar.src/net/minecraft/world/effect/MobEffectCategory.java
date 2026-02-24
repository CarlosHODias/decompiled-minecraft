/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ 
/*    */ public enum MobEffectCategory {
/*  6 */   BENEFICIAL(ChatFormatting.BLUE),
/*  7 */   HARMFUL(ChatFormatting.RED),
/*  8 */   NEUTRAL(ChatFormatting.BLUE);
/*    */   
/*    */   private final ChatFormatting tooltipFormatting;
/*    */   
/*    */   MobEffectCategory(ChatFormatting tooltipFormatting) {
/* 13 */     this.tooltipFormatting = tooltipFormatting;
/*    */   }
/*    */   
/*    */   public ChatFormatting getTooltipFormatting() {
/* 17 */     return this.tooltipFormatting;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/MobEffectCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */