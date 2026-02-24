/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetItemCountFunction extends LootItemConditionalFunction {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("count").forGetter(()), (App)Codec.BOOL.fieldOf("add").orElse(false).forGetter(()))).apply((Applicative)i, SetItemCountFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetItemCountFunction> CODEC;
/*    */   private final NumberProvider value;
/*    */   private final boolean add;
/*    */   
/*    */   private SetItemCountFunction(List<LootItemCondition> predicates, NumberProvider value, boolean add) {
/* 26 */     super(predicates);
/* 27 */     this.value = value;
/* 28 */     this.add = add;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemCountFunction> getType() {
/* 33 */     return LootItemFunctions.SET_COUNT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 38 */     return this.value.getReferencedContextParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 43 */     int base = this.add ? itemStack.getCount() : 0;
/* 44 */     itemStack.setCount(base + this.value.getInt(context));
/* 45 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider value) {
/* 49 */     return simpleBuilder(conditions -> new SetItemCountFunction(conditions, value, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider value, boolean add) {
/* 53 */     return simpleBuilder(conditions -> new SetItemCountFunction(conditions, value, add));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */