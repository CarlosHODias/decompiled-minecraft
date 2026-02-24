/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootItem extends LootPoolSingletonContainer {
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("name").forGetter(())).and(singletonFields(i)).apply((Applicative)i, LootItem::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<LootItem> CODEC;
/*    */   private final Holder<Item> item;
/*    */   
/*    */   private LootItem(Holder<Item> item, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 24 */     super(weight, quality, conditions, functions);
/* 25 */     this.item = item;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 30 */     return LootPoolEntries.ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, net.minecraft.world.level.storage.loot.LootContext context) {
/* 35 */     output.accept(new ItemStack(this.item));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike item) {
/* 39 */     return simpleBuilder((weight, quality, conditions, functions) -> new LootItem((Holder<Item>)item.asItem().builtInRegistryHolder(), weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/LootItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */