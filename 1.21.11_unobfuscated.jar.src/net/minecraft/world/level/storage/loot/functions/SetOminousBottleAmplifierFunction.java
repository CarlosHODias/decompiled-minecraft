/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetOminousBottleAmplifierFunction extends LootItemConditionalFunction {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)NumberProviders.CODEC.fieldOf("amplifier").forGetter(())).apply((Applicative)i, SetOminousBottleAmplifierFunction::new));
/*    */   }
/*    */   
/*    */   static final com.mojang.serialization.MapCodec<SetOminousBottleAmplifierFunction> CODEC;
/*    */   private final NumberProvider amplifierGenerator;
/*    */   
/*    */   private SetOminousBottleAmplifierFunction(List<LootItemCondition> predicates, NumberProvider amplifierGenerator) {
/* 26 */     super(predicates);
/* 27 */     this.amplifierGenerator = amplifierGenerator;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 32 */     return this.amplifierGenerator.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetOminousBottleAmplifierFunction> getType() {
/* 37 */     return LootItemFunctions.SET_OMINOUS_BOTTLE_AMPLIFIER;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 42 */     int amplifierValue = Mth.clamp(this.amplifierGenerator.getInt(context), 0, 4);
/* 43 */     itemStack.set(net.minecraft.core.component.DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(amplifierValue));
/* 44 */     return itemStack;
/*    */   }
/*    */   
/*    */   public NumberProvider amplifier() {
/* 48 */     return this.amplifierGenerator;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setAmplifier(NumberProvider generator) {
/* 52 */     return simpleBuilder(conditions -> new SetOminousBottleAmplifierFunction(conditions, generator));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetOminousBottleAmplifierFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */