/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BulkSectionAccess implements AutoCloseable {
/*    */   private final LevelAccessor level;
/* 14 */   private final Long2ObjectMap<LevelChunkSection> acquiredSections = (Long2ObjectMap<LevelChunkSection>)new Long2ObjectOpenHashMap();
/*    */   private LevelChunkSection lastSection;
/*    */   private long lastSectionKey;
/*    */   
/*    */   public BulkSectionAccess(LevelAccessor level) {
/* 19 */     this.level = level;
/*    */   }
/*    */   
/*    */   public LevelChunkSection getSection(BlockPos pos) {
/* 23 */     int sectionIndex = this.level.getSectionIndex(pos.getY());
/* 24 */     if (sectionIndex < 0 || sectionIndex >= this.level.getSectionsCount()) {
/* 25 */       return null;
/*    */     }
/* 27 */     long sectionKey = SectionPos.asLong(pos);
/* 28 */     if (this.lastSection == null || this.lastSectionKey != sectionKey) {
/* 29 */       this.lastSection = (LevelChunkSection)this.acquiredSections.computeIfAbsent(sectionKey, key -> {
/*    */             ChunkAccess chunk = this.level.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*    */             LevelChunkSection result = chunk.getSection(pos);
/*    */             result.acquire();
/*    */             return result;
/*    */           });
/* 35 */       this.lastSectionKey = sectionKey;
/*    */     } 
/* 37 */     return this.lastSection;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 41 */     LevelChunkSection section = getSection(pos);
/*    */     
/* 43 */     if (section == null) {
/* 44 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 46 */     int sectionRelativeX = SectionPos.sectionRelative(pos.getX());
/* 47 */     int sectionRelativeY = SectionPos.sectionRelative(pos.getY());
/* 48 */     int sectionRelativeZ = SectionPos.sectionRelative(pos.getZ());
/* 49 */     return section.getBlockState(sectionRelativeX, sectionRelativeY, sectionRelativeZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     for (ObjectIterator<LevelChunkSection> objectIterator = this.acquiredSections.values().iterator(); objectIterator.hasNext(); ) { LevelChunkSection section = objectIterator.next();
/* 55 */       section.release(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/BulkSectionAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */