/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetItemFunction extends LootItemConditionalFunction {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Item.CODEC.fieldOf("item").forGetter(())).apply((Applicative)i, SetItemFunction::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetItemFunction> CODEC;
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private SetItemFunction(List<LootItemCondition> predicates, Holder<Item> item) {
/* 21 */     super(predicates);
/* 22 */     this.item = item;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetItemFunction> getType() {
/* 27 */     return LootItemFunctions.SET_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 32 */     return itemStack.transmuteCopy((net.minecraft.world.level.ItemLike)this.item.value());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetItemFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */