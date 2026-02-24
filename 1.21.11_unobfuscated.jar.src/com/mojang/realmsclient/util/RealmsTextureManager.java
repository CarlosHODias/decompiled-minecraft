/*    */ package com.mojang.realmsclient.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.platform.NativeImage;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Base64;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsTextureManager {
/* 20 */   private static final Map<String, RealmsTexture> TEXTURES = Maps.newHashMap();
/*    */   
/* 22 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 23 */   private static final Identifier TEMPLATE_ICON_LOCATION = Identifier.withDefaultNamespace("textures/gui/presets/isles.png");
/*    */   
/*    */   public static Identifier worldTemplate(String id, String image) {
/* 26 */     if (image == null) {
/* 27 */       return TEMPLATE_ICON_LOCATION;
/*    */     }
/* 29 */     return getTexture(id, image);
/*    */   }
/*    */   
/*    */   private static Identifier getTexture(String id, String encodedImage) {
/* 33 */     RealmsTexture texture = TEXTURES.get(id);
/* 34 */     if (texture != null && texture.image().equals(encodedImage)) {
/* 35 */       return texture.textureId;
/*    */     }
/*    */     
/* 38 */     NativeImage image = loadImage(encodedImage);
/* 39 */     if (image == null) {
/* 40 */       Identifier missingTexture = MissingTextureAtlasSprite.getLocation();
/* 41 */       TEXTURES.put(id, new RealmsTexture(encodedImage, missingTexture));
/* 42 */       return missingTexture;
/*    */     } 
/*    */     
/* 45 */     Identifier textureId = Identifier.fromNamespaceAndPath("realms", "dynamic/" + id);
/* 46 */     Objects.requireNonNull(textureId); Minecraft.getInstance().getTextureManager().register(textureId, (AbstractTexture)new DynamicTexture(textureId::toString, image));
/* 47 */     TEXTURES.put(id, new RealmsTexture(encodedImage, textureId));
/*    */     
/* 49 */     return textureId;
/*    */   }
/*    */   public static final class RealmsTexture extends Record { private final String image; private final Identifier textureId;
/* 52 */     public RealmsTexture(String image, Identifier textureId) { this.image = image; this.textureId = textureId; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 52 */       //   0	7	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture; } public String image() { return this.image; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #52	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/util/RealmsTextureManager$RealmsTexture;
/* 52 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier textureId() { return this.textureId; }
/*    */      }
/*    */   
/*    */   private static NativeImage loadImage(String encodedImage) {
/* 56 */     byte[] bytes = Base64.getDecoder().decode(encodedImage);
/* 57 */     ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
/*    */     try {
/* 59 */       return NativeImage.read(buffer.put(bytes).flip());
/* 60 */     } catch (IOException e) {
/* 61 */       LOGGER.warn("Failed to load world image: {}", encodedImage, e);
/*    */     } finally {
/* 63 */       MemoryUtil.memFree(buffer);
/*    */     } 
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/RealmsTextureManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */