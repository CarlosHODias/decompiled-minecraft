/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class RenderRegionCache {
/* 10 */   private final Long2ObjectMap<SectionCopy> sectionCopyCache = (Long2ObjectMap<SectionCopy>)new Long2ObjectOpenHashMap();
/*    */   
/*    */   public RenderSectionRegion createRegion(Level level, long sectionNode) {
/* 13 */     int sectionX = SectionPos.x(sectionNode);
/* 14 */     int sectionY = SectionPos.y(sectionNode);
/* 15 */     int sectionZ = SectionPos.z(sectionNode);
/*    */     
/* 17 */     int minSectionX = sectionX - 1;
/* 18 */     int minSectionY = sectionY - 1;
/* 19 */     int minSectionZ = sectionZ - 1;
/* 20 */     int maxSectionX = sectionX + 1;
/* 21 */     int maxSectionY = sectionY + 1;
/* 22 */     int maxSectionZ = sectionZ + 1;
/*    */     
/* 24 */     SectionCopy[] regionSections = new SectionCopy[27];
/* 25 */     for (int regionSectionZ = minSectionZ; regionSectionZ <= maxSectionZ; regionSectionZ++) {
/* 26 */       for (int regionSectionY = minSectionY; regionSectionY <= maxSectionY; regionSectionY++) {
/* 27 */         for (int regionSectionX = minSectionX; regionSectionX <= maxSectionX; regionSectionX++) {
/* 28 */           int index = RenderSectionRegion.index(minSectionX, minSectionY, minSectionZ, regionSectionX, regionSectionY, regionSectionZ);
/* 29 */           regionSections[index] = getSectionDataCopy(level, regionSectionX, regionSectionY, regionSectionZ);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 34 */     return new RenderSectionRegion(level, minSectionX, minSectionY, minSectionZ, regionSections);
/*    */   }
/*    */   
/*    */   private SectionCopy getSectionDataCopy(Level level, int sectionX, int sectionY, int sectionZ) {
/* 38 */     return (SectionCopy)this.sectionCopyCache.computeIfAbsent(SectionPos.asLong(sectionX, sectionY, sectionZ), k -> {
/*    */           LevelChunk chunk = level.getChunk(sectionX, sectionZ);
/*    */           return new SectionCopy(chunk, chunk.getSectionIndexFromSectionY(sectionY));
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/RenderRegionCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */