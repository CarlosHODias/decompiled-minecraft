/*    */ package net.minecraft.world.level.lighting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.chunk.DataLayer;
/*    */ 
/*    */ public abstract class DataLayerStorageMap<M extends DataLayerStorageMap<M>>
/*    */ {
/*    */   private static final int CACHE_SIZE = 2;
/*  9 */   private final long[] lastSectionKeys = new long[2];
/* 10 */   private final DataLayer[] lastSections = new DataLayer[2];
/*    */   private boolean cacheEnabled;
/*    */   protected final Long2ObjectOpenHashMap<DataLayer> map;
/*    */   
/*    */   protected DataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map) {
/* 15 */     this.map = map;
/* 16 */     clearCache();
/* 17 */     this.cacheEnabled = true;
/*    */   }
/*    */   
/*    */   public abstract M copy();
/*    */   
/*    */   public DataLayer copyDataLayer(long sectionNode) {
/* 23 */     DataLayer newDataLayer = ((DataLayer)this.map.get(sectionNode)).copy();
/* 24 */     this.map.put(sectionNode, newDataLayer);
/* 25 */     clearCache();
/* 26 */     return newDataLayer;
/*    */   }
/*    */   
/*    */   public boolean hasLayer(long sectionNode) {
/* 30 */     return this.map.containsKey(sectionNode);
/*    */   }
/*    */   
/*    */   public DataLayer getLayer(long sectionNode) {
/* 34 */     if (this.cacheEnabled) {
/* 35 */       for (int i = 0; i < 2; i++) {
/* 36 */         if (sectionNode == this.lastSectionKeys[i]) {
/* 37 */           return this.lastSections[i];
/*    */         }
/*    */       } 
/*    */     }
/* 41 */     DataLayer data = (DataLayer)this.map.get(sectionNode);
/* 42 */     if (data != null) {
/* 43 */       if (this.cacheEnabled) {
/* 44 */         for (int i = 1; i > 0; i--) {
/* 45 */           this.lastSectionKeys[i] = this.lastSectionKeys[i - 1];
/* 46 */           this.lastSections[i] = this.lastSections[i - 1];
/*    */         } 
/* 48 */         this.lastSectionKeys[0] = sectionNode;
/* 49 */         this.lastSections[0] = data;
/*    */       } 
/* 51 */       return data;
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public DataLayer removeLayer(long sectionNode) {
/* 58 */     return (DataLayer)this.map.remove(sectionNode);
/*    */   }
/*    */   
/*    */   public void setLayer(long sectionNode, DataLayer layer) {
/* 62 */     this.map.put(sectionNode, layer);
/*    */   }
/*    */   
/*    */   public void clearCache() {
/* 66 */     for (int i = 0; i < 2; i++) {
/* 67 */       this.lastSectionKeys[i] = Long.MAX_VALUE;
/* 68 */       this.lastSections[i] = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void disableCache() {
/* 73 */     this.cacheEnabled = false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/lighting/DataLayerStorageMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */