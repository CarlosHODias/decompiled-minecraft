/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyBlockState extends LootItemConditionalFunction {
/*     */   static {
/*  26 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("block").forGetter(()), (App)Codec.STRING.listOf().fieldOf("properties").forGetter(()))).apply((Applicative)i, CopyBlockState::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<CopyBlockState> CODEC;
/*     */   private final Holder<Block> block;
/*     */   private final Set<Property<?>> properties;
/*     */   
/*     */   private CopyBlockState(List<LootItemCondition> predicates, Holder<Block> block, Set<Property<?>> properties) {
/*  35 */     super(predicates);
/*  36 */     this.block = block;
/*  37 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   private CopyBlockState(List<LootItemCondition> predicates, Holder<Block> block, List<String> propertyNames) {
/*  41 */     this(predicates, block, (Set<Property<?>>)propertyNames.stream()
/*  42 */         .map(((Block)block.value()).getStateDefinition()::getProperty)
/*  43 */         .filter(java.util.Objects::nonNull)
/*  44 */         .collect(Collectors.toSet()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<CopyBlockState> getType() {
/*  50 */     return LootItemFunctions.COPY_STATE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  55 */     return Set.of(LootContextParams.BLOCK_STATE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/*  60 */     BlockState state = (BlockState)context.getOptionalParameter(LootContextParams.BLOCK_STATE);
/*  61 */     if (state != null) {
/*  62 */       itemStack.update(net.minecraft.core.component.DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, itemState -> {
/*     */             for (Property<?> property : this.properties) {
/*     */               if (state.hasProperty(property)) {
/*     */                 state = state.with(property, state);
/*     */               }
/*     */             } 
/*     */             
/*     */             return state;
/*     */           });
/*     */     }
/*  72 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final Holder<Block> block;
/*  77 */     private final ImmutableSet.Builder<Property<?>> properties = ImmutableSet.builder();
/*     */     
/*     */     private Builder(Block block) {
/*  80 */       this.block = (Holder<Block>)block.builtInRegistryHolder();
/*     */     }
/*     */     
/*     */     public Builder copy(Property<?> property) {
/*  84 */       if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(property)) {
/*  85 */         throw new IllegalStateException("Property " + String.valueOf(property) + " is not present on block " + String.valueOf(this.block));
/*     */       }
/*  87 */       this.properties.add(property);
/*  88 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  93 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  98 */       return new CopyBlockState(getConditions(), this.block, (Set<Property<?>>)this.properties.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder copyState(Block block) {
/* 103 */     return new Builder(block);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/CopyBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */