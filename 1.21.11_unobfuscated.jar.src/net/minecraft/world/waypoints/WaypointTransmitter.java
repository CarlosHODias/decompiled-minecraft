/*     */ package net.minecraft.world.waypoints;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WaypointTransmitter
/*     */   extends Waypoint
/*     */ {
/*     */   public static final int REALLY_FAR_DISTANCE = 332;
/*     */   
/*     */   boolean isTransmittingWaypoint();
/*     */   
/*     */   Optional<Connection> makeWaypointConnectionWith(ServerPlayer paramServerPlayer);
/*     */   
/*     */   Waypoint.Icon waypointIcon();
/*     */   
/*     */   static boolean doesSourceIgnoreReceiver(LivingEntity source, ServerPlayer receiver) {
/*  31 */     if (receiver.isSpectator()) {
/*  32 */       return false;
/*     */     }
/*  34 */     if (source.isSpectator() || source.hasIndirectPassenger((Entity)receiver)) {
/*  35 */       return true;
/*     */     }
/*  37 */     double broadcastRange = Math.min(
/*  38 */         source.getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE), 
/*  39 */         receiver.getAttributeValue(Attributes.WAYPOINT_RECEIVE_RANGE));
/*     */     
/*  41 */     return (source.distanceTo((Entity)receiver) >= broadcastRange);
/*     */   }
/*     */   
/*     */   static boolean isChunkVisible(ChunkPos chunkPos, ServerPlayer receiver) {
/*  45 */     return receiver.getChunkTrackingView().isInViewDistance(chunkPos.x, chunkPos.z);
/*     */   }
/*     */   
/*     */   static boolean isReallyFar(LivingEntity source, ServerPlayer receiver) {
/*  49 */     return (source.distanceTo((Entity)receiver) > 332.0F);
/*     */   }
/*     */   public static interface Connection {
/*     */     void connect();
/*     */     void disconnect();
/*     */     void update();
/*     */     boolean isBroken(); }
/*     */   public static interface BlockConnection extends Connection { default boolean isBroken() {
/*  57 */       return (distanceManhattan() > 1);
/*     */     }
/*     */     
/*     */     int distanceManhattan(); }
/*     */   
/*     */   public static class EntityBlockConnection implements BlockConnection { private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private BlockPos lastPosition;
/*     */     
/*     */     public EntityBlockConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/*  68 */       this.source = source;
/*  69 */       this.receiver = receiver;
/*  70 */       this.icon = icon;
/*  71 */       this.lastPosition = source.blockPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/*  76 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)this.lastPosition));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/*  81 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/*  86 */       BlockPos currentPosition = this.source.blockPosition();
/*  87 */       if (currentPosition.distManhattan((Vec3i)this.lastPosition) > 0) {
/*  88 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, (Vec3i)currentPosition));
/*  89 */         this.lastPosition = currentPosition;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int distanceManhattan() {
/*  95 */       return this.lastPosition.distManhattan((Vec3i)this.source.blockPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 100 */       return (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface ChunkConnection
/*     */     extends Connection {
/*     */     int distanceChessboard();
/*     */     
/*     */     default boolean isBroken() {
/* 109 */       return (distanceChessboard() > 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityChunkConnection implements ChunkConnection {
/*     */     private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private ChunkPos lastPosition;
/*     */     
/*     */     public EntityChunkConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 120 */       this.source = source;
/* 121 */       this.icon = icon;
/* 122 */       this.receiver = receiver;
/* 123 */       this.lastPosition = source.chunkPosition();
/*     */     }
/*     */ 
/*     */     
/*     */     public int distanceChessboard() {
/* 128 */       return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/* 133 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/* 138 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 143 */       ChunkPos currentPosition = this.source.chunkPosition();
/* 144 */       if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
/* 145 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition));
/* 146 */         this.lastPosition = currentPosition;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 152 */       if (super.isBroken() || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)) {
/* 153 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 157 */       return WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EntityAzimuthConnection implements Connection {
/*     */     private final LivingEntity source;
/*     */     private final Waypoint.Icon icon;
/*     */     private final ServerPlayer receiver;
/*     */     private float lastAngle;
/*     */     
/*     */     public EntityAzimuthConnection(LivingEntity source, Waypoint.Icon icon, ServerPlayer receiver) {
/* 168 */       this.source = source;
/* 169 */       this.icon = icon;
/* 170 */       this.receiver = receiver;
/*     */       
/* 172 */       Vec3 direction = receiver.position().subtract(source.position()).rotateClockwise90();
/* 173 */       this.lastAngle = (float)Mth.atan2(direction.z(), direction.x());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBroken() {
/* 178 */       return (WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver) || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver) || !WaypointTransmitter.isReallyFar(this.source, this.receiver));
/*     */     }
/*     */ 
/*     */     
/*     */     public void connect() {
/* 183 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle));
/*     */     }
/*     */ 
/*     */     
/*     */     public void disconnect() {
/* 188 */       this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void update() {
/* 193 */       Vec3 direction = this.receiver.position().subtract(this.source.position()).rotateClockwise90();
/* 194 */       float currentAngle = (float)Mth.atan2(direction.z(), direction.x());
/* 195 */       if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
/* 196 */         this.receiver.connection.send((Packet)ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle));
/* 197 */         this.lastAngle = currentAngle;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/waypoints/WaypointTransmitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */