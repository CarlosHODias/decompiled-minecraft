/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.alchemy.Potion;
/*    */ import net.minecraft.world.item.alchemy.PotionContents;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetPotionFunction extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Potion.CODEC.fieldOf("id").forGetter(())).apply((Applicative)i, SetPotionFunction::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<SetPotionFunction> CODEC;
/*    */   private final Holder<Potion> potion;
/*    */   
/*    */   private SetPotionFunction(List<LootItemCondition> predicates, Holder<Potion> potion) {
/* 23 */     super(predicates);
/* 24 */     this.potion = potion;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetPotionFunction> getType() {
/* 29 */     return LootItemFunctions.SET_POTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 34 */     itemStack.update(net.minecraft.core.component.DataComponents.POTION_CONTENTS, PotionContents.EMPTY, this.potion, PotionContents::withPotion);
/* 35 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setPotion(Holder<Potion> value) {
/* 39 */     return simpleBuilder(conditions -> new SetPotionFunction(conditions, value));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetPotionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */