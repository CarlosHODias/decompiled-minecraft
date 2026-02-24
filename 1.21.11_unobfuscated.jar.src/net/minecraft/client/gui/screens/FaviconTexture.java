/*    */ package net.minecraft.client.gui.screens;
/*    */ 
/*    */ import com.google.common.hash.Hashing;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.client.renderer.texture.TextureManager;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FaviconTexture
/*    */   implements AutoCloseable {
/* 13 */   private static final Identifier MISSING_LOCATION = Identifier.withDefaultNamespace("textures/misc/unknown_server.png");
/*    */   
/*    */   private static final int WIDTH = 64;
/*    */   
/*    */   private static final int HEIGHT = 64;
/*    */   private final TextureManager textureManager;
/*    */   private final Identifier textureLocation;
/*    */   private DynamicTexture texture;
/*    */   private boolean closed;
/*    */   
/*    */   private FaviconTexture(TextureManager textureManager, Identifier textureLocation) {
/* 24 */     this.textureManager = textureManager;
/* 25 */     this.textureLocation = textureLocation;
/*    */   }
/*    */   
/*    */   public static FaviconTexture forWorld(TextureManager textureManager, String levelId) {
/* 29 */     return new FaviconTexture(textureManager, Identifier.withDefaultNamespace("worlds/" + Util.sanitizeName(levelId, Identifier::validPathChar) + "/" + String.valueOf(Hashing.sha1().hashUnencodedChars(levelId)) + "/icon"));
/*    */   }
/*    */   
/*    */   public static FaviconTexture forServer(TextureManager textureManager, String address) {
/* 33 */     return new FaviconTexture(textureManager, Identifier.withDefaultNamespace("servers/" + String.valueOf(Hashing.sha1().hashUnencodedChars(address)) + "/icon"));
/*    */   }
/*    */   
/*    */   public void upload(NativeImage image) {
/* 37 */     if (image.getWidth() != 64 || image.getHeight() != 64) {
/* 38 */       image.close();
/* 39 */       throw new IllegalArgumentException("Icon must be 64x64, but was " + image.getWidth() + "x" + image.getHeight());
/*    */     } 
/*    */     try {
/* 42 */       checkOpen();
/*    */       
/* 44 */       if (this.texture == null) {
/* 45 */         this.texture = new DynamicTexture(() -> "Favicon " + String.valueOf(this.textureLocation), image);
/*    */       } else {
/* 47 */         this.texture.setPixels(image);
/* 48 */         this.texture.upload();
/*    */       } 
/* 50 */       this.textureManager.register(this.textureLocation, (AbstractTexture)this.texture);
/* 51 */     } catch (Throwable t) {
/* 52 */       image.close();
/* 53 */       clear();
/* 54 */       throw t;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void clear() {
/* 59 */     checkOpen();
/* 60 */     if (this.texture != null) {
/* 61 */       this.textureManager.release(this.textureLocation);
/* 62 */       this.texture.close();
/* 63 */       this.texture = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Identifier textureLocation() {
/* 68 */     return (this.texture != null) ? this.textureLocation : MISSING_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 73 */     clear();
/* 74 */     this.closed = true;
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 78 */     return this.closed;
/*    */   }
/*    */   
/*    */   private void checkOpen() {
/* 82 */     if (this.closed)
/* 83 */       throw new IllegalStateException("Icon already closed"); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/FaviconTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */