package net.minecraft.world.waypoints;

public interface WaypointManager<T extends Waypoint> {
  void trackWaypoint(T paramT);
  
  void updateWaypoint(T paramT);
  
  void untrackWaypoint(T paramT);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/waypoints/WaypointManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */