/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugBrainDump;
/*     */ import net.minecraft.util.debug.DebugPoiInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ 
/*     */ public class PoiDebugRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer {
/*     */   private static final int MAX_RENDER_DIST_FOR_POI_INFO = 30;
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   private static final int ORANGE = -23296;
/*     */   private final BrainDebugRenderer brainRenderer;
/*     */   
/*     */   public PoiDebugRenderer(BrainDebugRenderer brainRenderer) {
/*  27 */     this.brainRenderer = brainRenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  32 */     BlockPos playerPos = BlockPos.containing(camX, camY, camZ);
/*     */     
/*  34 */     debugValues.forEachBlock(DebugSubscriptions.POIS, (pos, poi) -> {
/*     */           if (playerPos.closerThan((Vec3i)playerPos, 30.0D)) {
/*     */             highlightPoi(playerPos);
/*     */             
/*     */             renderPoiInfo(poi, playerPos);
/*     */           } 
/*     */         });
/*  41 */     this.brainRenderer.getGhostPois(debugValues).forEach((poiPos, value) -> {
/*     */           if (debugValues.getBlockValue(DebugSubscriptions.POIS, debugValues) != null) {
/*     */             return;
/*     */           }
/*     */           if (debugValues.closerThan((Vec3i)debugValues, 30.0D)) {
/*     */             renderGhostPoi(debugValues, value);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void highlightPoi(BlockPos poiPos) {
/*  52 */     float padding = 0.05F;
/*  53 */     Gizmos.cuboid(poiPos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/*     */   }
/*     */   
/*     */   private void renderGhostPoi(BlockPos poiPos, List<String> names) {
/*  57 */     float padding = 0.05F;
/*  58 */     Gizmos.cuboid(poiPos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/*  59 */     Gizmos.billboardTextOverBlock(names.toString(), poiPos, 0, -256, 0.32F);
/*  60 */     Gizmos.billboardTextOverBlock("Ghost POI", poiPos, 1, -65536, 0.32F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderPoiInfo(DebugPoiInfo poi, DebugValueAccess debugValues) {
/*  66 */     int row = 0;
/*  67 */     if (SharedConstants.DEBUG_BRAIN) {
/*  68 */       List<String> ticketHolderNames = getTicketHolderNames(poi, false, debugValues);
/*  69 */       if (ticketHolderNames.size() < 4) {
/*  70 */         renderTextOverPoi("Owners: " + String.valueOf(ticketHolderNames), poi, row, -256);
/*     */       } else {
/*  72 */         renderTextOverPoi("" + ticketHolderNames.size() + " ticket holders", poi, row, -256);
/*     */       } 
/*     */       
/*  75 */       row++;
/*     */       
/*  77 */       List<String> potentialTicketHolderNames = getTicketHolderNames(poi, true, debugValues);
/*  78 */       if (potentialTicketHolderNames.size() < 4) {
/*  79 */         renderTextOverPoi("Candidates: " + String.valueOf(potentialTicketHolderNames), poi, row, -23296);
/*     */       } else {
/*  81 */         renderTextOverPoi("" + potentialTicketHolderNames.size() + " potential owners", poi, row, -23296);
/*     */       } 
/*  83 */       row++;
/*     */     } 
/*     */     
/*  86 */     renderTextOverPoi("Free tickets: " + poi.freeTicketCount(), poi, row, -256);
/*     */     
/*  88 */     row++;
/*  89 */     renderTextOverPoi(poi.poiType().getRegisteredName(), poi, row, -1);
/*     */   }
/*     */   
/*     */   private static void renderTextOverPoi(String text, DebugPoiInfo poi, int row, int color) {
/*  93 */     Gizmos.billboardTextOverBlock(text, poi.pos(), row, color, 0.32F);
/*     */   }
/*     */   
/*     */   private List<String> getTicketHolderNames(DebugPoiInfo poi, boolean potential, DebugValueAccess debugValues) {
/*  97 */     List<String> names = new ArrayList<>();
/*  98 */     debugValues.forEachEntity(DebugSubscriptions.BRAINS, (entity, brainDump) -> {
/*     */           boolean include = potential ? brainDump.hasPotentialPoi(poi.pos()) : brainDump.hasPoi(poi.pos());
/*     */           if (include) {
/*     */             names.add(DebugEntityNameGenerator.getEntityName(entity.getUUID()));
/*     */           }
/*     */         });
/* 104 */     return names;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/PoiDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */