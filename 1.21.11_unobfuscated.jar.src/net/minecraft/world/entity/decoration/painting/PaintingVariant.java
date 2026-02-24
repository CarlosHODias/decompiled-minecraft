/*    */ package net.minecraft.world.entity.decoration.painting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class PaintingVariant extends Record {
/*    */   private final int width;
/*    */   private final int height;
/*    */   private final Identifier assetId;
/*    */   private final Optional<Component> title;
/*    */   private final Optional<Component> author;
/*    */   public static final com.mojang.serialization.Codec<PaintingVariant> DIRECT_CODEC;
/*    */   
/* 18 */   public PaintingVariant(int width, int height, Identifier assetId, Optional<Component> title, Optional<Component> author) { this.width = width; this.height = height; this.assetId = assetId; this.title = title; this.author = author; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/decoration/painting/PaintingVariant;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public Identifier assetId() { return this.assetId; } public Optional<Component> title() { return this.title; } public Optional<Component> author() { return this.author; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 25 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.ExtraCodecs.intRange(1, 16).fieldOf("width").forGetter(PaintingVariant::width), (App)net.minecraft.util.ExtraCodecs.intRange(1, 16).fieldOf("height").forGetter(PaintingVariant::height), (App)Identifier.CODEC.fieldOf("asset_id").forGetter(PaintingVariant::assetId), (App)ComponentSerialization.CODEC.optionalFieldOf("title").forGetter(PaintingVariant::title), (App)ComponentSerialization.CODEC.optionalFieldOf("author").forGetter(PaintingVariant::author)).apply((com.mojang.datafixers.kinds.Applicative)i, PaintingVariant::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PaintingVariant> DIRECT_STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, PaintingVariant::width, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, PaintingVariant::height, Identifier.STREAM_CODEC, PaintingVariant::assetId, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::title, ComponentSerialization.TRUSTED_OPTIONAL_STREAM_CODEC, PaintingVariant::author, PaintingVariant::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final com.mojang.serialization.Codec<net.minecraft.core.Holder<PaintingVariant>> CODEC = (com.mojang.serialization.Codec<net.minecraft.core.Holder<PaintingVariant>>)net.minecraft.resources.RegistryFixedCodec.create(net.minecraft.core.registries.Registries.PAINTING_VARIANT);
/*    */   
/* 44 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, net.minecraft.core.Holder<PaintingVariant>> STREAM_CODEC = net.minecraft.network.codec.ByteBufCodecs.holder(net.minecraft.core.registries.Registries.PAINTING_VARIANT, DIRECT_STREAM_CODEC);
/*    */   
/*    */   public int area() {
/* 47 */     return width() * height();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/decoration/painting/PaintingVariant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */