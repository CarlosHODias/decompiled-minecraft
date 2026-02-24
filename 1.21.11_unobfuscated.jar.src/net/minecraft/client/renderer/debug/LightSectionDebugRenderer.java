/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.lighting.LayerLightSectionStorage;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ 
/*     */ public class LightSectionDebugRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer
/*     */ {
/*  23 */   private static final Duration REFRESH_INTERVAL = Duration.ofMillis(500L);
/*     */   
/*     */   private static final int RADIUS = 10;
/*  26 */   private static final int LIGHT_AND_BLOCKS_COLOR = ARGB.colorFromFloat(0.25F, 1.0F, 1.0F, 0.0F);
/*  27 */   private static final int LIGHT_ONLY_COLOR = ARGB.colorFromFloat(0.125F, 0.25F, 0.125F, 0.0F);
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   private final LightLayer lightLayer;
/*  31 */   private Instant lastUpdateTime = Instant.now();
/*     */   private SectionData data;
/*     */   
/*     */   public LightSectionDebugRenderer(Minecraft minecraft, LightLayer lightLayer) {
/*  35 */     this.minecraft = minecraft;
/*  36 */     this.lightLayer = lightLayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  41 */     Instant time = Instant.now();
/*  42 */     if (this.data == null || Duration.between(this.lastUpdateTime, time).compareTo(REFRESH_INTERVAL) > 0) {
/*  43 */       this.lastUpdateTime = time;
/*  44 */       this.data = new SectionData(this.minecraft.level.getLightEngine(), SectionPos.of(this.minecraft.player.blockPosition()), 10, this.lightLayer);
/*     */     } 
/*     */     
/*  47 */     renderEdges(this.data.lightAndBlocksShape, this.data.minPos, LIGHT_AND_BLOCKS_COLOR);
/*  48 */     renderEdges(this.data.lightShape, this.data.minPos, LIGHT_ONLY_COLOR);
/*     */     
/*  50 */     renderFaces(this.data.lightAndBlocksShape, this.data.minPos, LIGHT_AND_BLOCKS_COLOR);
/*  51 */     renderFaces(this.data.lightShape, this.data.minPos, LIGHT_ONLY_COLOR);
/*     */   }
/*     */   
/*     */   private static void renderFaces(DiscreteVoxelShape shape, SectionPos minSection, int color) {
/*  55 */     shape.forAllFaces((direction, x, y, z) -> {
/*     */           int sectionX = x + minSection.getX(), sectionY = y + minSection.getY(), sectionZ = z + minSection.getZ();
/*     */           renderFace(direction, sectionX, sectionY, sectionZ, color);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void renderEdges(DiscreteVoxelShape shape, SectionPos minSection, int color) {
/*  64 */     shape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
/*     */           int sectionX0 = x0 + minSection.getX(), sectionY0 = y0 + minSection.getY(), sectionZ0 = z0 + minSection.getZ(), sectionX1 = x1 + minSection.getX(), sectionY1 = y1 + minSection.getY(), sectionZ1 = z1 + minSection.getZ();
/*     */           renderEdge(sectionX0, sectionY0, sectionZ0, sectionX1, sectionY1, sectionZ1, color);
/*     */         }, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void renderFace(Direction direction, int sectionX, int sectionY, int sectionZ, int color) {
/*  76 */     Vec3 cuboidCornerA = new Vec3(SectionPos.sectionToBlockCoord(sectionX), SectionPos.sectionToBlockCoord(sectionY), SectionPos.sectionToBlockCoord(sectionZ));
/*  77 */     Vec3 cuboidCornerB = cuboidCornerA.add(16.0D, 16.0D, 16.0D);
/*  78 */     Gizmos.rect(cuboidCornerA, cuboidCornerB, direction, 
/*     */ 
/*     */ 
/*     */         
/*  82 */         GizmoStyle.fill(color));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void renderEdge(int sectionX0, int sectionY0, int sectionZ0, int sectionX1, int sectionY1, int sectionZ1, int color) {
/*  87 */     double x0 = SectionPos.sectionToBlockCoord(sectionX0);
/*  88 */     double y0 = SectionPos.sectionToBlockCoord(sectionY0);
/*  89 */     double z0 = SectionPos.sectionToBlockCoord(sectionZ0);
/*  90 */     double x1 = SectionPos.sectionToBlockCoord(sectionX1);
/*  91 */     double y1 = SectionPos.sectionToBlockCoord(sectionY1);
/*  92 */     double z1 = SectionPos.sectionToBlockCoord(sectionZ1);
/*     */     
/*  94 */     int opaqueColor = ARGB.opaque(color);
/*  95 */     Gizmos.line(new Vec3(x0, y0, z0), new Vec3(x1, y1, z1), opaqueColor);
/*     */   }
/*     */   
/*     */   private static final class SectionData {
/*     */     private final DiscreteVoxelShape lightAndBlocksShape;
/*     */     private final DiscreteVoxelShape lightShape;
/*     */     private final SectionPos minPos;
/*     */     
/*     */     private SectionData(LevelLightEngine engine, SectionPos centerPos, int radius, LightLayer lightLayer) {
/* 104 */       int size = radius * 2 + 1;
/*     */       
/* 106 */       this.lightAndBlocksShape = (DiscreteVoxelShape)new BitSetDiscreteVoxelShape(size, size, size);
/* 107 */       this.lightShape = (DiscreteVoxelShape)new BitSetDiscreteVoxelShape(size, size, size);
/*     */       
/* 109 */       for (int z = 0; z < size; z++) {
/* 110 */         for (int y = 0; y < size; y++) {
/* 111 */           for (int x = 0; x < size; x++) {
/* 112 */             SectionPos pos = SectionPos.of(centerPos.x() + x - radius, centerPos.y() + y - radius, centerPos.z() + z - radius);
/* 113 */             LayerLightSectionStorage.SectionType type = engine.getDebugSectionType(lightLayer, pos);
/* 114 */             if (type == LayerLightSectionStorage.SectionType.LIGHT_AND_DATA) {
/* 115 */               this.lightAndBlocksShape.fill(x, y, z);
/* 116 */               this.lightShape.fill(x, y, z);
/* 117 */             } else if (type == LayerLightSectionStorage.SectionType.LIGHT_ONLY) {
/* 118 */               this.lightShape.fill(x, y, z);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 124 */       this.minPos = SectionPos.of(centerPos.x() - radius, centerPos.y() - radius, centerPos.z() - radius);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/LightSectionDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */