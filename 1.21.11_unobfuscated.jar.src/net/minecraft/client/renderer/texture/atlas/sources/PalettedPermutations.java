/*     */ package net.minecraft.client.renderer.texture.atlas.sources;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntUnaryOperator;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public final class PalettedPermutations extends Record implements SpriteSource {
/*     */   private final List<Identifier> textures;
/*     */   private final Identifier paletteKey;
/*     */   private final Map<String, Identifier> permutations;
/*     */   private final String separator;
/*     */   
/*  32 */   public PalettedPermutations(List<Identifier> textures, Identifier paletteKey, Map<String, Identifier> permutations, String separator) { this.textures = textures; this.paletteKey = paletteKey; this.permutations = permutations; this.separator = separator; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #32	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations;
/*  32 */     //   0	8	1	o	Ljava/lang/Object; } public List<Identifier> textures() { return this.textures; } public Identifier paletteKey() { return this.paletteKey; } public Map<String, Identifier> permutations() { return this.permutations; } public String separator() { return this.separator; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final String DEFAULT_SEPARATOR = "_"; public static final MapCodec<PalettedPermutations> MAP_CODEC;
/*     */   
/*     */   static {
/*  41 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.list(Identifier.CODEC).fieldOf("textures").forGetter(PalettedPermutations::textures), (App)Identifier.CODEC.fieldOf("palette_key").forGetter(PalettedPermutations::paletteKey), (App)Codec.unboundedMap((Codec)Codec.STRING, Identifier.CODEC).fieldOf("permutations").forGetter(PalettedPermutations::permutations), (App)Codec.STRING.optionalFieldOf("separator", "_").forGetter(PalettedPermutations::separator)).apply((Applicative)i, PalettedPermutations::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PalettedPermutations(List<Identifier> textures, Identifier paletteKey, Map<String, Identifier> permutations) {
/*  49 */     this(textures, paletteKey, permutations, "_");
/*     */   }
/*     */ 
/*     */   
/*     */   public void run(ResourceManager resourceManager, SpriteSource.Output output) {
/*  54 */     com.google.common.base.Supplier supplier = Suppliers.memoize(() -> loadPaletteEntryFromImage(resourceManager, this.paletteKey));
/*  55 */     Map<String, java.util.function.Supplier<IntUnaryOperator>> palettes = new HashMap<>();
/*  56 */     this.permutations.forEach((suffix, palette) -> palettes.put(suffix, Suppliers.memoize(())));
/*     */ 
/*     */     
/*  59 */     for (Identifier textureLocation : this.textures) {
/*  60 */       Identifier textureId = TEXTURE_ID_CONVERTER.idToFile(textureLocation);
/*  61 */       Optional<Resource> resource = resourceManager.getResource(textureId);
/*  62 */       if (resource.isEmpty()) {
/*  63 */         LOGGER.warn("Unable to find texture {}", textureId);
/*     */         continue;
/*     */       } 
/*  66 */       LazyLoadedImage baseImage = new LazyLoadedImage(textureId, resource.get(), palettes.size());
/*  67 */       for (Map.Entry<String, java.util.function.Supplier<IntUnaryOperator>> entry : palettes.entrySet()) {
/*  68 */         Identifier permutationLocation = textureLocation.withSuffix(this.separator + this.separator);
/*  69 */         output.add(permutationLocation, new PalettedSpriteSupplier(baseImage, entry.getValue(), permutationLocation));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static IntUnaryOperator createPaletteMapping(int[] keys, int[] values) {
/*  75 */     if (values.length != keys.length) {
/*  76 */       LOGGER.warn("Palette mapping has different sizes: {} and {}", keys.length, values.length);
/*  77 */       throw new IllegalArgumentException();
/*     */     } 
/*     */     
/*  80 */     Int2IntOpenHashMap int2IntOpenHashMap = new Int2IntOpenHashMap(values.length);
/*  81 */     for (int i = 0; i < keys.length; i++) {
/*  82 */       int key = keys[i];
/*  83 */       if (ARGB.alpha(key) != 0) {
/*  84 */         int2IntOpenHashMap.put(ARGB.transparent(key), values[i]);
/*     */       }
/*     */     } 
/*     */     
/*  88 */     return pixel -> {
/*     */         int pixelAlpha = ARGB.alpha(pixel);
/*     */         if (pixelAlpha == 0) {
/*     */           return pixel;
/*     */         }
/*     */         int pixelRGB = ARGB.transparent(pixel), value = palette.getOrDefault(pixelRGB, ARGB.opaque(pixelRGB)), valueAlpha = ARGB.alpha(value);
/*     */         return ARGB.color(pixelAlpha * valueAlpha / 255, value);
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] loadPaletteEntryFromImage(ResourceManager resourceManager, Identifier location) {
/* 102 */     Optional<Resource> resource = resourceManager.getResource(TEXTURE_ID_CONVERTER.idToFile(location));
/* 103 */     if (resource.isEmpty()) {
/* 104 */       LOGGER.error("Failed to load palette image {}", location);
/* 105 */       throw new IllegalArgumentException();
/*     */     }  
/* 107 */     try { InputStream is = ((Resource)resource.get()).open(); try { NativeImage image = NativeImage.read(is); 
/* 108 */         try { int[] arrayOfInt = image.getPixels();
/* 109 */           if (image != null) image.close();  if (is != null) is.close();  return arrayOfInt; } catch (Throwable throwable) { if (image != null) try { image.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (is != null) try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/* 110 */     { LOGGER.error("Couldn't load texture {}", location, exception);
/* 111 */       throw new IllegalArgumentException(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public MapCodec<PalettedPermutations> codec() {
/* 117 */     return MAP_CODEC;
/*     */   } private static final class PalettedSpriteSupplier extends Record implements SpriteSource.DiscardableLoader {
/*     */     private final LazyLoadedImage baseImage; private final java.util.function.Supplier<IntUnaryOperator> palette; private final Identifier permutationLocation;
/* 120 */     private PalettedSpriteSupplier(LazyLoadedImage baseImage, java.util.function.Supplier<IntUnaryOperator> palette, Identifier permutationLocation) { this.baseImage = baseImage; this.palette = palette; this.permutationLocation = permutationLocation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations$PalettedSpriteSupplier;
/* 120 */       //   0	8	1	o	Ljava/lang/Object; } public LazyLoadedImage baseImage() { return this.baseImage; } public java.util.function.Supplier<IntUnaryOperator> palette() { return this.palette; } public Identifier permutationLocation() { return this.permutationLocation; }
/*     */     
/*     */     public SpriteContents get(net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader loader) {
/*     */       try {
/* 124 */         NativeImage image = this.baseImage.get().mappedCopy(this.palette.get());
/* 125 */         return new SpriteContents(this.permutationLocation, new net.minecraft.client.resources.metadata.animation.FrameSize(image.getWidth(), image.getHeight()), image);
/* 126 */       } catch (java.io.IOException|IllegalArgumentException e) {
/* 127 */         PalettedPermutations.LOGGER.error("unable to apply palette to {}", this.permutationLocation, e);
/* 128 */         return null;
/*     */       } finally {
/* 130 */         this.baseImage.release();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void discard() {
/* 136 */       this.baseImage.release();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/PalettedPermutations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */