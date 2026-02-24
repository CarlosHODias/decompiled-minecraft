/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EmptyLootItem extends LootPoolSingletonContainer {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> singletonFields(i).apply((Applicative)i, EmptyLootItem::new));
/*    */   } public static final com.mojang.serialization.MapCodec<EmptyLootItem> CODEC;
/*    */   private EmptyLootItem(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 17 */     super(weight, quality, conditions, functions);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 22 */     return LootPoolEntries.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, net.minecraft.world.level.storage.loot.LootContext context) {}
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> emptyItem() {
/* 30 */     return simpleBuilder(EmptyLootItem::new);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/EmptyLootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */