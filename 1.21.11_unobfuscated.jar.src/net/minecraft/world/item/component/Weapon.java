/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ 
/*    */ public final class Weapon extends Record {
/*    */   private final int itemDamagePerAttack;
/*    */   private final float disableBlockingForSeconds;
/*    */   public static final float AXE_DISABLES_BLOCKING_FOR_SECONDS = 5.0F;
/*    */   public static final com.mojang.serialization.Codec<Weapon> CODEC;
/*    */   
/* 10 */   public Weapon(int itemDamagePerAttack, float disableBlockingForSeconds) { this.itemDamagePerAttack = itemDamagePerAttack; this.disableBlockingForSeconds = disableBlockingForSeconds; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/Weapon;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon; } public int itemDamagePerAttack() { return this.itemDamagePerAttack; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/Weapon;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/Weapon; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/Weapon;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/Weapon;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public float disableBlockingForSeconds() { return this.disableBlockingForSeconds; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 16 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("item_damage_per_attack", 1).forGetter(Weapon::itemDamagePerAttack), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("disable_blocking_for_seconds", 0.0F).forGetter(Weapon::disableBlockingForSeconds)).apply((com.mojang.datafixers.kinds.Applicative)i, Weapon::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, Weapon> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, Weapon::itemDamagePerAttack, net.minecraft.network.codec.ByteBufCodecs.FLOAT, Weapon::disableBlockingForSeconds, Weapon::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Weapon(int damagePerAttack) {
/* 28 */     this(damagePerAttack, 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/Weapon.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */