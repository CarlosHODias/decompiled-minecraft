/*    */ package net.minecraft.world.item.equipment.trim;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class TrimPattern extends Record {
/*    */   private final Identifier assetId;
/*    */   private final Component description;
/*    */   private final boolean decal;
/*    */   public static final Codec<TrimPattern> DIRECT_CODEC;
/*    */   
/* 15 */   public TrimPattern(Identifier assetId, Component description, boolean decal) { this.assetId = assetId; this.description = description; this.decal = decal; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern; } public Identifier assetId() { return this.assetId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/equipment/trim/TrimPattern;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/equipment/trim/TrimPattern;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public Component description() { return this.description; } public boolean decal() { return this.decal; } static {
/* 16 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("asset_id").forGetter(TrimPattern::assetId), (App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("description").forGetter(TrimPattern::description), (App)Codec.BOOL.fieldOf("decal").orElse(false).forGetter(TrimPattern::decal)).apply((com.mojang.datafixers.kinds.Applicative)i, TrimPattern::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, TrimPattern> DIRECT_STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(Identifier.STREAM_CODEC, TrimPattern::assetId, net.minecraft.network.chat.ComponentSerialization.STREAM_CODEC, TrimPattern::description, net.minecraft.network.codec.ByteBufCodecs.BOOL, TrimPattern::decal, TrimPattern::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final Codec<net.minecraft.core.Holder<TrimPattern>> CODEC = (Codec<net.minecraft.core.Holder<TrimPattern>>)net.minecraft.resources.RegistryFileCodec.create(net.minecraft.core.registries.Registries.TRIM_PATTERN, DIRECT_CODEC);
/* 30 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.Holder<TrimPattern>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holder(net.minecraft.core.registries.Registries.TRIM_PATTERN, DIRECT_STREAM_CODEC);
/*    */   
/*    */   public Component copyWithStyle(net.minecraft.core.Holder<TrimMaterial> material) {
/* 33 */     return (Component)this.description.copy().withStyle(((TrimMaterial)material.value()).description().getStyle());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/equipment/trim/TrimPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */