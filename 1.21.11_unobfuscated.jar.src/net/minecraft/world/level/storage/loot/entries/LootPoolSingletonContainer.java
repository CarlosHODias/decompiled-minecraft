/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public abstract class LootPoolSingletonContainer
/*     */   extends LootPoolEntryContainer {
/*     */   public static final int DEFAULT_WEIGHT = 1;
/*     */   public static final int DEFAULT_QUALITY = 0;
/*     */   protected final int weight;
/*     */   protected final int quality;
/*     */   protected final List<LootItemFunction> functions;
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   private final LootPoolEntry entry;
/*     */   
/*     */   protected LootPoolSingletonContainer(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/*  32 */     super(conditions);
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
/*  64 */     this.entry = new EntryBase()
/*     */       {
/*     */         public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/*  67 */           LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, output, context), context);
/*     */         }
/*     */       };
/*     */     this.weight = weight;
/*     */     this.quality = quality;
/*     */     this.functions = functions;
/*     */     this.compositeFunction = LootItemFunctions.compose(functions); } protected static <T extends LootPoolSingletonContainer> Products.P4<RecordCodecBuilder.Mu<T>, Integer, Integer, List<LootItemCondition>, List<LootItemFunction>> singletonFields(RecordCodecBuilder.Instance<T> i) {
/*     */     return i.group((App)Codec.INT.optionalFieldOf("weight", 1).forGetter(e -> e.weight), (App)Codec.INT.optionalFieldOf("quality", 0).forGetter(e -> e.quality)).and(commonFields((RecordCodecBuilder.Instance)i).t1()).and((App)LootItemFunctions.ROOT_CODEC.listOf().optionalFieldOf("functions", List.of()).forGetter(e -> e.functions));
/*  75 */   } public boolean expand(LootContext context, Consumer<LootPoolEntry> output) { if (canRun(context)) {
/*  76 */       output.accept(this.entry);
/*  77 */       return true;
/*     */     } 
/*     */     
/*  80 */     return false; } public void validate(ValidationContext context) { super.validate(context); for (int i = 0; i < this.functions.size(); i++)
/*     */       ((LootItemFunction)this.functions.get(i)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("functions", i)));  } protected abstract class EntryBase implements LootPoolEntry {
/*     */     public int getWeight(float luck) { return Math.max(Mth.floor(LootPoolSingletonContainer.this.weight + LootPoolSingletonContainer.this.quality * luck), 0); }
/*     */   } public static abstract class Builder<T extends Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> {
/*  84 */     protected int weight = 1;
/*  85 */     protected int quality = 0;
/*     */     
/*  87 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */ 
/*     */     
/*     */     public T apply(LootItemFunction.Builder function) {
/*  91 */       this.functions.add(function.build());
/*  92 */       return getThis();
/*     */     }
/*     */     
/*     */     protected List<LootItemFunction> getFunctions() {
/*  96 */       return (List<LootItemFunction>)this.functions.build();
/*     */     }
/*     */     
/*     */     public T setWeight(int weight) {
/* 100 */       this.weight = weight;
/* 101 */       return getThis();
/*     */     }
/*     */     
/*     */     public T setQuality(int quality) {
/* 105 */       this.quality = quality;
/* 106 */       return getThis();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DummyBuilder
/*     */     extends Builder<DummyBuilder>
/*     */   {
/*     */     private final LootPoolSingletonContainer.EntryConstructor constructor;
/*     */ 
/*     */     
/*     */     public DummyBuilder(LootPoolSingletonContainer.EntryConstructor constructor) {
/* 119 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DummyBuilder getThis() {
/* 124 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootPoolEntryContainer build() {
/* 129 */       return this.constructor.build(this.weight, this.quality, getConditions(), getFunctions());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder<?> simpleBuilder(EntryConstructor constructor) {
/* 134 */     return new DummyBuilder(constructor);
/*     */   }
/*     */   
/*     */   protected abstract void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface EntryConstructor {
/*     */     LootPoolSingletonContainer build(int param1Int1, int param1Int2, List<LootItemCondition> param1List, List<LootItemFunction> param1List1);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */