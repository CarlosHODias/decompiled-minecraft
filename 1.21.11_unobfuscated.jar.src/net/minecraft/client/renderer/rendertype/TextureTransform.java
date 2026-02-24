/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextureTransform
/*    */ {
/*    */   public static final double MAX_ENCHANTMENT_GLINT_SPEED_MILLIS = 8.0D;
/*    */   private final String name;
/*    */   private final Supplier<Matrix4f> supplier;
/*    */   
/*    */   public TextureTransform(String name, Supplier<Matrix4f> matrix) {
/* 17 */     this.name = name;
/* 18 */     this.supplier = matrix;
/*    */   }
/*    */   
/*    */   public Matrix4f getMatrix() {
/* 22 */     return this.supplier.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 27 */     return "TexturingStateShard[" + this.name + "]";
/*    */   }
/*    */   
/* 30 */   public static final TextureTransform DEFAULT_TEXTURING = new TextureTransform("default_texturing", Matrix4f::new);
/*    */   
/*    */   public static final class OffsetTextureTransform extends TextureTransform {
/*    */     public OffsetTextureTransform(float uOffset, float vOffset) {
/* 34 */       super("offset_texturing", () -> new Matrix4f().translation(uOffset, vOffset, 0.0F));
/*    */     }
/*    */   }
/*    */   
/*    */   private static Matrix4f setupGlintTexturing(float scale) {
/* 39 */     long millis = (long)(Util.getMillis() * (Double)(Minecraft.getInstance()).options.glintSpeed().get() * 8.0D);
/* 40 */     float layerOffset0 = (float)(millis % 110000L) / 110000.0F;
/* 41 */     float layerOffset1 = (float)(millis % 30000L) / 30000.0F;
/*    */ 
/*    */     
/* 44 */     Matrix4f matrix = new Matrix4f().translation(-layerOffset0, layerOffset1, 0.0F);
/*    */ 
/*    */     
/* 47 */     matrix.rotateZ(0.17453292F).scale(scale);
/* 48 */     return matrix;
/*    */   }
/*    */   
/* 51 */   public static final TextureTransform GLINT_TEXTURING = new TextureTransform("glint_texturing", () -> setupGlintTexturing(8.0F));
/*    */   
/* 53 */   public static final TextureTransform ENTITY_GLINT_TEXTURING = new TextureTransform("entity_glint_texturing", () -> setupGlintTexturing(0.5F));
/*    */   
/* 55 */   public static final TextureTransform ARMOR_ENTITY_GLINT_TEXTURING = new TextureTransform("armor_entity_glint_texturing", () -> setupGlintTexturing(0.16F));
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/rendertype/TextureTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */