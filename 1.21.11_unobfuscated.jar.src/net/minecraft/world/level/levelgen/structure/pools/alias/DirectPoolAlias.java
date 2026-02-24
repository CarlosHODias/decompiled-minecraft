/*    */ package net.minecraft.world.level.levelgen.structure.pools.alias;
/*    */ 
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*    */ 
/*    */ public final class DirectPoolAlias extends Record implements PoolAliasBinding {
/*    */   private final ResourceKey<StructureTemplatePool> alias;
/*    */   private final ResourceKey<StructureTemplatePool> target;
/*    */   static com.mojang.serialization.MapCodec<DirectPoolAlias> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/*    */   }
/*    */   
/* 19 */   public DirectPoolAlias(ResourceKey<StructureTemplatePool> alias, ResourceKey<StructureTemplatePool> target) { this.alias = alias; this.target = target; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias;
/* 19 */     //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<StructureTemplatePool> alias() { return this.alias; } public ResourceKey<StructureTemplatePool> target() { return this.target; } static {
/* 20 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)ResourceKey.codec(net.minecraft.core.registries.Registries.TEMPLATE_POOL).fieldOf("alias").forGetter(DirectPoolAlias::alias), (com.mojang.datafixers.kinds.App)ResourceKey.codec(net.minecraft.core.registries.Registries.TEMPLATE_POOL).fieldOf("target").forGetter(DirectPoolAlias::target)).apply((com.mojang.datafixers.kinds.Applicative)i, DirectPoolAlias::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEachResolved(net.minecraft.util.RandomSource random, java.util.function.BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> aliasAndTargetConsumer) {
/* 27 */     aliasAndTargetConsumer.accept(this.alias, this.target);
/*    */   }
/*    */ 
/*    */   
/*    */   public java.util.stream.Stream<ResourceKey<StructureTemplatePool>> allTargets() {
/* 32 */     return java.util.stream.Stream.of(this.target);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<DirectPoolAlias> codec() {
/* 37 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/alias/DirectPoolAlias.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */