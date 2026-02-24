/*    */ package net.minecraft.client.renderer.state;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class WorldBorderRenderState
/*    */ {
/*    */   public double minX;
/*    */   public double maxX;
/*    */   public double minZ;
/*    */   public double maxZ;
/*    */   public int tint;
/*    */   public double alpha;
/*    */   
/*    */   public List<DistancePerDirection> closestBorder(double x, double z) {
/* 18 */     DistancePerDirection[] directions = { new DistancePerDirection(Direction.NORTH, z - this.minZ), new DistancePerDirection(Direction.SOUTH, this.maxZ - z), new DistancePerDirection(Direction.WEST, x - this.minX), new DistancePerDirection(Direction.EAST, this.maxX - x) };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     return Arrays.<DistancePerDirection>stream(directions).sorted(Comparator.comparingDouble(d -> d.distance)).toList();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 28 */     this.alpha = 0.0D;
/*    */   }
/*    */   public static final class DistancePerDirection extends Record { private final Direction direction; private final double distance;
/* 31 */     public DistancePerDirection(Direction direction, double distance) { this.direction = direction; this.distance = distance; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 31 */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection; } public Direction direction() { return this.direction; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/state/WorldBorderRenderState$DistancePerDirection;
/* 31 */       //   0	8	1	o	Ljava/lang/Object; } public double distance() { return this.distance; }
/*    */      }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/WorldBorderRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */