/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ 
/*     */ public final class ChargedProjectiles implements TooltipProvider {
/*  20 */   public static final ChargedProjectiles EMPTY = new ChargedProjectiles(List.of()); public static final Codec<ChargedProjectiles> CODEC;
/*     */   static {
/*  22 */     CODEC = ItemStack.CODEC.listOf().xmap(ChargedProjectiles::new, projectiles -> projectiles.items);
/*     */ 
/*     */     
/*  25 */     STREAM_CODEC = ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ChargedProjectiles::new, projectiles -> projectiles.items);
/*     */   }
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> STREAM_CODEC; private final List<ItemStack> items;
/*     */   
/*     */   private ChargedProjectiles(List<ItemStack> items) {
/*  30 */     this.items = items;
/*     */   }
/*     */   
/*     */   public static ChargedProjectiles of(ItemStack itemStack) {
/*  34 */     return new ChargedProjectiles(List.of(itemStack.copy()));
/*     */   }
/*     */   
/*     */   public static ChargedProjectiles of(List<ItemStack> items) {
/*  38 */     return new ChargedProjectiles(List.copyOf(Lists.transform(items, ItemStack::copy)));
/*     */   }
/*     */   
/*     */   public boolean contains(Item item) {
/*  42 */     for (ItemStack projectile : this.items) {
/*  43 */       if (projectile.is(item)) {
/*  44 */         return true;
/*     */       }
/*     */     } 
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public List<ItemStack> getItems() {
/*  51 */     return Lists.transform(this.items, ItemStack::copy);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  55 */     return this.items.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  60 */     if (this == obj) {
/*  61 */       return true;
/*     */     }
/*  63 */     if (obj instanceof ChargedProjectiles) { ChargedProjectiles projectiles = (ChargedProjectiles)obj; if (ItemStack.listMatches(this.items, projectiles.items)); }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  68 */     return ItemStack.hashStackList(this.items);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  73 */     return "ChargedProjectiles[items=" + String.valueOf(this.items) + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/*  78 */     ItemStack current = null;
/*  79 */     int count = 0;
/*  80 */     for (ItemStack projectile : this.items) {
/*  81 */       if (current == null) {
/*  82 */         current = projectile;
/*  83 */         count = 1; continue;
/*  84 */       }  if (ItemStack.matches(current, projectile)) {
/*  85 */         count++; continue;
/*     */       } 
/*  87 */       addProjectileTooltip(context, consumer, current, count);
/*  88 */       current = projectile;
/*  89 */       count = 1;
/*     */     } 
/*     */     
/*  92 */     if (current != null) {
/*  93 */       addProjectileTooltip(context, consumer, current, count);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addProjectileTooltip(Item.TooltipContext context, Consumer<Component> consumer, ItemStack projectile, int count) {
/*  98 */     if (count == 1) {
/*  99 */       consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.single", new Object[] { projectile.getDisplayName() }));
/*     */     } else {
/* 101 */       consumer.accept(Component.translatable("item.minecraft.crossbow.projectile.multiple", new Object[] { count, projectile.getDisplayName() }));
/*     */     } 
/* 103 */     TooltipDisplay projectileDisplay = (TooltipDisplay)projectile.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
/* 104 */     projectile.addDetailsToTooltip(context, projectileDisplay, null, (TooltipFlag)TooltipFlag.NORMAL, line -> consumer.accept(Component.literal("  ").append(line).withStyle(ChatFormatting.GRAY)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/ChargedProjectiles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */