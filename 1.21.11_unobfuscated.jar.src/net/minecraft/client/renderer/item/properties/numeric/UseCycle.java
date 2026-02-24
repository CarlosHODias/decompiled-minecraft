/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class UseCycle extends Record implements RangeSelectItemModelProperty {
/*    */   private final float period;
/*    */   public static final com.mojang.serialization.MapCodec<UseCycle> MAP_CODEC;
/*    */   
/* 12 */   public UseCycle(float period) { this.period = period; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle; } public float period() { return this.period; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/numeric/UseCycle;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("period", 1.0F).forGetter(UseCycle::period)).apply((com.mojang.datafixers.kinds.Applicative)i, UseCycle::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float get(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, ItemOwner owner, int seed) {
/* 19 */     LivingEntity entity = (owner == null) ? null : owner.asLivingEntity();
/* 20 */     if (entity == null || entity.getUseItem() != itemStack) {
/* 21 */       return 0.0F;
/*    */     }
/*    */     
/* 24 */     return entity.getUseItemRemainingTicks() % this.period;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<UseCycle> type() {
/* 29 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/UseCycle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */