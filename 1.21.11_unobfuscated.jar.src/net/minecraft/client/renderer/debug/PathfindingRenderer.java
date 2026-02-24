/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.TextGizmo;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugPathInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathfindingRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer
/*     */ {
/*     */   private static final float MAX_RENDER_DIST = 80.0F;
/*     */   private static final int MAX_TARGETING_DIST = 8;
/*     */   private static final boolean SHOW_ONLY_SELECTED = false;
/*     */   private static final boolean SHOW_OPEN_CLOSED = true;
/*     */   private static final boolean SHOW_OPEN_CLOSED_COST_MALUS = false;
/*     */   private static final boolean SHOW_OPEN_CLOSED_NODE_TYPE_WITH_TEXT = false;
/*     */   private static final boolean SHOW_OPEN_CLOSED_NODE_TYPE_WITH_BOX = true;
/*     */   private static final boolean SHOW_GROUND_LABELS = true;
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  47 */     debugValues.forEachEntity(DebugSubscriptions.ENTITY_PATHS, (entity, info) -> renderPath(camX, camY, camZ, info.path(), info.maxNodeDistance()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void renderPath(double camX, double camY, double camZ, Path path, float maxNodeDistance) {
/*  54 */     renderPath(path, maxNodeDistance, true, true, camX, camY, camZ);
/*     */   }
/*     */   
/*     */   public static void renderPath(Path path, float maxNodeDistance, boolean renderOpenAndClosedSets, boolean renderGroundLabels, double camX, double camY, double camZ) {
/*  58 */     renderPathLine(path, camX, camY, camZ);
/*     */     
/*  60 */     BlockPos pos = path.getTarget();
/*  61 */     if (distanceToCamera(pos, camX, camY, camZ) <= 80.0F) {
/*  62 */       Gizmos.cuboid(new AABB((pos.getX() + 0.25F), (pos.getY() + 0.25F), pos.getZ() + 0.25D, (pos.getX() + 0.75F), (pos.getY() + 0.75F), (pos.getZ() + 0.75F)), GizmoStyle.fill(ARGB.colorFromFloat(0.5F, 0.0F, 1.0F, 0.0F)));
/*     */       
/*  64 */       for (int i = 0; i < path.getNodeCount(); i++) {
/*  65 */         Node n = path.getNode(i);
/*  66 */         if (distanceToCamera(n.asBlockPos(), camX, camY, camZ) <= 80.0F) {
/*  67 */           float r = (i == path.getNextNodeIndex()) ? 1.0F : 0.0F;
/*  68 */           float b = (i == path.getNextNodeIndex()) ? 0.0F : 1.0F;
/*  69 */           AABB aabb = new AABB((n.x + 0.5F - maxNodeDistance), (n.y + 0.01F * i), (n.z + 0.5F - maxNodeDistance), (n.x + 0.5F + maxNodeDistance), (n.y + 0.25F + 0.01F * i), (n.z + 0.5F + maxNodeDistance));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  77 */           Gizmos.cuboid(aabb, GizmoStyle.fill(ARGB.colorFromFloat(0.5F, r, 0.0F, b)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     Path.DebugData debugData = path.debugData();
/*  83 */     if (renderOpenAndClosedSets && debugData != null) {
/*  84 */       for (Node node : debugData.closedSet()) {
/*  85 */         if (distanceToCamera(node.asBlockPos(), camX, camY, camZ) <= 80.0F)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  93 */           Gizmos.cuboid(new AABB((node.x + 0.5F - maxNodeDistance / 2.0F), (node.y + 0.01F), (node.z + 0.5F - maxNodeDistance / 2.0F), (node.x + 0.5F + maxNodeDistance / 2.0F), node.y + 0.1D, (node.z + 0.5F + maxNodeDistance / 2.0F)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 100 */               GizmoStyle.fill(ARGB.colorFromFloat(0.5F, 1.0F, 0.8F, 0.8F)));
/*     */         }
/*     */       } 
/*     */       
/* 104 */       for (Node node : debugData.openSet()) {
/* 105 */         if (distanceToCamera(node.asBlockPos(), camX, camY, camZ) <= 80.0F)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 113 */           Gizmos.cuboid(new AABB((node.x + 0.5F - maxNodeDistance / 2.0F), (node.y + 0.01F), (node.z + 0.5F - maxNodeDistance / 2.0F), (node.x + 0.5F + maxNodeDistance / 2.0F), node.y + 0.1D, (node.z + 0.5F + maxNodeDistance / 2.0F)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 120 */               GizmoStyle.fill(ARGB.colorFromFloat(0.5F, 0.8F, 1.0F, 1.0F)));
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 126 */     if (renderGroundLabels) {
/* 127 */       for (int i = 0; i < path.getNodeCount(); i++) {
/* 128 */         Node n = path.getNode(i);
/* 129 */         if (distanceToCamera(n.asBlockPos(), camX, camY, camZ) <= 80.0F) {
/* 130 */           Gizmos.billboardText(String.valueOf(n.type), new Vec3(n.x + 0.5D, n.y + 0.75D, n.z + 0.5D), TextGizmo.Style.whiteAndCentered().withScale(0.32F)).setAlwaysOnTop();
/* 131 */           Gizmos.billboardText(String.format(Locale.ROOT, "%.2f", new Object[] { n.costMalus }), new Vec3(n.x + 0.5D, n.y + 0.25D, n.z + 0.5D), TextGizmo.Style.whiteAndCentered().withScale(0.32F)).setAlwaysOnTop();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void renderPathLine(Path path, double camX, double camY, double camZ) {
/* 138 */     if (path.getNodeCount() < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 142 */     Vec3 last = path.getNode(0).asVec3();
/* 143 */     for (int i = 1; i < path.getNodeCount(); i++) {
/* 144 */       Node n = path.getNode(i);
/*     */       
/* 146 */       if (distanceToCamera(n.asBlockPos(), camX, camY, camZ) > 80.0F) {
/* 147 */         last = n.asVec3();
/*     */       }
/*     */       else {
/*     */         
/* 151 */         float hue = i / path.getNodeCount() * 0.33F;
/* 152 */         int color = ARGB.opaque(Mth.hsvToRgb(hue, 0.9F, 0.9F));
/*     */         
/* 154 */         Gizmos.arrow(last.add(0.5D, 0.5D, 0.5D), n.asVec3().add(0.5D, 0.5D, 0.5D), color);
/* 155 */         last = n.asVec3();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static float distanceToCamera(BlockPos n, double camX, double camY, double camZ) {
/* 160 */     return (float)(Math.abs(n.getX() - camX) + Math.abs(n.getY() - camY) + Math.abs(n.getZ() - camZ));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/PathfindingRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */