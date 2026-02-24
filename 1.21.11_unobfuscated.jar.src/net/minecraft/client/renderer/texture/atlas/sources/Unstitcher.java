/*     */ package net.minecraft.client.renderer.texture.atlas.sources;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class Unstitcher extends Record implements SpriteSource {
/*     */   private final Identifier resource;
/*     */   private final List<Region> regions;
/*     */   private final double xDivisor;
/*     */   private final double yDivisor;
/*     */   
/*  23 */   public Unstitcher(Identifier resource, List<Region> regions, double xDivisor, double yDivisor) { this.resource = resource; this.regions = regions; this.xDivisor = xDivisor; this.yDivisor = yDivisor; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  23 */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher; } public Identifier resource() { return this.resource; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #23	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher;
/*  23 */     //   0	8	1	o	Ljava/lang/Object; } public List<Region> regions() { return this.regions; } public double xDivisor() { return this.xDivisor; } public double yDivisor() { return this.yDivisor; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  29 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<Unstitcher> MAP_CODEC;
/*     */   static {
/*  31 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("resource").forGetter(Unstitcher::resource), (App)net.minecraft.util.ExtraCodecs.nonEmptyList(Region.CODEC.listOf()).fieldOf("regions").forGetter(Unstitcher::regions), (App)Codec.DOUBLE.optionalFieldOf("divisor_x", 1.0D).forGetter(Unstitcher::xDivisor), (App)Codec.DOUBLE.optionalFieldOf("divisor_y", 1.0D).forGetter(Unstitcher::yDivisor)).apply((Applicative)i, Unstitcher::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(net.minecraft.server.packs.resources.ResourceManager resourceManager, SpriteSource.Output output) {
/*  40 */     Identifier resourceId = TEXTURE_ID_CONVERTER.idToFile(this.resource);
/*  41 */     Optional<Resource> resource = resourceManager.getResource(resourceId);
/*  42 */     if (resource.isPresent()) {
/*  43 */       LazyLoadedImage image = new LazyLoadedImage(resourceId, resource.get(), this.regions.size());
/*  44 */       for (Region region : this.regions) {
/*  45 */         output.add(region.sprite, new RegionInstance(image, region, this.xDivisor, this.yDivisor));
/*     */       }
/*     */     } else {
/*  48 */       LOGGER.warn("Missing sprite: {}", resourceId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public com.mojang.serialization.MapCodec<Unstitcher> codec() {
/*  54 */     return MAP_CODEC;
/*     */   }
/*     */   public static final class Region extends Record { private final Identifier sprite; private final double x; private final double y; private final double width; private final double height; public static final Codec<Region> CODEC;
/*  57 */     public Region(Identifier sprite, double x, double y, double width, double height) { this.sprite = sprite; this.x = x; this.y = y; this.width = width; this.height = height; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #57	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/atlas/sources/Unstitcher$Region;
/*  57 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier sprite() { return this.sprite; } public double x() { return this.x; } public double y() { return this.y; } public double width() { return this.width; } public double height() { return this.height; } static {
/*  58 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("sprite").forGetter(Region::sprite), (App)Codec.DOUBLE.fieldOf("x").forGetter(Region::x), (App)Codec.DOUBLE.fieldOf("y").forGetter(Region::y), (App)Codec.DOUBLE.fieldOf("width").forGetter(Region::width), (App)Codec.DOUBLE.fieldOf("height").forGetter(Region::height)).apply((Applicative)i, Region::new));
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class RegionInstance
/*     */     implements SpriteSource.DiscardableLoader
/*     */   {
/*     */     private final LazyLoadedImage image;
/*     */     
/*     */     private final Unstitcher.Region region;
/*     */     
/*     */     private final double xDivisor;
/*     */     private final double yDivisor;
/*     */     
/*     */     private RegionInstance(LazyLoadedImage image, Unstitcher.Region region, double xDivisor, double yDivisor) {
/*  74 */       this.image = image;
/*  75 */       this.region = region;
/*  76 */       this.xDivisor = xDivisor;
/*  77 */       this.yDivisor = yDivisor;
/*     */     }
/*     */ 
/*     */     
/*     */     public net.minecraft.client.renderer.texture.SpriteContents get(net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader loader) {
/*     */       try {
/*  83 */         NativeImage fullImage = this.image.get();
/*     */         
/*  85 */         double xScale = fullImage.getWidth() / this.xDivisor;
/*  86 */         double yScale = fullImage.getHeight() / this.yDivisor;
/*     */         
/*  88 */         int x = Mth.floor(this.region.x * xScale);
/*  89 */         int y = Mth.floor(this.region.y * yScale);
/*     */         
/*  91 */         int width = Mth.floor(this.region.width * xScale);
/*  92 */         int height = Mth.floor(this.region.height * yScale);
/*     */         
/*  94 */         NativeImage target = new NativeImage(NativeImage.Format.RGBA, width, height, false);
/*  95 */         fullImage.copyRect(target, x, y, 0, 0, width, height, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 101 */         return new net.minecraft.client.renderer.texture.SpriteContents(this.region.sprite, new net.minecraft.client.resources.metadata.animation.FrameSize(width, height), target);
/* 102 */       } catch (Exception e) {
/* 103 */         Unstitcher.LOGGER.error("Failed to unstitch region {}", this.region.sprite, e);
/*     */       } finally {
/* 105 */         this.image.release();
/*     */       } 
/* 107 */       return net.minecraft.client.renderer.texture.MissingTextureAtlasSprite.create();
/*     */     }
/*     */ 
/*     */     
/*     */     public void discard() {
/* 112 */       this.image.release();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/atlas/sources/Unstitcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */