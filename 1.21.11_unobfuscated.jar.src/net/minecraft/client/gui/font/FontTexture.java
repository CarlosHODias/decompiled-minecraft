/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import java.nio.file.Path;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedSheetGlyph;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.Dumpable;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ public class FontTexture
/*     */   extends AbstractTexture
/*     */   implements Dumpable
/*     */ {
/*     */   private static final int SIZE = 256;
/*     */   private final GlyphRenderTypes renderTypes;
/*     */   private final boolean colored;
/*     */   private final Node root;
/*     */   
/*     */   public FontTexture(Supplier<String> label, GlyphRenderTypes renderTypes, boolean colored) {
/*  28 */     this.colored = colored;
/*  29 */     this.root = new Node(0, 0, 256, 256);
/*  30 */     GpuDevice device = RenderSystem.getDevice();
/*  31 */     this.texture = device.createTexture(label, 7, colored ? TextureFormat.RGBA8 : TextureFormat.RED8, 256, 256, 1, 1);
/*  32 */     this.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
/*  33 */     this.textureView = device.createTextureView(this.texture);
/*  34 */     this.renderTypes = renderTypes;
/*     */   }
/*     */   
/*     */   public BakedSheetGlyph add(GlyphInfo info, GlyphBitmap glyph) {
/*  38 */     if (glyph.isColored() != this.colored) {
/*  39 */       return null;
/*     */     }
/*     */     
/*  42 */     Node node = this.root.insert(glyph);
/*     */     
/*  44 */     if (node != null) {
/*  45 */       glyph.upload(node.x, node.y, getTexture());
/*     */       
/*  47 */       float width = 256.0F;
/*  48 */       float height = 256.0F;
/*     */ 
/*     */ 
/*     */       
/*  52 */       float nudge = 0.01F;
/*     */       
/*  54 */       return new BakedSheetGlyph(info, this.renderTypes, 
/*     */ 
/*     */           
/*  57 */           getTextureView(), (node.x + 0.01F) / 256.0F, (node.x - 0.01F + 
/*     */           
/*  59 */           glyph.getPixelWidth()) / 256.0F, (node.y + 0.01F) / 256.0F, (node.y - 0.01F + 
/*     */           
/*  61 */           glyph.getPixelHeight()) / 256.0F, 
/*     */           
/*  63 */           glyph.getLeft(), 
/*  64 */           glyph.getRight(), 
/*  65 */           glyph.getTop(), 
/*  66 */           glyph.getBottom());
/*     */     } 
/*     */ 
/*     */     
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dumpContents(Identifier selfId, Path dir) {
/*  75 */     if (this.texture == null) {
/*     */       return;
/*     */     }
/*  78 */     String outputId = selfId.toDebugFileName();
/*  79 */     TextureUtil.writeAsPNG(dir, outputId, this.texture, 0, argb -> ((argb & 0xFF000000) == 0) ? -16777216 : argb);
/*     */   }
/*     */   
/*     */   private static class Node {
/*     */     private final int x;
/*     */     private final int y;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private Node left;
/*     */     private Node right;
/*     */     private boolean occupied;
/*     */     
/*     */     private Node(int x, int y, int width, int height) {
/*  92 */       this.x = x;
/*  93 */       this.y = y;
/*  94 */       this.width = width;
/*  95 */       this.height = height;
/*     */     }
/*     */     
/*     */     Node insert(GlyphBitmap glyph) {
/*  99 */       if (this.left != null && this.right != null) {
/* 100 */         Node newNode = this.left.insert(glyph);
/* 101 */         if (newNode == null) {
/* 102 */           newNode = this.right.insert(glyph);
/*     */         }
/* 104 */         return newNode;
/*     */       } 
/*     */       
/* 107 */       if (this.occupied) {
/* 108 */         return null;
/*     */       }
/* 110 */       int glyphWidth = glyph.getPixelWidth();
/* 111 */       int glyphHeight = glyph.getPixelHeight();
/*     */       
/* 113 */       if (glyphWidth > this.width || glyphHeight > this.height) {
/* 114 */         return null;
/*     */       }
/* 116 */       if (glyphWidth == this.width && glyphHeight == this.height) {
/* 117 */         this.occupied = true;
/* 118 */         return this;
/*     */       } 
/*     */       
/* 121 */       int deltaWidth = this.width - glyphWidth;
/* 122 */       int deltaHeight = this.height - glyphHeight;
/*     */       
/* 124 */       if (deltaWidth > deltaHeight) {
/* 125 */         this.left = new Node(this.x, this.y, glyphWidth, this.height);
/* 126 */         this.right = new Node(this.x + glyphWidth + 1, this.y, this.width - glyphWidth - 1, this.height);
/*     */       } else {
/* 128 */         this.left = new Node(this.x, this.y, this.width, glyphHeight);
/* 129 */         this.right = new Node(this.x, this.y + glyphHeight + 1, this.width, this.height - glyphHeight - 1);
/*     */       } 
/* 131 */       return this.left.insert(glyph);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/FontTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */