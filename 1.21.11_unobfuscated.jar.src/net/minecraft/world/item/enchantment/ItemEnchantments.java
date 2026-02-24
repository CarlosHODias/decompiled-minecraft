/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.EnchantmentTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.component.TooltipProvider;
/*     */ 
/*     */ public class ItemEnchantments implements TooltipProvider {
/*  31 */   public static final ItemEnchantments EMPTY = new ItemEnchantments(new Object2IntOpenHashMap());
/*     */   
/*  33 */   private static final Codec<Integer> LEVEL_CODEC = Codec.intRange(1, 255); public static final Codec<ItemEnchantments> CODEC;
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ItemEnchantments> STREAM_CODEC;
/*     */   private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments;
/*     */   
/*     */   static {
/*  38 */     CODEC = Codec.unboundedMap(Enchantment.CODEC, LEVEL_CODEC).xmap(map -> new ItemEnchantments(new Object2IntOpenHashMap(map)), enchantments -> enchantments.enchantments);
/*     */     
/*  40 */     STREAM_CODEC = StreamCodec.composite(
/*  41 */         ByteBufCodecs.map(Object2IntOpenHashMap::new, Enchantment.STREAM_CODEC, ByteBufCodecs.VAR_INT), c -> c.enchantments, ItemEnchantments::new);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ItemEnchantments(Object2IntOpenHashMap<Holder<Enchantment>> enchantments) {
/*  48 */     this.enchantments = enchantments;
/*     */     
/*  50 */     for (ObjectIterator<Object2IntMap.Entry<Holder<Enchantment>>> objectIterator = enchantments.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Holder<Enchantment>> entry = objectIterator.next();
/*  51 */       int level = entry.getIntValue();
/*  52 */       if (level < 0 || level > 255) {
/*  53 */         throw new IllegalArgumentException("Enchantment " + String.valueOf(entry.getKey()) + " has invalid level " + level);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public int getLevel(Holder<Enchantment> enchantment) {
/*  59 */     return this.enchantments.getInt(enchantment);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, net.minecraft.world.item.TooltipFlag flag, DataComponentGetter components) {
/*  64 */     HolderLookup.Provider registries = context.registries();
/*  65 */     HolderSet<Enchantment> order = getTagOrEmpty(registries, Registries.ENCHANTMENT, EnchantmentTags.TOOLTIP_ORDER);
/*  66 */     for (Holder<Enchantment> enchantment : order) {
/*  67 */       int level = this.enchantments.getInt(enchantment);
/*  68 */       if (level > 0) {
/*  69 */         consumer.accept(Enchantment.getFullname(enchantment, level));
/*     */       }
/*     */     } 
/*  72 */     for (ObjectIterator<Object2IntMap.Entry<Holder<Enchantment>>> objectIterator = this.enchantments.object2IntEntrySet().iterator(); objectIterator.hasNext(); ) { Object2IntMap.Entry<Holder<Enchantment>> entry = objectIterator.next();
/*  73 */       Holder<Enchantment> enchantment = (Holder<Enchantment>)entry.getKey();
/*  74 */       if (!order.contains(enchantment)) {
/*  75 */         consumer.accept(Enchantment.getFullname((Holder<Enchantment>)entry.getKey(), entry.getIntValue()));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private static <T> HolderSet<T> getTagOrEmpty(HolderLookup.Provider registries, ResourceKey<Registry<T>> registry, TagKey<T> tag) {
/*  81 */     if (registries != null) {
/*  82 */       Optional<HolderSet.Named<T>> maybeOrder = registries.lookupOrThrow(registry).get(tag);
/*  83 */       if (maybeOrder.isPresent()) {
/*  84 */         return (HolderSet<T>)maybeOrder.get();
/*     */       }
/*     */     } 
/*  87 */     return (HolderSet<T>)HolderSet.direct(new Holder[0]);
/*     */   }
/*     */   
/*     */   public Set<Holder<Enchantment>> keySet() {
/*  91 */     return Collections.unmodifiableSet((Set<? extends Holder<Enchantment>>)this.enchantments.keySet());
/*     */   }
/*     */   
/*     */   public Set<Object2IntMap.Entry<Holder<Enchantment>>> entrySet() {
/*  95 */     return Collections.unmodifiableSet((Set<? extends Object2IntMap.Entry<Holder<Enchantment>>>)this.enchantments.object2IntEntrySet());
/*     */   }
/*     */   
/*     */   public int size() {
/*  99 */     return this.enchantments.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 103 */     return this.enchantments.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (this == obj) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (obj instanceof ItemEnchantments) { ItemEnchantments that = (ItemEnchantments)obj;
/* 112 */       return this.enchantments.equals(that.enchantments); }
/*     */     
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 119 */     return this.enchantments.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return "ItemEnchantments{enchantments=" + String.valueOf(this.enchantments) + "}";
/*     */   }
/*     */   
/*     */   public static class Mutable {
/* 128 */     private final Object2IntOpenHashMap<Holder<Enchantment>> enchantments = new Object2IntOpenHashMap();
/*     */     
/*     */     public Mutable(ItemEnchantments enchantments) {
/* 131 */       this.enchantments.putAll((Map)enchantments.enchantments);
/*     */     }
/*     */     
/*     */     public void set(Holder<Enchantment> enchantment, int level) {
/* 135 */       if (level <= 0) {
/* 136 */         this.enchantments.removeInt(enchantment);
/*     */       } else {
/* 138 */         this.enchantments.put(enchantment, Math.min(level, 255));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void upgrade(Holder<Enchantment> enchantment, int level) {
/* 143 */       if (level > 0) {
/* 144 */         this.enchantments.merge(enchantment, Math.min(level, 255), Integer::max);
/*     */       }
/*     */     }
/*     */     
/*     */     public void removeIf(Predicate<Holder<Enchantment>> predicate) {
/* 149 */       this.enchantments.keySet().removeIf(predicate);
/*     */     }
/*     */     
/*     */     public int getLevel(Holder<Enchantment> enchantment) {
/* 153 */       return this.enchantments.getOrDefault(enchantment, 0);
/*     */     }
/*     */     
/*     */     public Set<Holder<Enchantment>> keySet() {
/* 157 */       return (Set<Holder<Enchantment>>)this.enchantments.keySet();
/*     */     }
/*     */     
/*     */     public ItemEnchantments toImmutable() {
/* 161 */       return new ItemEnchantments(this.enchantments);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/ItemEnchantments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */