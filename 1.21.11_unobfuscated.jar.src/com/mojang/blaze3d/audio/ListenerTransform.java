/*   */ package com.mojang.blaze3d.audio;public final class ListenerTransform extends Record { private final net.minecraft.world.phys.Vec3 position;
/*   */   private final net.minecraft.world.phys.Vec3 forward;
/*   */   private final net.minecraft.world.phys.Vec3 up;
/*   */   
/* 5 */   public ListenerTransform(net.minecraft.world.phys.Vec3 position, net.minecraft.world.phys.Vec3 forward, net.minecraft.world.phys.Vec3 up) { this.position = position; this.forward = forward; this.up = up; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/audio/ListenerTransform;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform; } public net.minecraft.world.phys.Vec3 position() { return this.position; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/audio/ListenerTransform;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/audio/ListenerTransform;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/audio/ListenerTransform;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.phys.Vec3 forward() { return this.forward; } public net.minecraft.world.phys.Vec3 up() { return this.up; }
/* 6 */    public static final ListenerTransform INITIAL = new ListenerTransform(net.minecraft.world.phys.Vec3.ZERO, new net.minecraft.world.phys.Vec3(0.0D, 0.0D, -1.0D), new net.minecraft.world.phys.Vec3(0.0D, 1.0D, 0.0D));
/*   */   
/*   */   public net.minecraft.world.phys.Vec3 right() {
/* 9 */     return this.forward.cross(this.up);
/*   */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/audio/ListenerTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */