/*    */ package net.minecraft.gizmos;
/*    */ public final class ArrowGizmo extends Record implements Gizmo {
/*    */   private final net.minecraft.world.phys.Vec3 start;
/*    */   private final net.minecraft.world.phys.Vec3 end;
/*    */   private final int color;
/*    */   private final float width;
/*    */   public static final float DEFAULT_WIDTH = 2.5F;
/*    */   
/*  9 */   public ArrowGizmo(net.minecraft.world.phys.Vec3 start, net.minecraft.world.phys.Vec3 end, int color, float width) { this.start = start; this.end = end; this.color = color; this.width = width; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/gizmos/ArrowGizmo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/gizmos/ArrowGizmo; } public net.minecraft.world.phys.Vec3 start() { return this.start; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gizmos/ArrowGizmo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/gizmos/ArrowGizmo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/gizmos/ArrowGizmo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/gizmos/ArrowGizmo;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.phys.Vec3 end() { return this.end; } public int color() { return this.color; } public float width() { return this.width; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void emit(GizmoPrimitives primitives, float alphaMultiplier) {
/* 14 */     int color = net.minecraft.util.ARGB.multiplyAlpha(this.color, alphaMultiplier);
/*    */     
/* 16 */     primitives.addLine(this.start, this.end, color, this.width);
/*    */ 
/*    */     
/* 19 */     org.joml.Quaternionf rotation = new org.joml.Quaternionf().rotationTo((org.joml.Vector3fc)new org.joml.Vector3f(1.0F, 0.0F, 0.0F), (org.joml.Vector3fc)this.end.subtract(this.start).toVector3f().normalize());
/* 20 */     float len = (float)net.minecraft.util.Mth.clamp(this.end.distanceTo(this.start) * 0.10000000149011612D, 0.10000000149011612D, 1.0D);
/*    */     
/* 22 */     org.joml.Vector3f[] tips = { rotation.transform(-len, len, 0.0F, new org.joml.Vector3f()), 
/* 23 */         rotation.transform(-len, 0.0F, len, new org.joml.Vector3f()), 
/* 24 */         rotation.transform(-len, -len, 0.0F, new org.joml.Vector3f()), 
/* 25 */         rotation.transform(-len, 0.0F, -len, new org.joml.Vector3f()) };
/*    */     
/* 27 */     for (org.joml.Vector3f tip : tips)
/* 28 */       primitives.addLine(this.end.add(tip.x, tip.y, tip.z), this.end, color, this.width); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gizmos/ArrowGizmo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */