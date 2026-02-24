/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class RandomPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final ResourceKey<StructureTemplatePool> alias;
/*    */   private final WeightedList<ResourceKey<StructureTemplatePool>> targets;
/*    */   static com.mojang.serialization.MapCodec<RandomPoolAlias> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/*    */   }
/*    */   
/* 21 */   public RandomPoolAlias(ResourceKey<StructureTemplatePool> alias, WeightedList<ResourceKey<StructureTemplatePool>> targets) { this.alias = alias; this.targets = targets; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #21	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias;
/* 21 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<StructureTemplatePool> alias() { return this.alias; } public WeightedList<ResourceKey<StructureTemplatePool>> targets() { return this.targets; } static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)ResourceKey.codec(net.minecraft.core.registries.Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(RandomPoolAlias::alias), (com.mojang.datafixers.kinds.App)WeightedList.nonEmptyCodec(ResourceKey.codec(net.minecraft.core.registries.Registries.TEMPLATE_POOL)).fieldOf("targets").forGetter(RandomPoolAlias::targets)).apply((com.mojang.datafixers.kinds.Applicative)i, RandomPoolAlias::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEachResolved(net.minecraft.util.RandomSource random, java.util.function.BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 29 */     this.targets.getRandom(random).ifPresent(target -> aliasAndTargetConsumer.accept(this.alias, aliasAndTargetConsumer));
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.stream.Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 34 */     return this.targets.unwrap().stream().map(net.minecraft.util.random.Weighted::value);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<RandomPoolAlias> codec() {
/* 39 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/alias/RandomPoolAlias.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */