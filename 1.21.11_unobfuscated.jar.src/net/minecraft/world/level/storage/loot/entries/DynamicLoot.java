/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DynamicLoot extends LootPoolSingletonContainer {
/*    */   static {
/* 15 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("name").forGetter(())).and(singletonFields(i)).apply((Applicative)i, DynamicLoot::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<DynamicLoot> CODEC;
/*    */   private final Identifier name;
/*    */   
/*    */   private DynamicLoot(Identifier name, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 22 */     super(weight, quality, conditions, functions);
/* 23 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 28 */     return LootPoolEntries.DYNAMIC;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, net.minecraft.world.level.storage.loot.LootContext context) {
/* 33 */     context.addDynamicDrops(this.name, output);
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> dynamicEntry(Identifier name) {
/* 37 */     return simpleBuilder((weight, quality, conditions, functions) -> new DynamicLoot(name, weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/DynamicLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */