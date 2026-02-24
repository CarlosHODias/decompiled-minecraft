/*    */ package net.minecraft.client.gui.font.providers;
/*    */ 
/*    */ 
/*    */ public final class ProviderReferenceDefinition extends Record implements GlyphProviderDefinition {
/*    */   private final net.minecraft.resources.Identifier id;
/*    */   public static final com.mojang.serialization.MapCodec<ProviderReferenceDefinition> CODEC;
/*    */   
/*  8 */   public ProviderReferenceDefinition(net.minecraft.resources.Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition; } public net.minecraft.resources.Identifier id() { return this.id; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/ProviderReferenceDefinition;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.resources.Identifier.CODEC.fieldOf("id").forGetter(ProviderReferenceDefinition::id)).apply((com.mojang.datafixers.kinds.Applicative)i, ProviderReferenceDefinition::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GlyphProviderType type() {
/* 15 */     return GlyphProviderType.REFERENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.datafixers.util.Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 20 */     return com.mojang.datafixers.util.Either.right(new GlyphProviderDefinition.Reference(this.id));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/providers/ProviderReferenceDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */