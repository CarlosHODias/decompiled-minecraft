/*     */ package com.mojang.blaze3d.font;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
/*     */ import net.minecraft.client.gui.font.providers.FreeTypeUtil;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.freetype.FT_Bitmap;
/*     */ import org.lwjgl.util.freetype.FT_Face;
/*     */ import org.lwjgl.util.freetype.FT_GlyphSlot;
/*     */ import org.lwjgl.util.freetype.FT_Vector;
/*     */ import org.lwjgl.util.freetype.FreeType;
/*     */ 
/*     */ public class TrueTypeGlyphProvider implements GlyphProvider {
/*     */   private ByteBuffer fontMemory;
/*     */   private FT_Face face;
/*     */   private final float oversample;
/*     */   private final CodepointMap<GlyphEntry> glyphs;
/*     */   
/*  30 */   public TrueTypeGlyphProvider(ByteBuffer fontMemory, FT_Face face, float size, float oversample, float shiftX, float shiftY, String skip) { this.glyphs = new CodepointMap(x$0 -> new GlyphEntry[x$0], x$0 -> new GlyphEntry[x$0][]);
/*     */ 
/*     */     
/*  33 */     this.fontMemory = fontMemory;
/*  34 */     this.face = face;
/*     */     
/*  36 */     this.oversample = oversample;
/*     */     
/*  38 */     IntArraySet intArraySet = new IntArraySet();
/*  39 */     Objects.requireNonNull(intArraySet); skip.codePoints().forEach(intArraySet::add);
/*     */     
/*  41 */     int pixelsPerEm = Math.round(size * oversample);
/*  42 */     FreeType.FT_Set_Pixel_Sizes(face, pixelsPerEm, pixelsPerEm);
/*     */     
/*  44 */     float transformX = shiftX * oversample;
/*  45 */     float transformY = -shiftY * oversample;
/*  46 */     MemoryStack stack = MemoryStack.stackPush(); 
/*  47 */     try { FT_Vector vector = FreeTypeUtil.setVector(FT_Vector.malloc(stack), transformX, transformY);
/*  48 */       FreeType.FT_Set_Transform(face, null, vector);
/*     */       
/*  50 */       IntBuffer indexPtr = stack.mallocInt(1);
/*  51 */       int codepoint = (int)FreeType.FT_Get_First_Char(face, indexPtr);
/*     */       while (true) {
/*  53 */         int index = indexPtr.get(0);
/*  54 */         if (index == 0) {
/*     */           break;
/*     */         }
/*     */         
/*  58 */         if (!intArraySet.contains(codepoint)) {
/*  59 */           this.glyphs.put(codepoint, new GlyphEntry(index));
/*     */         }
/*     */         
/*  62 */         codepoint = (int)FreeType.FT_Get_Next_Char(face, codepoint, indexPtr);
/*     */       } 
/*  64 */       if (stack != null) stack.close();  }
/*     */     catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  69 */      } public UnbakedGlyph getGlyph(int codepoint) { GlyphEntry entry = (GlyphEntry)this.glyphs.get(codepoint);
/*  70 */     return (entry != null) ? getOrLoadGlyphInfo(codepoint, entry) : null; }
/*     */ 
/*     */   
/*     */   private UnbakedGlyph getOrLoadGlyphInfo(int codepoint, GlyphEntry entry) {
/*  74 */     UnbakedGlyph result = entry.glyph;
/*  75 */     if (result == null) {
/*  76 */       FT_Face face = validateFontOpen();
/*  77 */       synchronized (face) {
/*     */         
/*  79 */         result = entry.glyph;
/*  80 */         if (result == null) {
/*  81 */           result = loadGlyph(codepoint, face, entry.index);
/*  82 */           entry.glyph = result;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnbakedGlyph loadGlyph(int codepoint, FT_Face face, int index) {
/*  95 */     int errorCode = FreeType.FT_Load_Glyph(face, index, 4194312);
/*  96 */     if (errorCode != 0) {
/*  97 */       FreeTypeUtil.assertError(errorCode, String.format(Locale.ROOT, "Loading glyph U+%06X", new Object[] { codepoint }));
/*     */     }
/*  99 */     FT_GlyphSlot glyph = face.glyph();
/* 100 */     if (glyph == null) {
/* 101 */       throw new NullPointerException(String.format(Locale.ROOT, "Glyph U+%06X not initialized", new Object[] { codepoint }));
/*     */     }
/*     */     
/* 104 */     float scaledAdvance = FreeTypeUtil.x(glyph.advance());
/*     */     
/* 106 */     FT_Bitmap bitmap = glyph.bitmap();
/* 107 */     int left = glyph.bitmap_left();
/* 108 */     int top = glyph.bitmap_top();
/* 109 */     int width = bitmap.width();
/* 110 */     int height = bitmap.rows();
/*     */     
/* 112 */     if (width <= 0 || height <= 0) {
/* 113 */       return (UnbakedGlyph)new EmptyGlyph(scaledAdvance / this.oversample);
/*     */     }
/*     */     
/* 116 */     return new Glyph(left, top, width, height, scaledAdvance, index);
/*     */   }
/*     */   
/*     */   private FT_Face validateFontOpen() {
/* 120 */     if (this.fontMemory == null || this.face == null) {
/* 121 */       throw new IllegalStateException("Provider already closed");
/*     */     }
/* 123 */     return this.face;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 128 */     if (this.face != null) {
/* 129 */       synchronized (FreeTypeUtil.LIBRARY_LOCK) {
/* 130 */         FreeTypeUtil.checkError(FreeType.FT_Done_Face(this.face), "Deleting face");
/*     */       } 
/* 132 */       this.face = null;
/*     */     } 
/* 134 */     MemoryUtil.memFree(this.fontMemory);
/* 135 */     this.fontMemory = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntSet getSupportedGlyphs() {
/* 140 */     return this.glyphs.keySet();
/*     */   }
/*     */   
/*     */   private static class GlyphEntry {
/*     */     private final int index;
/*     */     private volatile UnbakedGlyph glyph;
/*     */     
/*     */     private GlyphEntry(int index) {
/* 148 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   private class Glyph implements UnbakedGlyph {
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final float bearingX;
/*     */     private final float bearingY;
/*     */     private final GlyphInfo info;
/*     */     private final int index;
/*     */     
/*     */     private Glyph(float left, float top, int width, int height, float advance, int index) {
/* 161 */       this.width = width;
/* 162 */       this.height = height;
/*     */       
/* 164 */       this.info = GlyphInfo.simple(advance / TrueTypeGlyphProvider.this.oversample);
/*     */       
/* 166 */       this.bearingX = left / TrueTypeGlyphProvider.this.oversample;
/* 167 */       this.bearingY = top / TrueTypeGlyphProvider.this.oversample;
/*     */       
/* 169 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public GlyphInfo info() {
/* 174 */       return this.info;
/*     */     }
/*     */     
/*     */     public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher)
/*     */     {
/* 179 */       return stitcher.stitch(this.info, new GlyphBitmap()
/*     */           {
/*     */             public int getPixelWidth() {
/* 182 */               return TrueTypeGlyphProvider.Glyph.this.width;
/*     */             }
/*     */ 
/*     */             
/*     */             public int getPixelHeight() {
/* 187 */               return TrueTypeGlyphProvider.Glyph.this.height;
/*     */             }
/*     */ 
/*     */             
/*     */             public float getOversample() {
/* 192 */               return TrueTypeGlyphProvider.this.oversample;
/*     */             }
/*     */ 
/*     */             
/*     */             public float getBearingLeft() {
/* 197 */               return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */             }
/*     */ 
/*     */             
/*     */             public float getBearingTop() {
/* 202 */               return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */             }
/*     */             
/*     */             public void upload(int x, int y, GpuTexture texture)
/*     */             {
/* 207 */               FT_Face face = TrueTypeGlyphProvider.this.validateFontOpen();
/*     */               
/* 209 */               NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false); 
/* 210 */               try { if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index)) {
/* 211 */                   RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, 0, 0);
/*     */                 }
/* 213 */                 image.close(); }
/*     */               catch (Throwable throwable) { try { image.close(); }
/*     */                 catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */                  throw throwable; }
/* 218 */                } public boolean isColored() { return false; } }); } } class null implements GlyphBitmap { public boolean isColored() { return false; }
/*     */ 
/*     */     
/*     */     public int getPixelWidth() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.width;
/*     */     }
/*     */     
/*     */     public int getPixelHeight() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.height;
/*     */     }
/*     */     
/*     */     public float getOversample() {
/*     */       return TrueTypeGlyphProvider.this.oversample;
/*     */     }
/*     */     
/*     */     public float getBearingLeft() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.bearingX;
/*     */     }
/*     */     
/*     */     public float getBearingTop() {
/*     */       return TrueTypeGlyphProvider.Glyph.this.bearingY;
/*     */     }
/*     */     
/*     */     public void upload(int x, int y, GpuTexture texture) {
/*     */       FT_Face face = TrueTypeGlyphProvider.this.validateFontOpen();
/*     */       NativeImage image = new NativeImage(NativeImage.Format.LUMINANCE, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, false);
/*     */       try {
/*     */         if (image.copyFromFont(face, TrueTypeGlyphProvider.Glyph.this.index))
/*     */           RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, image, 0, 0, x, y, TrueTypeGlyphProvider.Glyph.this.width, TrueTypeGlyphProvider.Glyph.this.height, 0, 0); 
/*     */         image.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           image.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/font/TrueTypeGlyphProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */