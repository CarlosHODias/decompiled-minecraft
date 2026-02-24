/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GameTestBlockHighlightRenderer
/*    */ {
/*    */   private static final int SHOW_POS_DURATION_MS = 10000;
/*    */   private static final float PADDING = 0.02F;
/* 17 */   private final Map<BlockPos, Marker> markers = Maps.newHashMap();
/*    */   private static final class Marker extends Record { private final int color; private final String text; private final long removeAtTime;
/* 19 */     private Marker(int color, String text, long removeAtTime) { this.color = color; this.text = text; this.removeAtTime = removeAtTime; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 19 */       //   0	7	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker; } public int color() { return this.color; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;
/* 19 */       //   0	8	1	o	Ljava/lang/Object; } public String text() { return this.text; } public long removeAtTime() { return this.removeAtTime; }
/*    */      }
/*    */   
/*    */   public void highlightPos(BlockPos absolutePos, BlockPos relativePos) {
/* 23 */     String text = relativePos.toShortString();
/* 24 */     this.markers.put(absolutePos, new Marker(1610678016, text, Util.getMillis() + 10000L));
/*    */   }
/*    */   
/*    */   public void clear() {
/* 28 */     this.markers.clear();
/*    */   }
/*    */   
/*    */   public void emitGizmos() {
/* 32 */     long time = Util.getMillis();
/* 33 */     this.markers.entrySet().removeIf(entry -> (time > ((Marker)entry.getValue()).removeAtTime));
/* 34 */     this.markers.forEach((pos, marker) -> renderMarker(pos, marker));
/*    */   }
/*    */   
/*    */   private void renderMarker(BlockPos pos, Marker marker) {
/* 38 */     Gizmos.cuboid(pos, 0.02F, GizmoStyle.fill(marker.color()));
/* 39 */     if (!marker.text.isEmpty())
/* 40 */       Gizmos.billboardText(marker.text, Vec3.atLowerCornerWithOffset((Vec3i)pos, 0.5D, 1.2D, 0.5D), TextGizmo.Style.whiteAndCentered().withScale(0.16F)).setAlwaysOnTop(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */