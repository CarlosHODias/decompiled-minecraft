/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ 
/*    */ public final class UseEffects extends Record {
/*    */   private final boolean canSprint;
/*    */   private final boolean interactVibrations;
/*    */   private final float speedMultiplier;
/*    */   
/*  9 */   public UseEffects(boolean canSprint, boolean interactVibrations, float speedMultiplier) { this.canSprint = canSprint; this.interactVibrations = interactVibrations; this.speedMultiplier = speedMultiplier; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/component/UseEffects;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects; } public boolean canSprint() { return this.canSprint; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/component/UseEffects;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/component/UseEffects; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/component/UseEffects;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/component/UseEffects;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean interactVibrations() { return this.interactVibrations; } public float speedMultiplier() { return this.speedMultiplier; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F); public static final com.mojang.serialization.Codec<UseEffects> CODEC;
/*    */   static {
/* 16 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("can_sprint", DEFAULT.canSprint).forGetter(UseEffects::canSprint), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("interact_vibrations", DEFAULT.interactVibrations).forGetter(UseEffects::interactVibrations), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed_multiplier", DEFAULT.speedMultiplier).forGetter(UseEffects::speedMultiplier)).apply((com.mojang.datafixers.kinds.Applicative)i, UseEffects::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, UseEffects> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.BOOL, UseEffects::canSprint, net.minecraft.network.codec.ByteBufCodecs.BOOL, UseEffects::interactVibrations, net.minecraft.network.codec.ByteBufCodecs.FLOAT, UseEffects::speedMultiplier, UseEffects::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/UseEffects.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */