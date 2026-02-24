/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetContainerLootTable extends LootItemConditionalFunction {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)LootTable.KEY_CODEC.fieldOf("name").forGetter(()), (App)Codec.LONG.optionalFieldOf("seed", 0L).forGetter(()), (App)BuiltInRegistries.BLOCK_ENTITY_TYPE.holderByNameCodec().fieldOf("type").forGetter(()))).apply((Applicative)i, SetContainerLootTable::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final MapCodec<SetContainerLootTable> CODEC;
/*    */   
/*    */   private final ResourceKey<LootTable> name;
/*    */   private final long seed;
/*    */   private final Holder<BlockEntityType<?>> type;
/*    */   
/*    */   private SetContainerLootTable(List<LootItemCondition> predicates, ResourceKey<LootTable> name, long seed, Holder<BlockEntityType<?>> type) {
/* 32 */     super(predicates);
/* 33 */     this.name = name;
/* 34 */     this.seed = seed;
/* 35 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetContainerLootTable> getType() {
/* 40 */     return LootItemFunctions.SET_LOOT_TABLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 45 */     if (itemStack.isEmpty()) {
/* 46 */       return itemStack;
/*    */     }
/* 48 */     itemStack.set(net.minecraft.core.component.DataComponents.CONTAINER_LOOT, new net.minecraft.world.item.component.SeededContainerLoot(this.name, this.seed));
/* 49 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 54 */     super.validate(context);
/*    */ 
/*    */ 
/*    */     
/* 58 */     if (!context.allowsReferences()) {
/* 59 */       context.reportProblem((ProblemReporter.Problem)new ValidationContext.ReferenceNotAllowedProblem(this.name));
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     if (context.resolver().get(this.name).isEmpty()) {
/* 64 */       context.reportProblem((ProblemReporter.Problem)new ValidationContext.MissingReferenceProblem(this.name));
/*    */     }
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> type, ResourceKey<LootTable> value) {
/* 69 */     return simpleBuilder(conditions -> new SetContainerLootTable(conditions, value, 0L, (Holder<BlockEntityType<?>>)type.builtInRegistryHolder()));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> type, ResourceKey<LootTable> value, long seed) {
/* 73 */     return simpleBuilder(conditions -> new SetContainerLootTable(conditions, value, seed, (Holder<BlockEntityType<?>>)type.builtInRegistryHolder()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetContainerLootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */