/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public interface WorldGenLevel
/*    */   extends ServerLevelAccessor
/*    */ {
/*    */   long getSeed();
/*    */   
/*    */   default boolean ensureCanWrite(BlockPos pos) {
/* 12 */     return true;
/*    */   }
/*    */   
/*    */   default void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/WorldGenLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */