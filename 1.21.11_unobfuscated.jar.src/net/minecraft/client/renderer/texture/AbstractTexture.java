/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ 
/*    */ public abstract class AbstractTexture
/*    */   implements AutoCloseable {
/*    */   protected GpuTexture texture;
/*    */   protected GpuTextureView textureView;
/* 14 */   protected GpuSampler sampler = RenderSystem.getSamplerCache().getSampler(AddressMode.REPEAT, AddressMode.REPEAT, FilterMode.NEAREST, FilterMode.LINEAR, false);
/*    */ 
/*    */   
/*    */   public void close() {
/* 18 */     if (this.texture != null) {
/* 19 */       this.texture.close();
/* 20 */       this.texture = null;
/*    */     } 
/* 22 */     if (this.textureView != null) {
/* 23 */       this.textureView.close();
/* 24 */       this.textureView = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public GpuTexture getTexture() {
/* 29 */     if (this.texture == null) {
/* 30 */       throw new IllegalStateException("Texture does not exist, can't get it before something initializes it");
/*    */     }
/* 32 */     return this.texture;
/*    */   }
/*    */   
/*    */   public GpuTextureView getTextureView() {
/* 36 */     if (this.textureView == null) {
/* 37 */       throw new IllegalStateException("Texture view does not exist, can't get it before something initializes it");
/*    */     }
/* 39 */     return this.textureView;
/*    */   }
/*    */   
/*    */   public GpuSampler getSampler() {
/* 43 */     return this.sampler;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/AbstractTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */