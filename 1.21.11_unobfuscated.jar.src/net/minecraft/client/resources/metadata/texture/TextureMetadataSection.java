/*    */ package net.minecraft.client.resources.metadata.texture;public final class TextureMetadataSection extends Record { private final boolean blur; private final boolean clamp; private final net.minecraft.client.renderer.texture.MipmapStrategy mipmapStrategy;
/*    */   private final float alphaCutoffBias;
/*    */   public static final boolean DEFAULT_BLUR = false;
/*    */   public static final boolean DEFAULT_CLAMP = false;
/*    */   public static final float DEFAULT_ALPHA_CUTOFF_BIAS = 0.0F;
/*    */   public static final com.mojang.serialization.Codec<TextureMetadataSection> CODEC;
/*    */   
/*  8 */   public TextureMetadataSection(boolean blur, boolean clamp, net.minecraft.client.renderer.texture.MipmapStrategy mipmapStrategy, float alphaCutoffBias) { this.blur = blur; this.clamp = clamp; this.mipmapStrategy = mipmapStrategy; this.alphaCutoffBias = alphaCutoffBias; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection; } public boolean blur() { return this.blur; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/texture/TextureMetadataSection;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public boolean clamp() { return this.clamp; } public net.minecraft.client.renderer.texture.MipmapStrategy mipmapStrategy() { return this.mipmapStrategy; } public float alphaCutoffBias() { return this.alphaCutoffBias; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 18 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("blur", false).forGetter(TextureMetadataSection::blur), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("clamp", false).forGetter(TextureMetadataSection::clamp), (com.mojang.datafixers.kinds.App)net.minecraft.client.renderer.texture.MipmapStrategy.CODEC.optionalFieldOf("mipmap_strategy", net.minecraft.client.renderer.texture.MipmapStrategy.AUTO).forGetter(TextureMetadataSection::mipmapStrategy), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.optionalFieldOf("alpha_cutoff_bias", 0.0F).forGetter(TextureMetadataSection::alphaCutoffBias)).apply((com.mojang.datafixers.kinds.Applicative)i, TextureMetadataSection::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<TextureMetadataSection> TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("texture", CODEC); }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/texture/TextureMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */