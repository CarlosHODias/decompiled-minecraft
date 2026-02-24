/*    */ package net.minecraft.world;
/*    */ 
/*    */ public final class ItemStackWithSlot extends Record {
/*    */   private final int slot;
/*    */   private final net.minecraft.world.item.ItemStack stack;
/*    */   public static final com.mojang.serialization.Codec<ItemStackWithSlot> CODEC;
/*    */   
/*  8 */   public ItemStackWithSlot(int slot, net.minecraft.world.item.ItemStack stack) { this.slot = slot; this.stack = stack; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/ItemStackWithSlot;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot; } public int slot() { return this.slot; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/ItemStackWithSlot;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/ItemStackWithSlot; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/ItemStackWithSlot;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/ItemStackWithSlot;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.item.ItemStack stack() { return this.stack; }
/*    */ 
/*    */   
/*    */   static {
/* 12 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.UNSIGNED_BYTE.fieldOf("Slot").orElse(0).forGetter(ItemStackWithSlot::slot), (com.mojang.datafixers.kinds.App)net.minecraft.world.item.ItemStack.MAP_CODEC.forGetter(ItemStackWithSlot::stack)).apply((com.mojang.datafixers.kinds.Applicative)i, ItemStackWithSlot::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidInContainer(int containerSize) {
/* 19 */     return (this.slot >= 0 && this.slot < containerSize);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ItemStackWithSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */