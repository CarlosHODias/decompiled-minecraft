/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PositionalRandomFactory
/*    */ {
/*    */   default RandomSource at(BlockPos pos) {
/* 20 */     return at(pos.getX(), pos.getY(), pos.getZ());
/*    */   }
/*    */   
/*    */   default RandomSource fromHashOf(Identifier name) {
/* 24 */     return fromHashOf(name.toString());
/*    */   }
/*    */   
/*    */   RandomSource fromHashOf(String paramString);
/*    */   
/*    */   RandomSource fromSeed(long paramLong);
/*    */   
/*    */   RandomSource at(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   @VisibleForTesting
/*    */   void parityConfigString(StringBuilder paramStringBuilder);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/PositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */