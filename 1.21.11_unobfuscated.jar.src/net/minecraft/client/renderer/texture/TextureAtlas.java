/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextureAtlas
/*     */   extends AbstractTexture implements TickableTexture, Dumpable {
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  40 */   public static final Identifier LOCATION_BLOCKS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  45 */   public static final Identifier LOCATION_ITEMS = Identifier.withDefaultNamespace("textures/atlas/items.png");
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  50 */   public static final Identifier LOCATION_PARTICLES = Identifier.withDefaultNamespace("textures/atlas/particles.png");
/*     */ 
/*     */   
/*  53 */   private List<TextureAtlasSprite> sprites = List.of();
/*  54 */   private List<SpriteContents.AnimationState> animatedTexturesStates = List.of();
/*  55 */   private Map<Identifier, TextureAtlasSprite> texturesByName = Map.of();
/*     */   
/*     */   private TextureAtlasSprite missingSprite;
/*     */   private final Identifier location;
/*     */   private final int maxSupportedTextureSize;
/*     */   private int width;
/*     */   private int height;
/*     */   private int maxMipLevel;
/*     */   private int mipLevelCount;
/*  64 */   private GpuTextureView[] mipViews = new GpuTextureView[0];
/*     */   private GpuBuffer spriteUbos;
/*     */   
/*     */   public TextureAtlas(Identifier location) {
/*  68 */     this.location = location;
/*  69 */     this.maxSupportedTextureSize = RenderSystem.getDevice().getMaxTextureSize();
/*     */   }
/*     */ 
/*     */   
/*     */   private void createTexture(int newWidth, int newHeight, int newMipLevel) {
/*  74 */     LOGGER.info("Created: {}x{}x{} {}-atlas", new Object[] { newWidth, newHeight, newMipLevel, this.location });
/*     */     
/*  76 */     GpuDevice device = RenderSystem.getDevice();
/*  77 */     close();
/*  78 */     Objects.requireNonNull(this.location); this.texture = device.createTexture(this.location::toString, 15, TextureFormat.RGBA8, newWidth, newHeight, 1, newMipLevel + 1);
/*  79 */     this.textureView = device.createTextureView(this.texture);
/*  80 */     this.width = newWidth;
/*  81 */     this.height = newHeight;
/*  82 */     this.maxMipLevel = newMipLevel;
/*  83 */     this.mipLevelCount = newMipLevel + 1;
/*  84 */     this.mipViews = new GpuTextureView[this.mipLevelCount];
/*  85 */     for (int level = 0; level <= this.maxMipLevel; level++) {
/*  86 */       this.mipViews[level] = device.createTextureView(this.texture, level, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void upload(SpriteLoader.Preparations preparations) {
/*  91 */     createTexture(preparations.width(), preparations.height(), preparations.mipLevel());
/*  92 */     clearTextureData();
/*     */     
/*  94 */     this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
/*  95 */     this.texturesByName = Map.copyOf(preparations.regions());
/*  96 */     this.missingSprite = this.texturesByName.get(MissingTextureAtlasSprite.getLocation());
/*  97 */     if (this.missingSprite == null) {
/*  98 */       throw new IllegalStateException("Atlas '" + String.valueOf(this.location) + "' (" + this.texturesByName.size() + " sprites) has no missing texture sprite");
/*     */     }
/*     */     
/* 101 */     List<TextureAtlasSprite> sprites = new ArrayList<>();
/* 102 */     List<SpriteContents.AnimationState> animationStates = new ArrayList<>();
/* 103 */     int animatedSpriteCount = (int)preparations.regions().values().stream().filter(TextureAtlasSprite::isAnimated).count();
/* 104 */     int spriteUboSize = Mth.roundToward(SpriteContents.UBO_SIZE, RenderSystem.getDevice().getUniformOffsetAlignment());
/* 105 */     int uboBlockSize = spriteUboSize * this.mipLevelCount;
/* 106 */     ByteBuffer spriteUboBuffer = MemoryUtil.memAlloc(animatedSpriteCount * uboBlockSize);
/* 107 */     int animationIndex = 0;
/*     */     
/* 109 */     for (TextureAtlasSprite sprite : preparations.regions().values()) {
/* 110 */       if (sprite.isAnimated()) {
/* 111 */         sprite.uploadSpriteUbo(spriteUboBuffer, animationIndex * uboBlockSize, this.maxMipLevel, this.width, this.height, spriteUboSize);
/* 112 */         animationIndex++;
/*     */       } 
/*     */     } 
/*     */     
/* 116 */     GpuBuffer spriteUbos = (animationIndex > 0) ? RenderSystem.getDevice().createBuffer(() -> String.valueOf(this.location) + " sprite UBOs", 128, spriteUboBuffer) : null;
/*     */     
/* 118 */     animationIndex = 0;
/* 119 */     for (TextureAtlasSprite sprite : preparations.regions().values()) {
/* 120 */       sprites.add(sprite);
/*     */       
/* 122 */       if (sprite.isAnimated() && spriteUbos != null) {
/* 123 */         SpriteContents.AnimationState animationState = sprite.createAnimationState(spriteUbos.slice((animationIndex * uboBlockSize), uboBlockSize), spriteUboSize);
/* 124 */         animationIndex++;
/* 125 */         if (animationState != null) {
/* 126 */           animationStates.add(animationState);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     this.spriteUbos = spriteUbos;
/* 132 */     this.sprites = sprites;
/* 133 */     this.animatedTexturesStates = List.copyOf(animationStates);
/*     */     
/* 135 */     uploadInitialContents();
/*     */     
/* 137 */     if (SharedConstants.DEBUG_DUMP_TEXTURE_ATLAS) {
/* 138 */       Path dumpDir = TextureUtil.getDebugTexturePath();
/*     */       try {
/* 140 */         Files.createDirectories(dumpDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 141 */         dumpContents(this.location, dumpDir);
/* 142 */       } catch (Exception e) {
/* 143 */         LOGGER.warn("Failed to dump atlas contents to {}", dumpDir);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void uploadInitialContents() {
/* 149 */     GpuDevice device = RenderSystem.getDevice();
/* 150 */     int spriteUboSize = Mth.roundToward(SpriteContents.UBO_SIZE, RenderSystem.getDevice().getUniformOffsetAlignment());
/* 151 */     int uboBlockSize = spriteUboSize * this.mipLevelCount;
/* 152 */     GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
/*     */ 
/*     */     
/* 155 */     List<TextureAtlasSprite> staticSprites = this.sprites.stream().filter(s -> !s.isAnimated()).toList();
/* 156 */     List<GpuTextureView[]> scratchTextures = (List)new ArrayList<>();
/* 157 */     ByteBuffer buffer = MemoryUtil.memAlloc(staticSprites.size() * uboBlockSize);
/*     */ 
/*     */     
/* 160 */     for (int i = 0; i < staticSprites.size(); i++) {
/* 161 */       TextureAtlasSprite sprite = staticSprites.get(i);
/* 162 */       sprite.uploadSpriteUbo(buffer, i * uboBlockSize, this.maxMipLevel, this.width, this.height, spriteUboSize);
/* 163 */       GpuTexture scratchTexture = device.createTexture(() -> sprite.contents().name().toString(), 5, TextureFormat.RGBA8, sprite.contents().width(), sprite.contents().height(), 1, this.mipLevelCount);
/* 164 */       GpuTextureView[] views = new GpuTextureView[this.mipLevelCount];
/* 165 */       for (int level = 0; level <= this.maxMipLevel; level++) {
/* 166 */         sprite.uploadFirstFrame(scratchTexture, level);
/* 167 */         views[level] = device.createTextureView(scratchTexture);
/*     */       } 
/* 169 */       scratchTextures.add(views);
/*     */     } 
/*     */ 
/*     */     
/* 173 */     GpuBuffer ubo = device.createBuffer(() -> "SpriteAnimationInfo", 128, buffer); 
/* 174 */     try { for (int level = 0; level < this.mipLevelCount; level++) {
/* 175 */         RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.location), this.mipViews[level], OptionalInt.empty()); 
/* 176 */         try { renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
/*     */           
/* 178 */           for (int j = 0; j < staticSprites.size(); j++) {
/* 179 */             renderPass.bindTexture("Sprite", ((GpuTextureView[])scratchTextures.get(j))[level], sampler);
/* 180 */             renderPass.setUniform("SpriteAnimationInfo", ubo.slice((j * uboBlockSize + level * spriteUboSize), SpriteContents.UBO_SIZE));
/* 181 */             renderPass.draw(0, 6);
/*     */           } 
/* 183 */           if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/*     */             try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/* 185 */       }  if (ubo != null) ubo.close();  } catch (Throwable throwable) { if (ubo != null)
/*     */         try { ubo.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 188 */      for (GpuTextureView[] views : scratchTextures) {
/* 189 */       for (GpuTextureView view : views) {
/* 190 */         view.close();
/* 191 */         view.texture().close();
/*     */       } 
/*     */     } 
/* 194 */     MemoryUtil.memFree(buffer);
/*     */     
/* 196 */     uploadAnimationFrames();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dumpContents(Identifier selfId, Path dir) throws IOException {
/* 201 */     String outputId = selfId.toDebugFileName();
/* 202 */     TextureUtil.writeAsPNG(dir, outputId, getTexture(), this.maxMipLevel, argb -> argb);
/* 203 */     dumpSpriteNames(dir, outputId, this.texturesByName);
/*     */   }
/*     */   
/*     */   private static void dumpSpriteNames(Path dir, String outputId, Map<Identifier, TextureAtlasSprite> regions) {
/* 207 */     Path outputPath = dir.resolve(outputId + ".txt"); 
/* 208 */     try { Writer output = Files.newBufferedWriter(outputPath, new java.nio.file.OpenOption[0]); 
/* 209 */       try { for (Map.Entry<Identifier, TextureAtlasSprite> e : (Iterable<Map.Entry<Identifier, TextureAtlasSprite>>)regions.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
/* 210 */           TextureAtlasSprite value = e.getValue();
/* 211 */           output.write(String.format(Locale.ROOT, "%s\tx=%d\ty=%d\tw=%d\th=%d%n", new Object[] { e.getKey(), value.getX(), value.getY(), value.contents().width(), value.contents().height() }));
/*     */         } 
/* 213 */         if (output != null) output.close();  } catch (Throwable throwable) { if (output != null) try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 214 */     { LOGGER.warn("Failed to write file {}", outputPath, e); }
/*     */   
/*     */   }
/*     */   
/*     */   public void cycleAnimationFrames() {
/* 219 */     if (this.texture == null) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     for (SpriteContents.AnimationState animationState : this.animatedTexturesStates) {
/* 224 */       animationState.tick();
/*     */     }
/*     */     
/* 227 */     uploadAnimationFrames();
/*     */   }
/*     */   
/*     */   private void uploadAnimationFrames() {
/* 231 */     if (this.animatedTexturesStates.stream().anyMatch(SpriteContents.AnimationState::needsToDraw))
/* 232 */       for (int level = 0; level <= this.maxMipLevel; level++) {
/* 233 */         RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.location), this.mipViews[level], OptionalInt.empty()); 
/* 234 */         try { for (SpriteContents.AnimationState animationState : this.animatedTexturesStates) {
/* 235 */             if (animationState.needsToDraw()) {
/* 236 */               animationState.drawToAtlas(renderPass, animationState.getDrawUbo(level));
/*     */             }
/*     */           } 
/* 239 */           if (renderPass != null) renderPass.close();  }
/*     */         catch (Throwable throwable) { if (renderPass != null)
/*     */             try { renderPass.close(); }
/*     */             catch (Throwable throwable1)
/*     */             { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */       
/* 246 */       }   } public void tick() { cycleAnimationFrames(); }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getSprite(Identifier location) {
/* 250 */     TextureAtlasSprite result = this.texturesByName.getOrDefault(location, this.missingSprite);
/* 251 */     if (result == null) {
/* 252 */       throw new IllegalStateException("Tried to lookup sprite, but atlas is not initialized");
/*     */     }
/* 254 */     return result;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite missingSprite() {
/* 258 */     return Objects.<TextureAtlasSprite>requireNonNull(this.missingSprite, "Atlas not initialized");
/*     */   }
/*     */   
/*     */   public void clearTextureData() {
/* 262 */     this.sprites.forEach(TextureAtlasSprite::close);
/*     */     
/* 264 */     this.sprites = List.of();
/* 265 */     this.animatedTexturesStates = List.of();
/* 266 */     this.texturesByName = Map.of();
/* 267 */     this.missingSprite = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 272 */     super.close();
/* 273 */     for (GpuTextureView view : this.mipViews) {
/* 274 */       view.close();
/*     */     }
/* 276 */     for (SpriteContents.AnimationState animationState : this.animatedTexturesStates) {
/* 277 */       animationState.close();
/*     */     }
/* 279 */     if (this.spriteUbos != null) {
/* 280 */       this.spriteUbos.close();
/* 281 */       this.spriteUbos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Identifier location() {
/* 286 */     return this.location;
/*     */   }
/*     */   
/*     */   public int maxSupportedTextureSize() {
/* 290 */     return this.maxSupportedTextureSize;
/*     */   }
/*     */   
/*     */   int getWidth() {
/* 294 */     return this.width;
/*     */   }
/*     */   
/*     */   int getHeight() {
/* 298 */     return this.height;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/TextureAtlas.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */