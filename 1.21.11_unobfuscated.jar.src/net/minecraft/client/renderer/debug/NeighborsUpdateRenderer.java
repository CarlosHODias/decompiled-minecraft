/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class NeighborsUpdateRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 20 */     int shrinkTime = DebugSubscriptions.NEIGHBOR_UPDATES.expireAfterTicks();
/* 21 */     double shrinkSpeed = 1.0D / (shrinkTime * 2);
/*    */     
/* 23 */     Map<BlockPos, LastUpdate> lastUpdates = new HashMap<>();
/*    */     
/* 25 */     debugValues.forEachEvent(DebugSubscriptions.NEIGHBOR_UPDATES, (blockPos, remainingTicks, totalLifetime) -> {
/*    */           long age = (totalLifetime - remainingTicks);
/*    */           
/*    */           LastUpdate lastUpdate = (LastUpdate)lastUpdates.getOrDefault(blockPos, LastUpdate.NONE);
/*    */           lastUpdates.put(blockPos, lastUpdate.tryCount((int)age));
/*    */         });
/* 31 */     for (Map.Entry<BlockPos, LastUpdate> entry : lastUpdates.entrySet()) {
/* 32 */       BlockPos pos = entry.getKey();
/* 33 */       LastUpdate lastUpdate = entry.getValue();
/* 34 */       AABB aabb = new AABB(pos).inflate(0.002D).deflate(shrinkSpeed * lastUpdate.age);
/* 35 */       Gizmos.cuboid(aabb, GizmoStyle.stroke(-1));
/*    */     } 
/*    */     
/* 38 */     for (Map.Entry<BlockPos, LastUpdate> entry : lastUpdates.entrySet()) {
/* 39 */       BlockPos pos = entry.getKey();
/* 40 */       LastUpdate lastUpdate = entry.getValue();
/* 41 */       Gizmos.billboardText(String.valueOf(lastUpdate.count), Vec3.atCenterOf((Vec3i)pos), TextGizmo.Style.whiteAndCentered());
/*    */     } 
/*    */   }
/*    */   private static final class LastUpdate extends Record { private final int count; private final int age;
/* 45 */     private LastUpdate(int count, int age) { this.count = count; this.age = age; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 45 */       //   0	7	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate; } public int count() { return this.count; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #45	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;
/* 45 */       //   0	8	1	o	Ljava/lang/Object; } public int age() { return this.age; }
/* 46 */      private static final LastUpdate NONE = new LastUpdate(0, Integer.MAX_VALUE);
/*    */ 
/*    */     
/*    */     public LastUpdate tryCount(int age) {
/* 50 */       if (age == this.age) {
/* 51 */         return new LastUpdate(this.count + 1, age);
/*    */       }
/*    */       
/* 54 */       if (age < this.age) {
/* 55 */         return new LastUpdate(1, age);
/*    */       }
/* 57 */       return this;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/NeighborsUpdateRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */