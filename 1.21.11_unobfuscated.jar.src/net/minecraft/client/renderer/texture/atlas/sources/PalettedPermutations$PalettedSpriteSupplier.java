/*     */ package net.minecraft.client.renderer.texture.atlas.sources;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import java.io.IOException;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class PalettedSpriteSupplier
/*     */   extends Record
/*     */   implements SpriteSource.DiscardableLoader
/*     */ {
/*     */   private final LazyLoadedImage baseImage;
/*     */   private final Supplier<IntUnaryOperator> palette;
/*     */   private final Identifier permutationLocation;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #120	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   private PalettedSpriteSupplier(LazyLoadedImage baseImage, Supplier<IntUnaryOperator> palette, Identifier permutationLocation) {
/* 120 */     this.baseImage = baseImage; this.palette = palette; this.permutationLocation = permutationLocation; } public LazyLoadedImage baseImage() { return this.baseImage; } public Supplier<IntUnaryOperator> palette() { return this.palette; } public Identifier permutationLocation() { return this.permutationLocation; }
/*     */   
/*     */   public SpriteContents get(SpriteResourceLoader loader) {
/*     */     try {
/* 124 */       NativeImage image = this.baseImage.get().mappedCopy(this.palette.get());
/* 125 */       return new SpriteContents(this.permutationLocation, new FrameSize(image.getWidth(), image.getHeight()), image);
/* 126 */     } catch (IOException|IllegalArgumentException e) {
/* 127 */       PalettedPermutations.LOGGER.error("unable to apply palette to {}", this.permutationLocation, e);
/* 128 */       return null;
/*     */     } finally {
/* 130 */       this.baseImage.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void discard() {
/* 136 */     this.baseImage.release();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */