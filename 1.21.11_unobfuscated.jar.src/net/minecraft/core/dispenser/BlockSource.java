/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ public final class BlockSource extends Record {
/*    */   private final net.minecraft.server.level.ServerLevel level;
/*    */   private final net.minecraft.core.BlockPos pos;
/*    */   private final net.minecraft.world.level.block.state.BlockState state;
/*    */   private final net.minecraft.world.level.block.entity.DispenserBlockEntity blockEntity;
/*    */   
/*  9 */   public BlockSource(net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.block.entity.DispenserBlockEntity blockEntity) { this.level = level; this.pos = pos; this.state = state; this.blockEntity = blockEntity; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/dispenser/BlockSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/core/dispenser/BlockSource; } public net.minecraft.server.level.ServerLevel level() { return this.level; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/dispenser/BlockSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/dispenser/BlockSource; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/dispenser/BlockSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/dispenser/BlockSource;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.BlockPos pos() { return this.pos; } public net.minecraft.world.level.block.state.BlockState state() { return this.state; } public net.minecraft.world.level.block.entity.DispenserBlockEntity blockEntity() { return this.blockEntity; }
/*    */    public net.minecraft.world.phys.Vec3 center() {
/* 11 */     return this.pos.getCenter();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/dispenser/BlockSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */