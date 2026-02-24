/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public class BlockInWorld
/*    */ {
/*    */   private final LevelReader level;
/*    */   private final BlockPos pos;
/*    */   private final boolean loadChunks;
/*    */   private BlockState state;
/*    */   private BlockEntity entity;
/*    */   private boolean cachedEntity;
/*    */   
/*    */   public BlockInWorld(LevelReader level, BlockPos pos, boolean loadChunks) {
/* 20 */     this.level = level;
/* 21 */     this.pos = pos.immutable();
/* 22 */     this.loadChunks = loadChunks;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 26 */     if (this.state == null && (this.loadChunks || this.level.hasChunkAt(this.pos))) {
/* 27 */       this.state = this.level.getBlockState(this.pos);
/*    */     }
/*    */     
/* 30 */     return this.state;
/*    */   }
/*    */   
/*    */   public BlockEntity getEntity() {
/* 34 */     if (this.entity == null && !this.cachedEntity) {
/* 35 */       this.entity = this.level.getBlockEntity(this.pos);
/* 36 */       this.cachedEntity = true;
/*    */     } 
/*    */     
/* 39 */     return this.entity;
/*    */   }
/*    */   
/*    */   public LevelReader getLevel() {
/* 43 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 47 */     return this.pos;
/*    */   }
/*    */   
/*    */   public static Predicate<BlockInWorld> hasState(Predicate<BlockState> predicate) {
/* 51 */     return input -> (input != null && predicate.test(input.getState()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/pattern/BlockInWorld.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */