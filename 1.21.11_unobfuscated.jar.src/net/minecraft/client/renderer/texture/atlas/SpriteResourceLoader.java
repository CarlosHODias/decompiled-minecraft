/*    */ package net.minecraft.client.renderer.texture.atlas;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.texture.SpriteContents;
/*    */ import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
/*    */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*    */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.server.packs.resources.Resource;
/*    */ import net.minecraft.server.packs.resources.ResourceMetadata;
/*    */ import net.minecraft.util.Mth;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface SpriteResourceLoader
/*    */ {
/* 25 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   static SpriteResourceLoader create(Set<MetadataSectionType<?>> additionalMetadataSections) {
/* 28 */     return (spriteLocation, resource) -> {
/*    */         Optional<AnimationMetadataSection> animationInfo; Optional<TextureMetadataSection> textureInfo; List<MetadataSectionType.WithValue<?>> additionalMetadata;
/*    */         NativeImage image;
/*    */         FrameSize frameSize;
/*    */         try {
/*    */           ResourceMetadata metadata = resource.metadata();
/*    */           animationInfo = metadata.getSection(AnimationMetadataSection.TYPE);
/*    */           textureInfo = metadata.getSection(TextureMetadataSection.TYPE);
/*    */           additionalMetadata = metadata.getTypedSections(additionalMetadataSections);
/* 37 */         } catch (Exception e) {
/*    */           LOGGER.error("Unable to parse metadata from {}", spriteLocation, e); return null;
/*    */         }  try {
/*    */           InputStream is = resource.open(); 
/*    */           try { image = NativeImage.read(is); if (is != null)
/*    */               is.close();  }
/* 43 */           catch (Throwable throwable) { if (is != null) try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*    */         
/* 45 */         } catch (IOException e) {
/*    */           LOGGER.error("Using missing texture, unable to load {}", spriteLocation, e);
/*    */           return null;
/*    */         } 
/*    */         if (animationInfo.isPresent()) {
/*    */           frameSize = ((AnimationMetadataSection)animationInfo.get()).calculateFrameSize(image.getWidth(), image.getHeight());
/*    */           if (!Mth.isMultipleOf(image.getWidth(), frameSize.width()) || !Mth.isMultipleOf(image.getHeight(), frameSize.height())) {
/*    */             LOGGER.error("Image {} size {},{} is not multiple of frame size {},{}", new Object[] { spriteLocation, image.getWidth(), image.getHeight(), frameSize.width(), frameSize.height() });
/*    */             image.close();
/*    */             return null;
/*    */           } 
/*    */         } else {
/*    */           frameSize = new FrameSize(image.getWidth(), image.getHeight());
/*    */         } 
/*    */         return new SpriteContents(spriteLocation, frameSize, image, animationInfo, additionalMetadata, textureInfo);
/*    */       };
/*    */   }
/*    */   
/*    */   SpriteContents loadSprite(Identifier paramIdentifier, Resource paramResource);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/SpriteResourceLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */