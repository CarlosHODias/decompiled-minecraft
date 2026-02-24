/*     */ package net.minecraft.client.renderer.debug;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.SectionOcclusionGraph;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ public class ChunkCullingDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*  21 */   public static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   public ChunkCullingDebugRenderer(Minecraft minecraft) {
/*  26 */     this.minecraft = minecraft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  31 */     LevelRenderer levelRenderer = this.minecraft.levelRenderer;
/*     */     
/*  33 */     boolean sectionPath = this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_SECTION_PATHS);
/*  34 */     boolean sectionVisibility = this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_SECTION_VISIBILITY);
/*  35 */     if (sectionPath || sectionVisibility) {
/*  36 */       SectionOcclusionGraph sectionOcclusionGraph = levelRenderer.getSectionOcclusionGraph();
/*  37 */       for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = levelRenderer.getVisibleSections().iterator(); objectListIterator.hasNext(); ) { SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/*  38 */         SectionOcclusionGraph.Node node = sectionOcclusionGraph.getNode(section);
/*  39 */         if (node == null) {
/*     */           continue;
/*     */         }
/*  42 */         BlockPos renderOffset = section.getRenderOrigin();
/*     */         
/*  44 */         if (sectionPath) {
/*  45 */           int color = (node.step == 0) ? 0 : Mth.hsvToRgb(node.step / 50.0F, 0.9F, 0.9F);
/*  46 */           for (int i = 0; i < DIRECTIONS.length; i++) {
/*  47 */             if (node.hasSourceDirection(i)) {
/*  48 */               Direction direction = DIRECTIONS[i];
/*  49 */               Gizmos.line(
/*  50 */                   Vec3.atLowerCornerWithOffset((Vec3i)renderOffset, 8.0D, 8.0D, 8.0D), 
/*  51 */                   Vec3.atLowerCornerWithOffset((Vec3i)renderOffset, (8 - 16 * 
/*  52 */                     direction.getStepX()), (8 - 16 * 
/*  53 */                     direction.getStepY()), (8 - 16 * 
/*  54 */                     direction.getStepZ())), 
/*     */                   
/*  56 */                   ARGB.opaque(color));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/*  62 */         if (sectionVisibility && section.getSectionMesh().hasRenderableLayers()) {
/*  63 */           int c = 0;
/*  64 */           for (Direction direction1 : DIRECTIONS) {
/*  65 */             for (Direction direction2 : DIRECTIONS) {
/*  66 */               boolean b = section.getSectionMesh().facesCanSeeEachother(direction1, direction2);
/*  67 */               if (!b) {
/*  68 */                 c++;
/*  69 */                 Gizmos.line(
/*  70 */                     Vec3.atLowerCornerWithOffset((Vec3i)renderOffset, (8 + 8 * 
/*  71 */                       direction1.getStepX()), (8 + 8 * 
/*  72 */                       direction1.getStepY()), (8 + 8 * 
/*  73 */                       direction1.getStepZ())), 
/*     */                     
/*  75 */                     Vec3.atLowerCornerWithOffset((Vec3i)renderOffset, (8 + 8 * 
/*  76 */                       direction2.getStepX()), (8 + 8 * 
/*  77 */                       direction2.getStepY()), (8 + 8 * 
/*  78 */                       direction2.getStepZ())), 
/*     */                     
/*  80 */                     ARGB.color(255, 255, 0, 0));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/*  86 */           if (c > 0) {
/*  87 */             float delta = 0.5F;
/*  88 */             float a = 0.2F;
/*  89 */             Gizmos.cuboid(section.getBoundingBox().deflate(0.5D), GizmoStyle.fill(ARGB.colorFromFloat(0.2F, 0.9F, 0.9F, 0.0F)));
/*     */           } 
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/*  95 */     Frustum capturedFrustum = levelRenderer.getCapturedFrustum();
/*  96 */     if (capturedFrustum != null) {
/*  97 */       Vec3 offset = new Vec3(capturedFrustum.getCamX(), capturedFrustum.getCamY(), capturedFrustum.getCamZ());
/*     */       
/*  99 */       Vector4f[] frustumPoints = capturedFrustum.getFrustumPoints();
/*     */ 
/*     */       
/* 102 */       addFrustumQuad(offset, frustumPoints, 0, 1, 2, 3, 0, 1, 1);
/*     */ 
/*     */       
/* 105 */       addFrustumQuad(offset, frustumPoints, 4, 5, 6, 7, 1, 0, 0);
/*     */ 
/*     */       
/* 108 */       addFrustumQuad(offset, frustumPoints, 0, 1, 5, 4, 1, 1, 0);
/*     */ 
/*     */       
/* 111 */       addFrustumQuad(offset, frustumPoints, 2, 3, 7, 6, 0, 0, 1);
/*     */ 
/*     */       
/* 114 */       addFrustumQuad(offset, frustumPoints, 0, 4, 7, 3, 0, 1, 0);
/*     */ 
/*     */       
/* 117 */       addFrustumQuad(offset, frustumPoints, 1, 5, 6, 2, 1, 0, 1);
/*     */       
/* 119 */       addFrustumLine(offset, frustumPoints[0], frustumPoints[1]);
/* 120 */       addFrustumLine(offset, frustumPoints[1], frustumPoints[2]);
/*     */       
/* 122 */       addFrustumLine(offset, frustumPoints[2], frustumPoints[3]);
/*     */       
/* 124 */       addFrustumLine(offset, frustumPoints[3], frustumPoints[0]);
/*     */ 
/*     */       
/* 127 */       addFrustumLine(offset, frustumPoints[4], frustumPoints[5]);
/*     */       
/* 129 */       addFrustumLine(offset, frustumPoints[5], frustumPoints[6]);
/*     */       
/* 131 */       addFrustumLine(offset, frustumPoints[6], frustumPoints[7]);
/*     */       
/* 133 */       addFrustumLine(offset, frustumPoints[7], frustumPoints[4]);
/*     */ 
/*     */       
/* 136 */       addFrustumLine(offset, frustumPoints[0], frustumPoints[4]);
/*     */       
/* 138 */       addFrustumLine(offset, frustumPoints[1], frustumPoints[5]);
/*     */       
/* 140 */       addFrustumLine(offset, frustumPoints[2], frustumPoints[6]);
/*     */       
/* 142 */       addFrustumLine(offset, frustumPoints[3], frustumPoints[7]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addFrustumLine(Vec3 offset, Vector4f a, Vector4f b) {
/* 147 */     Gizmos.line(new Vec3(offset.x + a.x, offset.y + a.y, offset.z + a.z), new Vec3(offset.x + b.x, offset.y + b.y, offset.z + b.z), -16777216);
/*     */   }
/*     */   
/*     */   private void addFrustumQuad(Vec3 offset, Vector4f[] frustumPoints, int i0, int i1, int i2, int i3, int r, int g, int b) {
/* 151 */     float a = 0.25F;
/* 152 */     Gizmos.rect(new Vec3(frustumPoints[i0]
/* 153 */           .x(), frustumPoints[i0].y(), frustumPoints[i0].z()).add(offset), new Vec3(frustumPoints[i1]
/* 154 */           .x(), frustumPoints[i1].y(), frustumPoints[i1].z()).add(offset), new Vec3(frustumPoints[i2]
/* 155 */           .x(), frustumPoints[i2].y(), frustumPoints[i2].z()).add(offset), new Vec3(frustumPoints[i3]
/* 156 */           .x(), frustumPoints[i3].y(), frustumPoints[i3].z()).add(offset), 
/* 157 */         GizmoStyle.fill(ARGB.colorFromFloat(0.25F, r, g, b)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/ChunkCullingDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */