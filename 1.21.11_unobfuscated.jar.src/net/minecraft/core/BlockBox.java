/*    */ package net.minecraft.core;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class BlockBox extends Record implements Iterable<BlockPos> {
/*    */   private final BlockPos min;
/*    */   private final BlockPos max;
/*    */   
/* 10 */   public BlockPos min() { return this.min; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/BlockBox;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/BlockBox; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/BlockBox;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/BlockBox; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/BlockBox;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/BlockBox;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos max() { return this.max; }
/* 11 */    public static final net.minecraft.network.codec.StreamCodec<ByteBuf, BlockBox> STREAM_CODEC = new net.minecraft.network.codec.StreamCodec<ByteBuf, BlockBox>()
/*    */     {
/*    */       public BlockBox decode(ByteBuf input) {
/* 14 */         return new BlockBox(
/* 15 */             FriendlyByteBuf.readBlockPos(input), 
/* 16 */             FriendlyByteBuf.readBlockPos(input));
/*    */       }
/*    */ 
/*    */ 
/*    */       
/*    */       public void encode(ByteBuf output, BlockBox value) {
/* 22 */         FriendlyByteBuf.writeBlockPos(output, value.min());
/* 23 */         FriendlyByteBuf.writeBlockPos(output, value.max());
/*    */       }
/*    */     };
/*    */   
/*    */   public BlockBox(BlockPos min, BlockPos max) {
/* 28 */     this.min = BlockPos.min(min, max);
/* 29 */     this.max = BlockPos.max(min, max);
/*    */   }
/*    */   
/*    */   public static BlockBox of(BlockPos pos) {
/* 33 */     return new BlockBox(pos, pos);
/*    */   }
/*    */   
/*    */   public static BlockBox of(BlockPos a, BlockPos b) {
/* 37 */     return new BlockBox(a, b);
/*    */   }
/*    */   
/*    */   public BlockBox include(BlockPos pos) {
/* 41 */     return new BlockBox(BlockPos.min(this.min, pos), BlockPos.max(this.max, pos));
/*    */   }
/*    */   
/*    */   public boolean isBlock() {
/* 45 */     return this.min.equals(this.max);
/*    */   }
/*    */   
/*    */   public boolean contains(BlockPos pos) {
/* 49 */     return (pos.getX() >= this.min.getX() && pos.getY() >= this.min.getY() && pos.getZ() >= this.min.getZ() && 
/* 50 */       pos.getX() <= this.max.getX() && pos.getY() <= this.max.getY() && pos.getZ() <= this.max.getZ());
/*    */   }
/*    */   
/*    */   public net.minecraft.world.phys.AABB aabb() {
/* 54 */     return net.minecraft.world.phys.AABB.encapsulatingFullBlocks(this.min, this.max);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.Iterator<BlockPos> iterator() {
/* 59 */     return BlockPos.betweenClosed(this.min, this.max).iterator();
/*    */   }
/*    */   
/*    */   public int sizeX() {
/* 63 */     return this.max.getX() - this.min.getX() + 1;
/*    */   }
/*    */   
/*    */   public int sizeY() {
/* 67 */     return this.max.getY() - this.min.getY() + 1;
/*    */   }
/*    */   
/*    */   public int sizeZ() {
/* 71 */     return this.max.getZ() - this.min.getZ() + 1;
/*    */   }
/*    */   
/*    */   public BlockBox extend(Direction direction, int amount) {
/* 75 */     if (amount == 0) {
/* 76 */       return this;
/*    */     }
/* 78 */     if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
/* 79 */       return of(this.min, BlockPos.max(this.min, this.max.relative(direction, amount)));
/*    */     }
/* 81 */     return of(BlockPos.min(this.min.relative(direction, amount), this.max), this.max);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBox move(Direction direction, int amount) {
/* 86 */     if (amount == 0) {
/* 87 */       return this;
/*    */     }
/* 89 */     return new BlockBox(
/* 90 */         this.min.relative(direction, amount), 
/* 91 */         this.max.relative(direction, amount));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBox offset(Vec3i offset) {
/* 96 */     return new BlockBox(
/* 97 */         this.min.offset(offset), 
/* 98 */         this.max.offset(offset));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/BlockBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */