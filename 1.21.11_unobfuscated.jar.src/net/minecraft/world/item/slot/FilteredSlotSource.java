/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.advancements.criterion.ItemPredicate;
/*    */ 
/*    */ public class FilteredSlotSource extends TransformedSlotSource {
/*    */   static {
/*  8 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)ItemPredicate.CODEC.fieldOf("item_filter").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, FilteredSlotSource::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<FilteredSlotSource> MAP_CODEC;
/*    */   private final ItemPredicate filter;
/*    */   
/*    */   private FilteredSlotSource(SlotSource slotSource, ItemPredicate filter) {
/* 15 */     super(slotSource);
/* 16 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<FilteredSlotSource> codec() {
/* 21 */     return MAP_CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SlotCollection transform(SlotCollection slots) {
/* 26 */     return slots.filter((java.util.function.Predicate<net.minecraft.world.item.ItemStack>)this.filter);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/FilteredSlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */