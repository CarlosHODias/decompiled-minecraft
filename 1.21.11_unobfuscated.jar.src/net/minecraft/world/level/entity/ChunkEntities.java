/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ public class ChunkEntities<T>
/*    */ {
/*    */   private final ChunkPos pos;
/*    */   private final List<T> entities;
/*    */   
/*    */   public ChunkEntities(ChunkPos pos, List<T> entities) {
/* 14 */     this.pos = pos;
/* 15 */     this.entities = entities;
/*    */   }
/*    */   
/*    */   public ChunkPos getPos() {
/* 19 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Stream<T> getEntities() {
/* 23 */     return this.entities.stream();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 27 */     return this.entities.isEmpty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/ChunkEntities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */