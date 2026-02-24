/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ 
/*    */ final class UnobstructedPredicate extends Record implements BlockPredicate {
/*    */   private final Vec3i offset;
/*    */   public static com.mojang.serialization.MapCodec<UnobstructedPredicate> CODEC;
/*    */   
/* 10 */   UnobstructedPredicate(Vec3i offset) { this.offset = offset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate; } public Vec3i offset() { return this.offset; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(UnobstructedPredicate::offset)).apply((com.mojang.datafixers.kinds.Applicative)i, UnobstructedPredicate::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 17 */     return BlockPredicateType.UNOBSTRUCTED;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(net.minecraft.world.level.WorldGenLevel worldGenLevel, net.minecraft.core.BlockPos pos) {
/* 22 */     return worldGenLevel.isUnobstructed(null, net.minecraft.world.phys.shapes.Shapes.block().move((Vec3i)pos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/blockpredicates/UnobstructedPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */