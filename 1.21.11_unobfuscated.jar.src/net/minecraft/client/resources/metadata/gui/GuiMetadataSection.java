/*    */ package net.minecraft.client.resources.metadata.gui;
/*    */ 
/*    */ 
/*    */ public final class GuiMetadataSection extends Record {
/*    */   private final GuiSpriteScaling scaling;
/*    */   
/*  7 */   public GuiMetadataSection(GuiSpriteScaling scaling) { this.scaling = scaling; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection; } public GuiSpriteScaling scaling() { return this.scaling; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/gui/GuiMetadataSection;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public static final GuiMetadataSection DEFAULT = new GuiMetadataSection(GuiSpriteScaling.DEFAULT); public static final com.mojang.serialization.Codec<GuiMetadataSection> CODEC;
/*    */   static {
/* 10 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)GuiSpriteScaling.CODEC.optionalFieldOf("scaling", GuiSpriteScaling.DEFAULT).forGetter(GuiMetadataSection::scaling)).apply((com.mojang.datafixers.kinds.Applicative)i, GuiMetadataSection::new));
/*    */   }
/*    */ 
/*    */   
/* 14 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<GuiMetadataSection> TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("gui", CODEC);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/gui/GuiMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */