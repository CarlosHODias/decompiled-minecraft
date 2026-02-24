/*    */ package net.minecraft.world.level.block.entity;public interface BoundingBoxRenderable { Mode renderMode();
/*    */   RenderableBox getRenderableBox();
/*    */   
/*    */   public static final class RenderableBox extends Record { private final net.minecraft.core.BlockPos localPos;
/*    */     private final net.minecraft.core.Vec3i size;
/*    */     
/*  7 */     public RenderableBox(net.minecraft.core.BlockPos localPos, net.minecraft.core.Vec3i size) { this.localPos = localPos; this.size = size; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*  7 */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox; } public net.minecraft.core.BlockPos localPos() { return this.localPos; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #7	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BoundingBoxRenderable$RenderableBox;
/*  7 */       //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Vec3i size() { return this.size; }
/*    */      public static RenderableBox fromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
/*  9 */       int x = Math.min(x1, x2);
/* 10 */       int y = Math.min(y1, y2);
/* 11 */       int z = Math.min(z1, z2);
/* 12 */       return new RenderableBox(new net.minecraft.core.BlockPos(x, y, z), new net.minecraft.core.Vec3i(Math.max(x1, x2) - x, Math.max(y1, y2) - y, Math.max(z1, z2) - z));
/*    */     } }
/*    */ 
/*    */   
/*    */   public enum Mode {
/* 17 */     NONE,
/* 18 */     BOX,
/* 19 */     BOX_AND_INVISIBLE_BLOCKS;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BoundingBoxRenderable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */