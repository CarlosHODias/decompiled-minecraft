/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ 
/*    */ public final class DimensionPadding extends Record {
/*    */   private final int bottom;
/*    */   private final int top;
/*    */   private static final com.mojang.serialization.Codec<DimensionPadding> RECORD_CODEC;
/*    */   public static final com.mojang.serialization.Codec<DimensionPadding> CODEC;
/*    */   
/* 10 */   public DimensionPadding(int bottom, int top) { this.bottom = bottom; this.top = top; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding; } public int bottom() { return this.bottom; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int top() { return this.top; } static {
/* 11 */     RECORD_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.lenientOptionalFieldOf("bottom", 0).forGetter(()), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.lenientOptionalFieldOf("top", 0).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, DimensionPadding::new));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 16 */     CODEC = com.mojang.serialization.Codec.either(net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT, RECORD_CODEC).xmap(e -> (DimensionPadding)e.map(DimensionPadding::new, java.util.function.Function.identity()), padding -> padding.hasEqualTopAndBottom() ? com.mojang.datafixers.util.Either.left(padding.bottom) : com.mojang.datafixers.util.Either.right(padding));
/*    */   }
/*    */ 
/*    */   
/* 20 */   public static final DimensionPadding ZERO = new DimensionPadding(0);
/*    */   
/*    */   public DimensionPadding(int value) {
/* 23 */     this(value, value);
/*    */   }
/*    */   
/*    */   public boolean hasEqualTopAndBottom() {
/* 27 */     return (this.top == this.bottom);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/DimensionPadding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */