/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class TagEntry extends LootPoolSingletonContainer {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TagKey.codec(Registries.ITEM).fieldOf("name").forGetter(()), (App)Codec.BOOL.fieldOf("expand").forGetter(())).and(singletonFields(i)).apply((Applicative)i, TagEntry::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<TagEntry> CODEC;
/*    */   private final TagKey<Item> tag;
/*    */   private final boolean expand;
/*    */   
/*    */   private TagEntry(TagKey<Item> tag, boolean expand, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 29 */     super(weight, quality, conditions, functions);
/* 30 */     this.tag = tag;
/* 31 */     this.expand = expand;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootPoolEntryType getType() {
/* 36 */     return LootPoolEntries.TAG;
/*    */   }
/*    */ 
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 41 */     net.minecraft.core.registries.BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).forEach(item -> output.accept(new ItemStack(item)));
/*    */   }
/*    */   
/*    */   private boolean expandTag(LootContext context, Consumer<LootPoolEntry> output) {
/* 45 */     if (canRun(context)) {
/* 46 */       for (Holder<Item> item : (Iterable<Holder<Item>>)net.minecraft.core.registries.BuiltInRegistries.ITEM.getTagOrEmpty(this.tag)) {
/* 47 */         output.accept(new LootPoolSingletonContainer.EntryBase(this)
/*    */             {
/*    */               public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 50 */                 output.accept(new ItemStack(item));
/*    */               }
/*    */             });
/*    */       } 
/* 54 */       return true;
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean expand(LootContext context, Consumer<LootPoolEntry> output) {
/* 61 */     if (this.expand) {
/* 62 */       return expandTag(context, output);
/*    */     }
/* 64 */     return super.expand(context, output);
/*    */   }
/*    */ 
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> tagContents(TagKey<Item> tag) {
/* 69 */     return simpleBuilder((weight, quality, conditions, functions) -> new TagEntry(tag, false, weight, quality, conditions, functions));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> expandTag(TagKey<Item> tag) {
/* 73 */     return simpleBuilder((weight, quality, conditions, functions) -> new TagEntry(tag, true, weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/entries/TagEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */