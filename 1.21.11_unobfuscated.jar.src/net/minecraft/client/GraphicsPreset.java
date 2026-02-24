/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.GraphicsWorkarounds;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*     */ import net.minecraft.server.level.ParticleStatus;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum GraphicsPreset
/*     */   implements StringRepresentable {
/*  14 */   FAST("fast", "options.graphics.fast"),
/*  15 */   FANCY("fancy", "options.graphics.fancy"),
/*  16 */   FABULOUS("fabulous", "options.graphics.fabulous"),
/*  17 */   CUSTOM("custom", "options.graphics.custom");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public static final Codec<GraphicsPreset> CODEC = (Codec<GraphicsPreset>)StringRepresentable.fromEnum(GraphicsPreset::values); private final String serializedName;
/*     */   
/*     */   GraphicsPreset(String serializedName, String key) {
/*  26 */     this.serializedName = serializedName;
/*  27 */     this.key = key;
/*     */   }
/*     */   private final String key;
/*     */   
/*     */   public String getSerializedName() {
/*  32 */     return this.serializedName;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  36 */     return this.key;
/*     */   }
/*     */   public void apply(Minecraft minecraft) {
/*     */     int viewDistance;
/*  40 */     OptionsSubScreen screen = (minecraft.screen instanceof OptionsSubScreen) ? (OptionsSubScreen)minecraft.screen : null;
/*  41 */     GpuDevice device = RenderSystem.getDevice();
/*  42 */     switch (ordinal()) {
/*     */       case 0:
/*  44 */         viewDistance = 8;
/*  45 */         set(screen, minecraft.options.biomeBlendRadius(), 1);
/*  46 */         set(screen, minecraft.options.renderDistance(), 8);
/*  47 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.NONE);
/*  48 */         set(screen, minecraft.options.simulationDistance(), 6);
/*  49 */         set(screen, minecraft.options.ambientOcclusion(), false);
/*  50 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FAST);
/*  51 */         set(screen, minecraft.options.particles(), ParticleStatus.DECREASED);
/*  52 */         set(screen, minecraft.options.mipmapLevels(), 2);
/*  53 */         set(screen, minecraft.options.entityShadows(), false);
/*  54 */         set(screen, minecraft.options.entityDistanceScaling(), 0.75D);
/*  55 */         set(screen, minecraft.options.menuBackgroundBlurriness(), 2);
/*  56 */         set(screen, minecraft.options.cloudRange(), 32);
/*  57 */         set(screen, minecraft.options.cutoutLeaves(), false);
/*  58 */         set(screen, minecraft.options.improvedTransparency(), false);
/*  59 */         set(screen, minecraft.options.weatherRadius(), 5);
/*  60 */         set(screen, minecraft.options.maxAnisotropyBit(), 1);
/*  61 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.NONE);
/*     */         break;
/*     */       case 1:
/*  64 */         viewDistance = 16;
/*  65 */         set(screen, minecraft.options.biomeBlendRadius(), 2);
/*  66 */         set(screen, minecraft.options.renderDistance(), 16);
/*  67 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.PLAYER_AFFECTED);
/*  68 */         set(screen, minecraft.options.simulationDistance(), 12);
/*  69 */         set(screen, minecraft.options.ambientOcclusion(), true);
/*  70 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FANCY);
/*  71 */         set(screen, minecraft.options.particles(), ParticleStatus.ALL);
/*  72 */         set(screen, minecraft.options.mipmapLevels(), 4);
/*  73 */         set(screen, minecraft.options.entityShadows(), true);
/*  74 */         set(screen, minecraft.options.entityDistanceScaling(), 1.0D);
/*  75 */         set(screen, minecraft.options.menuBackgroundBlurriness(), 5);
/*  76 */         set(screen, minecraft.options.cloudRange(), 64);
/*  77 */         set(screen, minecraft.options.cutoutLeaves(), true);
/*  78 */         set(screen, minecraft.options.improvedTransparency(), false);
/*  79 */         set(screen, minecraft.options.weatherRadius(), 10);
/*  80 */         set(screen, minecraft.options.maxAnisotropyBit(), 1);
/*  81 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.RGSS);
/*     */         break;
/*     */       case 2:
/*  84 */         viewDistance = 32;
/*  85 */         set(screen, minecraft.options.biomeBlendRadius(), 2);
/*  86 */         set(screen, minecraft.options.renderDistance(), 32);
/*  87 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.PLAYER_AFFECTED);
/*  88 */         set(screen, minecraft.options.simulationDistance(), 12);
/*  89 */         set(screen, minecraft.options.ambientOcclusion(), true);
/*  90 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FANCY);
/*  91 */         set(screen, minecraft.options.particles(), ParticleStatus.ALL);
/*  92 */         set(screen, minecraft.options.mipmapLevels(), 4);
/*  93 */         set(screen, minecraft.options.entityShadows(), true);
/*  94 */         set(screen, minecraft.options.entityDistanceScaling(), 1.25D);
/*  95 */         set(screen, minecraft.options.menuBackgroundBlurriness(), 5);
/*  96 */         set(screen, minecraft.options.cloudRange(), 128);
/*  97 */         set(screen, minecraft.options.cutoutLeaves(), true);
/*  98 */         set(screen, minecraft.options.improvedTransparency(), (Util.getPlatform() != Util.OS.OSX));
/*  99 */         set(screen, minecraft.options.weatherRadius(), 10);
/* 100 */         set(screen, minecraft.options.maxAnisotropyBit(), 2);
/*     */         
/* 102 */         if (GraphicsWorkarounds.get(device).isAmd()) {
/* 103 */           set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.RGSS); break;
/*     */         } 
/* 105 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.ANISOTROPIC);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   <T> void set(OptionsSubScreen screen, OptionInstance<T> option, T value) {
/* 112 */     if (option.get() != value) {
/* 113 */       option.set(value);
/* 114 */       if (screen != null)
/* 115 */         screen.resetOption(option); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/GraphicsPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */