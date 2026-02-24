/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.renderer.block.model.multipart.MultiPartModel;
/*     */ import net.minecraft.client.renderer.block.model.multipart.Selector;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MultiPartDefinition
/*     */   extends Record
/*     */ {
/*     */   private final List<Selector> selectors;
/*     */   
/*     */   public MultiPartDefinition(List<Selector> selectors) {
/*  92 */     this.selectors = selectors; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  92 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition; } public List<Selector> selectors() { return this.selectors; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition; } public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*  95 */   } public static final Codec<MultiPartDefinition> CODEC = ExtraCodecs.nonEmptyList(Selector.CODEC.listOf())
/*  96 */     .xmap(MultiPartDefinition::new, MultiPartDefinition::selectors);
/*     */   
/*     */   public MultiPartModel.Unbaked instantiate(StateDefinition<Block, BlockState> stateDefinition) {
/*  99 */     ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel.Unbaked>> instantiatedSelectors = ImmutableList.builderWithExpectedSize(this.selectors.size());
/* 100 */     for (Selector selector : this.selectors) {
/* 101 */       instantiatedSelectors.add(new MultiPartModel.Selector(
/* 102 */             selector.instantiate(stateDefinition), 
/* 103 */             selector.variant()));
/*     */     }
/*     */     
/* 106 */     return new MultiPartModel.Unbaked((List)instantiatedSelectors.build());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockModelDefinition$MultiPartDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */