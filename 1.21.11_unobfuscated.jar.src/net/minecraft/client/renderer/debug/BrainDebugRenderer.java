/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.util.debug.DebugBrainDump;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BrainDebugRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer
/*     */ {
/*     */   private static final boolean SHOW_NAME_FOR_ALL = true;
/*     */   private static final boolean SHOW_PROFESSION_FOR_ALL = false;
/*     */   private static final boolean SHOW_BEHAVIORS_FOR_ALL = false;
/*     */   private static final boolean SHOW_ACTIVITIES_FOR_ALL = false;
/*     */   private static final boolean SHOW_INVENTORY_FOR_ALL = false;
/*     */   private static final boolean SHOW_GOSSIPS_FOR_ALL = false;
/*     */   private static final boolean SHOW_HEALTH_FOR_ALL = false;
/*     */   private static final boolean SHOW_WANTS_GOLEM_FOR_ALL = true;
/*     */   private static final boolean SHOW_ANGER_LEVEL_FOR_ALL = false;
/*     */   private static final boolean SHOW_NAME_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_PROFESSION_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_BEHAVIORS_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_ACTIVITIES_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_MEMORIES_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_INVENTORY_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_GOSSIPS_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_HEALTH_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_WANTS_GOLEM_FOR_SELECTED = true;
/*     */   private static final boolean SHOW_ANGER_LEVEL_FOR_SELECTED = true;
/*     */   private static final int MAX_RENDER_DIST_FOR_BRAIN_INFO = 30;
/*     */   private static final int MAX_TARGETING_DIST = 8;
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   private static final int CYAN = -16711681;
/*     */   private static final int GRAY = -3355444;
/*     */   private static final int PINK = -98404;
/*     */   private static final int ORANGE = -23296;
/*     */   private final Minecraft minecraft;
/*     */   private UUID lastLookedAtUuid;
/*     */   
/*     */   public BrainDebugRenderer(Minecraft minecraft) {
/*  70 */     this.minecraft = minecraft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  75 */     doRender(debugValues);
/*     */     
/*  77 */     if (!this.minecraft.player.isSpectator()) {
/*  78 */       updateLastLookedAtUuid();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doRender(DebugValueAccess debugValues) {
/*  83 */     debugValues.forEachEntity(DebugSubscriptions.BRAINS, (entity, brainDump) -> {
/*     */           if (this.minecraft.player.closerThan(entity, 30.0D)) {
/*     */             renderBrainInfo(entity, brainDump);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void renderBrainInfo(Entity entity, DebugBrainDump brainDump) {
/*  91 */     boolean selected = isMobSelected(entity);
/*  92 */     int row = 0;
/*     */ 
/*     */     
/*  95 */     Gizmos.billboardTextOverMob(entity, row, brainDump.name(), -1, 0.48F);
/*  96 */     row++;
/*     */ 
/*     */     
/*  99 */     if (selected) {
/* 100 */       Gizmos.billboardTextOverMob(entity, row, brainDump.profession() + " " + brainDump.profession() + " xp", -1, 0.32F);
/* 101 */       row++;
/*     */     } 
/*     */     
/* 104 */     if (selected) {
/* 105 */       int color = (brainDump.health() < brainDump.maxHealth()) ? -23296 : -1;
/* 106 */       Gizmos.billboardTextOverMob(entity, row, "health: " + String.format(Locale.ROOT, "%.1f", new Object[] { brainDump.health() }) + " / " + String.format(Locale.ROOT, "%.1f", new Object[] { brainDump.maxHealth() }), color, 0.32F);
/* 107 */       row++;
/*     */     } 
/*     */     
/* 110 */     if (selected && 
/* 111 */       !brainDump.inventory().equals("")) {
/* 112 */       Gizmos.billboardTextOverMob(entity, row, brainDump.inventory(), -98404, 0.32F);
/* 113 */       row++;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     if (selected) {
/* 118 */       for (String goal : (Iterable<String>)brainDump.behaviors()) {
/* 119 */         Gizmos.billboardTextOverMob(entity, row, goal, -16711681, 0.32F);
/* 120 */         row++;
/*     */       } 
/*     */     }
/* 123 */     if (selected) {
/* 124 */       for (String activity : (Iterable<String>)brainDump.activities()) {
/* 125 */         Gizmos.billboardTextOverMob(entity, row, activity, -16711936, 0.32F);
/* 126 */         row++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (brainDump.wantsGolem()) {
/* 132 */       Gizmos.billboardTextOverMob(entity, row, "Wants Golem", -23296, 0.32F);
/* 133 */       row++;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     if (selected && 
/* 138 */       brainDump.angerLevel() != -1) {
/* 139 */       Gizmos.billboardTextOverMob(entity, row, "Anger Level: " + brainDump.angerLevel(), -98404, 0.32F);
/* 140 */       row++;
/*     */     } 
/*     */ 
/*     */     
/* 144 */     if (selected) {
/* 145 */       for (String gossip : (Iterable<String>)brainDump.gossips()) {
/* 146 */         if (gossip.startsWith(brainDump.name())) {
/* 147 */           Gizmos.billboardTextOverMob(entity, row, gossip, -1, 0.32F);
/*     */         } else {
/* 149 */           Gizmos.billboardTextOverMob(entity, row, gossip, -23296, 0.32F);
/*     */         } 
/* 151 */         row++;
/*     */       } 
/*     */     }
/*     */     
/* 155 */     if (selected) {
/* 156 */       for (String memory : (Iterable<String>)Lists.reverse(brainDump.memories())) {
/* 157 */         Gizmos.billboardTextOverMob(entity, row, memory, -3355444, 0.32F);
/* 158 */         row++;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isMobSelected(Entity entity) {
/* 164 */     return Objects.equals(this.lastLookedAtUuid, entity.getUUID());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<BlockPos, List<String>> getGhostPois(DebugValueAccess debugValues) {
/* 172 */     Map<BlockPos, List<String>> ghostPois = Maps.newHashMap();
/* 173 */     debugValues.forEachEntity(DebugSubscriptions.BRAINS, (entity, brainDump) -> {
/*     */           for (BlockPos poiPos : (Iterable<BlockPos>)Iterables.concat(brainDump.pois(), brainDump.potentialPois())) {
/*     */             ((List<String>)ghostPois.computeIfAbsent(poiPos, ())).add(brainDump.name());
/*     */           }
/*     */         });
/* 178 */     return ghostPois;
/*     */   }
/*     */   
/*     */   private void updateLastLookedAtUuid() {
/* 182 */     DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(entity -> this.lastLookedAtUuid = entity.getUUID());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/BrainDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */