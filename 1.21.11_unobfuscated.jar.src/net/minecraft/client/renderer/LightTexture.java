/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ public class LightTexture
/*     */   implements AutoCloseable
/*     */ {
/*     */   public static final int FULL_BRIGHT = 15728880;
/*     */   public static final int FULL_SKY = 15728640;
/*     */   public static final int FULL_BLOCK = 240;
/*     */   private static final int TEXTURE_SIZE = 16;
/*  37 */   private static final int LIGHTMAP_UBO_SIZE = new Std140SizeCalculator()
/*  38 */     .putFloat()
/*  39 */     .putFloat()
/*  40 */     .putFloat()
/*  41 */     .putFloat()
/*  42 */     .putFloat()
/*  43 */     .putFloat()
/*  44 */     .putFloat()
/*  45 */     .putVec3()
/*  46 */     .putVec3()
/*  47 */     .get();
/*     */   
/*     */   private final GpuTexture texture;
/*     */   
/*     */   private final GpuTextureView textureView;
/*     */   
/*     */   private boolean updateLightTexture;
/*     */   
/*     */   private float blockLightRedFlicker;
/*     */   
/*     */   private final GameRenderer renderer;
/*     */   private final Minecraft minecraft;
/*     */   private final MappableRingBuffer ubo;
/*  60 */   private final RandomSource randomSource = RandomSource.create();
/*     */   
/*     */   public LightTexture(GameRenderer renderer, Minecraft minecraft) {
/*  63 */     this.renderer = renderer;
/*  64 */     this.minecraft = minecraft;
/*  65 */     GpuDevice device = RenderSystem.getDevice();
/*  66 */     this.texture = device.createTexture("Light Texture", 12, TextureFormat.RGBA8, 16, 16, 1, 1);
/*  67 */     this.textureView = device.createTextureView(this.texture);
/*     */     
/*  69 */     device.createCommandEncoder().clearColorTexture(this.texture, -1);
/*  70 */     this.ubo = new MappableRingBuffer(() -> "Lightmap UBO", 130, LIGHTMAP_UBO_SIZE);
/*     */   }
/*     */   
/*     */   public GpuTextureView getTextureView() {
/*  74 */     return this.textureView;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  79 */     this.texture.close();
/*  80 */     this.textureView.close();
/*  81 */     this.ubo.close();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  85 */     this.blockLightRedFlicker += (this.randomSource.nextFloat() - this.randomSource.nextFloat()) * this.randomSource.nextFloat() * this.randomSource.nextFloat() * 0.1F;
/*  86 */     this.blockLightRedFlicker *= 0.9F;
/*  87 */     this.updateLightTexture = true;
/*     */   }
/*     */   
/*     */   private float calculateDarknessScale(LivingEntity camera, float darknessGamma, float partialTickTime) {
/*  91 */     float darkness = 0.45F * darknessGamma;
/*  92 */     return Math.max(0.0F, Mth.cos(((camera.tickCount - partialTickTime) * 3.1415927F * 0.025F)) * darkness);
/*     */   } public void updateLightTexture(float partialTicks) {
/*     */     Vector3f ambientColor;
/*     */     float nightVision;
/*  96 */     if (!this.updateLightTexture) {
/*     */       return;
/*     */     }
/*  99 */     this.updateLightTexture = false;
/* 100 */     ProfilerFiller profiler = Profiler.get();
/* 101 */     profiler.push("lightTex");
/* 102 */     ClientLevel level = this.minecraft.level;
/* 103 */     if (level == null) {
/*     */       return;
/*     */     }
/* 106 */     Camera camera = this.minecraft.gameRenderer.getMainCamera();
/*     */     
/* 108 */     int skyLightColor = (Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, partialTicks);
/* 109 */     float ambientLight = level.dimensionType().ambientLight();
/*     */ 
/*     */     
/* 112 */     float skyFactor = (Float)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, partialTicks);
/* 113 */     EndFlashState endFlashState = level.endFlashState();
/* 114 */     if (endFlashState != null) {
/* 115 */       ambientColor = new Vector3f(0.99F, 1.12F, 1.0F);
/* 116 */       if (!((Boolean)this.minecraft.options.hideLightningFlash().get())) {
/* 117 */         float intensity = endFlashState.getIntensity(partialTicks);
/* 118 */         if (this.minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
/* 119 */           skyFactor += intensity / 3.0F;
/*     */         } else {
/* 121 */           skyFactor += intensity;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 125 */       ambientColor = new Vector3f(1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 128 */     float darknessEffectScale = ((Double)this.minecraft.options.darknessEffectScale().get()).floatValue();
/* 129 */     float darknessGamma = this.minecraft.player.getEffectBlendFactor(MobEffects.DARKNESS, partialTicks) * darknessEffectScale;
/* 130 */     float darknessScale = calculateDarknessScale((LivingEntity)this.minecraft.player, darknessGamma, partialTicks) * darknessEffectScale;
/*     */ 
/*     */ 
/*     */     
/* 134 */     float waterVision = this.minecraft.player.getWaterVision();
/* 135 */     if (this.minecraft.player.hasEffect(MobEffects.NIGHT_VISION)) {
/* 136 */       nightVision = GameRenderer.getNightVisionScale((LivingEntity)this.minecraft.player, partialTicks);
/* 137 */     } else if (waterVision > 0.0F && this.minecraft.player.hasEffect(MobEffects.CONDUIT_POWER)) {
/* 138 */       nightVision = waterVision;
/*     */     } else {
/* 140 */       nightVision = 0.0F;
/*     */     } 
/*     */ 
/*     */     
/* 144 */     float blockFactor = this.blockLightRedFlicker + 1.5F;
/* 145 */     float brightness = ((Double)this.minecraft.options.gamma().get()).floatValue();
/* 146 */     CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
/*     */     
/* 148 */     GpuBuffer.MappedView view = commandEncoder.mapBuffer(this.ubo.currentBuffer(), false, true); 
/* 149 */     try { Std140Builder.intoBuffer(view.data())
/* 150 */         .putFloat(ambientLight)
/* 151 */         .putFloat(skyFactor)
/* 152 */         .putFloat(blockFactor)
/* 153 */         .putFloat(nightVision)
/* 154 */         .putFloat(darknessScale)
/* 155 */         .putFloat(this.renderer.getDarkenWorldAmount(partialTicks))
/* 156 */         .putFloat(Math.max(0.0F, brightness - darknessGamma))
/* 157 */         .putVec3((Vector3fc)ARGB.vector3fFromRGB24(skyLightColor))
/* 158 */         .putVec3((Vector3fc)ambientColor);
/* 159 */       if (view != null) view.close();  } catch (Throwable throwable) { if (view != null)
/*     */         try { view.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 161 */      RenderPass renderPass = commandEncoder.createRenderPass(() -> "Update light", this.textureView, OptionalInt.empty()); 
/* 162 */     try { renderPass.setPipeline(RenderPipelines.LIGHTMAP);
/* 163 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 164 */       renderPass.setUniform("LightmapInfo", this.ubo.currentBuffer());
/* 165 */       renderPass.draw(0, 3);
/* 166 */       if (renderPass != null) renderPass.close();  } catch (Throwable throwable) { if (renderPass != null)
/* 167 */         try { renderPass.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  this.ubo.rotate();
/* 168 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public static float getBrightness(DimensionType dimensionType, int level) {
/* 172 */     return getBrightness(dimensionType.ambientLight(), level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getBrightness(float ambientLight, int level) {
/* 179 */     float v = level / 15.0F;
/*     */     
/* 181 */     float curvedV = v / (4.0F - 3.0F * v);
/* 182 */     return Mth.lerp(ambientLight, curvedV, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int pack(int block, int sky) {
/* 189 */     return block << 4 | sky << 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int block(int lightCoords) {
/* 196 */     return lightCoords >>> 4 & 0xF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int sky(int lightCoords) {
/* 203 */     return lightCoords >>> 20 & 0xF;
/*     */   }
/*     */   
/*     */   public static int lightCoordsWithEmission(int lightCoords, int emission) {
/* 207 */     if (emission == 0) {
/* 208 */       return lightCoords;
/*     */     }
/* 210 */     int sky = Math.max(sky(lightCoords), emission);
/* 211 */     int block = Math.max(block(lightCoords), emission);
/* 212 */     return pack(block, sky);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/LightTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */