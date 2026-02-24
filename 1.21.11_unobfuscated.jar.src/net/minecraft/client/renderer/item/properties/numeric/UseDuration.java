/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class UseDuration extends Record implements RangeSelectItemModelProperty {
/*    */   private final boolean remaining;
/*    */   public static final com.mojang.serialization.MapCodec<UseDuration> MAP_CODEC;
/*    */   
/* 12 */   public UseDuration(boolean remaining) { this.remaining = remaining; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration; } public boolean remaining() { return this.remaining; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseDuration;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("remaining", false).forGetter(UseDuration::remaining)).apply((com.mojang.datafixers.kinds.Applicative)i, UseDuration::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.ItemOwner owner, int seed) {
/* 19 */     LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/* 20 */     if (entity == null || entity.getUseItem() != itemStack) {
/* 21 */       return 0.0F;
/*    */     }
/*    */     
/* 24 */     return this.remaining ? entity.getUseItemRemainingTicks() : useDuration(itemStack, entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<UseDuration> type() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public static int useDuration(ItemStack itemStack, LivingEntity owner) {
/* 33 */     return itemStack.getUseDuration(owner) - owner.getUseItemRemainingTicks();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/UseDuration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */