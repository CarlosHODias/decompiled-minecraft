/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class MissingTextureAtlasSprite
/*    */ {
/*    */   private static final int MISSING_IMAGE_WIDTH = 16;
/*    */   private static final int MISSING_IMAGE_HEIGHT = 16;
/*    */   private static final String MISSING_TEXTURE_NAME = "missingno";
/* 12 */   private static final Identifier MISSING_TEXTURE_LOCATION = Identifier.withDefaultNamespace("missingno");
/*    */   
/*    */   public static NativeImage generateMissingImage() {
/* 15 */     return generateMissingImage(16, 16);
/*    */   }
/*    */   
/*    */   public static NativeImage generateMissingImage(int width, int height) {
/* 19 */     NativeImage result = new NativeImage(width, height, false);
/* 20 */     int pink = -524040;
/* 21 */     for (int y = 0; y < height; y++) {
/* 22 */       for (int x = 0; x < width; x++) {
/* 23 */         if (((y < height / 2) ? true : false) ^ ((x < width / 2) ? true : false)) {
/* 24 */           result.setPixel(x, y, -524040);
/*    */         } else {
/* 26 */           result.setPixel(x, y, -16777216);
/*    */         } 
/*    */       } 
/*    */     } 
/* 30 */     return result;
/*    */   }
/*    */   
/*    */   public static SpriteContents create() {
/* 34 */     NativeImage contents = generateMissingImage(16, 16);
/* 35 */     return new SpriteContents(MISSING_TEXTURE_LOCATION, new FrameSize(16, 16), contents);
/*    */   }
/*    */   
/*    */   public static Identifier getLocation() {
/* 39 */     return MISSING_TEXTURE_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/MissingTextureAtlasSprite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */