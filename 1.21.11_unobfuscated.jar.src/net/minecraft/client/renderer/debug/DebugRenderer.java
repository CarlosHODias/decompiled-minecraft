/*     */ package net.minecraft.client.renderer.debug;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.util.debug.DebugValueAccess;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DebugRenderer
/*     */ {
/*  23 */   private final List<SimpleDebugRenderer> renderers = new ArrayList<>();
/*     */   private long lastDebugEntriesVersion;
/*     */   
/*     */   public DebugRenderer() {
/*  27 */     refreshRendererList();
/*     */   }
/*     */   
/*     */   public void refreshRendererList() {
/*  31 */     Minecraft minecraft = Minecraft.getInstance();
/*  32 */     this.renderers.clear();
/*     */     
/*  34 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_BORDERS)) {
/*  35 */       this.renderers.add(new ChunkBorderRenderer(minecraft));
/*     */     }
/*  37 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.CHUNK_SECTION_OCTREE)) {
/*  38 */       this.renderers.add(new OctreeDebugRenderer(minecraft));
/*     */     }
/*  40 */     if (SharedConstants.DEBUG_PATHFINDING) {
/*  41 */       this.renderers.add(new PathfindingRenderer());
/*     */     }
/*  43 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_WATER_LEVELS)) {
/*  44 */       this.renderers.add(new WaterDebugRenderer(minecraft));
/*     */     }
/*  46 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_HEIGHTMAP)) {
/*  47 */       this.renderers.add(new HeightMapRenderer(minecraft));
/*     */     }
/*  49 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_COLLISION_BOXES)) {
/*  50 */       this.renderers.add(new CollisionBoxRenderer(minecraft));
/*     */     }
/*  52 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_ENTITY_SUPPORTING_BLOCKS)) {
/*  53 */       this.renderers.add(new SupportBlockRenderer(minecraft));
/*     */     }
/*  55 */     if (SharedConstants.DEBUG_NEIGHBORSUPDATE) {
/*  56 */       this.renderers.add(new NeighborsUpdateRenderer());
/*     */     }
/*  58 */     if (SharedConstants.DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER) {
/*  59 */       this.renderers.add(new RedstoneWireOrientationsRenderer());
/*     */     }
/*  61 */     if (SharedConstants.DEBUG_STRUCTURES) {
/*  62 */       this.renderers.add(new StructureRenderer());
/*     */     }
/*  64 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_BLOCK_LIGHT_LEVELS) || minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_LEVELS)) {
/*  65 */       this.renderers.add(new LightDebugRenderer(minecraft, minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_BLOCK_LIGHT_LEVELS), minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_LEVELS)));
/*     */     }
/*  67 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SOLID_FACES)) {
/*  68 */       this.renderers.add(new SolidFaceRenderer(minecraft));
/*     */     }
/*  70 */     if (SharedConstants.DEBUG_VILLAGE_SECTIONS) {
/*  71 */       this.renderers.add(new VillageSectionsDebugRenderer());
/*     */     }
/*  73 */     if (SharedConstants.DEBUG_BRAIN) {
/*  74 */       this.renderers.add(new BrainDebugRenderer(minecraft));
/*     */     }
/*  76 */     if (SharedConstants.DEBUG_POI) {
/*  77 */       this.renderers.add(new PoiDebugRenderer(new BrainDebugRenderer(minecraft)));
/*     */     }
/*  79 */     if (SharedConstants.DEBUG_BEES) {
/*  80 */       this.renderers.add(new BeeDebugRenderer(minecraft));
/*     */     }
/*  82 */     if (SharedConstants.DEBUG_RAIDS) {
/*  83 */       this.renderers.add(new RaidDebugRenderer(minecraft));
/*     */     }
/*  85 */     if (SharedConstants.DEBUG_GOAL_SELECTOR) {
/*  86 */       this.renderers.add(new GoalSelectorDebugRenderer(minecraft));
/*     */     }
/*  88 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_CHUNKS_ON_SERVER)) {
/*  89 */       this.renderers.add(new ChunkDebugRenderer(minecraft));
/*     */     }
/*  91 */     if (SharedConstants.DEBUG_GAME_EVENT_LISTENERS) {
/*  92 */       this.renderers.add(new GameEventListenerRenderer());
/*     */     }
/*  94 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.VISUALIZE_SKY_LIGHT_SECTIONS)) {
/*  95 */       this.renderers.add(new LightSectionDebugRenderer(minecraft, LightLayer.SKY));
/*     */     }
/*  97 */     if (SharedConstants.DEBUG_BREEZE_MOB) {
/*  98 */       this.renderers.add(new BreezeDebugRenderer(minecraft));
/*     */     }
/* 100 */     if (SharedConstants.DEBUG_ENTITY_BLOCK_INTERSECTION) {
/* 101 */       this.renderers.add(new EntityBlockIntersectionDebugRenderer());
/*     */     }
/* 103 */     if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES)) {
/* 104 */       this.renderers.add(new EntityHitboxDebugRenderer(minecraft));
/*     */     }
/*     */     
/* 107 */     this.renderers.add(new ChunkCullingDebugRenderer(minecraft));
/*     */   }
/*     */   
/*     */   public void emitGizmos(Frustum frustum, double camX, double camY, double camZ, float partialTicks) {
/* 111 */     Minecraft minecraft = Minecraft.getInstance();
/* 112 */     DebugValueAccess debugValues = minecraft.getConnection().createDebugValueAccess();
/*     */     
/* 114 */     if (minecraft.debugEntries.getCurrentlyEnabledVersion() != this.lastDebugEntriesVersion) {
/* 115 */       this.lastDebugEntriesVersion = minecraft.debugEntries.getCurrentlyEnabledVersion();
/* 116 */       refreshRendererList();
/*     */     } 
/*     */     
/* 119 */     for (SimpleDebugRenderer renderer : this.renderers) {
/* 120 */       renderer.emitGizmos(camX, camY, camZ, debugValues, frustum, partialTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Optional<Entity> getTargetedEntity(Entity cameraEntity, int maxTargetingRange) {
/* 125 */     if (cameraEntity == null) {
/* 126 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     Vec3 from = cameraEntity.getEyePosition();
/* 132 */     Vec3 pick = cameraEntity.getViewVector(1.0F).scale(maxTargetingRange);
/* 133 */     Vec3 to = from.add(pick);
/*     */     
/* 135 */     AABB box = cameraEntity.getBoundingBox().expandTowards(pick).inflate(1.0D);
/*     */     
/* 137 */     int rangeSquared = maxTargetingRange * maxTargetingRange;
/* 138 */     EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, rangeSquared);
/* 139 */     if (hitResult == null) {
/* 140 */       return Optional.empty();
/*     */     }
/*     */     
/* 143 */     if (from.distanceToSqr(hitResult.getLocation()) > rangeSquared)
/*     */     {
/* 145 */       return Optional.empty();
/*     */     }
/*     */     
/* 148 */     return Optional.of(hitResult.getEntity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 mixColor(float hueShift) {
/*     */     // Byte code:
/*     */     //   0: ldc_w 5.99999
/*     */     //   3: fstore_1
/*     */     //   4: fload_0
/*     */     //   5: fconst_0
/*     */     //   6: fconst_1
/*     */     //   7: invokestatic clamp : (FFF)F
/*     */     //   10: ldc_w 5.99999
/*     */     //   13: fmul
/*     */     //   14: f2i
/*     */     //   15: istore_2
/*     */     //   16: fload_0
/*     */     //   17: ldc_w 5.99999
/*     */     //   20: fmul
/*     */     //   21: iload_2
/*     */     //   22: i2f
/*     */     //   23: fsub
/*     */     //   24: fstore_3
/*     */     //   25: iload_2
/*     */     //   26: tableswitch default -> 154, 0 -> 64, 1 -> 78, 2 -> 94, 3 -> 108, 4 -> 124, 5 -> 138
/*     */     //   64: new net/minecraft/world/phys/Vec3
/*     */     //   67: dup
/*     */     //   68: dconst_1
/*     */     //   69: fload_3
/*     */     //   70: f2d
/*     */     //   71: dconst_0
/*     */     //   72: invokespecial <init> : (DDD)V
/*     */     //   75: goto -> 168
/*     */     //   78: new net/minecraft/world/phys/Vec3
/*     */     //   81: dup
/*     */     //   82: fconst_1
/*     */     //   83: fload_3
/*     */     //   84: fsub
/*     */     //   85: f2d
/*     */     //   86: dconst_1
/*     */     //   87: dconst_0
/*     */     //   88: invokespecial <init> : (DDD)V
/*     */     //   91: goto -> 168
/*     */     //   94: new net/minecraft/world/phys/Vec3
/*     */     //   97: dup
/*     */     //   98: dconst_0
/*     */     //   99: dconst_1
/*     */     //   100: fload_3
/*     */     //   101: f2d
/*     */     //   102: invokespecial <init> : (DDD)V
/*     */     //   105: goto -> 168
/*     */     //   108: new net/minecraft/world/phys/Vec3
/*     */     //   111: dup
/*     */     //   112: dconst_0
/*     */     //   113: dconst_1
/*     */     //   114: fload_3
/*     */     //   115: f2d
/*     */     //   116: dsub
/*     */     //   117: dconst_1
/*     */     //   118: invokespecial <init> : (DDD)V
/*     */     //   121: goto -> 168
/*     */     //   124: new net/minecraft/world/phys/Vec3
/*     */     //   127: dup
/*     */     //   128: fload_3
/*     */     //   129: f2d
/*     */     //   130: dconst_0
/*     */     //   131: dconst_1
/*     */     //   132: invokespecial <init> : (DDD)V
/*     */     //   135: goto -> 168
/*     */     //   138: new net/minecraft/world/phys/Vec3
/*     */     //   141: dup
/*     */     //   142: dconst_1
/*     */     //   143: dconst_0
/*     */     //   144: dconst_1
/*     */     //   145: fload_3
/*     */     //   146: f2d
/*     */     //   147: dsub
/*     */     //   148: invokespecial <init> : (DDD)V
/*     */     //   151: goto -> 168
/*     */     //   154: new java/lang/IllegalStateException
/*     */     //   157: dup
/*     */     //   158: iload_2
/*     */     //   159: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*     */     //   164: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   167: athrow
/*     */     //   168: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #153	-> 0
/*     */     //   #154	-> 4
/*     */     //   #155	-> 16
/*     */     //   #156	-> 25
/*     */     //   #157	-> 64
/*     */     //   #158	-> 78
/*     */     //   #159	-> 94
/*     */     //   #160	-> 108
/*     */     //   #161	-> 124
/*     */     //   #162	-> 138
/*     */     //   #163	-> 154
/*     */     //   #156	-> 168
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	169	0	hueShift	F
/*     */     //   4	165	1	regions	F
/*     */     //   16	153	2	region	I
/*     */     //   25	144	3	progress	F
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 shiftHue(float r, float g, float b, float hs) {
/* 169 */     Vec3 rshifted = mixColor(hs).scale(r);
/* 170 */     Vec3 gshifted = mixColor((hs + 0.33333334F) % 1.0F).scale(g);
/* 171 */     Vec3 bshifted = mixColor((hs + 0.6666667F) % 1.0F).scale(b);
/* 172 */     Vec3 combined = rshifted.add(gshifted).add(bshifted);
/* 173 */     double max = Math.max(Math.max(1.0D, combined.x), Math.max(combined.y, combined.z));
/* 174 */     return new Vec3(combined.x / max, combined.y / max, combined.z / max);
/*     */   }
/*     */   
/*     */   public static interface SimpleDebugRenderer {
/*     */     void emitGizmos(double param1Double1, double param1Double2, double param1Double3, DebugValueAccess param1DebugValueAccess, Frustum param1Frustum, float param1Float);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/DebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */