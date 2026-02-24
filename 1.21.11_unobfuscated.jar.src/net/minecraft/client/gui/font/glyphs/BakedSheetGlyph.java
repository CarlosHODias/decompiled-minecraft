/*     */ package net.minecraft.client.gui.font.glyphs;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.GlyphRenderTypes;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public class BakedSheetGlyph implements EffectGlyph, BakedGlyph {
/*     */   public static final float Z_FIGHTER = 0.001F;
/*     */   private final GlyphInfo info;
/*     */   private final GlyphRenderTypes renderTypes;
/*     */   private final GpuTextureView textureView;
/*     */   private final float u0;
/*     */   private final float u1;
/*     */   private final float v0;
/*     */   private final float v1;
/*     */   private final float left;
/*     */   private final float right;
/*     */   private final float up;
/*     */   private final float down;
/*     */   
/*     */   public BakedSheetGlyph(GlyphInfo info, GlyphRenderTypes renderTypes, GpuTextureView textureView, float u0, float u1, float v0, float v1, float left, float right, float up, float down) {
/*  30 */     this.info = info;
/*  31 */     this.renderTypes = renderTypes;
/*  32 */     this.textureView = textureView;
/*  33 */     this.u0 = u0;
/*  34 */     this.u1 = u1;
/*  35 */     this.v0 = v0;
/*  36 */     this.v1 = v1;
/*     */     
/*  38 */     this.left = left;
/*  39 */     this.right = right;
/*  40 */     this.up = up;
/*  41 */     this.down = down;
/*     */   }
/*     */   
/*     */   private float left(GlyphInstance instance) {
/*  45 */     return instance.x + this.left + (
/*  46 */       instance.style.isItalic() ? Math.min(shearTop(), shearBottom()) : 0.0F) - 
/*  47 */       extraThickness(instance.style.isBold());
/*     */   }
/*     */   
/*     */   private float top(GlyphInstance instance) {
/*  51 */     return instance.y + this.up - 
/*  52 */       extraThickness(instance.style.isBold());
/*     */   }
/*     */   
/*     */   private float right(GlyphInstance instance) {
/*  56 */     return instance.x + this.right + (
/*  57 */       instance.hasShadow() ? instance.shadowOffset : 0.0F) + (
/*  58 */       instance.style.isItalic() ? Math.max(shearTop(), shearBottom()) : 0.0F) + 
/*  59 */       extraThickness(instance.style.isBold());
/*     */   }
/*     */   
/*     */   private float bottom(GlyphInstance instance) {
/*  63 */     return instance.y + this.down + (
/*  64 */       instance.hasShadow() ? instance.shadowOffset : 0.0F) + 
/*  65 */       extraThickness(instance.style.isBold());
/*     */   }
/*     */   private void renderChar(GlyphInstance glyphInstance, Matrix4f pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/*     */     float depth;
/*  69 */     Style style = glyphInstance.style();
/*  70 */     boolean italic = style.isItalic();
/*  71 */     float x = glyphInstance.x();
/*  72 */     float y = glyphInstance.y();
/*  73 */     int color = glyphInstance.color();
/*  74 */     boolean bold = style.isBold();
/*     */ 
/*     */     
/*  77 */     float zFighter = flat ? 0.0F : 0.001F;
/*     */     
/*  79 */     if (glyphInstance.hasShadow()) {
/*  80 */       int shadowColor = glyphInstance.shadowColor();
/*  81 */       render(italic, x + glyphInstance.shadowOffset(), y + glyphInstance.shadowOffset(), 0.0F, pose, buffer, shadowColor, bold, packedLightCoords);
/*  82 */       if (bold) {
/*  83 */         render(italic, x + glyphInstance.boldOffset() + glyphInstance.shadowOffset(), y + glyphInstance.shadowOffset(), zFighter, pose, buffer, shadowColor, true, packedLightCoords);
/*     */       }
/*  85 */       depth = flat ? 0.0F : 0.03F;
/*     */     } else {
/*  87 */       depth = 0.0F;
/*     */     } 
/*     */     
/*  90 */     render(italic, x, y, depth, pose, buffer, color, bold, packedLightCoords);
/*  91 */     if (bold) {
/*  92 */       render(italic, x + glyphInstance.boldOffset(), y, depth + zFighter, pose, buffer, color, true, packedLightCoords);
/*     */     }
/*     */   }
/*     */   
/*     */   private void render(boolean italic, float x, float y, float z, Matrix4f pose, VertexConsumer builder, int color, boolean bold, int packedLightCoords) {
/*  97 */     float x0 = x + this.left;
/*  98 */     float x1 = x + this.right;
/*     */     
/* 100 */     float y0 = y + this.up;
/* 101 */     float y1 = y + this.down;
/*     */     
/* 103 */     float shearY0 = italic ? shearTop() : 0.0F;
/* 104 */     float shearY1 = italic ? shearBottom() : 0.0F;
/*     */     
/* 106 */     float extraThickness = extraThickness(bold);
/*     */     
/* 108 */     builder.addVertex((Matrix4fc)pose, x0 + shearY0 - extraThickness, y0 - extraThickness, z).setColor(color).setUv(this.u0, this.v0).setLight(packedLightCoords);
/* 109 */     builder.addVertex((Matrix4fc)pose, x0 + shearY1 - extraThickness, y1 + extraThickness, z).setColor(color).setUv(this.u0, this.v1).setLight(packedLightCoords);
/* 110 */     builder.addVertex((Matrix4fc)pose, x1 + shearY1 + extraThickness, y1 + extraThickness, z).setColor(color).setUv(this.u1, this.v1).setLight(packedLightCoords);
/* 111 */     builder.addVertex((Matrix4fc)pose, x1 + shearY0 + extraThickness, y0 - extraThickness, z).setColor(color).setUv(this.u1, this.v0).setLight(packedLightCoords);
/*     */   }
/*     */   
/*     */   private static float extraThickness(boolean bold) {
/* 115 */     return bold ? 0.1F : 0.0F;
/*     */   }
/*     */   
/*     */   private float shearBottom() {
/* 119 */     return 1.0F - 0.25F * this.down;
/*     */   }
/*     */   
/*     */   private float shearTop() {
/* 123 */     return 1.0F - 0.25F * this.up;
/*     */   }
/*     */   
/*     */   private void renderEffect(EffectInstance effect, Matrix4f pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 127 */     float depth = flat ? 0.0F : effect.depth;
/* 128 */     if (effect.hasShadow()) {
/* 129 */       buildEffect(effect, effect.shadowOffset(), depth, effect.shadowColor(), buffer, packedLightCoords, pose);
/* 130 */       depth += flat ? 0.0F : 0.03F;
/*     */     } 
/* 132 */     buildEffect(effect, 0.0F, depth, effect.color, buffer, packedLightCoords, pose);
/*     */   }
/*     */   
/*     */   private void buildEffect(EffectInstance effect, float offset, float z, int color, VertexConsumer buffer, int packedLightCoords, Matrix4f pose) {
/* 136 */     buffer.addVertex((Matrix4fc)pose, effect.x0 + offset, effect.y1 + offset, z).setColor(color).setUv(this.u0, this.v0).setLight(packedLightCoords);
/* 137 */     buffer.addVertex((Matrix4fc)pose, effect.x1 + offset, effect.y1 + offset, z).setColor(color).setUv(this.u0, this.v1).setLight(packedLightCoords);
/* 138 */     buffer.addVertex((Matrix4fc)pose, effect.x1 + offset, effect.y0 + offset, z).setColor(color).setUv(this.u1, this.v1).setLight(packedLightCoords);
/* 139 */     buffer.addVertex((Matrix4fc)pose, effect.x0 + offset, effect.y0 + offset, z).setColor(color).setUv(this.u1, this.v0).setLight(packedLightCoords);
/*     */   }
/*     */ 
/*     */   
/*     */   public GlyphInfo info() {
/* 144 */     return this.info;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextRenderable.Styled createGlyph(float x, float y, int color, int shadowColor, Style style, float boldOffset, float shadowOffset) {
/* 149 */     return new GlyphInstance(x, y, color, shadowColor, this, style, boldOffset, shadowOffset);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextRenderable createEffect(float x0, float y0, float x1, float y1, float depth, int color, int shadowColor, float shadowOffset) {
/* 154 */     return new EffectInstance(this, x0, y0, x1, y1, depth, color, shadowColor, shadowOffset);
/*     */   }
/*     */   private static final class GlyphInstance extends Record implements TextRenderable.Styled { private final float x; private final float y; private final int color; private final int shadowColor; private final BakedSheetGlyph glyph; private final Style style; private final float boldOffset; private final float shadowOffset;
/* 157 */     private GlyphInstance(float x, float y, int color, int shadowColor, BakedSheetGlyph glyph, Style style, float boldOffset, float shadowOffset) { this.x = x; this.y = y; this.color = color; this.shadowColor = shadowColor; this.glyph = glyph; this.style = style; this.boldOffset = boldOffset; this.shadowOffset = shadowOffset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 157 */       //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance; } public float x() { return this.x; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #157	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;
/* 157 */       //   0	8	1	o	Ljava/lang/Object; } public float y() { return this.y; } public int color() { return this.color; } public int shadowColor() { return this.shadowColor; } public BakedSheetGlyph glyph() { return this.glyph; } public Style style() { return this.style; } public float boldOffset() { return this.boldOffset; } public float shadowOffset() { return this.shadowOffset; }
/*     */     
/*     */     public float left() {
/* 160 */       return this.glyph.left(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public float top() {
/* 165 */       return this.glyph.top(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public float right() {
/* 170 */       return this.glyph.right(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public float activeRight() {
/* 175 */       return this.x + this.glyph.info.getAdvance(this.style.isBold());
/*     */     }
/*     */ 
/*     */     
/*     */     public float bottom() {
/* 180 */       return this.glyph.bottom(this);
/*     */     }
/*     */     
/*     */     private boolean hasShadow() {
/* 184 */       return (shadowColor() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void render(Matrix4f pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 189 */       this.glyph.renderChar(this, pose, buffer, packedLightCoords, flat);
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderType renderType(Font.DisplayMode displayMode) {
/* 194 */       return this.glyph.renderTypes.select(displayMode);
/*     */     }
/*     */ 
/*     */     
/*     */     public GpuTextureView textureView() {
/* 199 */       return this.glyph.textureView;
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderPipeline guiPipeline() {
/* 204 */       return this.glyph.renderTypes.guiPipeline();
/*     */     } }
/*     */   private static final class EffectInstance extends Record implements TextRenderable { private final BakedSheetGlyph glyph; private final float x0; private final float y0; private final float x1; private final float y1; private final float depth; private final int color; private final int shadowColor; private final float shadowOffset;
/*     */     
/* 208 */     private EffectInstance(BakedSheetGlyph glyph, float x0, float y0, float x1, float y1, float depth, int color, int shadowColor, float shadowOffset) { this.glyph = glyph; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.depth = depth; this.color = color; this.shadowColor = shadowColor; this.shadowOffset = shadowOffset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #208	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #208	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #208	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;
/* 208 */       //   0	8	1	o	Ljava/lang/Object; } public BakedSheetGlyph glyph() { return this.glyph; } public float x0() { return this.x0; } public float y0() { return this.y0; } public float x1() { return this.x1; } public float y1() { return this.y1; } public float depth() { return this.depth; } public int color() { return this.color; } public int shadowColor() { return this.shadowColor; } public float shadowOffset() { return this.shadowOffset; }
/*     */     
/*     */     public float left() {
/* 211 */       return this.x0;
/*     */     }
/*     */ 
/*     */     
/*     */     public float top() {
/* 216 */       return this.y0;
/*     */     }
/*     */ 
/*     */     
/*     */     public float right() {
/* 221 */       return this.x1 + (hasShadow() ? this.shadowOffset : 0.0F);
/*     */     }
/*     */ 
/*     */     
/*     */     public float bottom() {
/* 226 */       return this.y1 + (hasShadow() ? this.shadowOffset : 0.0F);
/*     */     }
/*     */     
/*     */     private boolean hasShadow() {
/* 230 */       return (shadowColor() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void render(Matrix4f pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 235 */       this.glyph.renderEffect(this, pose, buffer, packedLightCoords, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderType renderType(Font.DisplayMode displayMode) {
/* 240 */       return this.glyph.renderTypes.select(displayMode);
/*     */     }
/*     */ 
/*     */     
/*     */     public GpuTextureView textureView() {
/* 245 */       return this.glyph.textureView;
/*     */     }
/*     */ 
/*     */     
/*     */     public RenderPipeline guiPipeline() {
/* 250 */       return this.glyph.renderTypes.guiPipeline();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/glyphs/BakedSheetGlyph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */