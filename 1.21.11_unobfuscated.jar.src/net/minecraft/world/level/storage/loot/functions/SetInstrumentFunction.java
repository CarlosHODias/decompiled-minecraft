/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Instrument;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetInstrumentFunction extends LootItemConditionalFunction {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)TagKey.hashedCodec(Registries.INSTRUMENT).fieldOf("options").forGetter(())).apply((Applicative)i, SetInstrumentFunction::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetInstrumentFunction> CODEC;
/*    */   private final TagKey<Instrument> options;
/*    */   
/*    */   private SetInstrumentFunction(List<LootItemCondition> predicates, TagKey<Instrument> options) {
/* 27 */     super(predicates);
/* 28 */     this.options = options;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetInstrumentFunction> getType() {
/* 33 */     return LootItemFunctions.SET_INSTRUMENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 38 */     Registry<Instrument> instruments = context.getLevel().registryAccess().lookupOrThrow(Registries.INSTRUMENT);
/* 39 */     Optional<Holder<Instrument>> instrument = instruments.getRandomElementOf(this.options, context.getRandom());
/* 40 */     if (instrument.isPresent()) {
/* 41 */       itemStack.set(net.minecraft.core.component.DataComponents.INSTRUMENT, new InstrumentComponent(instrument.get()));
/*    */     }
/* 43 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setInstrumentOptions(TagKey<Instrument> options) {
/* 47 */     return simpleBuilder(conditions -> new SetInstrumentFunction(conditions, options));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetInstrumentFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */