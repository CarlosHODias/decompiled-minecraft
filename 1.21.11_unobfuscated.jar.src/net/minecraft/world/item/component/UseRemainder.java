/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class UseRemainder extends Record {
/*  8 */   public UseRemainder(ItemStack convertInto) { this.convertInto = convertInto; } private final ItemStack convertInto; public ItemStack convertInto() { return this.convertInto; }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseRemainder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseRemainder;
/*    */   }
/*    */   
/* 14 */   public static final com.mojang.serialization.Codec<UseRemainder> CODEC = ItemStack.CODEC.xmap(UseRemainder::new, UseRemainder::convertInto);
/*    */   
/* 16 */   public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, UseRemainder> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, UseRemainder::convertInto, UseRemainder::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack convertIntoRemainder(ItemStack usedStack, int stackCountBeforeUsing, boolean hasInfiniteMaterials, OnExtraCreatedRemainder onExtraCreatedRemainder) {
/* 23 */     if (hasInfiniteMaterials) {
/* 24 */       return usedStack;
/*    */     }
/*    */     
/* 27 */     if (usedStack.getCount() >= stackCountBeforeUsing) {
/* 28 */       return usedStack;
/*    */     }
/*    */     
/* 31 */     ItemStack remainderStack = this.convertInto.copy();
/*    */     
/* 33 */     if (usedStack.isEmpty()) {
/* 34 */       return remainderStack;
/*    */     }
/*    */     
/* 37 */     onExtraCreatedRemainder.apply(remainderStack);
/*    */     
/* 39 */     return usedStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 44 */     if (this == o) return true; 
/* 45 */     if (o == null || getClass() != o.getClass()) return false; 
/* 46 */     UseRemainder that = (UseRemainder)o;
/* 47 */     return ItemStack.matches(this.convertInto, that.convertInto);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     return ItemStack.hashItemAndComponents(this.convertInto);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface OnExtraCreatedRemainder {
/*    */     void apply(ItemStack param1ItemStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/UseRemainder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */