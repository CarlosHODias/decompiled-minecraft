/*    */ package net.minecraft.world.item.slot;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public interface SlotSources {
/*    */   static {
/* 15 */     TYPED_CODEC = BuiltInRegistries.SLOT_SOURCE_TYPE.byNameCodec().dispatch(SlotSource::codec, c -> c);
/* 16 */   } public static final Codec<SlotSource> CODEC = Codec.lazyInitialized(() -> Codec.withAlternative(TYPED_CODEC, GroupSlotSource.INLINE_CODEC)); public static final Codec<SlotSource> TYPED_CODEC;
/*    */   
/*    */   static MapCodec<? extends SlotSource> bootstrap(Registry<MapCodec<? extends SlotSource>> registry) {
/* 19 */     Registry.register(registry, "group", GroupSlotSource.MAP_CODEC);
/* 20 */     Registry.register(registry, "filtered", FilteredSlotSource.MAP_CODEC);
/* 21 */     Registry.register(registry, "limit_slots", LimitSlotSource.MAP_CODEC);
/* 22 */     Registry.register(registry, "slot_range", RangeSlotSource.MAP_CODEC);
/* 23 */     Registry.register(registry, "contents", ContentsSlotSource.MAP_CODEC);
/* 24 */     return (MapCodec<? extends SlotSource>)Registry.register(registry, "empty", EmptySlotSource.MAP_CODEC);
/*    */   }
/*    */   static Function<LootContext, SlotCollection> group(Collection<? extends SlotSource> list) {
/*    */     SlotSource first, second;
/* 28 */     List<SlotSource> terms = List.copyOf(list);
/* 29 */     switch (terms.size()) { case 0: 
/*    */       case 1:
/* 31 */         Objects.requireNonNull(terms.getFirst());
/*    */       case 2:
/* 33 */         first = terms.get(0);
/* 34 */         second = terms.get(1);
/*    */       default:
/*    */         break; }
/*    */     
/*    */     return context -> {
/*    */         List<SlotCollection> collections = new ArrayList<>();
/*    */         for (SlotSource term : (Iterable<SlotSource>)terms)
/*    */           collections.add(term.provide(context)); 
/*    */         return SlotCollection.concat(collections);
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/slot/SlotSources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */