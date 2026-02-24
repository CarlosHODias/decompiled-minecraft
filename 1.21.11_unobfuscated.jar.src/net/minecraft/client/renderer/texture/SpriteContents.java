/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.metadata.animation.AnimationFrame;
/*     */ import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
/*     */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*     */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.util.ARGB;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SpriteContents
/*     */   implements AutoCloseable, Stitcher.Entry
/*     */ {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  44 */   public static final int UBO_SIZE = new Std140SizeCalculator()
/*  45 */     .putMat4f()
/*  46 */     .putMat4f()
/*  47 */     .putFloat()
/*  48 */     .putFloat()
/*  49 */     .putInt()
/*  50 */     .get();
/*     */   
/*     */   private final Identifier name;
/*     */   
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final NativeImage originalImage;
/*     */   private NativeImage[] byMipLevel;
/*     */   private final AnimatedTexture animatedTexture;
/*     */   private final List<MetadataSectionType.WithValue<?>> additionalMetadata;
/*     */   private final MipmapStrategy mipmapStrategy;
/*     */   private final float alphaCutoffBias;
/*     */   
/*     */   public SpriteContents(Identifier name, FrameSize frameSize, NativeImage image) {
/*  64 */     this(name, frameSize, image, Optional.empty(), List.of(), Optional.empty());
/*     */   }
/*     */   
/*     */   public SpriteContents(Identifier name, FrameSize frameSize, NativeImage image, Optional<AnimationMetadataSection> animationInfo, List<MetadataSectionType.WithValue<?>> additionalMetadata, Optional<TextureMetadataSection> textureInfo) {
/*  68 */     this.name = name;
/*  69 */     this.width = frameSize.width();
/*  70 */     this.height = frameSize.height();
/*  71 */     this.additionalMetadata = additionalMetadata;
/*     */     
/*  73 */     this
/*     */       
/*  75 */       .animatedTexture = animationInfo.<AnimatedTexture>map(animation -> createAnimatedTexture(frameSize, frameSize.getWidth(), frameSize.getHeight(), image)).orElse(null);
/*  76 */     this.originalImage = image;
/*  77 */     this.byMipLevel = new NativeImage[] { this.originalImage };
/*  78 */     this.mipmapStrategy = textureInfo.<MipmapStrategy>map(TextureMetadataSection::mipmapStrategy).orElse(MipmapStrategy.AUTO);
/*  79 */     this.alphaCutoffBias = (Float)textureInfo.<Float>map(TextureMetadataSection::alphaCutoffBias).orElse(0.0F);
/*     */   }
/*     */   
/*     */   public void increaseMipLevel(int mipLevel) {
/*     */     try {
/*  84 */       this.byMipLevel = MipmapGenerator.generateMipLevels(this.name, this.byMipLevel, mipLevel, this.mipmapStrategy, this.alphaCutoffBias);
/*  85 */     } catch (Throwable t) {
/*  86 */       CrashReport report = CrashReport.forThrowable(t, "Generating mipmaps for frame");
/*     */       
/*  88 */       CrashReportCategory frameCategory = report.addCategory("Frame being iterated");
/*  89 */       frameCategory.setDetail("Sprite name", this.name);
/*  90 */       frameCategory.setDetail("Sprite size", () -> "" + this.width + " x " + this.width);
/*  91 */       frameCategory.setDetail("Sprite frames", () -> "" + getFrameCount() + " frames");
/*  92 */       frameCategory.setDetail("Mipmap levels", mipLevel);
/*  93 */       frameCategory.setDetail("Original image size", () -> "" + this.originalImage.getWidth() + "x" + this.originalImage.getWidth());
/*     */       
/*  95 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getFrameCount() {
/* 100 */     return (this.animatedTexture != null) ? this.animatedTexture.frames.size() : 1;
/*     */   }
/*     */   
/*     */   public boolean isAnimated() {
/* 104 */     return (getFrameCount() > 1);
/*     */   }
/*     */   private AnimatedTexture createAnimatedTexture(FrameSize frameSize, int fullWidth, int fullHeight, AnimationMetadataSection metadata) {
/*     */     List<FrameInfo> frames;
/* 108 */     int frameRowSize = fullWidth / frameSize.width();
/* 109 */     int frameColumnSize = fullHeight / frameSize.height();
/* 110 */     int totalFrameCount = frameRowSize * frameColumnSize;
/*     */     
/* 112 */     int defaultFrameTime = metadata.defaultFrameTime();
/*     */ 
/*     */     
/* 115 */     if (metadata.frames().isEmpty()) {
/* 116 */       frames = new ArrayList<>(totalFrameCount);
/*     */       
/* 118 */       for (int i = 0; i < totalFrameCount; i++) {
/* 119 */         frames.add(new FrameInfo(i, defaultFrameTime));
/*     */       }
/*     */     } else {
/* 122 */       List<AnimationFrame> metadataFrames = metadata.frames().get();
/* 123 */       frames = new ArrayList<>(metadataFrames.size());
/*     */       
/* 125 */       for (AnimationFrame frame : metadataFrames) {
/* 126 */         frames.add(new FrameInfo(frame.index(), frame.timeOr(defaultFrameTime)));
/*     */       }
/*     */       
/* 129 */       int index = 0;
/* 130 */       IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
/*     */       
/* 132 */       Iterator<FrameInfo> iterator = frames.iterator();
/* 133 */       while (iterator.hasNext()) {
/* 134 */         FrameInfo frame = iterator.next();
/*     */         boolean isValid = true;
/* 136 */         if (frame.time <= 0) {
/* 137 */           LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", new Object[] { this.name, index, frame.time });
/* 138 */           isValid = false;
/*     */         } 
/* 140 */         if (frame.index < 0 || frame.index >= totalFrameCount) {
/* 141 */           LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", new Object[] { this.name, index, frame.index });
/* 142 */           isValid = false;
/*     */         } 
/*     */         
/* 145 */         if (isValid) {
/* 146 */           intOpenHashSet.add(frame.index);
/*     */         } else {
/* 148 */           iterator.remove();
/*     */         } 
/* 150 */         index++;
/*     */       } 
/*     */       
/* 153 */       int[] unusedFrameIndices = IntStream.range(0, totalFrameCount).filter(i -> !usedFrameIndices.contains(i)).toArray();
/* 154 */       if (unusedFrameIndices.length > 0) {
/* 155 */         LOGGER.warn("Unused frames in sprite {}: {}", this.name, Arrays.toString(unusedFrameIndices));
/*     */       }
/*     */     } 
/*     */     
/* 159 */     if (frames.size() <= 1) {
/* 160 */       return null;
/*     */     }
/*     */     
/* 163 */     return new AnimatedTexture(List.copyOf(frames), frameRowSize, metadata.interpolatedFrames());
/*     */   }
/*     */ 
/*     */   
/*     */   public int width() {
/* 168 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int height() {
/* 173 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier name() {
/* 178 */     return this.name;
/*     */   }
/*     */   
/*     */   public IntStream getUniqueFrames() {
/* 182 */     return (this.animatedTexture != null) ? this.animatedTexture.getUniqueFrames() : IntStream.of(1);
/*     */   }
/*     */   
/*     */   public AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/* 186 */     return (this.animatedTexture != null) ? this.animatedTexture.createAnimationState(uboSlice, spriteUboSize) : null;
/*     */   }
/*     */   
/*     */   public <T> Optional<T> getAdditionalMetadata(MetadataSectionType<T> type) {
/* 190 */     for (MetadataSectionType.WithValue<?> metadata : this.additionalMetadata) {
/* 191 */       Optional<T> result = metadata.unwrapToType(type);
/* 192 */       if (result.isPresent()) {
/* 193 */         return result;
/*     */       }
/*     */     } 
/* 196 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 201 */     for (NativeImage image : this.byMipLevel) {
/* 202 */       image.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 208 */     return "SpriteContents{name=" + String.valueOf(this.name) + ", frameCount=" + getFrameCount() + ", height=" + this.height + ", width=" + this.width + "}";
/*     */   }
/*     */   
/*     */   public boolean isTransparent(int frame, int x, int y) {
/* 212 */     int actualX = x;
/* 213 */     int actualY = y;
/* 214 */     if (this.animatedTexture != null) {
/* 215 */       actualX += this.animatedTexture.getFrameX(frame) * this.width;
/* 216 */       actualY += this.animatedTexture.getFrameY(frame) * this.height;
/*     */     } 
/* 218 */     return (ARGB.alpha(this.originalImage.getPixel(actualX, actualY)) == 0);
/*     */   }
/*     */   
/*     */   public void uploadFirstFrame(GpuTexture destination, int level) {
/* 222 */     RenderSystem.getDevice().createCommandEncoder().writeToTexture(destination, this.byMipLevel[level], level, 0, 0, 0, this.width >> level, this.height >> level, 0, 0);
/*     */   }
/*     */   private static final class FrameInfo extends Record { private final int index; private final int time;
/* 225 */     private FrameInfo(int index, int time) { this.index = index; this.time = time; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 225 */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo; } public int index() { return this.index; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 225 */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo; } public int time() { return this.time; }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } }
/*     */   private class AnimatedTexture { private final List<SpriteContents.FrameInfo> frames; private final int frameRowSize; private final boolean interpolateFrames;
/*     */     
/*     */     private AnimatedTexture(List<SpriteContents.FrameInfo> frames, int frameRowSize, boolean interpolateFrames) {
/* 233 */       this.frames = frames;
/* 234 */       this.frameRowSize = frameRowSize;
/* 235 */       this.interpolateFrames = interpolateFrames;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int getFrameX(int index) {
/* 244 */       return index % this.frameRowSize;
/*     */     }
/*     */     
/*     */     private int getFrameY(int index) {
/* 248 */       return index / this.frameRowSize;
/*     */     }
/*     */     
/*     */     public SpriteContents.AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/* 252 */       GpuDevice device = RenderSystem.getDevice();
/* 253 */       Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 254 */       GpuBufferSlice[] spriteUbosByMip = new GpuBufferSlice[SpriteContents.this.byMipLevel.length];
/*     */       
/* 256 */       for (int frame : getUniqueFrames().toArray()) {
/* 257 */         GpuTexture texture = device.createTexture(() -> String.valueOf(SpriteContents.this.name) + " animation frame " + String.valueOf(SpriteContents.this.name), 5, TextureFormat.RGBA8, SpriteContents.this.width, SpriteContents.this.height, 1, SpriteContents.this.byMipLevel.length + 1);
/* 258 */         int offsetX = getFrameX(frame) * SpriteContents.this.width;
/* 259 */         int offsetY = getFrameY(frame) * SpriteContents.this.height;
/* 260 */         for (int i = 0; i < SpriteContents.this.byMipLevel.length; i++) {
/* 261 */           RenderSystem.getDevice().createCommandEncoder().writeToTexture(texture, SpriteContents.this.byMipLevel[i], i, 0, 0, 0, SpriteContents.this.width >> i, SpriteContents.this.height >> i, offsetX >> i, offsetY >> i);
/*     */         }
/* 263 */         int2ObjectOpenHashMap.put(frame, RenderSystem.getDevice().createTextureView(texture));
/*     */       } 
/*     */       
/* 266 */       for (int level = 0; level < SpriteContents.this.byMipLevel.length; level++) {
/* 267 */         spriteUbosByMip[level] = uboSlice.slice((level * spriteUboSize), spriteUboSize);
/*     */       }
/*     */       
/* 270 */       return new SpriteContents.AnimationState(SpriteContents.this, this, (Int2ObjectMap<GpuTextureView>)int2ObjectOpenHashMap, spriteUbosByMip);
/*     */     }
/*     */     
/*     */     public IntStream getUniqueFrames() {
/* 274 */       return this.frames.stream().mapToInt(f -> f.index).distinct();
/*     */     } }
/*     */ 
/*     */   
/*     */   public class AnimationState
/*     */     implements AutoCloseable {
/*     */     private int frame;
/*     */     private int subFrame;
/*     */     private final SpriteContents.AnimatedTexture animationInfo;
/*     */     private final Int2ObjectMap<GpuTextureView> frameTexturesByIndex;
/*     */     private final GpuBufferSlice[] spriteUbosByMip;
/*     */     private boolean isDirty = true;
/*     */     
/*     */     private AnimationState(SpriteContents this$0, SpriteContents.AnimatedTexture animationInfo, Int2ObjectMap<GpuTextureView> frameTexturesByIndex, GpuBufferSlice[] spriteUbosByMip) {
/* 288 */       this.animationInfo = animationInfo;
/* 289 */       this.frameTexturesByIndex = frameTexturesByIndex;
/* 290 */       this.spriteUbosByMip = spriteUbosByMip;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 294 */       this.subFrame++;
/* 295 */       this.isDirty = false;
/* 296 */       SpriteContents.FrameInfo currentFrame = this.animationInfo.frames.get(this.frame);
/* 297 */       if (this.subFrame >= currentFrame.time) {
/* 298 */         int oldFrame = currentFrame.index;
/* 299 */         this.frame = (this.frame + 1) % this.animationInfo.frames.size();
/* 300 */         this.subFrame = 0;
/*     */         
/* 302 */         int newFrame = ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).index;
/* 303 */         if (oldFrame != newFrame) {
/* 304 */           this.isDirty = true;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public GpuBufferSlice getDrawUbo(int level) {
/* 310 */       return this.spriteUbosByMip[level];
/*     */     }
/*     */     
/*     */     public boolean needsToDraw() {
/* 314 */       return (this.animationInfo.interpolateFrames || this.isDirty);
/*     */     }
/*     */     
/*     */     public void drawToAtlas(RenderPass renderPass, GpuBufferSlice ubo) {
/* 318 */       GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
/* 319 */       List<SpriteContents.FrameInfo> frames = this.animationInfo.frames;
/* 320 */       int oldFrame = ((SpriteContents.FrameInfo)frames.get(this.frame)).index;
/* 321 */       float frameProgress = this.subFrame / ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).time;
/* 322 */       int frameProgressAsInt = (int)(frameProgress * 1000.0F);
/*     */       
/* 324 */       if (this.animationInfo.interpolateFrames) {
/* 325 */         int newFrame = ((SpriteContents.FrameInfo)frames.get((this.frame + 1) % frames.size())).index;
/*     */         
/* 327 */         renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_INTERPOLATE);
/* 328 */         renderPass.bindTexture("CurrentSprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/* 329 */         renderPass.bindTexture("NextSprite", (GpuTextureView)this.frameTexturesByIndex.get(newFrame), sampler);
/* 330 */       } else if (this.isDirty) {
/* 331 */         renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
/* 332 */         renderPass.bindTexture("Sprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/*     */       } 
/*     */       
/* 335 */       renderPass.setUniform("SpriteAnimationInfo", ubo);
/*     */       
/* 337 */       renderPass.draw(frameProgressAsInt << 3, 6);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 342 */       for (ObjectIterator<GpuTextureView> objectIterator = this.frameTexturesByIndex.values().iterator(); objectIterator.hasNext(); ) { GpuTextureView view = objectIterator.next();
/* 343 */         view.texture().close();
/* 344 */         view.close(); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/SpriteContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */