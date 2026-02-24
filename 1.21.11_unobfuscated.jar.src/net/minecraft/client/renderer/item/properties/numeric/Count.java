/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class Count extends Record implements RangeSelectItemModelProperty {
/*    */   private final boolean normalize;
/*    */   public static final com.mojang.serialization.MapCodec<Count> MAP_CODEC;
/*    */   
/* 12 */   public Count(boolean normalize) { this.normalize = normalize; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/Count;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Count; } public boolean normalize() { return this.normalize; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/Count;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Count; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/Count;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/Count;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("normalize", true).forGetter(Count::normalize)).apply((com.mojang.datafixers.kinds.Applicative)i, Count::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.ItemOwner owner, int seed) {
/* 19 */     float count = itemStack.getCount();
/* 20 */     float maxCount = itemStack.getMaxStackSize();
/*    */     
/* 22 */     if (this.normalize) {
/* 23 */       return Mth.clamp(count / maxCount, 0.0F, 1.0F);
/*    */     }
/* 25 */     return Mth.clamp(count, 0.0F, maxCount);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<Count> type() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/Count.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */