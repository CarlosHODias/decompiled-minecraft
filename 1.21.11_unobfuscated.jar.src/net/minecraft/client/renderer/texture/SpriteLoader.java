/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSourceList;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SpriteLoader
/*     */ {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Identifier location;
/*     */   private final int maxSupportedTextureSize;
/*     */   
/*     */   public SpriteLoader(Identifier location, int maxSupportedTextureSize) {
/*  40 */     this.location = location;
/*  41 */     this.maxSupportedTextureSize = maxSupportedTextureSize;
/*     */   }
/*     */   
/*     */   public static SpriteLoader create(TextureAtlas atlas) {
/*  45 */     return new SpriteLoader(atlas.location(), atlas.maxSupportedTextureSize());
/*     */   }
/*     */   
/*     */   private Preparations stitch(List<SpriteContents> sprites, int maxMipmapLevels, Executor executor) {
/*  49 */     Zone ignored = Profiler.get().zone(() -> "stitch " + String.valueOf(this.location)); 
/*  50 */     try { int mipLevel; int maxTextureSize = this.maxSupportedTextureSize;
/*     */       
/*  52 */       int minTexelSize = Integer.MAX_VALUE;
/*  53 */       int lowestOneBit = 1 << maxMipmapLevels;
/*     */       
/*  55 */       for (SpriteContents spriteInfo : sprites) {
/*  56 */         minTexelSize = Math.min(minTexelSize, Math.min(spriteInfo.width(), spriteInfo.height()));
/*  57 */         int lowestTextureBit = Math.min(Integer.lowestOneBit(spriteInfo.width()), Integer.lowestOneBit(spriteInfo.height()));
/*  58 */         if (lowestTextureBit < lowestOneBit) {
/*  59 */           LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { spriteInfo.name(), spriteInfo.width(), spriteInfo.height(), Mth.log2(lowestOneBit), Mth.log2(lowestTextureBit) });
/*  60 */           lowestOneBit = lowestTextureBit;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  65 */       int minSize = Math.min(minTexelSize, lowestOneBit);
/*  66 */       int minPowerOfTwo = Mth.log2(minSize);
/*     */ 
/*     */       
/*  69 */       if (minPowerOfTwo < maxMipmapLevels) {
/*  70 */         LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.location, maxMipmapLevels, minPowerOfTwo, minSize });
/*  71 */         mipLevel = minPowerOfTwo;
/*     */       } else {
/*  73 */         mipLevel = maxMipmapLevels;
/*     */       } 
/*     */       
/*  76 */       Options options = (Minecraft.getInstance()).options;
/*  77 */       int anisotropyBit = (mipLevel == 0 || options.textureFiltering().get() != TextureFilteringMethod.ANISOTROPIC) ? 0 : (Integer)options.maxAnisotropyBit().get();
/*  78 */       Stitcher<SpriteContents> stitcher = new Stitcher<>(maxTextureSize, maxTextureSize, mipLevel, anisotropyBit);
/*  79 */       for (SpriteContents spriteInfo : sprites) {
/*  80 */         stitcher.registerSprite(spriteInfo);
/*     */       }
/*     */       
/*     */       try {
/*  84 */         stitcher.stitch();
/*  85 */       } catch (StitcherException e) {
/*  86 */         CrashReport report = CrashReport.forThrowable(e, "Stitching");
/*  87 */         CrashReportCategory category = report.addCategory("Stitcher");
/*  88 */         category.setDetail("Sprites", e.getAllSprites().stream().map(s -> String.format(Locale.ROOT, "%s[%dx%d]", new Object[] { s.name(), s.width(), s.height() })).collect(Collectors.joining(",")));
/*  89 */         category.setDetail("Max Texture Size", maxTextureSize);
/*  90 */         throw new ReportedException(report);
/*     */       } 
/*     */ 
/*     */       
/*  94 */       int width = stitcher.getWidth();
/*  95 */       int height = stitcher.getHeight();
/*  96 */       Map<Identifier, TextureAtlasSprite> result = getStitchedSprites(stitcher, width, height);
/*  97 */       TextureAtlasSprite missingSprite = result.get(MissingTextureAtlasSprite.getLocation());
/*     */       
/*  99 */       CompletableFuture<Void> readyForUpload = CompletableFuture.runAsync(() -> result.values().forEach(()), executor);
/*     */       
/* 101 */       Preparations preparations = new Preparations(width, height, mipLevel, missingSprite, result, readyForUpload);
/* 102 */       if (ignored != null) ignored.close();  return preparations; } catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 106 */      } private static CompletableFuture<List<SpriteContents>> runSpriteSuppliers(SpriteResourceLoader resourceLoader, List<SpriteSource.Loader> sprites, Executor executor) { List<CompletableFuture<SpriteContents>> spriteFutures = sprites.stream().map(supplier -> CompletableFuture.supplyAsync((), executor)).toList();
/* 107 */     return Util.sequence(spriteFutures).thenApply(l -> l.stream().filter(Objects::nonNull).toList()); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Preparations> loadAndStitch(ResourceManager manager, Identifier atlasInfoLocation, int maxMipmapLevels, Executor taskExecutor, Set<MetadataSectionType<?>> additionalMetadata) {
/* 111 */     SpriteResourceLoader spriteResourceLoader = SpriteResourceLoader.create(additionalMetadata);
/* 112 */     return CompletableFuture.supplyAsync(() -> SpriteSourceList.load(manager, atlasInfoLocation).list(manager), taskExecutor)
/* 113 */       .thenCompose(sprites -> runSpriteSuppliers(spriteResourceLoader, sprites, taskExecutor))
/* 114 */       .thenApply(resources -> stitch(taskExecutor, maxMipmapLevels, maxMipmapLevels));
/*     */   }
/*     */   
/*     */   private Map<Identifier, TextureAtlasSprite> getStitchedSprites(Stitcher<SpriteContents> stitcher, int atlasWidth, int atlasHeight) {
/* 118 */     Map<Identifier, TextureAtlasSprite> result = new HashMap<>();
/*     */     
/* 120 */     stitcher.gatherSprites((contents, x, y, padding) -> result.put(atlasWidth.name(), new TextureAtlasSprite(this.location, atlasWidth, result, atlasWidth, x, y, padding)));
/*     */ 
/*     */ 
/*     */     
/* 124 */     return result;
/*     */   }
/*     */   public static final class Preparations extends Record { private final int width; private final int height; private final int mipLevel; private final TextureAtlasSprite missing; private final Map<Identifier, TextureAtlasSprite> regions; private final CompletableFuture<Void> readyForUpload;
/* 127 */     public Preparations(int width, int height, int mipLevel, TextureAtlasSprite missing, Map<Identifier, TextureAtlasSprite> regions, CompletableFuture<Void> readyForUpload) { this.width = width; this.height = height; this.mipLevel = mipLevel; this.missing = missing; this.regions = regions; this.readyForUpload = readyForUpload; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 127 */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/SpriteLoader$Preparations;
/* 127 */       //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public int mipLevel() { return this.mipLevel; } public TextureAtlasSprite missing() { return this.missing; } public Map<Identifier, TextureAtlasSprite> regions() { return this.regions; } public CompletableFuture<Void> readyForUpload() { return this.readyForUpload; }
/*     */      public TextureAtlasSprite getSprite(Identifier id) {
/* 129 */       return this.regions.get(id);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/SpriteLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */