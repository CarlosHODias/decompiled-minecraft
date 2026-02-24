/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.TextureFormat;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DynamicTexture
/*    */   extends AbstractTexture
/*    */   implements Dumpable
/*    */ {
/* 19 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private NativeImage pixels;
/*    */   
/*    */   public DynamicTexture(Supplier<String> label, NativeImage image) {
/* 23 */     this.pixels = image;
/* 24 */     createTexture(label);
/* 25 */     upload();
/*    */   }
/*    */   
/*    */   public DynamicTexture(String label, int width, int height, boolean zero) {
/* 29 */     this.pixels = new NativeImage(width, height, zero);
/* 30 */     createTexture(label);
/*    */   }
/*    */   
/*    */   public DynamicTexture(Supplier<String> label, int width, int height, boolean zero) {
/* 34 */     this.pixels = new NativeImage(width, height, zero);
/* 35 */     createTexture(label);
/*    */   }
/*    */   
/*    */   private void createTexture(Supplier<String> label) {
/* 39 */     GpuDevice device = RenderSystem.getDevice();
/* 40 */     this.texture = device.createTexture(label, 5, TextureFormat.RGBA8, this.pixels.getWidth(), this.pixels.getHeight(), 1, 1);
/* 41 */     this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
/* 42 */     this.textureView = device.createTextureView(this.texture);
/*    */   }
/*    */   
/*    */   private void createTexture(String label) {
/* 46 */     GpuDevice device = RenderSystem.getDevice();
/* 47 */     this.texture = device.createTexture(label, 5, TextureFormat.RGBA8, this.pixels.getWidth(), this.pixels.getHeight(), 1, 1);
/* 48 */     this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
/* 49 */     this.textureView = device.createTextureView(this.texture);
/*    */   }
/*    */   
/*    */   public void upload() {
/* 53 */     if (this.pixels != null && this.texture != null) {
/* 54 */       RenderSystem.getDevice().createCommandEncoder().writeToTexture(this.texture, this.pixels);
/*    */     } else {
/* 56 */       LOGGER.warn("Trying to upload disposed texture {}", getTexture().getLabel());
/*    */     } 
/*    */   }
/*    */   
/*    */   public NativeImage getPixels() {
/* 61 */     return this.pixels;
/*    */   }
/*    */   
/*    */   public void setPixels(NativeImage pixels) {
/* 65 */     if (this.pixels != null) {
/* 66 */       this.pixels.close();
/*    */     }
/* 68 */     this.pixels = pixels;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 73 */     if (this.pixels != null) {
/* 74 */       this.pixels.close();
/* 75 */       this.pixels = null;
/*    */     } 
/* 77 */     super.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public void dumpContents(Identifier selfId, Path dir) throws IOException {
/* 82 */     if (this.pixels != null) {
/* 83 */       String outputId = selfId.toDebugFileName() + ".png";
/* 84 */       Path path = dir.resolve(outputId);
/* 85 */       this.pixels.writeToFile(path);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/DynamicTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */