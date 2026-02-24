/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetComponentsFunction extends LootItemConditionalFunction {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)DataComponentPatch.CODEC.fieldOf("components").forGetter(())).apply((Applicative)i, SetComponentsFunction::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetComponentsFunction> CODEC;
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   private SetComponentsFunction(List<LootItemCondition> predicates, DataComponentPatch components) {
/* 21 */     super(predicates);
/* 22 */     this.components = components;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetComponentsFunction> getType() {
/* 27 */     return LootItemFunctions.SET_COMPONENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 32 */     itemStack.applyComponentsAndValidate(this.components);
/* 33 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static <T> LootItemConditionalFunction.Builder<?> setComponent(DataComponentType<T> type, T value) {
/* 37 */     return simpleBuilder(conditions -> new SetComponentsFunction(conditions, DataComponentPatch.builder().set(type, value).build()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetComponentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */