/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class MainHand extends Record implements SelectItemModelProperty<HumanoidArm> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/MainHand;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/MainHand;
/*    */   }
/*    */   
/* 13 */   public static final com.mojang.serialization.Codec<HumanoidArm> VALUE_CODEC = HumanoidArm.CODEC;
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/MainHand;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/MainHand; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/MainHand;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/MainHand;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public static final SelectItemModelProperty.Type<MainHand, HumanoidArm> TYPE = SelectItemModelProperty.Type.create(com.mojang.serialization.MapCodec.unit(new MainHand()), VALUE_CODEC);
/*    */ 
/*    */   
/*    */   public HumanoidArm get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/* 19 */     return (owner == null) ? null : owner.getMainArm();
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<MainHand, HumanoidArm> type() {
/* 24 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.Codec<HumanoidArm> valueCodec() {
/* 29 */     return VALUE_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/MainHand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */