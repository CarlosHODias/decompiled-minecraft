/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleModelSelectors
/*    */   extends Record
/*    */ {
/*    */   private final Map<String, BlockStateModel.Unbaked> models;
/*    */   
/*    */   public SimpleModelSelectors(Map<String, BlockStateModel.Unbaked> models) {
/* 68 */     this.models = models; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 68 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors; } public Map<String, BlockStateModel.Unbaked> models() { return this.models; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #68	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 71 */   } public static final Codec<SimpleModelSelectors> CODEC = ExtraCodecs.nonEmptyMap((Codec)Codec.unboundedMap((Codec)Codec.STRING, BlockStateModel.Unbaked.CODEC))
/* 72 */     .xmap(SimpleModelSelectors::new, SimpleModelSelectors::models);
/*    */   
/*    */   public void instantiate(StateDefinition<Block, BlockState> stateDefinition, Supplier<String> source, BiConsumer<BlockState, BlockStateModel.UnbakedRoot> output) {
/* 75 */     this.models.forEach((selectorString, model) -> {
/*    */           try {
/*    */             Predicate<StateHolder<Block, BlockState>> selector = VariantSelector.predicate(stateDefinition, selectorString);
/*    */             BlockStateModel.UnbakedRoot wrapper = model.asRoot();
/*    */             UnmodifiableIterator<BlockState> unmodifiableIterator = stateDefinition.getPossibleStates().iterator();
/*    */             while (unmodifiableIterator.hasNext()) {
/*    */               BlockState state = unmodifiableIterator.next();
/*    */               if (selector.test(state))
/*    */                 output.accept(state, wrapper); 
/*    */             } 
/* 85 */           } catch (Exception e) {
/*    */             BlockModelDefinition.LOGGER.warn("Exception loading blockstate definition: '{}' for variant: '{}': {}", new Object[] { source.get(), selectorString, e.getMessage() });
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockModelDefinition$SimpleModelSelectors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */