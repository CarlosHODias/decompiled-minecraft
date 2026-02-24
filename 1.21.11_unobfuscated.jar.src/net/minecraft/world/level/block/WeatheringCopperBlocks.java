/*    */ package net.minecraft.world.level.block;
/*    */ public final class WeatheringCopperBlocks extends Record {
/*    */   private final Block unaffected;
/*    */   private final Block exposed;
/*    */   private final Block weathered;
/*    */   private final Block oxidized;
/*    */   private final Block waxed;
/*    */   private final Block waxedExposed;
/*    */   private final Block waxedWeathered;
/*    */   private final Block waxedOxidized;
/*    */   
/* 12 */   public WeatheringCopperBlocks(Block unaffected, Block exposed, Block weathered, Block oxidized, Block waxed, Block waxedExposed, Block waxedWeathered, Block waxedOxidized) { this.unaffected = unaffected; this.exposed = exposed; this.weathered = weathered; this.oxidized = oxidized; this.waxed = waxed; this.waxedExposed = waxedExposed; this.waxedWeathered = waxedWeathered; this.waxedOxidized = waxedOxidized; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks; } public Block unaffected() { return this.unaffected; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/WeatheringCopperBlocks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperBlocks;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Block exposed() { return this.exposed; } public Block weathered() { return this.weathered; } public Block oxidized() { return this.oxidized; } public Block waxed() { return this.waxed; } public Block waxedExposed() { return this.waxedExposed; } public Block waxedWeathered() { return this.waxedWeathered; } public Block waxedOxidized() { return this.waxedOxidized; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper> WeatheringCopperBlocks create(String id, org.apache.commons.lang3.function.TriFunction<String, java.util.function.Function<net.minecraft.world.level.block.state.BlockBehaviour.Properties, Block>, net.minecraft.world.level.block.state.BlockBehaviour.Properties, Block> register, java.util.function.Function<net.minecraft.world.level.block.state.BlockBehaviour.Properties, WaxedBlock> waxedBlockFactory, java.util.function.BiFunction<WeatheringCopper.WeatherState, net.minecraft.world.level.block.state.BlockBehaviour.Properties, WeatheringBlock> weatheringFactory, java.util.function.Function<WeatheringCopper.WeatherState, net.minecraft.world.level.block.state.BlockBehaviour.Properties> propertiesSupplier) {
/* 34 */     java.util.Objects.requireNonNull(waxedBlockFactory);
/* 35 */     java.util.Objects.requireNonNull(waxedBlockFactory);
/* 36 */     java.util.Objects.requireNonNull(waxedBlockFactory);
/* 37 */     java.util.Objects.requireNonNull(waxedBlockFactory); return new WeatheringCopperBlocks((Block)register.apply(id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.UNAFFECTED, p), propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply("exposed_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.EXPOSED, p), propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply("weathered_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.WEATHERED, p), propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply("oxidized_" + id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.OXIDIZED, p), propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)), (Block)register.apply("waxed_" + id, waxedBlockFactory::apply, propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply("waxed_exposed_" + id, waxedBlockFactory::apply, propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply("waxed_weathered_" + id, waxedBlockFactory::apply, propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply("waxed_oxidized_" + id, waxedBlockFactory::apply, propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)));
/*    */   }
/*    */ 
/*    */   
/*    */   public com.google.common.collect.ImmutableBiMap<Block, Block> weatheringMapping() {
/* 42 */     return com.google.common.collect.ImmutableBiMap.of(this.unaffected, this.exposed, this.exposed, this.weathered, this.weathered, this.oxidized);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.google.common.collect.ImmutableBiMap<Block, Block> waxedMapping() {
/* 50 */     return com.google.common.collect.ImmutableBiMap.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public com.google.common.collect.ImmutableList<Block> asList() {
/* 59 */     return com.google.common.collect.ImmutableList.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void forEach(java.util.function.Consumer<Block> consumer) {
/* 68 */     consumer.accept(this.unaffected);
/* 69 */     consumer.accept(this.exposed);
/* 70 */     consumer.accept(this.weathered);
/* 71 */     consumer.accept(this.oxidized);
/* 72 */     consumer.accept(this.waxed);
/* 73 */     consumer.accept(this.waxedExposed);
/* 74 */     consumer.accept(this.waxedWeathered);
/* 75 */     consumer.accept(this.waxedOxidized);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/WeatheringCopperBlocks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */