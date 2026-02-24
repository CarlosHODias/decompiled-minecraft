/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ 
/*     */ public final class TeleportTransition extends Record {
/*     */   private final net.minecraft.server.level.ServerLevel newLevel;
/*     */   private final net.minecraft.world.phys.Vec3 position;
/*     */   private final net.minecraft.world.phys.Vec3 deltaMovement;
/*     */   private final float yRot;
/*     */   private final float xRot;
/*     */   private final boolean missingRespawnBlock;
/*     */   private final boolean asPassenger;
/*     */   private final java.util.Set<net.minecraft.world.entity.Relative> relatives;
/*     */   private final PostTeleportTransition postTeleportTransition;
/*     */   
/*  15 */   public TeleportTransition(net.minecraft.server.level.ServerLevel newLevel, net.minecraft.world.phys.Vec3 position, net.minecraft.world.phys.Vec3 deltaMovement, float yRot, float xRot, boolean missingRespawnBlock, boolean asPassenger, java.util.Set<net.minecraft.world.entity.Relative> relatives, PostTeleportTransition postTeleportTransition) { this.newLevel = newLevel; this.position = position; this.deltaMovement = deltaMovement; this.yRot = yRot; this.xRot = xRot; this.missingRespawnBlock = missingRespawnBlock; this.asPassenger = asPassenger; this.relatives = relatives; this.postTeleportTransition = postTeleportTransition; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/portal/TeleportTransition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  15 */     //   0	7	0	this	Lnet/minecraft/world/level/portal/TeleportTransition; } public net.minecraft.server.level.ServerLevel newLevel() { return this.newLevel; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/portal/TeleportTransition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/portal/TeleportTransition; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/portal/TeleportTransition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/portal/TeleportTransition;
/*  15 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.phys.Vec3 position() { return this.position; } public net.minecraft.world.phys.Vec3 deltaMovement() { return this.deltaMovement; } public float yRot() { return this.yRot; } public float xRot() { return this.xRot; } public boolean missingRespawnBlock() { return this.missingRespawnBlock; } public boolean asPassenger() { return this.asPassenger; } public java.util.Set<net.minecraft.world.entity.Relative> relatives() { return this.relatives; } public PostTeleportTransition postTeleportTransition() { return this.postTeleportTransition; }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PostTeleportTransition { void onTransition(net.minecraft.world.entity.Entity param1Entity);
/*     */     
/*     */     default PostTeleportTransition then(PostTeleportTransition postTeleportTransition) {
/*  22 */       return entity -> {
/*     */           onTransition(postTeleportTransition);
/*     */           postTeleportTransition.onTransition(postTeleportTransition);
/*     */         };
/*     */     } }
/*     */   public static final PostTeleportTransition DO_NOTHING = entity -> {
/*     */     
/*     */     };
/*  30 */   public static final PostTeleportTransition PLAY_PORTAL_SOUND = TeleportTransition::playPortalSound;
/*  31 */   public static final PostTeleportTransition PLACE_PORTAL_TICKET = TeleportTransition::placePortalTicket;
/*     */   
/*     */   private static void playPortalSound(net.minecraft.world.entity.Entity entity) {
/*  34 */     if (entity instanceof net.minecraft.server.level.ServerPlayer) { net.minecraft.server.level.ServerPlayer player = (net.minecraft.server.level.ServerPlayer)entity;
/*  35 */       player.connection.send((net.minecraft.network.protocol.Packet)new net.minecraft.network.protocol.game.ClientboundLevelEventPacket(1032, net.minecraft.core.BlockPos.ZERO, 0, false)); }
/*     */   
/*     */   }
/*     */   
/*     */   private static void placePortalTicket(net.minecraft.world.entity.Entity entity) {
/*  40 */     entity.placePortalTicket(net.minecraft.core.BlockPos.containing((net.minecraft.core.Position)entity.position()));
/*     */   }
/*     */   
/*     */   public TeleportTransition(net.minecraft.server.level.ServerLevel newLevel, net.minecraft.world.phys.Vec3 pos, net.minecraft.world.phys.Vec3 speed, float yRot, float xRot, PostTeleportTransition postTeleportTransition) {
/*  44 */     this(newLevel, pos, speed, yRot, xRot, java.util.Set.of(), postTeleportTransition);
/*     */   }
/*     */   
/*     */   public TeleportTransition(net.minecraft.server.level.ServerLevel newLevel, net.minecraft.world.phys.Vec3 pos, net.minecraft.world.phys.Vec3 speed, float yRot, float xRot, java.util.Set<net.minecraft.world.entity.Relative> relatives, PostTeleportTransition postTeleportTransition) {
/*  48 */     this(newLevel, pos, speed, yRot, xRot, false, false, relatives, postTeleportTransition);
/*     */   }
/*     */   
/*     */   public static TeleportTransition createDefault(net.minecraft.server.level.ServerPlayer player, PostTeleportTransition postTeleportTransition) {
/*  52 */     net.minecraft.server.level.ServerLevel newLevel = player.level().getServer().findRespawnDimension();
/*  53 */     net.minecraft.world.level.storage.LevelData.RespawnData respawnData = newLevel.getRespawnData();
/*  54 */     return new TeleportTransition(newLevel, findAdjustedSharedSpawnPos(newLevel, (net.minecraft.world.entity.Entity)player), net.minecraft.world.phys.Vec3.ZERO, respawnData.yaw(), respawnData.pitch(), false, false, java.util.Set.of(), postTeleportTransition);
/*     */   }
/*     */   
/*     */   public static TeleportTransition missingRespawnBlock(net.minecraft.server.level.ServerPlayer player, PostTeleportTransition postTeleportTransition) {
/*  58 */     net.minecraft.server.level.ServerLevel newLevel = player.level().getServer().findRespawnDimension();
/*  59 */     net.minecraft.world.level.storage.LevelData.RespawnData respawnData = newLevel.getRespawnData();
/*  60 */     return new TeleportTransition(newLevel, findAdjustedSharedSpawnPos(newLevel, (net.minecraft.world.entity.Entity)player), net.minecraft.world.phys.Vec3.ZERO, respawnData.yaw(), respawnData.pitch(), true, false, java.util.Set.of(), postTeleportTransition);
/*     */   }
/*     */   
/*     */   private static net.minecraft.world.phys.Vec3 findAdjustedSharedSpawnPos(net.minecraft.server.level.ServerLevel newLevel, net.minecraft.world.entity.Entity entity) {
/*  64 */     return entity.adjustSpawnLocation(newLevel, newLevel.getRespawnData().pos()).getBottomCenter();
/*     */   }
/*     */   
/*     */   public TeleportTransition withRotation(float yRot, float xRot) {
/*  68 */     return new TeleportTransition(
/*  69 */         newLevel(), 
/*  70 */         position(), 
/*  71 */         deltaMovement(), yRot, xRot, 
/*     */ 
/*     */         
/*  74 */         missingRespawnBlock(), 
/*  75 */         asPassenger(), 
/*  76 */         relatives(), 
/*  77 */         postTeleportTransition());
/*     */   }
/*     */   
/*     */   public TeleportTransition withPosition(net.minecraft.world.phys.Vec3 position) {
/*  81 */     return new TeleportTransition(
/*  82 */         newLevel(), position, 
/*     */         
/*  84 */         deltaMovement(), 
/*  85 */         yRot(), 
/*  86 */         xRot(), 
/*  87 */         missingRespawnBlock(), 
/*  88 */         asPassenger(), 
/*  89 */         relatives(), 
/*  90 */         postTeleportTransition());
/*     */   }
/*     */   
/*     */   public TeleportTransition transitionAsPassenger() {
/*  94 */     return new TeleportTransition(
/*  95 */         newLevel(), 
/*  96 */         position(), 
/*  97 */         deltaMovement(), 
/*  98 */         yRot(), 
/*  99 */         xRot(), 
/* 100 */         missingRespawnBlock(), true, 
/*     */         
/* 102 */         relatives(), 
/* 103 */         postTeleportTransition());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/portal/TeleportTransition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */