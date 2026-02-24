/*    */ package net.minecraft.client.waypoints;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*    */ import net.minecraft.world.waypoints.TrackedWaypointManager;
/*    */ import net.minecraft.world.waypoints.Waypoint;
/*    */ 
/*    */ public class ClientWaypointManager implements TrackedWaypointManager {
/* 15 */   private final Map<Either<UUID, String>, TrackedWaypoint> waypoints = new ConcurrentHashMap<>();
/*    */ 
/*    */   
/*    */   public void trackWaypoint(TrackedWaypoint waypoint) {
/* 19 */     this.waypoints.put(waypoint.id(), waypoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateWaypoint(TrackedWaypoint waypoint) {
/* 24 */     ((TrackedWaypoint)this.waypoints.get(waypoint.id())).update(waypoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public void untrackWaypoint(TrackedWaypoint waypoint) {
/* 29 */     this.waypoints.remove(waypoint.id());
/*    */   }
/*    */   
/*    */   public boolean hasWaypoints() {
/* 33 */     return !this.waypoints.isEmpty();
/*    */   }
/*    */   
/*    */   public void forEachWaypoint(Entity fromEntity, Consumer<TrackedWaypoint> consumer) {
/* 37 */     this.waypoints.values().stream()
/* 38 */       .sorted(Comparator.<TrackedWaypoint>comparingDouble(waypoint -> waypoint.distanceSquared(fromEntity)).reversed())
/* 39 */       .forEachOrdered(consumer);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/waypoints/ClientWaypointManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */