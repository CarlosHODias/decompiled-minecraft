/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class GlobalPos extends Record {
/*    */   private final ResourceKey<Level> dimension;
/*    */   private final BlockPos pos;
/*    */   public static final com.mojang.serialization.MapCodec<GlobalPos> MAP_CODEC;
/*    */   
/* 12 */   public GlobalPos(ResourceKey<Level> dimension, BlockPos pos) { this.dimension = dimension; this.pos = pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/GlobalPos;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/core/GlobalPos; } public ResourceKey<Level> dimension() { return this.dimension; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/GlobalPos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/GlobalPos;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalPos::dimension), (com.mojang.datafixers.kinds.App)BlockPos.CODEC.fieldOf("pos").forGetter(GlobalPos::pos)).apply((com.mojang.datafixers.kinds.Applicative)i, GlobalPos::of));
/*    */   }
/*    */ 
/*    */   
/* 20 */   public static final com.mojang.serialization.Codec<GlobalPos> CODEC = MAP_CODEC.codec();
/*    */   
/* 22 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, GlobalPos> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 23 */       ResourceKey.streamCodec(net.minecraft.core.registries.Registries.DIMENSION), GlobalPos::dimension, BlockPos.STREAM_CODEC, GlobalPos::pos, GlobalPos::of);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GlobalPos of(ResourceKey<Level> dimension, BlockPos pos) {
/* 29 */     return new GlobalPos(dimension, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return String.valueOf(this.dimension) + " " + String.valueOf(this.dimension);
/*    */   }
/*    */   
/*    */   public boolean isCloseEnough(ResourceKey<Level> dimension, BlockPos pos, int maxDistance) {
/* 38 */     return (this.dimension.equals(dimension) && this.pos.distChessboard(pos) <= maxDistance);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/GlobalPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */