/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.CustomData;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetCustomDataFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)TagParser.LENIENT_CODEC.fieldOf("tag").forGetter(())).apply((Applicative)i, SetCustomDataFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetCustomDataFunction> CODEC;
/*    */   private final CompoundTag tag;
/*    */   
/*    */   private SetCustomDataFunction(List<LootItemCondition> predicates, CompoundTag tag) {
/* 23 */     super(predicates);
/* 24 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetCustomDataFunction> getType() {
/* 29 */     return LootItemFunctions.SET_CUSTOM_DATA;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 34 */     CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, itemStack, tag -> tag.merge(this.tag));
/* 35 */     return itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static LootItemConditionalFunction.Builder<?> setCustomData(CompoundTag value) {
/* 43 */     return simpleBuilder(conditions -> new SetCustomDataFunction(conditions, value));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetCustomDataFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */