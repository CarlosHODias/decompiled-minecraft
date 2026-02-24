/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ApplyBonusCount extends LootItemConditionalFunction {
/*     */   private static final class FormulaType extends Record {
/*     */     private final Identifier id;
/*     */     private final Codec<? extends ApplyBonusCount.Formula> codec;
/*     */     
/*  27 */     private FormulaType(Identifier id, Codec<? extends ApplyBonusCount.Formula> codec) { this.id = id; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  27 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;
/*  27 */       //   0	8	1	o	Ljava/lang/Object; } public Codec<? extends ApplyBonusCount.Formula> codec() { return this.codec; }
/*     */   
/*     */   }
/*     */   
/*     */   private static final class BinomialWithBonusCount extends Record implements Formula {
/*     */     private final int extraRounds;
/*     */     private final float probability;
/*     */     private static final Codec<BinomialWithBonusCount> CODEC;
/*     */     
/*  36 */     private BinomialWithBonusCount(int extraRounds, float probability) { this.extraRounds = extraRounds; this.probability = probability; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*  36 */       //   0	8	1	o	Ljava/lang/Object; } public int extraRounds() { return this.extraRounds; } public float probability() { return this.probability; } static {
/*  37 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("extra").forGetter(BinomialWithBonusCount::extraRounds), (App)Codec.FLOAT.fieldOf("probability").forGetter(BinomialWithBonusCount::probability)).apply((Applicative)i, BinomialWithBonusCount::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  42 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), (Codec)CODEC);
/*     */ 
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  46 */       for (int i = 0; i < level + this.extraRounds; i++) {
/*  47 */         if (random.nextFloat() < this.probability) {
/*  48 */           count++;
/*     */         }
/*     */       } 
/*  51 */       return count;
/*     */     }
/*     */ 
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType() {
/*  56 */       return TYPE;
/*     */     } }
/*     */   private static final class UniformBonusCount extends Record implements Formula { private final int bonusMultiplier; public static final Codec<UniformBonusCount> CODEC;
/*     */     
/*  60 */     private UniformBonusCount(int bonusMultiplier) { this.bonusMultiplier = bonusMultiplier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*  60 */       //   0	8	1	o	Ljava/lang/Object; } public int bonusMultiplier() { return this.bonusMultiplier; } static {
/*  61 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier)).apply((Applicative)i, UniformBonusCount::new));
/*     */     }
/*     */ 
/*     */     
/*  65 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), (Codec)CODEC);
/*     */ 
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  69 */       return count + random.nextInt(this.bonusMultiplier * level + 1);
/*     */     }
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType()
/*     */     {
/*  74 */       return TYPE;
/*     */     } } private static final class OreDrops extends Record implements Formula { public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops; } public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */     } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #78	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*  79 */     } public static final OreDrops INSTANCE = new OreDrops();
/*     */     
/*  81 */     public static final Codec<OreDrops> CODEC = MapCodec.unitCodec(INSTANCE);
/*  82 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("ore_drops"), (Codec)CODEC);
/*     */ 
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  86 */       if (level > 0) {
/*  87 */         int bonus = random.nextInt(level + 2) - 1;
/*  88 */         if (bonus < 0) {
/*  89 */           bonus = 0;
/*     */         }
/*  91 */         return count * (bonus + 1);
/*     */       } 
/*     */       
/*  94 */       return count;
/*     */     }
/*     */ 
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType() {
/*  99 */       return TYPE;
/*     */     } }
/*     */ 
/*     */   
/* 103 */   private static final Map<Identifier, FormulaType> FORMULAS = (Map<Identifier, FormulaType>)java.util.stream.Stream.<FormulaType>of(new FormulaType[] { BinomialWithBonusCount.TYPE, OreDrops.TYPE, UniformBonusCount.TYPE
/*     */ 
/*     */ 
/*     */       
/* 107 */       }).collect(java.util.stream.Collectors.toMap(FormulaType::id, Function.identity())); private static final Codec<FormulaType> FORMULA_TYPE_CODEC;
/*     */   static {
/* 109 */     FORMULA_TYPE_CODEC = Identifier.CODEC.comapFlatMap(location -> { FormulaType type = FORMULAS.get(location); return (type != null) ? DataResult.success(type) : DataResult.error(()); }, FormulaType::id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private static final MapCodec<Formula> FORMULA_CODEC = net.minecraft.util.ExtraCodecs.dispatchOptionalValue("formula", "parameters", FORMULA_TYPE_CODEC, Formula::getType, FormulaType::codec); public static final MapCodec<ApplyBonusCount> CODEC; private final Holder<Enchantment> enchantment; private final Formula formula;
/*     */   static {
/* 119 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(()), (App)FORMULA_CODEC.forGetter(()))).apply((Applicative)i, ApplyBonusCount::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ApplyBonusCount(List<LootItemCondition> predicates, Holder<Enchantment> enchantment, Formula formula) {
/* 128 */     super(predicates);
/* 129 */     this.enchantment = enchantment;
/* 130 */     this.formula = formula;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<ApplyBonusCount> getType() {
/* 135 */     return LootItemFunctions.APPLY_BONUS;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 140 */     return Set.of(LootContextParams.TOOL);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 145 */     ItemStack tool = (ItemStack)context.getOptionalParameter(LootContextParams.TOOL);
/*     */     
/* 147 */     if (tool != null) {
/* 148 */       int level = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool);
/* 149 */       int newCount = this.formula.calculateNewCount(context.getRandom(), itemStack.getCount(), level);
/* 150 */       itemStack.setCount(newCount);
/*     */     } 
/* 152 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addBonusBinomialDistributionCount(Holder<Enchantment> enchantment, float probability, int extraRounds) {
/* 156 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new BinomialWithBonusCount(extraRounds, probability)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addOreBonusCount(Holder<Enchantment> enchantment) {
/* 160 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, OreDrops.INSTANCE));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment) {
/* 164 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(1)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment, int bonusMultiplier) {
/* 168 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(bonusMultiplier)));
/*     */   }
/*     */   
/*     */   private static interface Formula {
/*     */     int calculateNewCount(RandomSource param1RandomSource, int param1Int1, int param1Int2);
/*     */     
/*     */     ApplyBonusCount.FormulaType getType();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/ApplyBonusCount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */