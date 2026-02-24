/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DiscardItem extends LootItemConditionalFunction {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply((Applicative)i, DiscardItem::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<DiscardItem> CODEC;
/*    */   
/*    */   protected DiscardItem(List<LootItemCondition> predicates) {
/* 17 */     super(predicates);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<DiscardItem> getType() {
/* 22 */     return LootItemFunctions.DISCARD;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 27 */     return ItemStack.EMPTY;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> discardItem() {
/* 31 */     return simpleBuilder(DiscardItem::new);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/DiscardItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */