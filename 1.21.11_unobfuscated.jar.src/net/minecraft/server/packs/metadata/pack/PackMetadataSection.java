/*    */ package net.minecraft.server.packs.metadata.pack;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ 
/*    */ public final class PackMetadataSection extends Record {
/*    */   private final net.minecraft.network.chat.Component description;
/*    */   private final net.minecraft.util.InclusiveRange<PackFormat> supportedFormats;
/*    */   private static final com.mojang.serialization.Codec<PackMetadataSection> FALLBACK_CODEC;
/*    */   
/* 12 */   public PackMetadataSection(net.minecraft.network.chat.Component description, net.minecraft.util.InclusiveRange<PackFormat> supportedFormats) { this.description = description; this.supportedFormats = supportedFormats; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection; } public net.minecraft.network.chat.Component description() { return this.description; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/metadata/pack/PackMetadataSection;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.InclusiveRange<PackFormat> supportedFormats() { return this.supportedFormats; } static {
/* 13 */     FALLBACK_CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("description").forGetter(PackMetadataSection::description)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ 
/*    */   
/*    */   private static com.mojang.serialization.Codec<PackMetadataSection> codecForPackType(PackType packType) {
/* 18 */     return RecordCodecBuilder.create(i -> i.group((App)net.minecraft.network.chat.ComponentSerialization.CODEC.fieldOf("description").forGetter(PackMetadataSection::description), (App)PackFormat.packCodec(packType).forGetter(PackMetadataSection::supportedFormats)).apply((com.mojang.datafixers.kinds.Applicative)i, PackMetadataSection::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<PackMetadataSection> CLIENT_TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("pack", codecForPackType(PackType.CLIENT_RESOURCES));
/* 25 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<PackMetadataSection> SERVER_TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("pack", codecForPackType(PackType.SERVER_DATA));
/* 26 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<PackMetadataSection> FALLBACK_TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("pack", FALLBACK_CODEC);
/*    */   
/*    */   public static net.minecraft.server.packs.metadata.MetadataSectionType<PackMetadataSection> forPackType(PackType packType) {
/* 29 */     switch (packType) { default: throw new MatchException(null, null);case CLIENT_RESOURCES: case SERVER_DATA: break; }  return 
/*    */       
/* 31 */       SERVER_TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/metadata/pack/PackMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */