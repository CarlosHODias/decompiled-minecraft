/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public final class VibrationInfo extends Record {
/*    */   private final net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent;
/*    */   private final float distance;
/*    */   private final net.minecraft.world.phys.Vec3 pos;
/*    */   private final UUID uuid;
/*    */   private final UUID projectileOwnerUuid;
/*    */   private final Entity entity;
/*    */   public static final com.mojang.serialization.Codec<VibrationInfo> CODEC;
/*    */   
/* 17 */   public VibrationInfo(net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent, float distance, net.minecraft.world.phys.Vec3 pos, UUID uuid, UUID projectileOwnerUuid, Entity entity) { this.gameEvent = gameEvent; this.distance = distance; this.pos = pos; this.uuid = uuid; this.projectileOwnerUuid = projectileOwnerUuid; this.entity = entity; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo; } public net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent() { return this.gameEvent; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationInfo;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public float distance() { return this.distance; } public net.minecraft.world.phys.Vec3 pos() { return this.pos; } public UUID uuid() { return this.uuid; } public UUID projectileOwnerUuid() { return this.projectileOwnerUuid; } public Entity entity() { return this.entity; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.level.gameevent.GameEvent.CODEC.fieldOf("game_event").forGetter(VibrationInfo::gameEvent), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, Float.MAX_VALUE).fieldOf("distance").forGetter(VibrationInfo::distance), (com.mojang.datafixers.kinds.App)net.minecraft.world.phys.Vec3.CODEC.fieldOf("pos").forGetter(VibrationInfo::pos), (com.mojang.datafixers.kinds.App)net.minecraft.core.UUIDUtil.CODEC.lenientOptionalFieldOf("source").forGetter(()), (com.mojang.datafixers.kinds.App)net.minecraft.core.UUIDUtil.CODEC.lenientOptionalFieldOf("projectile_owner").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VibrationInfo(net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent, float distance, net.minecraft.world.phys.Vec3 pos, UUID uuid, UUID projectileOwnerUuid) {
/* 34 */     this(gameEvent, distance, pos, uuid, projectileOwnerUuid, null);
/*    */   }
/*    */   
/*    */   public VibrationInfo(net.minecraft.core.Holder<net.minecraft.world.level.gameevent.GameEvent> gameEvent, float distance, net.minecraft.world.phys.Vec3 pos, Entity entity) {
/* 38 */     this(gameEvent, distance, pos, (entity == null) ? null : entity.getUUID(), getProjectileOwner(entity), entity);
/*    */   }
/*    */   
/*    */   private static UUID getProjectileOwner(Entity entity) {
/* 42 */     if (entity instanceof net.minecraft.world.entity.projectile.Projectile) { net.minecraft.world.entity.projectile.Projectile projectile = (net.minecraft.world.entity.projectile.Projectile)entity; if (projectile.getOwner() != null)
/* 43 */         return projectile.getOwner().getUUID();  }
/*    */     
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   public Optional<Entity> getEntity(ServerLevel level) {
/* 49 */     return Optional.<Entity>ofNullable(this.entity).or(() -> {
/*    */           java.util.Objects.requireNonNull(level);
/*    */           return Optional.<UUID>ofNullable(this.uuid).map(level::getEntity);
/*    */         }); } public Optional<Entity> getProjectileOwner(ServerLevel level) {
/* 53 */     return getEntity(level)
/* 54 */       .filter(e -> e instanceof net.minecraft.world.entity.projectile.Projectile)
/* 55 */       .map(e -> (net.minecraft.world.entity.projectile.Projectile)e)
/* 56 */       .map(net.minecraft.world.entity.projectile.Projectile::getOwner)
/* 57 */       .or(() -> {
/*    */           java.util.Objects.requireNonNull(level);
/*    */           return Optional.<UUID>ofNullable(this.projectileOwnerUuid).map(level::getEntity);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/vibrations/VibrationInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */