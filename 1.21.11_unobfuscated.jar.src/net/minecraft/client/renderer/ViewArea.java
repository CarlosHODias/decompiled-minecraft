/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ 
/*     */ public class ViewArea {
/*     */   protected final LevelRenderer levelRenderer;
/*     */   protected final Level level;
/*     */   protected int sectionGridSizeY;
/*     */   protected int sectionGridSizeX;
/*     */   protected int sectionGridSizeZ;
/*     */   private int viewDistance;
/*     */   private SectionPos cameraSectionPos;
/*     */   public SectionRenderDispatcher.RenderSection[] sections;
/*     */   
/*     */   public ViewArea(SectionRenderDispatcher sectionRenderDispatcher, Level level, int renderDistance, LevelRenderer levelRenderer) {
/*  22 */     this.levelRenderer = levelRenderer;
/*  23 */     this.level = level;
/*     */     
/*  25 */     setViewDistance(renderDistance);
/*  26 */     createSections(sectionRenderDispatcher);
/*  27 */     this.cameraSectionPos = SectionPos.of(this.viewDistance + 1, 0, this.viewDistance + 1);
/*     */   }
/*     */   
/*     */   protected void createSections(SectionRenderDispatcher sectionRenderDispatcher) {
/*  31 */     if (!Minecraft.getInstance().isSameThread()) {
/*  32 */       throw new IllegalStateException("createSections called from wrong thread: " + Thread.currentThread().getName());
/*     */     }
/*  34 */     int totalSections = this.sectionGridSizeX * this.sectionGridSizeY * this.sectionGridSizeZ;
/*  35 */     this.sections = new SectionRenderDispatcher.RenderSection[totalSections];
/*     */     
/*  37 */     for (int x = 0; x < this.sectionGridSizeX; x++) {
/*  38 */       for (int y = 0; y < this.sectionGridSizeY; y++) {
/*  39 */         for (int z = 0; z < this.sectionGridSizeZ; z++) {
/*  40 */           int index = getSectionIndex(x, y, z);
/*  41 */           Objects.requireNonNull(sectionRenderDispatcher); this.sections[index] = new SectionRenderDispatcher.RenderSection(sectionRenderDispatcher, index, SectionPos.asLong(x, y + this.level.getMinSectionY(), z));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void releaseAllBuffers() {
/*  48 */     for (SectionRenderDispatcher.RenderSection section : this.sections) {
/*  49 */       section.reset();
/*     */     }
/*     */   }
/*     */   
/*     */   private int getSectionIndex(int x, int y, int z) {
/*  54 */     return (z * this.sectionGridSizeY + y) * this.sectionGridSizeX + x;
/*     */   }
/*     */   
/*     */   protected void setViewDistance(int renderDistance) {
/*  58 */     int dist = renderDistance * 2 + 1;
/*  59 */     this.sectionGridSizeX = dist;
/*  60 */     this.sectionGridSizeY = this.level.getSectionsCount();
/*  61 */     this.sectionGridSizeZ = dist;
/*  62 */     this.viewDistance = renderDistance;
/*     */   }
/*     */   
/*     */   public int getViewDistance() {
/*  66 */     return this.viewDistance;
/*     */   }
/*     */   
/*     */   public LevelHeightAccessor getLevelHeightAccessor() {
/*  70 */     return (LevelHeightAccessor)this.level;
/*     */   }
/*     */   
/*     */   public void repositionCamera(SectionPos cameraSectionPos) {
/*  74 */     for (int gridX = 0; gridX < this.sectionGridSizeX; gridX++) {
/*  75 */       int lowestX = cameraSectionPos.x() - this.viewDistance;
/*  76 */       int newSectionX = lowestX + Math.floorMod(gridX - lowestX, this.sectionGridSizeX);
/*     */       
/*  78 */       for (int gridZ = 0; gridZ < this.sectionGridSizeZ; gridZ++) {
/*  79 */         int lowestZ = cameraSectionPos.z() - this.viewDistance;
/*  80 */         int newSectionZ = lowestZ + Math.floorMod(gridZ - lowestZ, this.sectionGridSizeZ);
/*     */         
/*  82 */         for (int gridY = 0; gridY < this.sectionGridSizeY; gridY++) {
/*  83 */           int newSectionY = this.level.getMinSectionY() + gridY;
/*  84 */           SectionRenderDispatcher.RenderSection section = this.sections[getSectionIndex(gridX, gridY, gridZ)];
/*     */           
/*  86 */           long sectionNode = section.getSectionNode();
/*  87 */           if (sectionNode != SectionPos.asLong(newSectionX, newSectionY, newSectionZ)) {
/*  88 */             section.setSectionNode(SectionPos.asLong(newSectionX, newSectionY, newSectionZ));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*  93 */     this.cameraSectionPos = cameraSectionPos;
/*  94 */     this.levelRenderer.getSectionOcclusionGraph().invalidate();
/*     */   }
/*     */   
/*     */   public SectionPos getCameraSectionPos() {
/*  98 */     return this.cameraSectionPos;
/*     */   }
/*     */   
/*     */   public void setDirty(int sectionX, int sectionY, int sectionZ, boolean playerChanged) {
/* 102 */     SectionRenderDispatcher.RenderSection section = getRenderSection(sectionX, sectionY, sectionZ);
/* 103 */     if (section != null) {
/* 104 */       section.setDirty(playerChanged);
/*     */     }
/*     */   }
/*     */   
/*     */   protected SectionRenderDispatcher.RenderSection getRenderSectionAt(BlockPos pos) {
/* 109 */     return getRenderSection(SectionPos.asLong(pos));
/*     */   }
/*     */   
/*     */   protected SectionRenderDispatcher.RenderSection getRenderSection(long sectionNode) {
/* 113 */     int sectionX = SectionPos.x(sectionNode);
/* 114 */     int sectionY = SectionPos.y(sectionNode);
/* 115 */     int sectionZ = SectionPos.z(sectionNode);
/* 116 */     return getRenderSection(sectionX, sectionY, sectionZ);
/*     */   }
/*     */   
/*     */   private SectionRenderDispatcher.RenderSection getRenderSection(int sectionX, int sectionY, int sectionZ) {
/* 120 */     if (!containsSection(sectionX, sectionY, sectionZ)) {
/* 121 */       return null;
/*     */     }
/* 123 */     int y = sectionY - this.level.getMinSectionY();
/* 124 */     int x = Math.floorMod(sectionX, this.sectionGridSizeX);
/* 125 */     int z = Math.floorMod(sectionZ, this.sectionGridSizeZ);
/*     */     
/* 127 */     return this.sections[getSectionIndex(x, y, z)];
/*     */   }
/*     */   
/*     */   private boolean containsSection(int sectionX, int sectionY, int sectionZ) {
/* 131 */     if (sectionY < this.level.getMinSectionY() || sectionY > this.level.getMaxSectionY()) {
/* 132 */       return false;
/*     */     }
/* 134 */     if (sectionX < this.cameraSectionPos.x() - this.viewDistance || sectionX > this.cameraSectionPos.x() + this.viewDistance) {
/* 135 */       return false;
/*     */     }
/* 137 */     if (sectionZ < this.cameraSectionPos.z() - this.viewDistance || sectionZ > this.cameraSectionPos.z() + this.viewDistance) {
/* 138 */       return false;
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/ViewArea.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */