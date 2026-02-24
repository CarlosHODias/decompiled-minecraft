/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.TextureFormat;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public class CubeMapTexture
/*    */   extends ReloadableTexture
/*    */ {
/* 16 */   private static final String[] SUFFIXES = new String[] { "_1.png", "_3.png", "_5.png", "_4.png", "_0.png", "_2.png" };
/*    */   
/*    */   public CubeMapTexture(Identifier resourceId) {
/* 19 */     super(resourceId);
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 24 */     Identifier location = resourceId();
/* 25 */     TextureContents first = TextureContents.load(resourceManager, location.withSuffix(SUFFIXES[0])); 
/* 26 */     try { int width = first.image().getWidth();
/* 27 */       int height = first.image().getHeight();
/* 28 */       NativeImage stackedImage = new NativeImage(width, height * 6, false);
/* 29 */       first.image().copyRect(stackedImage, 0, 0, 0, 0, width, height, false, true);
/* 30 */       for (int i = 1; i < 6; i++) {
/* 31 */         TextureContents part = TextureContents.load(resourceManager, location.withSuffix(SUFFIXES[i])); 
/* 32 */         try { if (part.image().getWidth() != width || part.image().getHeight() != height) {
/* 33 */             throw new IOException("Image dimensions of cubemap '" + String.valueOf(location) + "' sides do not match: part 0 is " + width + "x" + height + ", but part " + i + " is " + part.image().getWidth() + "x" + part.image().getHeight());
/*    */           }
/* 35 */           part.image().copyRect(stackedImage, 0, 0, 0, i * height, width, height, false, true);
/* 36 */           if (part != null) part.close();  } catch (Throwable throwable) { if (part != null)
/*    */             try { part.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 38 */       }  TextureContents textureContents = new TextureContents(stackedImage, new TextureMetadataSection(true, false, MipmapStrategy.MEAN, 0.0F));
/* 39 */       if (first != null) first.close();  return textureContents; }
/*    */     catch (Throwable throwable) { if (first != null)
/*    */         try { first.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 44 */      } protected void doLoad(NativeImage image) { GpuDevice device = RenderSystem.getDevice();
/* 45 */     int width = image.getWidth();
/* 46 */     int height = image.getHeight() / 6;
/* 47 */     close();
/* 48 */     Objects.requireNonNull(resourceId()); this.texture = device.createTexture(resourceId()::toString, 21, TextureFormat.RGBA8, width, height, 6, 1);
/* 49 */     this.textureView = device.createTextureView(this.texture);
/* 50 */     for (int i = 0; i < 6; i++)
/* 51 */       device.createCommandEncoder().writeToTexture(this.texture, image, 0, i, 0, 0, width, height, 0, height * i);  }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/CubeMapTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */