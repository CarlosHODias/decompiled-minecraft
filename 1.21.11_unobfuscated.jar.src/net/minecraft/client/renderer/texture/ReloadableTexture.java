/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.TextureFormat;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ 
/*    */ public abstract class ReloadableTexture
/*    */   extends AbstractTexture {
/*    */   private final Identifier resourceId;
/*    */   
/*    */   public ReloadableTexture(Identifier resourceId) {
/* 19 */     this.resourceId = resourceId;
/*    */   }
/*    */   
/*    */   public Identifier resourceId() {
/* 23 */     return this.resourceId;
/*    */   }
/*    */   
/*    */   public void apply(TextureContents contents) {
/* 27 */     boolean clamp = contents.clamp();
/* 28 */     boolean blur = contents.blur();
/* 29 */     AddressMode addressMode = clamp ? AddressMode.CLAMP_TO_EDGE : AddressMode.REPEAT;
/* 30 */     FilterMode minMag = blur ? FilterMode.LINEAR : FilterMode.NEAREST;
/* 31 */     this.sampler = RenderSystem.getSamplerCache().getSampler(addressMode, addressMode, minMag, minMag, false);
/*    */     
/* 33 */     NativeImage image = contents.image(); 
/* 34 */     try { doLoad(image);
/* 35 */       if (image != null) image.close();  } catch (Throwable throwable) { if (image != null)
/*    */         try { image.close(); }
/*    */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */           throw throwable; }
/* 39 */      } protected void doLoad(NativeImage image) { GpuDevice device = RenderSystem.getDevice();
/* 40 */     close();
/* 41 */     Objects.requireNonNull(this.resourceId); this.texture = device.createTexture(this.resourceId::toString, 5, TextureFormat.RGBA8, image.getWidth(), image.getHeight(), 1, 1);
/* 42 */     this.textureView = device.createTextureView(this.texture);
/* 43 */     device.createCommandEncoder().writeToTexture(this.texture, image); }
/*    */ 
/*    */   
/*    */   public abstract TextureContents loadContents(ResourceManager paramResourceManager) throws IOException;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/ReloadableTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */