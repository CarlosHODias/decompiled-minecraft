/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ public enum FullChunkStatus {
/*  4 */   INACCESSIBLE,
/*  5 */   FULL,
/*  6 */   BLOCK_TICKING,
/*  7 */   ENTITY_TICKING;
/*    */ 
/*    */   
/*    */   public boolean isOrAfter(FullChunkStatus step) {
/* 11 */     return (ordinal() >= step.ordinal());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/FullChunkStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */