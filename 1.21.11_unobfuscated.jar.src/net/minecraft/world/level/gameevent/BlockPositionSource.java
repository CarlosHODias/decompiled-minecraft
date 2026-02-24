/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class BlockPositionSource extends Record implements PositionSource {
/*    */   private final BlockPos pos;
/*    */   public static final com.mojang.serialization.MapCodec<BlockPositionSource> CODEC;
/*    */   
/* 13 */   public BlockPositionSource(BlockPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource; } public BlockPos pos() { return this.pos; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(BlockPositionSource::pos)).apply((com.mojang.datafixers.kinds.Applicative)i, BlockPositionSource::new)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final StreamCodec<io.netty.buffer.ByteBuf, BlockPositionSource> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPositionSource::pos, BlockPositionSource::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public java.util.Optional<net.minecraft.world.phys.Vec3> getPosition(net.minecraft.world.level.Level level) {
/* 25 */     return java.util.Optional.of(net.minecraft.world.phys.Vec3.atCenterOf((net.minecraft.core.Vec3i)this.pos));
/*    */   }
/*    */ 
/*    */   
/*    */   public PositionSourceType<BlockPositionSource> getType() {
/* 30 */     return PositionSourceType.BLOCK;
/*    */   }
/*    */   
/*    */   public static class Type
/*    */     implements PositionSourceType<BlockPositionSource> {
/*    */     public com.mojang.serialization.MapCodec<BlockPositionSource> codec() {
/* 36 */       return BlockPositionSource.CODEC;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<io.netty.buffer.ByteBuf, BlockPositionSource> streamCodec() {
/* 41 */       return BlockPositionSource.STREAM_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/BlockPositionSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */