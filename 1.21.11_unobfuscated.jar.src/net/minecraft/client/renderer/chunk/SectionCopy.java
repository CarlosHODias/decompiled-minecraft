/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*    */ import net.minecraft.world.level.chunk.PalettedContainer;
/*    */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ class SectionCopy
/*    */ {
/*    */   private final Map<BlockPos, BlockEntity> blockEntities;
/*    */   private final PalettedContainer<BlockState> section;
/*    */   private final boolean debug;
/*    */   private final LevelHeightAccessor levelHeightAccessor;
/*    */   
/*    */   SectionCopy(LevelChunk levelChunk, int sectionIndex) {
/* 28 */     this.levelHeightAccessor = (LevelHeightAccessor)levelChunk;
/* 29 */     this.debug = levelChunk.getLevel().isDebug();
/* 30 */     this.blockEntities = (Map<BlockPos, BlockEntity>)ImmutableMap.copyOf(levelChunk.getBlockEntities());
/* 31 */     if (levelChunk instanceof net.minecraft.world.level.chunk.EmptyLevelChunk) {
/* 32 */       this.section = null;
/*    */     } else {
/* 34 */       LevelChunkSection[] sections = levelChunk.getSections();
/* 35 */       if (sectionIndex < 0 || sectionIndex >= sections.length) {
/* 36 */         this.section = null;
/*    */       } else {
/* 38 */         LevelChunkSection levelChunkSection = sections[sectionIndex];
/* 39 */         this.section = levelChunkSection.hasOnlyAir() ? null : levelChunkSection.getStates().copy();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 45 */     return this.blockEntities.get(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 50 */     int x = pos.getX();
/* 51 */     int y = pos.getY();
/* 52 */     int z = pos.getZ();
/* 53 */     if (this.debug) {
/* 54 */       BlockState blockState = null;
/* 55 */       if (y == 60) {
/* 56 */         blockState = Blocks.BARRIER.defaultBlockState();
/*    */       }
/* 58 */       if (y == 70) {
/* 59 */         blockState = DebugLevelSource.getBlockStateFor(x, z);
/*    */       }
/* 61 */       return (blockState == null) ? Blocks.AIR.defaultBlockState() : blockState;
/*    */     } 
/*    */     
/* 64 */     if (this.section == null) {
/* 65 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/*    */     try {
/* 69 */       return (BlockState)this.section.get(x & 0xF, y & 0xF, z & 0xF);
/* 70 */     } catch (Throwable t) {
/* 71 */       CrashReport report = CrashReport.forThrowable(t, "Getting block state");
/* 72 */       CrashReportCategory category = report.addCategory("Block being got");
/* 73 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation(this.levelHeightAccessor, x, y, z));
/* 74 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/SectionCopy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */