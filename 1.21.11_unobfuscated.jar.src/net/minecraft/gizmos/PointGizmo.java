/*   */ package net.minecraft.gizmos;
/*   */ public final class PointGizmo extends Record implements Gizmo { private final net.minecraft.world.phys.Vec3 pos;
/*   */   private final int color;
/*   */   private final float size;
/*   */   
/* 6 */   public PointGizmo(net.minecraft.world.phys.Vec3 pos, int color, float size) { this.pos = pos; this.color = color; this.size = size; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/PointGizmo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/gizmos/PointGizmo; } public net.minecraft.world.phys.Vec3 pos() { return this.pos; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/PointGizmo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/gizmos/PointGizmo; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/PointGizmo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/gizmos/PointGizmo;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; } public float size() { return this.size; }
/*   */   
/*   */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 9 */     primitives.addPoint(this.pos, net.minecraft.util.ARGB.multiplyAlpha(this.color, alphaMultiplier), this.size);
/*   */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/PointGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */