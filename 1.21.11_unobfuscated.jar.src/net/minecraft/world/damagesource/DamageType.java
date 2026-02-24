/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ 
/*    */ public final class DamageType extends Record {
/*    */   private final String msgId;
/*    */   private final DamageScaling scaling;
/*    */   private final float exhaustion;
/*    */   private final DamageEffects effects;
/*    */   private final DeathMessageType deathMessageType;
/*    */   public static final com.mojang.serialization.Codec<DamageType> DIRECT_CODEC;
/*    */   
/* 12 */   public DamageType(String msgId, DamageScaling scaling, float exhaustion, DamageEffects effects, DeathMessageType deathMessageType) { this.msgId = msgId; this.scaling = scaling; this.exhaustion = exhaustion; this.effects = effects; this.deathMessageType = deathMessageType; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/DamageType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType; } public String msgId() { return this.msgId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/DamageType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/damagesource/DamageType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/DamageType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/damagesource/DamageType;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public DamageScaling scaling() { return this.scaling; } public float exhaustion() { return this.exhaustion; } public DamageEffects effects() { return this.effects; } public DeathMessageType deathMessageType() { return this.deathMessageType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 19 */     DIRECT_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("message_id").forGetter(DamageType::msgId), (com.mojang.datafixers.kinds.App)DamageScaling.CODEC.fieldOf("scaling").forGetter(DamageType::scaling), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.fieldOf("exhaustion").forGetter(DamageType::exhaustion), (com.mojang.datafixers.kinds.App)DamageEffects.CODEC.optionalFieldOf("effects", DamageEffects.HURT).forGetter(DamageType::effects), (com.mojang.datafixers.kinds.App)DeathMessageType.CODEC.optionalFieldOf("death_message_type", DeathMessageType.DEFAULT).forGetter(DamageType::deathMessageType)).apply((com.mojang.datafixers.kinds.Applicative)i, DamageType::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final com.mojang.serialization.Codec<net.minecraft.core.Holder<DamageType>> CODEC = (com.mojang.serialization.Codec<net.minecraft.core.Holder<DamageType>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.DAMAGE_TYPE);
/* 28 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.Holder<DamageType>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.DAMAGE_TYPE);
/*    */   
/*    */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion) {
/* 31 */     this(msgdId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, DamageScaling scaling, float exhaustion, DamageEffects effects) {
/* 35 */     this(msgdId, scaling, exhaustion, effects, DeathMessageType.DEFAULT);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, float exhaustion, DamageEffects effects) {
/* 39 */     this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, effects);
/*    */   }
/*    */   
/*    */   public DamageType(String msgdId, float exhaustion) {
/* 43 */     this(msgdId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/damagesource/DamageType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */