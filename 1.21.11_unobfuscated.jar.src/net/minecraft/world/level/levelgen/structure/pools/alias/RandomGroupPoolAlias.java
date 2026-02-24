/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiConsumer;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public final class RandomGroupPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final WeightedList<List<PoolAliasBinding>> groups;
/*    */   static com.mojang.serialization.MapCodec<RandomGroupPoolAlias> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/*    */   }
/*    */   
/* 22 */   public RandomGroupPoolAlias(WeightedList<List<PoolAliasBinding>> groups) { this.groups = groups; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public WeightedList<List<PoolAliasBinding>> groups() { return this.groups; } static {
/* 23 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)WeightedList.nonEmptyCodec(com.mojang.serialization.Codec.list(PoolAliasBinding.CODEC)).fieldOf("groups").forGetter(RandomGroupPoolAlias::groups)).apply((com.mojang.datafixers.kinds.Applicative)i, RandomGroupPoolAlias::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEachResolved(RandomSource random, BiConsumer<net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool>, net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool>> aliasAndTargetConsumer) {
/* 29 */     this.groups.getRandom(random).ifPresent(combination -> combination.forEach(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public java.util.stream.Stream<net.minecraft.resources.ResourceKey<net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool>> allTargets() {
/* 36 */     return this.groups.unwrap().stream()
/* 37 */       .flatMap(weightedEntry -> ((List)weightedEntry.value()).stream())
/* 38 */       .flatMap(PoolAliasBinding::allTargets);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<RandomGroupPoolAlias> codec() {
/* 43 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/alias/RandomGroupPoolAlias.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */