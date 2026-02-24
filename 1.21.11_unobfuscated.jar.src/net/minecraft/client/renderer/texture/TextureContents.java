/*    */ package net.minecraft.client.renderer.texture;
/*    */ public final class TextureContents extends Record implements java.io.Closeable {
/*    */   private final com.mojang.blaze3d.platform.NativeImage image;
/*    */   private final net.minecraft.client.resources.metadata.texture.TextureMetadataSection metadata;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/TextureContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/TextureContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/*    */   }
/*    */   
/* 14 */   public TextureContents(com.mojang.blaze3d.platform.NativeImage image, net.minecraft.client.resources.metadata.texture.TextureMetadataSection metadata) { this.image = image; this.metadata = metadata; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/TextureContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/TextureContents;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public com.mojang.blaze3d.platform.NativeImage image() { return this.image; } public net.minecraft.client.resources.metadata.texture.TextureMetadataSection metadata() { return this.metadata; }
/*    */ 
/*    */   
/*    */   public static TextureContents load(net.minecraft.server.packs.resources.ResourceManager resourceManager, net.minecraft.resources.Identifier location) throws java.io.IOException {
/*    */     com.mojang.blaze3d.platform.NativeImage image;
/* 19 */     net.minecraft.server.packs.resources.Resource resource = resourceManager.getResourceOrThrow(location);
/*    */ 
/*    */     
/* 22 */     java.io.InputStream is = resource.open(); 
/* 23 */     try { image = com.mojang.blaze3d.platform.NativeImage.read(is);
/* 24 */       if (is != null) is.close();  } catch (Throwable throwable) { if (is != null)
/*    */         try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 26 */      net.minecraft.client.resources.metadata.texture.TextureMetadataSection metadata = resource.metadata().getSection(net.minecraft.client.resources.metadata.texture.TextureMetadataSection.TYPE).orElse(null);
/* 27 */     return new TextureContents(image, metadata);
/*    */   }
/*    */   
/*    */   public static TextureContents createMissing() {
/* 31 */     return new TextureContents(MissingTextureAtlasSprite.generateMissingImage(), null);
/*    */   }
/*    */   
/*    */   public boolean blur() {
/* 35 */     return (this.metadata != null) ? this.metadata.blur() : false;
/*    */   }
/*    */   
/*    */   public boolean clamp() {
/* 39 */     return (this.metadata != null) ? this.metadata.clamp() : false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 44 */     this.image.close();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/TextureContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */