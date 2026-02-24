/*    */ package net.minecraft.client.renderer.texture.atlas.sources;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class DirectoryLister extends Record implements net.minecraft.client.renderer.texture.atlas.SpriteSource {
/*    */   private final String sourcePath;
/*    */   private final String idPrefix;
/*    */   public static final com.mojang.serialization.MapCodec<DirectoryLister> MAP_CODEC;
/*    */   
/* 11 */   public DirectoryLister(String sourcePath, String idPrefix) { this.sourcePath = sourcePath; this.idPrefix = idPrefix; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister; } public String sourcePath() { return this.sourcePath; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/DirectoryLister;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public String idPrefix() { return this.idPrefix; }
/*    */ 
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("source").forGetter(DirectoryLister::sourcePath), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("prefix").forGetter(DirectoryLister::idPrefix)).apply((com.mojang.datafixers.kinds.Applicative)i, DirectoryLister::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run(net.minecraft.server.packs.resources.ResourceManager resourceManager, net.minecraft.client.renderer.texture.atlas.SpriteSource.Output output) {
/* 22 */     net.minecraft.resources.FileToIdConverter converter = new net.minecraft.resources.FileToIdConverter("textures/" + this.sourcePath, ".png");
/*    */     
/* 24 */     converter.listMatchingResources(resourceManager).forEach((identifier, resource) -> {
/*    */           Identifier spriteLocation = converter.fileToId(output).withPrefix(this.idPrefix);
/*    */           converter.add(spriteLocation, resource);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<DirectoryLister> codec() {
/* 32 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/DirectoryLister.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */