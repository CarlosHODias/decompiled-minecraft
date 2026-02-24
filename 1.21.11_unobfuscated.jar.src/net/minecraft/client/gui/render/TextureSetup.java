/*    */ package net.minecraft.client.gui.render;
/*    */ 
/*    */ public final class TextureSetup extends Record {
/*    */   private final com.mojang.blaze3d.textures.GpuTextureView texure0;
/*    */   private final com.mojang.blaze3d.textures.GpuTextureView texure1;
/*    */   private final com.mojang.blaze3d.textures.GpuTextureView texure2;
/*    */   private final com.mojang.blaze3d.textures.GpuSampler sampler0;
/*    */   private final com.mojang.blaze3d.textures.GpuSampler sampler1;
/*    */   private final com.mojang.blaze3d.textures.GpuSampler sampler2;
/*    */   
/* 11 */   public TextureSetup(com.mojang.blaze3d.textures.GpuTextureView texure0, com.mojang.blaze3d.textures.GpuTextureView texure1, com.mojang.blaze3d.textures.GpuTextureView texure2, com.mojang.blaze3d.textures.GpuSampler sampler0, com.mojang.blaze3d.textures.GpuSampler sampler1, com.mojang.blaze3d.textures.GpuSampler sampler2) { this.texure0 = texure0; this.texure1 = texure1; this.texure2 = texure2; this.sampler0 = sampler0; this.sampler1 = sampler1; this.sampler2 = sampler2; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/TextureSetup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/client/gui/render/TextureSetup; } public com.mojang.blaze3d.textures.GpuTextureView texure0() { return this.texure0; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/TextureSetup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/TextureSetup; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/TextureSetup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/TextureSetup;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public com.mojang.blaze3d.textures.GpuTextureView texure1() { return this.texure1; } public com.mojang.blaze3d.textures.GpuTextureView texure2() { return this.texure2; } public com.mojang.blaze3d.textures.GpuSampler sampler0() { return this.sampler0; } public com.mojang.blaze3d.textures.GpuSampler sampler1() { return this.sampler1; } public com.mojang.blaze3d.textures.GpuSampler sampler2() { return this.sampler2; }
/* 12 */    private static final TextureSetup NO_TEXTURE_SETUP = new TextureSetup(null, null, null, null, null, null);
/*    */   private static int sortKeySeed;
/*    */   
/*    */   public static TextureSetup singleTexture(com.mojang.blaze3d.textures.GpuTextureView texture, com.mojang.blaze3d.textures.GpuSampler sampler) {
/* 16 */     return new TextureSetup(texture, null, null, sampler, null, null);
/*    */   }
/*    */   
/*    */   public static TextureSetup singleTextureWithLightmap(com.mojang.blaze3d.textures.GpuTextureView texture, com.mojang.blaze3d.textures.GpuSampler sampler) {
/* 20 */     return new TextureSetup(texture, null, (net.minecraft.client.Minecraft.getInstance()).gameRenderer.lightTexture().getTextureView(), sampler, null, com.mojang.blaze3d.systems.RenderSystem.getSamplerCache().getClampToEdge(com.mojang.blaze3d.textures.FilterMode.LINEAR));
/*    */   }
/*    */   
/*    */   public static TextureSetup doubleTexture(com.mojang.blaze3d.textures.GpuTextureView texture0, com.mojang.blaze3d.textures.GpuSampler sampler0, com.mojang.blaze3d.textures.GpuTextureView texture1, com.mojang.blaze3d.textures.GpuSampler sampler1) {
/* 24 */     return new TextureSetup(texture0, texture1, null, sampler0, sampler1, null);
/*    */   }
/*    */   
/*    */   public static TextureSetup noTexture() {
/* 28 */     return NO_TEXTURE_SETUP;
/*    */   }
/*    */   
/*    */   public int getSortKey() {
/* 32 */     return net.minecraft.SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER ? (hashCode() * (sortKeySeed + 1)) : hashCode();
/*    */   }
/*    */   
/*    */   public static void updateSortKeySeed() {
/* 36 */     sortKeySeed = Math.round(100000.0F * (float)Math.random());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/TextureSetup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */