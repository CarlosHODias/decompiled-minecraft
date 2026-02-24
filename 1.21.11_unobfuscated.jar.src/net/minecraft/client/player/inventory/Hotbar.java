/*    */ package net.minecraft.client.player.inventory;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class Hotbar {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 25 */   private static final int SIZE = Inventory.getSelectionSize(); public static final Codec<Hotbar> CODEC;
/*    */   
/*    */   static {
/* 28 */     CODEC = Codec.PASSTHROUGH.listOf().validate(list -> Util.fixedSize(list, SIZE)).xmap(Hotbar::new, hotbar -> hotbar.items);
/*    */   }
/* 30 */   private static final DynamicOps<Tag> DEFAULT_OPS = (DynamicOps<Tag>)NbtOps.INSTANCE;
/* 31 */   private static final Dynamic<?> EMPTY_STACK = new Dynamic(DEFAULT_OPS, 
/* 32 */       ItemStack.OPTIONAL_CODEC.encodeStart(DEFAULT_OPS, ItemStack.EMPTY).getOrThrow());
/*    */ 
/*    */   
/*    */   private List<Dynamic<?>> items;
/*    */ 
/*    */ 
/*    */   
/*    */   private Hotbar(List<Dynamic<?>> items) {
/* 40 */     this.items = items;
/*    */   }
/*    */   
/*    */   public Hotbar() {
/* 44 */     this(Collections.nCopies(SIZE, EMPTY_STACK));
/*    */   }
/*    */   
/*    */   public List<ItemStack> load(HolderLookup.Provider registries) {
/* 48 */     return this.items.stream()
/* 49 */       .map(dynamic -> (ItemStack)ItemStack.OPTIONAL_CODEC.parse(RegistryOps.injectRegistryContext(dynamic, registries)).resultOrPartial(()).orElse(ItemStack.EMPTY))
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 54 */       .toList();
/*    */   }
/*    */   
/*    */   public void storeFrom(Inventory inventory, RegistryAccess lookupProvider) {
/* 58 */     RegistryOps<Tag> registryOps = lookupProvider.createSerializationContext(DEFAULT_OPS);
/* 59 */     ImmutableList.Builder<Dynamic<?>> newItems = ImmutableList.builderWithExpectedSize(SIZE);
/* 60 */     for (int i = 0; i < SIZE; i++) {
/* 61 */       ItemStack item = inventory.getItem(i);
/* 62 */       Optional<Dynamic<?>> result = ItemStack.OPTIONAL_CODEC.encodeStart((DynamicOps)registryOps, item)
/* 63 */         .resultOrPartial(error -> LOGGER.warn("Could not encode hotbar item: {}", error))
/* 64 */         .map(tag -> new Dynamic(DEFAULT_OPS, tag));
/* 65 */       newItems.add(result.orElse(EMPTY_STACK));
/*    */     } 
/* 67 */     this.items = (List<Dynamic<?>>)newItems.build();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 71 */     for (Dynamic<?> item : this.items) {
/* 72 */       if (!isEmpty(item)) {
/* 73 */         return false;
/*    */       }
/*    */     } 
/* 76 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isEmpty(Dynamic<?> item) {
/* 80 */     return EMPTY_STACK.equals(item);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/player/inventory/Hotbar.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */