/*     */ package net.minecraft.client.gui.font.providers;
/*     */ import com.mojang.blaze3d.font.GlyphBitmap;
/*     */ import com.mojang.blaze3d.font.GlyphInfo;
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.blaze3d.font.UnbakedGlyph;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSets;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.font.CodepointMap;
/*     */ import net.minecraft.client.gui.font.glyphs.BakedGlyph;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BitmapProvider implements GlyphProvider {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final NativeImage image;
/*     */   private final CodepointMap<Glyph> glyphs;
/*     */   
/*     */   private BitmapProvider(NativeImage image, CodepointMap<Glyph> glyphs) {
/*  37 */     this.image = image;
/*  38 */     this.glyphs = glyphs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  43 */     this.image.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnbakedGlyph getGlyph(int codepoint) {
/*  48 */     return (UnbakedGlyph)this.glyphs.get(codepoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public IntSet getSupportedGlyphs() {
/*  53 */     return IntSets.unmodifiable(this.glyphs.keySet());
/*     */   }
/*     */   public static final class Definition extends Record implements GlyphProviderDefinition { private final Identifier file; private final int height; private final int ascent; private final int[][] codepointGrid; private static final Codec<int[][]> CODEPOINT_GRID_CODEC; public static final MapCodec<Definition> CODEC;
/*  56 */     public Definition(Identifier file, int height, int ascent, int[][] codepointGrid) { this.file = file; this.height = height; this.ascent = ascent; this.codepointGrid = codepointGrid; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  56 */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition; } public Identifier file() { return this.file; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Definition;
/*  56 */       //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public int ascent() { return this.ascent; } public int[][] codepointGrid() { return this.codepointGrid; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  80 */       CODEPOINT_GRID_CODEC = Codec.STRING.listOf().xmap(input -> { int lineCount = input.size(), result[][] = new int[lineCount][]; for (int i = 0; i < lineCount; i++) result[i] = ((String)input.get(i)).codePoints().toArray();  return result; }, grid -> { List<String> result = new ArrayList<>(grid.length); for (int[] line : grid) result.add(new String(line, 0, line.length));  return result; }).validate(Definition::validateDimensions);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 110 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("file").forGetter(Definition::file), (App)Codec.INT.optionalFieldOf("height", 8).forGetter(Definition::height), (App)Codec.INT.fieldOf("ascent").forGetter(Definition::ascent), (App)CODEPOINT_GRID_CODEC.fieldOf("chars").forGetter(Definition::codepointGrid)).apply((Applicative)i, Definition::new)).validate(Definition::validate);
/*     */     }
/*     */     
/* 113 */     private static DataResult<Definition> validate(Definition builder) { if (builder.ascent > builder.height) {
/* 114 */         return DataResult.error(() -> "Ascent " + builder.ascent + " higher than height " + builder.height);
/*     */       }
/* 116 */       return DataResult.success(builder); } private static DataResult<int[][]> validateDimensions(int[][] grid) { int lineCount = grid.length; if (lineCount == 0)
/*     */         return DataResult.error(() -> "Expected to find data in codepoint grid");  int[] firstLine = grid[0]; int lineWidth = firstLine.length; if (lineWidth == 0)
/*     */         return DataResult.error(() -> "Expected to find data in codepoint grid");  for (int i = 1; i < lineCount; i++) { int[] line = grid[i]; if (line.length != lineWidth)
/*     */           return DataResult.error(() -> "Lines in codepoint grid have to be the same length (found: " + line.length + " codepoints, expected: " + lineWidth + "), pad with \\u0000");  }
/*     */        return DataResult.success(grid); }
/* 121 */     public GlyphProviderType type() { return GlyphProviderType.BITMAP; }
/*     */ 
/*     */ 
/*     */     
/*     */     public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> unpack() {
/* 126 */       return Either.left(this::load);
/*     */     }
/*     */     
/*     */     private GlyphProvider load(ResourceManager resourceManager) throws IOException {
/* 130 */       Identifier texture = this.file.withPrefix("textures/");
/* 131 */       InputStream resource = resourceManager.open(texture); 
/* 132 */       try { NativeImage image = NativeImage.read(NativeImage.Format.RGBA, resource);
/*     */         
/* 134 */         int w = image.getWidth();
/* 135 */         int h = image.getHeight();
/*     */         
/* 137 */         int glyphWidth = w / (this.codepointGrid[0]).length;
/* 138 */         int glyphHeight = h / this.codepointGrid.length;
/*     */         
/* 140 */         float pixelScale = this.height / glyphHeight;
/*     */         
/* 142 */         CodepointMap<BitmapProvider.Glyph> charMap = new CodepointMap(x$0 -> new BitmapProvider.Glyph[x$0], x$0 -> new BitmapProvider.Glyph[x$0][]);
/*     */         
/* 144 */         for (int slotY = 0; slotY < this.codepointGrid.length; slotY++) {
/* 145 */           int linePos = 0;
/* 146 */           for (int c : this.codepointGrid[slotY]) {
/* 147 */             int slotX = linePos++;
/* 148 */             if (c != 0) {
/*     */ 
/*     */               
/* 151 */               int actualGlyphWidth = getActualGlyphWidth(image, glyphWidth, glyphHeight, slotX, slotY);
/*     */               
/* 153 */               BitmapProvider.Glyph prev = (BitmapProvider.Glyph)charMap.put(c, new BitmapProvider.Glyph(pixelScale, image, slotX * glyphWidth, slotY * glyphHeight, glyphWidth, glyphHeight, (int)(0.5D + (actualGlyphWidth * pixelScale)) + 1, this.ascent));
/* 154 */               if (prev != null) {
/* 155 */                 BitmapProvider.LOGGER.warn("Codepoint '{}' declared multiple times in {}", Integer.toHexString(c), texture);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 160 */         BitmapProvider bitmapProvider = new BitmapProvider(image, charMap);
/* 161 */         if (resource != null) resource.close();  return bitmapProvider; } catch (Throwable throwable) { if (resource != null)
/*     */           try { resource.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/* 165 */        } private int getActualGlyphWidth(NativeImage image, int glyphWidth, int glyphHeight, int xGlyph, int yGlyph) { int width = glyphWidth - 1;
/* 166 */       for (; width >= 0; width--) {
/* 167 */         int xPixel = xGlyph * glyphWidth + width;
/* 168 */         for (int y = 0; y < glyphHeight; y++) {
/* 169 */           int yPixel = yGlyph * glyphHeight + y;
/*     */           
/* 171 */           if (image.getLuminanceOrAlpha(xPixel, yPixel) != 0) {
/* 172 */             return width + 1;
/*     */           }
/*     */         } 
/*     */       } 
/* 176 */       return width + 1; }
/*     */      }
/*     */   private static final class Glyph extends Record implements UnbakedGlyph { private final float scale; private final NativeImage image; private final int offsetX; private final int offsetY; private final int width; private final int height; private final int advance; private final int ascent;
/*     */     
/* 180 */     private Glyph(float scale, NativeImage image, int offsetX, int offsetY, int width, int height, int advance, int ascent) { this.scale = scale; this.image = image; this.offsetX = offsetX; this.offsetY = offsetY; this.width = width; this.height = height; this.advance = advance; this.ascent = ascent; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #180	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/providers/BitmapProvider$Glyph;
/* 180 */       //   0	8	1	o	Ljava/lang/Object; } public float scale() { return this.scale; } public NativeImage image() { return this.image; } public int offsetX() { return this.offsetX; } public int offsetY() { return this.offsetY; } public int width() { return this.width; } public int height() { return this.height; } public int advance() { return this.advance; } public int ascent() { return this.ascent; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GlyphInfo info() {
/* 193 */       return GlyphInfo.simple(this.advance);
/*     */     }
/*     */     
/*     */     public BakedGlyph bake(UnbakedGlyph.Stitcher stitcher)
/*     */     {
/* 198 */       return stitcher.stitch(info(), new GlyphBitmap()
/*     */           {
/*     */             public float getOversample() {
/* 201 */               return 1.0F / BitmapProvider.Glyph.this.scale;
/*     */             }
/*     */ 
/*     */             
/*     */             public int getPixelWidth() {
/* 206 */               return BitmapProvider.Glyph.this.width;
/*     */             }
/*     */ 
/*     */             
/*     */             public int getPixelHeight() {
/* 211 */               return BitmapProvider.Glyph.this.height;
/*     */             }
/*     */ 
/*     */             
/*     */             public float getBearingTop() {
/* 216 */               return BitmapProvider.Glyph.this.ascent;
/*     */             }
/*     */ 
/*     */             
/*     */             public void upload(int x, int y, GpuTexture texture) {
/* 221 */               RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, BitmapProvider.Glyph.this.image, 0, 0, x, y, BitmapProvider.Glyph.this.width, BitmapProvider.Glyph.this.height, BitmapProvider.Glyph.this.offsetX, BitmapProvider.Glyph.this.offsetY);
/*     */             }
/*     */             
/*     */             public boolean isColored()
/*     */             {
/* 226 */               return (BitmapProvider.Glyph.this.image.format().components() > 1); } }); } } class null implements GlyphBitmap { public float getOversample() { return 1.0F / BitmapProvider.Glyph.this.scale; } public int getPixelWidth() { return BitmapProvider.Glyph.this.width; } public boolean isColored() { return (BitmapProvider.Glyph.this.image.format().components() > 1); }
/*     */ 
/*     */     
/*     */     public int getPixelHeight() {
/*     */       return BitmapProvider.Glyph.this.height;
/*     */     }
/*     */     
/*     */     public float getBearingTop() {
/*     */       return BitmapProvider.Glyph.this.ascent;
/*     */     }
/*     */     
/*     */     public void upload(int x, int y, GpuTexture texture) {
/*     */       RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, BitmapProvider.Glyph.this.image, 0, 0, x, y, BitmapProvider.Glyph.this.width, BitmapProvider.Glyph.this.height, BitmapProvider.Glyph.this.offsetX, BitmapProvider.Glyph.this.offsetY);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/providers/BitmapProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */