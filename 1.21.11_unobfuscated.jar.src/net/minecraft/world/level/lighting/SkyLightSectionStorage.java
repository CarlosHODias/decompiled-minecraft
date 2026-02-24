/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public class SkyLightSectionStorage extends LayerLightSectionStorage<SkyLightSectionStorage.SkyDataLayerStorageMap> {
/*     */   protected SkyLightSectionStorage(LightChunkGetter chunkSource) {
/*  14 */     super(LightLayer.SKY, chunkSource, new SkyDataLayerStorageMap(new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), Integer.MAX_VALUE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLightValue(long blockNode) {
/*  19 */     return getLightValue(blockNode, false);
/*     */   }
/*     */   
/*     */   protected int getLightValue(long blockNode, boolean updating) {
/*  23 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*  24 */     int sectionY = SectionPos.y(sectionNode);
/*  25 */     SkyDataLayerStorageMap sections = updating ? this.updatingSectionData : this.visibleSectionData;
/*  26 */     int topSection = sections.topSections.get(SectionPos.getZeroNode(sectionNode));
/*  27 */     if (topSection == sections.currentLowestY || sectionY >= topSection) {
/*  28 */       if (updating && !lightOnInSection(sectionNode)) {
/*  29 */         return 0;
/*     */       }
/*  31 */       return 15;
/*     */     } 
/*  33 */     DataLayer layer = getDataLayer(sections, sectionNode);
/*  34 */     if (layer == null) {
/*  35 */       blockNode = BlockPos.getFlatIndex(blockNode);
/*  36 */       while (layer == null) {
/*  37 */         sectionY++;
/*  38 */         if (sectionY >= topSection) {
/*  39 */           return 15;
/*     */         }
/*  41 */         sectionNode = SectionPos.offset(sectionNode, Direction.UP);
/*  42 */         layer = getDataLayer(sections, sectionNode);
/*     */       } 
/*     */     } 
/*  45 */     return layer.get(
/*  46 */         SectionPos.sectionRelative(BlockPos.getX(blockNode)), 
/*  47 */         SectionPos.sectionRelative(BlockPos.getY(blockNode)), 
/*  48 */         SectionPos.sectionRelative(BlockPos.getZ(blockNode)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNodeAdded(long sectionNode) {
/*  54 */     int y = SectionPos.y(sectionNode);
/*  55 */     if (this.updatingSectionData.currentLowestY > y) {
/*  56 */       this.updatingSectionData.currentLowestY = y;
/*  57 */       this.updatingSectionData.topSections.defaultReturnValue(this.updatingSectionData.currentLowestY);
/*     */     } 
/*  59 */     long zeroNode = SectionPos.getZeroNode(sectionNode);
/*  60 */     int oldTop = this.updatingSectionData.topSections.get(zeroNode);
/*  61 */     if (oldTop < y + 1) {
/*  62 */       this.updatingSectionData.topSections.put(zeroNode, y + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNodeRemoved(long sectionNode) {
/*  68 */     long zeroNode = SectionPos.getZeroNode(sectionNode);
/*  69 */     int y = SectionPos.y(sectionNode);
/*  70 */     if (this.updatingSectionData.topSections.get(zeroNode) == y + 1) {
/*  71 */       long newTopSection = sectionNode;
/*  72 */       while (!storingLightForSection(newTopSection) && hasLightDataAtOrBelow(y)) {
/*  73 */         y--;
/*  74 */         newTopSection = SectionPos.offset(newTopSection, Direction.DOWN);
/*     */       } 
/*  76 */       if (storingLightForSection(newTopSection)) {
/*  77 */         this.updatingSectionData.topSections.put(zeroNode, y + 1);
/*     */       } else {
/*  79 */         this.updatingSectionData.topSections.remove(zeroNode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected DataLayer createDataLayer(long sectionNode) {
/*  86 */     DataLayer queuedLayer = (DataLayer)this.queuedSections.get(sectionNode);
/*  87 */     if (queuedLayer != null) {
/*  88 */       return queuedLayer;
/*     */     }
/*     */     
/*  91 */     int topSection = this.updatingSectionData.topSections.get(SectionPos.getZeroNode(sectionNode));
/*     */     
/*  93 */     if (topSection == this.updatingSectionData.currentLowestY || SectionPos.y(sectionNode) >= topSection) {
/*     */       
/*  95 */       if (lightOnInSection(sectionNode)) {
/*  96 */         return new DataLayer(15);
/*     */       }
/*  98 */       return new DataLayer();
/*     */     } 
/*     */ 
/*     */     
/* 102 */     long aboveSection = SectionPos.offset(sectionNode, Direction.UP);
/*     */     DataLayer aboveData;
/* 104 */     while ((aboveData = getDataLayer(aboveSection, true)) == null) {
/* 105 */       aboveSection = SectionPos.offset(aboveSection, Direction.UP);
/*     */     }
/*     */     
/* 108 */     return repeatFirstLayer(aboveData);
/*     */   }
/*     */ 
/*     */   
/*     */   private static DataLayer repeatFirstLayer(DataLayer data) {
/* 113 */     if (data.isDefinitelyHomogenous()) {
/* 114 */       return data.copy();
/*     */     }
/* 116 */     byte[] input = data.getData();
/* 117 */     byte[] output = new byte[2048];
/*     */     
/* 119 */     for (int i = 0; i < 16; i++) {
/* 120 */       System.arraycopy(input, 0, output, i * 128, 128);
/*     */     }
/*     */     
/* 123 */     return new DataLayer(output);
/*     */   }
/*     */   
/*     */   protected boolean hasLightDataAtOrBelow(int sectionY) {
/* 127 */     return (sectionY >= this.updatingSectionData.currentLowestY);
/*     */   }
/*     */   
/*     */   protected boolean isAboveData(long sectionNode) {
/* 131 */     long zeroNode = SectionPos.getZeroNode(sectionNode);
/* 132 */     int topSection = this.updatingSectionData.topSections.get(zeroNode);
/* 133 */     return (topSection == this.updatingSectionData.currentLowestY || SectionPos.y(sectionNode) >= topSection);
/*     */   }
/*     */   
/*     */   protected int getTopSectionY(long zeroNode) {
/* 137 */     return this.updatingSectionData.topSections.get(zeroNode);
/*     */   }
/*     */   
/*     */   protected int getBottomSectionY() {
/* 141 */     return this.updatingSectionData.currentLowestY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class SkyDataLayerStorageMap
/*     */     extends DataLayerStorageMap<SkyDataLayerStorageMap>
/*     */   {
/*     */     private int currentLowestY;
/*     */ 
/*     */     
/*     */     private final Long2IntOpenHashMap topSections;
/*     */ 
/*     */     
/*     */     public SkyDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> map, Long2IntOpenHashMap topSections, int currentLowestY) {
/* 156 */       super(map);
/* 157 */       this.topSections = topSections;
/* 158 */       topSections.defaultReturnValue(currentLowestY);
/* 159 */       this.currentLowestY = currentLowestY;
/*     */     }
/*     */ 
/*     */     
/*     */     public SkyDataLayerStorageMap copy() {
/* 164 */       return new SkyDataLayerStorageMap(this.map.clone(), this.topSections.clone(), this.currentLowestY);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/lighting/SkyLightSectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */