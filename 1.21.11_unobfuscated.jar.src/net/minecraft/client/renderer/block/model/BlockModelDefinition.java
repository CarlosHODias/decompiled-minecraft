/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.block.model.multipart.MultiPartModel;
/*     */ import net.minecraft.client.renderer.block.model.multipart.Selector;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ 
/*     */ public final class BlockModelDefinition extends Record {
/*     */   private final Optional<SimpleModelSelectors> simpleModels;
/*     */   private final Optional<MultiPartDefinition> multiPart;
/*     */   
/*  25 */   public BlockModelDefinition(Optional<SimpleModelSelectors> simpleModels, Optional<MultiPartDefinition> multiPart) { this.simpleModels = simpleModels; this.multiPart = multiPart; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  25 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition; } public Optional<SimpleModelSelectors> simpleModels() { return this.simpleModels; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition;
/*  25 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<MultiPartDefinition> multiPart() { return this.multiPart; }
/*     */ 
/*     */ 
/*     */   
/*  29 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   
/*     */   public static final Codec<BlockModelDefinition> CODEC;
/*     */ 
/*     */   
/*     */   static {
/*  35 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)SimpleModelSelectors.CODEC.optionalFieldOf("variants").forGetter(BlockModelDefinition::simpleModels), (App)MultiPartDefinition.CODEC.optionalFieldOf("multipart").forGetter(BlockModelDefinition::multiPart)).apply((com.mojang.datafixers.kinds.Applicative)i, BlockModelDefinition::new)).validate(o -> 
/*  36 */         (o.simpleModels().isEmpty() && o.multiPart().isEmpty()) ? DataResult.error(()) : DataResult.success(o));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<BlockState, BlockStateModel.UnbakedRoot> instantiate(StateDefinition<Block, BlockState> stateDefinition, Supplier<String> source) {
/*  43 */     Map<BlockState, BlockStateModel.UnbakedRoot> matchedStates = new java.util.IdentityHashMap<>();
/*     */     
/*  45 */     this.simpleModels.ifPresent(s -> s.instantiate(stateDefinition, source, ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     this.multiPart.ifPresent(m -> {
/*     */           ImmutableList immutableList = stateDefinition.getPossibleStates();
/*     */           
/*     */           MultiPartModel.Unbaked unbaked = m.instantiate(stateDefinition);
/*     */           
/*     */           for (BlockState state : (Iterable<BlockState>)immutableList) {
/*     */             matchedStates.putIfAbsent(state, unbaked);
/*     */           }
/*     */         });
/*  65 */     return matchedStates;
/*     */   }
/*     */   public static final class SimpleModelSelectors extends Record { private final Map<String, BlockStateModel.Unbaked> models;
/*  68 */     public SimpleModelSelectors(Map<String, BlockStateModel.Unbaked> models) { this.models = models; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #68	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;
/*  68 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, BlockStateModel.Unbaked> models() { return this.models; }
/*     */ 
/*     */     
/*  71 */     public static final Codec<SimpleModelSelectors> CODEC = net.minecraft.util.ExtraCodecs.nonEmptyMap((Codec)Codec.unboundedMap((Codec)Codec.STRING, BlockStateModel.Unbaked.CODEC))
/*  72 */       .xmap(SimpleModelSelectors::new, SimpleModelSelectors::models);
/*     */     
/*     */     public void instantiate(StateDefinition<Block, BlockState> stateDefinition, Supplier<String> source, BiConsumer<BlockState, BlockStateModel.UnbakedRoot> output) {
/*  75 */       this.models.forEach((selectorString, model) -> {
/*     */             try {
/*     */               Predicate<net.minecraft.world.level.block.state.StateHolder<Block, BlockState>> selector = VariantSelector.predicate(stateDefinition, selectorString);
/*     */               BlockStateModel.UnbakedRoot wrapper = model.asRoot();
/*     */               UnmodifiableIterator<BlockState> unmodifiableIterator = stateDefinition.getPossibleStates().iterator();
/*     */               while (unmodifiableIterator.hasNext()) {
/*     */                 BlockState state = unmodifiableIterator.next();
/*     */                 if (selector.test(state))
/*     */                   output.accept(state, wrapper); 
/*     */               } 
/*  85 */             } catch (Exception e) {
/*     */               BlockModelDefinition.LOGGER.warn("Exception loading blockstate definition: '{}' for variant: '{}': {}", new Object[] { source.get(), selectorString, e.getMessage() });
/*     */             } 
/*     */           });
/*     */     } }
/*     */   public static final class MultiPartDefinition extends Record { private final List<Selector> selectors;
/*     */     
/*  92 */     public MultiPartDefinition(List<Selector> selectors) { this.selectors = selectors; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;
/*  92 */       //   0	8	1	o	Ljava/lang/Object; } public List<Selector> selectors() { return this.selectors; }
/*     */ 
/*     */     
/*  95 */     public static final Codec<MultiPartDefinition> CODEC = net.minecraft.util.ExtraCodecs.nonEmptyList(Selector.CODEC.listOf())
/*  96 */       .xmap(MultiPartDefinition::new, MultiPartDefinition::selectors);
/*     */     
/*     */     public MultiPartModel.Unbaked instantiate(StateDefinition<Block, BlockState> stateDefinition) {
/*  99 */       ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel.Unbaked>> instantiatedSelectors = ImmutableList.builderWithExpectedSize(this.selectors.size());
/* 100 */       for (Selector selector : this.selectors) {
/* 101 */         instantiatedSelectors.add(new MultiPartModel.Selector(
/* 102 */               selector.instantiate(stateDefinition), 
/* 103 */               selector.variant()));
/*     */       }
/*     */       
/* 106 */       return new MultiPartModel.Unbaked((List)instantiatedSelectors.build());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockModelDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */