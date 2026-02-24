/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.SuspiciousStewEffects;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class SetStewEffectFunction extends LootItemConditionalFunction {
/*     */   private static final Codec<List<EffectEntry>> EFFECTS_LIST;
/*     */   public static final com.mojang.serialization.MapCodec<SetStewEffectFunction> CODEC;
/*     */   private final List<EffectEntry> effects;
/*     */   
/*     */   static {
/*  28 */     EFFECTS_LIST = EffectEntry.CODEC.listOf().validate(entries -> {
/*     */           ObjectOpenHashSet<Holder<MobEffect>> objectOpenHashSet = new ObjectOpenHashSet();
/*     */           
/*     */           for (EffectEntry entry : (Iterable<EffectEntry>)entries) {
/*     */             if (!objectOpenHashSet.add(entry.effect())) {
/*     */               return DataResult.error(());
/*     */             }
/*     */           } 
/*     */           return DataResult.success(entries);
/*     */         });
/*  38 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)EFFECTS_LIST.optionalFieldOf("effects", List.of()).forGetter(())).apply((Applicative)i, SetStewEffectFunction::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SetStewEffectFunction(List<LootItemCondition> predicates, List<EffectEntry> effects) {
/*  45 */     super(predicates);
/*  46 */     this.effects = effects;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<SetStewEffectFunction> getType() {
/*  51 */     return LootItemFunctions.SET_STEW_EFFECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  56 */     return (Set<ContextKey<?>>)this.effects.stream().flatMap(p -> p.duration().getReferencedContextParams().stream()).collect(com.google.common.collect.ImmutableSet.toImmutableSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  61 */     if (!itemStack.is(net.minecraft.world.item.Items.SUSPICIOUS_STEW) || this.effects.isEmpty()) {
/*  62 */       return itemStack;
/*     */     }
/*     */     
/*  65 */     EffectEntry entry = (EffectEntry)net.minecraft.util.Util.getRandom(this.effects, context.getRandom());
/*     */     
/*  67 */     Holder<MobEffect> effect = entry.effect();
/*  68 */     int duration = entry.duration().getInt(context);
/*  69 */     if (!((MobEffect)effect.value()).isInstantenous()) {
/*  70 */       duration *= 20;
/*     */     }
/*     */     
/*  73 */     SuspiciousStewEffects.Entry newEntry = new SuspiciousStewEffects.Entry(effect, duration);
/*  74 */     itemStack.update(net.minecraft.core.component.DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY, newEntry, SuspiciousStewEffects::withEffectAdded);
/*     */     
/*  76 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*  80 */     private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*     */ 
/*     */     
/*     */     protected Builder getThis() {
/*  84 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withEffect(Holder<MobEffect> effect, NumberProvider duration) {
/*  88 */       this.effects.add(new SetStewEffectFunction.EffectEntry(effect, duration));
/*  89 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/*  94 */       return new SetStewEffectFunction(getConditions(), (List<SetStewEffectFunction.EffectEntry>)this.effects.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder stewEffect() {
/*  99 */     return new Builder();
/*     */   }
/*     */   private static final class EffectEntry extends Record { private final Holder<MobEffect> effect; private final NumberProvider duration; public static final Codec<EffectEntry> CODEC;
/* 102 */     private EffectEntry(Holder<MobEffect> effect, NumberProvider duration) { this.effect = effect; this.duration = duration; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 102 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry; } public Holder<MobEffect> effect() { return this.effect; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #102	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$EffectEntry;
/* 102 */       //   0	8	1	o	Ljava/lang/Object; } public NumberProvider duration() { return this.duration; } static {
/* 103 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MobEffect.CODEC.fieldOf("type").forGetter(EffectEntry::effect), (App)NumberProviders.CODEC.fieldOf("duration").forGetter(EffectEntry::duration)).apply((Applicative)i, EffectEntry::new));
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */