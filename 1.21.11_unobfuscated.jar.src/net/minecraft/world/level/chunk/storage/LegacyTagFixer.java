/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface LegacyTagFixer
/*    */ {
/*    */   public static final Supplier<LegacyTagFixer> EMPTY = () -> ();
/*    */   
/*    */   default void markChunkDone(ChunkPos pos) {}
/*    */   
/*    */   default int targetDataVersion() {
/* 18 */     return -1;
/*    */   }
/*    */   
/*    */   CompoundTag applyFix(CompoundTag paramCompoundTag);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/storage/LegacyTagFixer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */