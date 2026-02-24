/*   */ package net.minecraft.client.renderer.state;public final class BlockOutlineRenderState extends Record { private final net.minecraft.core.BlockPos pos; private final boolean isTranslucent; private final boolean highContrast;
/*   */   private final net.minecraft.world.phys.shapes.VoxelShape shape;
/*   */   private final net.minecraft.world.phys.shapes.VoxelShape collisionShape;
/*   */   private final net.minecraft.world.phys.shapes.VoxelShape occlusionShape;
/*   */   private final net.minecraft.world.phys.shapes.VoxelShape interactionShape;
/*   */   
/* 7 */   public net.minecraft.world.phys.shapes.VoxelShape interactionShape() { return this.interactionShape; } public net.minecraft.world.phys.shapes.VoxelShape occlusionShape() { return this.occlusionShape; } public net.minecraft.world.phys.shapes.VoxelShape collisionShape() { return this.collisionShape; } public net.minecraft.world.phys.shapes.VoxelShape shape() { return this.shape; } public boolean highContrast() { return this.highContrast; } public boolean isTranslucent() { return this.isTranslucent; } public net.minecraft.core.BlockPos pos() { return this.pos; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;
/* 7 */     //   0	8	1	o	Ljava/lang/Object; } public BlockOutlineRenderState(net.minecraft.core.BlockPos pos, boolean isTranslucent, boolean highContrast, net.minecraft.world.phys.shapes.VoxelShape shape, net.minecraft.world.phys.shapes.VoxelShape collisionShape, net.minecraft.world.phys.shapes.VoxelShape occlusionShape, net.minecraft.world.phys.shapes.VoxelShape interactionShape) { this.pos = pos; this.isTranslucent = isTranslucent; this.highContrast = highContrast; this.shape = shape; this.collisionShape = collisionShape; this.occlusionShape = occlusionShape; this.interactionShape = interactionShape; }
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/BlockOutlineRenderState; }
/*   */   public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/BlockOutlineRenderState;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 9 */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/BlockOutlineRenderState; } public BlockOutlineRenderState(net.minecraft.core.BlockPos pos, boolean isTranslucent, boolean highContrast, net.minecraft.world.phys.shapes.VoxelShape shape) { this(pos, isTranslucent, highContrast, shape, null, null, null); }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/BlockOutlineRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */