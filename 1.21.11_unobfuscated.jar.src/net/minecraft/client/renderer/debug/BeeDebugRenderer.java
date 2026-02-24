/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.debug.DebugBeeInfo;
/*     */ import net.minecraft.util.debug.DebugGoalInfo;
/*     */ import net.minecraft.util.debug.DebugHiveInfo;
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
/*     */ public class BeeDebugRenderer
/*     */   implements DebugRenderer.SimpleDebugRenderer
/*     */ {
/*     */   private static final boolean SHOW_GOAL_FOR_ALL_BEES = true;
/*     */   private static final boolean SHOW_NAME_FOR_ALL_BEES = true;
/*     */   private static final boolean SHOW_HIVE_FOR_ALL_BEES = true;
/*     */   private static final boolean SHOW_FLOWER_POS_FOR_ALL_BEES = true;
/*     */   private static final boolean SHOW_TRAVEL_TICKS_FOR_ALL_BEES = true;
/*     */   private static final boolean SHOW_GOAL_FOR_SELECTED_BEE = true;
/*     */   private static final boolean SHOW_NAME_FOR_SELECTED_BEE = true;
/*     */   private static final boolean SHOW_HIVE_FOR_SELECTED_BEE = true;
/*     */   private static final boolean SHOW_FLOWER_POS_FOR_SELECTED_BEE = true;
/*     */   private static final boolean SHOW_TRAVEL_TICKS_FOR_SELECTED_BEE = true;
/*     */   private static final boolean SHOW_HIVE_MEMBERS = true;
/*     */   private static final boolean SHOW_BLACKLISTS = true;
/*     */   private static final int MAX_RENDER_DIST_FOR_HIVE_OVERLAY = 30;
/*     */   private static final int MAX_RENDER_DIST_FOR_BEE_OVERLAY = 30;
/*     */   private static final int MAX_TARGETING_DIST = 8;
/*     */   private static final float TEXT_SCALE = 0.32F;
/*     */   private static final int ORANGE = -23296;
/*     */   private static final int GRAY = -3355444;
/*     */   private static final int PINK = -98404;
/*     */   private final Minecraft minecraft;
/*     */   private UUID lastLookedAtUuid;
/*     */   
/*     */   public BeeDebugRenderer(Minecraft minecraft) {
/*  68 */     this.minecraft = minecraft;
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/*  73 */     doRender(debugValues);
/*     */     
/*  75 */     if (!this.minecraft.player.isSpectator()) {
/*  76 */       updateLastLookedAtUuid();
/*     */     }
/*     */   }
/*     */   
/*     */   private void doRender(DebugValueAccess debugValues) {
/*  81 */     BlockPos playerPos = getCamera().blockPosition();
/*     */     
/*  83 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (this.minecraft.player.closerThan(debugValues, 30.0D)) {
/*     */             DebugGoalInfo goalInfo = (DebugGoalInfo)debugValues.getEntityValue(DebugSubscriptions.GOAL_SELECTORS, debugValues);
/*     */             
/*     */             renderBeeInfo(debugValues, beeInfo, goalInfo);
/*     */           } 
/*     */         });
/*     */     
/*  91 */     renderFlowerInfos(debugValues);
/*     */ 
/*     */ 
/*     */     
/*  95 */     Map<BlockPos, Set<UUID>> hiveBlacklistMap = createHiveBlacklistMap(debugValues);
/*     */     
/*  97 */     debugValues.forEachBlock(DebugSubscriptions.BEE_HIVES, (pos, hive) -> {
/*     */           if (playerPos.closerThan((Vec3i)playerPos, 30.0D)) {
/*     */             highlightHive(playerPos);
/*     */             
/*     */             Set<UUID> beesWhoBlacklistThisHive = (Set<UUID>)playerPos.getOrDefault(playerPos, Set.of());
/*     */             
/*     */             renderHiveInfo(playerPos, hive, beesWhoBlacklistThisHive, hiveBlacklistMap);
/*     */           } 
/*     */         });
/* 106 */     getGhostHives(debugValues).forEach((ghostHivePos, value) -> {
/*     */           if (playerPos.closerThan((Vec3i)playerPos, 30.0D)) {
/*     */             renderGhostHive(playerPos, value);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<BlockPos, Set<UUID>> createHiveBlacklistMap(DebugValueAccess debugValues) {
/* 118 */     Map<BlockPos, Set<UUID>> hiveBlacklistMap = new HashMap<>();
/* 119 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, bee) -> {
/*     */           for (BlockPos blacklistedFlowerPos : (Iterable<BlockPos>)bee.blacklistedHives()) {
/*     */             ((Set<UUID>)hiveBlacklistMap.computeIfAbsent(blacklistedFlowerPos, ())).add(entity.getUUID());
/*     */           }
/*     */         });
/* 124 */     return hiveBlacklistMap;
/*     */   }
/*     */   
/*     */   private void renderFlowerInfos(DebugValueAccess debugValues) {
/* 128 */     Map<BlockPos, Set<UUID>> beesPerFlower = new HashMap<>();
/*     */     
/* 130 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, bee) -> {
/*     */           if (bee.flowerPos().isPresent()) {
/*     */             ((Set<UUID>)beesPerFlower.computeIfAbsent(bee.flowerPos().get(), ())).add(entity.getUUID());
/*     */           }
/*     */         });
/*     */     
/* 136 */     beesPerFlower.forEach((flowerPos, beesWithThisFlower) -> {
/*     */           Set<String> beeNames = (Set<String>)beesWithThisFlower.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet());
/*     */           int row = 1;
/*     */           Gizmos.billboardTextOverBlock(beeNames.toString(), flowerPos, row++, -256, 0.32F);
/*     */           Gizmos.billboardTextOverBlock("Flower", flowerPos, row++, -1, 0.32F);
/*     */           Gizmos.cuboid(flowerPos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.8F, 0.8F, 0.0F)));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getBeeUuidsAsString(Collection<UUID> uuids) {
/* 148 */     if (uuids.isEmpty())
/* 149 */       return "-"; 
/* 150 */     if (uuids.size() > 3) {
/* 151 */       return "" + uuids.size() + " bees";
/*     */     }
/* 153 */     return ((Set)uuids.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet())).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void highlightHive(BlockPos hivePos) {
/* 158 */     float padding = 0.05F;
/* 159 */     Gizmos.cuboid(hivePos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/*     */   }
/*     */   
/*     */   private void renderGhostHive(BlockPos ghostHivePos, List<String> hiveMemberNames) {
/* 163 */     float padding = 0.05F;
/* 164 */     Gizmos.cuboid(ghostHivePos, 0.05F, GizmoStyle.fill(ARGB.colorFromFloat(0.3F, 0.2F, 0.2F, 1.0F)));
/* 165 */     Gizmos.billboardTextOverBlock(hiveMemberNames.toString(), ghostHivePos, 0, -256, 0.32F);
/* 166 */     Gizmos.billboardTextOverBlock("Ghost Hive", ghostHivePos, 1, -65536, 0.32F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderHiveInfo(BlockPos hivePos, DebugHiveInfo hive, Collection<UUID> beesWhoBlacklistThisHive, DebugValueAccess debugValues) {
/* 172 */     int row = 0;
/*     */     
/* 174 */     if (!beesWhoBlacklistThisHive.isEmpty()) {
/* 175 */       renderTextOverHive("Blacklisted by " + getBeeUuidsAsString(beesWhoBlacklistThisHive), hivePos, row++, -65536);
/*     */     }
/*     */     
/* 178 */     renderTextOverHive("Out: " + getBeeUuidsAsString(getHiveMembers(hivePos, debugValues)), hivePos, row++, -3355444);
/*     */     
/* 180 */     if (hive.occupantCount() == 0) {
/* 181 */       renderTextOverHive("In: -", hivePos, row++, -256);
/* 182 */     } else if (hive.occupantCount() == 1) {
/* 183 */       renderTextOverHive("In: 1 bee", hivePos, row++, -256);
/*     */     } else {
/* 185 */       renderTextOverHive("In: " + hive.occupantCount() + " bees", hivePos, row++, -256);
/*     */     } 
/*     */     
/* 188 */     renderTextOverHive("Honey: " + hive.honeyLevel(), hivePos, row++, -23296);
/*     */     
/* 190 */     renderTextOverHive(hive.type().getName().getString() + hive.type().getName().getString(), hivePos, row++, -1);
/*     */   }
/*     */   
/*     */   private void renderBeeInfo(Entity entity, DebugBeeInfo beeInfo, DebugGoalInfo goalInfo) {
/* 194 */     boolean selected = isBeeSelected(entity);
/*     */ 
/*     */     
/* 197 */     int row = 0;
/*     */ 
/*     */     
/* 200 */     Gizmos.billboardTextOverMob(entity, row++, beeInfo.toString(), -1, 0.48F);
/*     */ 
/*     */ 
/*     */     
/* 204 */     if (beeInfo.hivePos().isEmpty()) {
/* 205 */       Gizmos.billboardTextOverMob(entity, row++, "No hive", -98404, 0.32F);
/*     */     } else {
/* 207 */       Gizmos.billboardTextOverMob(entity, row++, "Hive: " + getPosDescription(entity, beeInfo.hivePos().get()), -256, 0.32F);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 212 */     if (beeInfo.flowerPos().isEmpty()) {
/* 213 */       Gizmos.billboardTextOverMob(entity, row++, "No flower", -98404, 0.32F);
/*     */     } else {
/* 215 */       Gizmos.billboardTextOverMob(entity, row++, "Flower: " + getPosDescription(entity, beeInfo.flowerPos().get()), -256, 0.32F);
/*     */     } 
/*     */ 
/*     */     
/* 219 */     if (goalInfo != null) {
/* 220 */       for (DebugGoalInfo.DebugGoal goal : (Iterable<DebugGoalInfo.DebugGoal>)goalInfo.goals()) {
/* 221 */         if (goal.isRunning()) {
/* 222 */           Gizmos.billboardTextOverMob(entity, row++, goal.name(), -16711936, 0.32F);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 228 */     if (beeInfo.travelTicks() > 0) {
/* 229 */       int color = (beeInfo.travelTicks() < 2400) ? -3355444 : -23296;
/* 230 */       Gizmos.billboardTextOverMob(entity, row++, "Travelling: " + beeInfo.travelTicks() + " ticks", color, 0.32F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void renderTextOverHive(String text, BlockPos hivePos, int row, int color) {
/* 236 */     Gizmos.billboardTextOverBlock(text, hivePos, row, color, 0.32F);
/*     */   }
/*     */   
/*     */   private Camera getCamera() {
/* 240 */     return this.minecraft.gameRenderer.getMainCamera();
/*     */   }
/*     */   
/*     */   private String getPosDescription(Entity entity, BlockPos pos) {
/* 244 */     double dist = pos.distToCenterSqr((Position)entity.position());
/* 245 */     double distRounded = Math.round(dist * 10.0D) / 10.0D;
/* 246 */     return pos.toShortString() + " (dist " + pos.toShortString() + ")";
/*     */   }
/*     */   
/*     */   private boolean isBeeSelected(Entity entity) {
/* 250 */     return Objects.equals(this.lastLookedAtUuid, entity.getUUID());
/*     */   }
/*     */   
/*     */   private Collection<UUID> getHiveMembers(BlockPos hivePos, DebugValueAccess debugValues) {
/* 254 */     Set<UUID> hiveMembers = new HashSet<>();
/* 255 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (beeInfo.hasHive(hivePos)) {
/*     */             hiveMembers.add(entity.getUUID());
/*     */           }
/*     */         });
/* 260 */     return hiveMembers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<BlockPos, List<String>> getGhostHives(DebugValueAccess debugValues) {
/* 268 */     Map<BlockPos, List<String>> ghostHives = new HashMap<>();
/* 269 */     debugValues.forEachEntity(DebugSubscriptions.BEES, (entity, beeInfo) -> {
/*     */           if (beeInfo.hivePos().isPresent() && debugValues.getBlockValue(DebugSubscriptions.BEE_HIVES, beeInfo.hivePos().get()) == null) {
/*     */             ((List<String>)ghostHives.computeIfAbsent(beeInfo.hivePos().get(), ())).add(DebugEntityNameGenerator.getEntityName(entity));
/*     */           }
/*     */         });
/*     */     
/* 275 */     return ghostHives;
/*     */   }
/*     */   
/*     */   private void updateLastLookedAtUuid() {
/* 279 */     DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(entity -> this.lastLookedAtUuid = entity.getUUID());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/BeeDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */