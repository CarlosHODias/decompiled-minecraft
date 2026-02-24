/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LootTable {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  37 */   public static final Codec<ResourceKey<LootTable>> KEY_CODEC = ResourceKey.codec(Registries.LOOT_TABLE);
/*     */   
/*  39 */   public static final ContextKeySet DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;
/*     */   
/*     */   public static final long RANDOMIZE_SEED = 0L;
/*     */   
/*  43 */   public static final Codec<LootTable> DIRECT_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final Codec<Holder<LootTable>> CODEC = (Codec<Holder<LootTable>>)RegistryFileCodec.create(Registries.LOOT_TABLE, DIRECT_CODEC);
/*     */   
/*  53 */   public static final LootTable EMPTY = new LootTable(LootContextParamSets.EMPTY, Optional.empty(), List.of(), List.of());
/*     */   
/*     */   private final ContextKeySet paramSet;
/*     */   
/*     */   private final Optional<Identifier> randomSequence;
/*     */   
/*     */   private final List<LootPool> pools;
/*     */   private final List<LootItemFunction> functions;
/*     */   private final java.util.function.BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private LootTable(ContextKeySet paramSet, Optional<Identifier> randomSequence, List<LootPool> pools, List<LootItemFunction> functions) {
/*  64 */     this.paramSet = paramSet;
/*  65 */     this.randomSequence = randomSequence;
/*  66 */     this.pools = pools;
/*  67 */     this.functions = functions;
/*  68 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*     */   }
/*     */   
/*     */   public static Consumer<ItemStack> createStackSplitter(ServerLevel level, Consumer<ItemStack> output) {
/*  72 */     return result -> {
/*     */         if (!result.isItemEnabled(level.enabledFeatures())) {
/*     */           return;
/*     */         }
/*     */         if (result.getCount() < result.getMaxStackSize()) {
/*     */           output.accept(result);
/*     */         } else {
/*     */           int count = result.getCount();
/*     */           while (count > 0) {
/*     */             ItemStack copy = result.copyWithCount(Math.min(result.getMaxStackSize(), count));
/*     */             count -= copy.getCount();
/*     */             output.accept(copy);
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public void getRandomItemsRaw(LootParams params, Consumer<ItemStack> output) {
/*  90 */     getRandomItemsRaw(new LootContext.Builder(params).create(this.randomSequence), output);
/*     */   }
/*     */   
/*     */   public void getRandomItemsRaw(LootContext context, Consumer<ItemStack> output) {
/*  94 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(this);
/*  95 */     if (context.pushVisitedElement(breadcrumb)) {
/*  96 */       Consumer<ItemStack> decoratedOutput = LootItemFunction.decorate(this.compositeFunction, output, context);
/*  97 */       for (LootPool pool : this.pools) {
/*  98 */         pool.addRandomItems(decoratedOutput, context);
/*     */       }
/* 100 */       context.popVisitedElement(breadcrumb);
/*     */     } else {
/* 102 */       LOGGER.warn("Detected infinite loop in loot tables");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootParams params, long optionalLootTableSeed, Consumer<ItemStack> output) {
/* 107 */     getRandomItemsRaw(new LootContext.Builder(params).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence), createStackSplitter(params.getLevel(), output));
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootParams params, Consumer<ItemStack> output) {
/* 111 */     getRandomItemsRaw(params, createStackSplitter(params.getLevel(), output));
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootContext context, Consumer<ItemStack> output) {
/* 115 */     getRandomItemsRaw(context, createStackSplitter(context.getLevel(), output));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, RandomSource randomSource) {
/* 119 */     return getRandomItems(new LootContext.Builder(params).withOptionalRandomSource(randomSource).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, long optionalLootTableSeed) {
/* 123 */     return getRandomItems(new LootContext.Builder(params).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params) {
/* 127 */     return getRandomItems(new LootContext.Builder(params).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   private ObjectArrayList<ItemStack> getRandomItems(LootContext context) {
/* 131 */     ObjectArrayList<ItemStack> result = new ObjectArrayList();
/* 132 */     Objects.requireNonNull(result); getRandomItems(context, result::add);
/* 133 */     return result;
/*     */   }
/*     */   
/*     */   public ContextKeySet getParamSet() {
/* 137 */     return this.paramSet;
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/* 141 */     for (int i = 0; i < this.pools.size(); i++) {
/* 142 */       ((LootPool)this.pools.get(i)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("pools", i)));
/*     */     }
/*     */     
/* 145 */     for (int j = 0; j < this.functions.size(); j++) {
/* 146 */       ((LootItemFunction)this.functions.get(j)).validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.IndexedFieldPathElement("functions", j)));
/*     */     }
/*     */   }
/*     */   
/*     */   public void fill(Container container, LootParams params, long optionalRandomSeed) {
/* 151 */     LootContext context = new LootContext.Builder(params).withOptionalRandomSeed(optionalRandomSeed).create(this.randomSequence);
/* 152 */     ObjectArrayList<ItemStack> itemStacks = getRandomItems(context);
/* 153 */     RandomSource random = context.getRandom();
/* 154 */     List<Integer> availableSlots = getAvailableSlots(container, random);
/* 155 */     shuffleAndSplitItems(itemStacks, availableSlots.size(), random);
/* 156 */     for (ObjectListIterator<ItemStack> objectListIterator = itemStacks.iterator(); objectListIterator.hasNext(); ) { ItemStack itemStack = objectListIterator.next();
/* 157 */       if (availableSlots.isEmpty()) {
/* 158 */         LOGGER.warn("Tried to over-fill a container");
/*     */         
/*     */         return;
/*     */       } 
/* 162 */       if (itemStack.isEmpty()) {
/* 163 */         container.setItem((Integer)availableSlots.remove(availableSlots.size() - 1), ItemStack.EMPTY); continue;
/*     */       } 
/* 165 */       container.setItem((Integer)availableSlots.remove(availableSlots.size() - 1), itemStack); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void shuffleAndSplitItems(ObjectArrayList<ItemStack> result, int availableSlots, RandomSource random) {
/* 171 */     List<ItemStack> splittableItems = Lists.newArrayList();
/* 172 */     for (ObjectListIterator<ItemStack> objectListIterator = result.iterator(); objectListIterator.hasNext(); ) {
/* 173 */       ItemStack itemStack = objectListIterator.next();
/* 174 */       if (itemStack.isEmpty()) {
/* 175 */         objectListIterator.remove(); continue;
/* 176 */       }  if (itemStack.getCount() > 1) {
/* 177 */         splittableItems.add(itemStack);
/* 178 */         objectListIterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     while (availableSlots - result.size() - splittableItems.size() > 0 && !splittableItems.isEmpty()) {
/* 183 */       ItemStack itemStack = splittableItems.remove(Mth.nextInt(random, 0, splittableItems.size() - 1));
/* 184 */       int remove = Mth.nextInt(random, 1, itemStack.getCount() / 2);
/* 185 */       ItemStack copy = itemStack.split(remove);
/*     */       
/* 187 */       if (itemStack.getCount() > 1 && random.nextBoolean()) {
/* 188 */         splittableItems.add(itemStack);
/*     */       } else {
/* 190 */         result.add(itemStack);
/*     */       } 
/*     */       
/* 193 */       if (copy.getCount() > 1 && random.nextBoolean()) {
/* 194 */         splittableItems.add(copy); continue;
/*     */       } 
/* 196 */       result.add(copy);
/*     */     } 
/*     */ 
/*     */     
/* 200 */     result.addAll(splittableItems);
/*     */     
/* 202 */     Util.shuffle((List)result, random);
/*     */   }
/*     */   
/*     */   private List<Integer> getAvailableSlots(Container container, RandomSource random) {
/* 206 */     ObjectArrayList<Integer> slots = new ObjectArrayList();
/*     */     
/* 208 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 209 */       if (container.getItem(i).isEmpty()) {
/* 210 */         slots.add(i);
/*     */       }
/*     */     } 
/*     */     
/* 214 */     Util.shuffle((List)slots, random);
/* 215 */     return (List<Integer>)slots;
/*     */   }
/*     */   
/*     */   public static class Builder implements FunctionUserBuilder<Builder> {
/* 219 */     private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();
/*     */     
/* 221 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */     
/* 223 */     private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
/* 224 */     private Optional<Identifier> randomSequence = Optional.empty();
/*     */     
/*     */     public Builder withPool(LootPool.Builder pool) {
/* 227 */       this.pools.add(pool.build());
/* 228 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setParamSet(ContextKeySet paramSet) {
/* 232 */       this.paramSet = paramSet;
/* 233 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRandomSequence(Identifier key) {
/* 237 */       this.randomSequence = Optional.of(key);
/* 238 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder function) {
/* 243 */       this.functions.add(function.build());
/* 244 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder unwrap() {
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public LootTable build() {
/* 253 */       return new LootTable(this.paramSet, this.randomSequence, (List<LootPool>)this.pools.build(), (List<LootItemFunction>)this.functions.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootTable() {
/* 258 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/LootTable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */