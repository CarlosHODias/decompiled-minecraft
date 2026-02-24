/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ 
/*    */ public interface ReadOnlyScoreInfo
/*    */ {
/*    */   int value();
/*    */   
/*    */   boolean isLocked();
/*    */   
/*    */   NumberFormat numberFormat();
/*    */   
/*    */   default MutableComponent formatValue(NumberFormat defaultFormat) {
/* 17 */     return ((NumberFormat)Objects.<NumberFormat>requireNonNullElse(numberFormat(), defaultFormat)).format(value());
/*    */   }
/*    */   
/*    */   static MutableComponent safeFormatValue(ReadOnlyScoreInfo scoreInfo, NumberFormat defaultFormat) {
/* 21 */     return (scoreInfo != null) ? scoreInfo.formatValue(defaultFormat) : defaultFormat.format(0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/ReadOnlyScoreInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */