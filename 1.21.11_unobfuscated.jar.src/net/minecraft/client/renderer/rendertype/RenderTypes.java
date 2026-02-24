/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
/*     */ import net.minecraft.client.renderer.entity.ItemRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
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
/*     */ public class RenderTypes
/*     */ {
/*     */   static final BiFunction<Identifier, Boolean, RenderType> OUTLINE;
/*     */   
/*     */   static {
/*  33 */     OUTLINE = Util.memoize((texture, cullState) -> RenderType.create("outline", RenderSetup.builder(cullState ? RenderPipelines.OUTLINE_CULL : RenderPipelines.OUTLINE_NO_CULL).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.OUTLINE_TARGET).setOutline(RenderSetup.OutlineProperty.IS_OUTLINE).createRenderSetup()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Supplier<GpuSampler> MOVING_BLOCK_SAMPLER = () -> RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.NEAREST, true);
/*     */ 
/*     */ 
/*     */   
/*  43 */   private static final RenderType SOLID_MOVING_BLOCK = RenderType.create("solid_moving_block", RenderSetup.builder(RenderPipelines.SOLID_BLOCK)
/*  44 */       .useLightmap()
/*  45 */       .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, MOVING_BLOCK_SAMPLER)
/*  46 */       .affectsCrumbling()
/*  47 */       .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
/*  48 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType solidMovingBlock() {
/*  51 */     return SOLID_MOVING_BLOCK;
/*     */   }
/*     */   
/*  54 */   private static final RenderType CUTOUT_MOVING_BLOCK = RenderType.create("cutout_moving_block", RenderSetup.builder(RenderPipelines.CUTOUT_BLOCK)
/*  55 */       .useLightmap()
/*  56 */       .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, MOVING_BLOCK_SAMPLER)
/*  57 */       .affectsCrumbling()
/*  58 */       .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
/*  59 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType cutoutMovingBlock() {
/*  62 */     return CUTOUT_MOVING_BLOCK;
/*     */   }
/*     */   
/*  65 */   private static final RenderType TRANSLUCENT_MOVING_BLOCK = RenderType.create("translucent_moving_block", RenderSetup.builder(RenderPipelines.TRANSLUCENT_MOVING_BLOCK)
/*  66 */       .useLightmap()
/*  67 */       .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, MOVING_BLOCK_SAMPLER)
/*  68 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/*  69 */       .sortOnUpload()
/*  70 */       .bufferSize(786432)
/*  71 */       .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
/*  72 */       .createRenderSetup()); private static final Function<Identifier, RenderType> ARMOR_CUTOUT_NO_CULL; private static final Function<Identifier, RenderType> ARMOR_TRANSLUCENT; private static final Function<Identifier, RenderType> ENTITY_SOLID; private static final Function<Identifier, RenderType> ENTITY_SOLID_Z_OFFSET_FORWARD; private static final Function<Identifier, RenderType> ENTITY_CUTOUT; private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL; private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL_Z_OFFSET; private static final Function<Identifier, RenderType> ITEM_ENTITY_TRANSLUCENT_CULL; private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_TRANSLUCENT; private static final BiFunction<Identifier, Boolean, RenderType> ENTITY_TRANSLUCENT_EMISSIVE; private static final Function<Identifier, RenderType> ENTITY_SMOOTH_CUTOUT; private static final BiFunction<Identifier, Boolean, RenderType> BEACON_BEAM; private static final Function<Identifier, RenderType> ENTITY_DECAL; private static final Function<Identifier, RenderType> ENTITY_NO_OUTLINE; private static final Function<Identifier, RenderType> ENTITY_SHADOW; private static final Function<Identifier, RenderType> DRAGON_EXPLOSION_ALPHA; private static final Function<Identifier, RenderType> EYES;
/*     */   
/*     */   public static RenderType translucentMovingBlock() {
/*  75 */     return TRANSLUCENT_MOVING_BLOCK;
/*     */   }
/*     */   
/*  78 */   static { ARMOR_CUTOUT_NO_CULL = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_CUTOUT_NO_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
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
/*     */           return RenderType.create("armor_cutout_no_cull", state);
/*     */         });
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
/* 108 */     ARMOR_TRANSLUCENT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_TRANSLUCENT).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().sortOnUpload().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("armor_translucent", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     ENTITY_SOLID = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SOLID).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_solid", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     ENTITY_SOLID_Z_OFFSET_FORWARD = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SOLID_Z_OFFSET_FORWARD).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING_FORWARD).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_solid_z_offset_forward", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     ENTITY_CUTOUT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_cutout", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     ENTITY_CUTOUT_NO_CULL = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_NO_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_cutout_no_cull", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     ENTITY_CUTOUT_NO_CULL_Z_OFFSET = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_NO_CULL_Z_OFFSET).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_cutout_no_cull_z_offset", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ITEM_ENTITY_TRANSLUCENT_CULL).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).useLightmap().useOverlay().affectsCrumbling().sortOnUpload().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("item_entity_translucent_cull", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     ENTITY_TRANSLUCENT = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT).withTexture("Sampler0", texture).useLightmap().useOverlay().affectsCrumbling().sortOnUpload().setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_translucent", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     ENTITY_TRANSLUCENT_EMISSIVE = Util.memoize((texture, affectsOutline) -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT_EMISSIVE).withTexture("Sampler0", texture).useOverlay().affectsCrumbling().sortOnUpload().setOutline(affectsOutline ? RenderSetup.OutlineProperty.AFFECTS_OUTLINE : RenderSetup.OutlineProperty.NONE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_translucent_emissive", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     ENTITY_SMOOTH_CUTOUT = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SMOOTH_CUTOUT).withTexture("Sampler0", texture).useLightmap().useOverlay().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_smooth_cutout", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     BEACON_BEAM = Util.memoize((texture, translucent) -> {
/*     */           RenderSetup state = RenderSetup.builder(translucent ? RenderPipelines.BEACON_BEAM_TRANSLUCENT : RenderPipelines.BEACON_BEAM_OPAQUE).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("beacon_beam", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     ENTITY_DECAL = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_DECAL).withTexture("Sampler0", texture).useLightmap().useOverlay().createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_decal", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     ENTITY_NO_OUTLINE = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_NO_OUTLINE).withTexture("Sampler0", texture).useLightmap().useOverlay().sortOnUpload().createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_no_outline", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 332 */     ENTITY_SHADOW = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.ENTITY_SHADOW).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_shadow", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     DRAGON_EXPLOSION_ALPHA = Util.memoize(texture -> {
/*     */           RenderSetup state = RenderSetup.builder(RenderPipelines.DRAGON_EXPLOSION_ALPHA).withTexture("Sampler0", texture).setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return RenderType.create("entity_alpha", state);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     EYES = Util.memoize(texture -> RenderType.create("eyes", RenderSetup.builder(RenderPipelines.EYES).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup())); }
/*     */   public static RenderType armorCutoutNoCull(Identifier texture) { return ARMOR_CUTOUT_NO_CULL.apply(texture); }
/*     */   public static RenderType createArmorDecalCutoutNoCull(Identifier texture) { RenderSetup state = RenderSetup.builder(RenderPipelines.ARMOR_DECAL_CUTOUT_NO_CULL).withTexture("Sampler0", texture).useLightmap().useOverlay().setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING).affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup(); return RenderType.create("armor_decal_cutout_no_cull", state); }
/*     */   public static RenderType armorTranslucent(Identifier texture) { return ARMOR_TRANSLUCENT.apply(texture); }
/*     */   public static RenderType entitySolid(Identifier texture) { return ENTITY_SOLID.apply(texture); }
/*     */   public static RenderType entitySolidZOffsetForward(Identifier texture) { return ENTITY_SOLID_Z_OFFSET_FORWARD.apply(texture); }
/*     */   public static RenderType entityCutout(Identifier texture) { return ENTITY_CUTOUT.apply(texture); }
/*     */   public static RenderType entityCutoutNoCull(Identifier texture, boolean affectsOutline) { return ENTITY_CUTOUT_NO_CULL.apply(texture, affectsOutline); }
/* 368 */   public static RenderType entityCutoutNoCull(Identifier texture) { return entityCutoutNoCull(texture, true); } public static RenderType entityCutoutNoCullZOffset(Identifier texture, boolean affectsOutline) { return ENTITY_CUTOUT_NO_CULL_Z_OFFSET.apply(texture, affectsOutline); } public static RenderType entityCutoutNoCullZOffset(Identifier texture) { return entityCutoutNoCullZOffset(texture, true); } public static RenderType eyes(Identifier texture) { return EYES.apply(texture); }
/*     */   public static RenderType itemEntityTranslucentCull(Identifier texture) { return ITEM_ENTITY_TRANSLUCENT_CULL.apply(texture); }
/*     */   public static RenderType entityTranslucent(Identifier texture, boolean affectsOutline) { return ENTITY_TRANSLUCENT.apply(texture, affectsOutline); }
/*     */   public static RenderType entityTranslucent(Identifier texture) { return entityTranslucent(texture, true); }
/* 372 */   public static RenderType entityTranslucentEmissive(Identifier texture, boolean affectsOutline) { return ENTITY_TRANSLUCENT_EMISSIVE.apply(texture, affectsOutline); } public static RenderType entityTranslucentEmissive(Identifier texture) { return entityTranslucentEmissive(texture, true); } public static RenderType entitySmoothCutout(Identifier texture) { return ENTITY_SMOOTH_CUTOUT.apply(texture); } public static RenderType beaconBeam(Identifier texture, boolean translucent) { return BEACON_BEAM.apply(texture, translucent); } public static RenderType entityDecal(Identifier texture) { return ENTITY_DECAL.apply(texture); } public static RenderType entityNoOutline(Identifier texture) { return ENTITY_NO_OUTLINE.apply(texture); } public static RenderType entityShadow(Identifier texture) { return ENTITY_SHADOW.apply(texture); } public static RenderType dragonExplosionAlpha(Identifier texture) { return DRAGON_EXPLOSION_ALPHA.apply(texture); } public static RenderType breezeEyes(Identifier texture) { return ENTITY_TRANSLUCENT_EMISSIVE.apply(texture, false); }
/*     */ 
/*     */   
/*     */   public static RenderType breezeWind(Identifier texture, float uOffset, float vOffset) {
/* 376 */     return RenderType.create("breeze_wind", RenderSetup.builder(RenderPipelines.BREEZE_WIND)
/* 377 */         .withTexture("Sampler0", texture)
/* 378 */         .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
/* 379 */         .useLightmap()
/* 380 */         .sortOnUpload()
/* 381 */         .createRenderSetup());
/*     */   }
/*     */ 
/*     */   
/*     */   public static RenderType energySwirl(Identifier texture, float uOffset, float vOffset) {
/* 386 */     return RenderType.create("energy_swirl", RenderSetup.builder(RenderPipelines.ENERGY_SWIRL)
/* 387 */         .withTexture("Sampler0", texture)
/* 388 */         .setTextureTransform(new TextureTransform.OffsetTextureTransform(uOffset, vOffset))
/* 389 */         .useLightmap()
/* 390 */         .useOverlay()
/* 391 */         .sortOnUpload()
/* 392 */         .createRenderSetup());
/*     */   }
/*     */   
/* 395 */   private static final RenderType LEASH = RenderType.create("leash", RenderSetup.builder(RenderPipelines.LEASH)
/* 396 */       .useLightmap()
/* 397 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType leash() {
/* 400 */     return LEASH;
/*     */   }
/*     */   
/* 403 */   private static final RenderType WATER_MASK = RenderType.create("water_mask", RenderSetup.builder(RenderPipelines.WATER_MASK).createRenderSetup());
/*     */   
/*     */   public static RenderType waterMask() {
/* 406 */     return WATER_MASK;
/*     */   }
/*     */   
/*     */   public static RenderType outline(Identifier texture) {
/* 410 */     return OUTLINE.apply(texture, false);
/*     */   }
/*     */   
/* 413 */   private static final RenderType ARMOR_ENTITY_GLINT = RenderType.create("armor_entity_glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 414 */       .withTexture("Sampler0", ItemRenderer.ENCHANTED_GLINT_ARMOR)
/* 415 */       .setTextureTransform(TextureTransform.ARMOR_ENTITY_GLINT_TEXTURING)
/* 416 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 417 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType armorEntityGlint() {
/* 420 */     return ARMOR_ENTITY_GLINT;
/*     */   }
/*     */   
/* 423 */   private static final RenderType GLINT_TRANSLUCENT = RenderType.create("glint_translucent", RenderSetup.builder(RenderPipelines.GLINT)
/* 424 */       .withTexture("Sampler0", ItemRenderer.ENCHANTED_GLINT_ITEM)
/* 425 */       .setTextureTransform(TextureTransform.GLINT_TEXTURING)
/* 426 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 427 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType glintTranslucent() {
/* 430 */     return GLINT_TRANSLUCENT;
/*     */   }
/*     */   
/* 433 */   private static final RenderType GLINT = RenderType.create("glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 434 */       .withTexture("Sampler0", ItemRenderer.ENCHANTED_GLINT_ITEM)
/* 435 */       .setTextureTransform(TextureTransform.GLINT_TEXTURING)
/* 436 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType glint() {
/* 439 */     return GLINT;
/*     */   }
/*     */   
/* 442 */   private static final RenderType ENTITY_GLINT = RenderType.create("entity_glint", RenderSetup.builder(RenderPipelines.GLINT)
/* 443 */       .withTexture("Sampler0", ItemRenderer.ENCHANTED_GLINT_ITEM)
/* 444 */       .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
/* 445 */       .createRenderSetup()); private static final Function<Identifier, RenderType> CRUMBLING; private static final Function<Identifier, RenderType> TEXT;
/*     */   
/*     */   public static RenderType entityGlint() {
/* 448 */     return ENTITY_GLINT;
/*     */   }
/*     */   static {
/* 451 */     CRUMBLING = Util.memoize(texture -> RenderType.create("crumbling", RenderSetup.builder(RenderPipelines.CRUMBLING).withTexture("Sampler0", texture).sortOnUpload().createRenderSetup()));
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
/* 462 */     TEXT = Util.memoize(texture -> RenderType.create("text", RenderSetup.builder(RenderPipelines.TEXT).withTexture("Sampler0", texture).useLightmap().bufferSize(786432).createRenderSetup()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static RenderType crumbling(Identifier texture) {
/*     */     return CRUMBLING.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType text(Identifier texture) {
/* 471 */     return TEXT.apply(texture);
/*     */   }
/*     */ 
/*     */   
/* 475 */   private static final RenderType TEXT_BACKGROUND = RenderType.create("text_background", RenderSetup.builder(RenderPipelines.TEXT_BACKGROUND)
/* 476 */       .useLightmap()
/* 477 */       .sortOnUpload()
/* 478 */       .createRenderSetup()); private static final Function<Identifier, RenderType> TEXT_INTENSITY; private static final Function<Identifier, RenderType> TEXT_POLYGON_OFFSET; private static final Function<Identifier, RenderType> TEXT_INTENSITY_POLYGON_OFFSET; private static final Function<Identifier, RenderType> TEXT_SEE_THROUGH;
/*     */   
/*     */   public static RenderType textBackground() {
/* 481 */     return TEXT_BACKGROUND;
/*     */   }
/*     */   static {
/* 484 */     TEXT_INTENSITY = Util.memoize(texture -> RenderType.create("text_intensity", RenderSetup.builder(RenderPipelines.TEXT_INTENSITY).withTexture("Sampler0", texture).useLightmap().bufferSize(786432).createRenderSetup()));
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
/* 496 */     TEXT_POLYGON_OFFSET = Util.memoize(texture -> RenderType.create("text_polygon_offset", RenderSetup.builder(RenderPipelines.TEXT_POLYGON_OFFSET).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
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
/* 508 */     TEXT_INTENSITY_POLYGON_OFFSET = Util.memoize(texture -> RenderType.create("text_intensity_polygon_offset", RenderSetup.builder(RenderPipelines.TEXT_INTENSITY).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
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
/* 520 */     TEXT_SEE_THROUGH = Util.memoize(texture -> RenderType.create("text_see_through", RenderSetup.builder(RenderPipelines.TEXT_SEE_THROUGH).withTexture("Sampler0", texture).useLightmap().createRenderSetup()));
/*     */   } public static RenderType textIntensity(Identifier texture) {
/*     */     return TEXT_INTENSITY.apply(texture);
/*     */   } public static RenderType textPolygonOffset(Identifier texture) {
/*     */     return TEXT_POLYGON_OFFSET.apply(texture);
/*     */   } public static RenderType textIntensityPolygonOffset(Identifier texture) {
/*     */     return TEXT_INTENSITY_POLYGON_OFFSET.apply(texture);
/*     */   } public static RenderType textSeeThrough(Identifier texture) {
/* 528 */     return TEXT_SEE_THROUGH.apply(texture);
/*     */   }
/*     */ 
/*     */   
/* 532 */   private static final RenderType TEXT_BACKGROUND_SEE_THROUGH = RenderType.create("text_background_see_through", RenderSetup.builder(RenderPipelines.TEXT_BACKGROUND_SEE_THROUGH)
/* 533 */       .useLightmap()
/* 534 */       .sortOnUpload()
/* 535 */       .createRenderSetup()); private static final Function<Identifier, RenderType> TEXT_INTENSITY_SEE_THROUGH;
/*     */   
/*     */   public static RenderType textBackgroundSeeThrough() {
/* 538 */     return TEXT_BACKGROUND_SEE_THROUGH;
/*     */   }
/*     */   static {
/* 541 */     TEXT_INTENSITY_SEE_THROUGH = Util.memoize(texture -> RenderType.create("text_intensity_see_through", RenderSetup.builder(RenderPipelines.TEXT_INTENSITY_SEE_THROUGH).withTexture("Sampler0", texture).useLightmap().sortOnUpload().createRenderSetup()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RenderType textIntensitySeeThrough(Identifier texture) {
/* 550 */     return TEXT_INTENSITY_SEE_THROUGH.apply(texture);
/*     */   }
/*     */   
/* 553 */   private static final RenderType LIGHTNING = RenderType.create("lightning", RenderSetup.builder(RenderPipelines.LIGHTNING)
/* 554 */       .setOutputTarget(OutputTarget.WEATHER_TARGET)
/* 555 */       .sortOnUpload()
/* 556 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType lightning() {
/* 559 */     return LIGHTNING;
/*     */   }
/*     */   
/* 562 */   private static final RenderType DRAGON_RAYS = RenderType.create("dragon_rays", RenderSetup.builder(RenderPipelines.DRAGON_RAYS)
/* 563 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType dragonRays() {
/* 566 */     return DRAGON_RAYS;
/*     */   }
/*     */   
/* 569 */   private static final RenderType DRAGON_RAYS_DEPTH = RenderType.create("dragon_rays_depth", RenderSetup.builder(RenderPipelines.DRAGON_RAYS_DEPTH)
/* 570 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType dragonRaysDepth() {
/* 573 */     return DRAGON_RAYS_DEPTH;
/*     */   }
/*     */   
/* 576 */   private static final RenderType TRIPWIRE_MOVING_BLOCk = RenderType.create("tripwire_moving_block", RenderSetup.builder(RenderPipelines.TRIPWIRE_BLOCK)
/* 577 */       .useLightmap()
/* 578 */       .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, MOVING_BLOCK_SAMPLER)
/* 579 */       .setOutputTarget(OutputTarget.WEATHER_TARGET)
/* 580 */       .affectsCrumbling()
/* 581 */       .sortOnUpload()
/* 582 */       .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
/* 583 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType tripwireMovingBlock() {
/* 586 */     return TRIPWIRE_MOVING_BLOCk;
/*     */   }
/*     */   
/* 589 */   private static final RenderType END_PORTAL = RenderType.create("end_portal", RenderSetup.builder(RenderPipelines.END_PORTAL)
/* 590 */       .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
/* 591 */       .withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
/* 592 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType endPortal() {
/* 595 */     return END_PORTAL;
/*     */   }
/*     */   
/* 598 */   private static final RenderType END_GATEWAY = RenderType.create("end_gateway", RenderSetup.builder(RenderPipelines.END_GATEWAY)
/* 599 */       .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
/* 600 */       .withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
/* 601 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType endGateway() {
/* 604 */     return END_GATEWAY;
/*     */   }
/*     */   
/* 607 */   public static final RenderType LINES = RenderType.create("lines", RenderSetup.builder(RenderPipelines.LINES)
/* 608 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 609 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 610 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType lines() {
/* 613 */     return LINES;
/*     */   }
/*     */   
/* 616 */   public static final RenderType LINES_TRANSLUCENT = RenderType.create("lines_translucent", RenderSetup.builder(RenderPipelines.LINES_TRANSLUCENT)
/* 617 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 618 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 619 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType linesTranslucent() {
/* 622 */     return LINES_TRANSLUCENT;
/*     */   }
/*     */   
/* 625 */   public static final RenderType SECONDARY_BLOCK_OUTLINE = RenderType.create("secondary_block_outline", RenderSetup.builder(RenderPipelines.SECONDARY_BLOCK_OUTLINE)
/* 626 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 627 */       .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
/* 628 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType secondaryBlockOutline() {
/* 631 */     return SECONDARY_BLOCK_OUTLINE;
/*     */   }
/*     */   
/* 634 */   private static final RenderType DEBUG_FILLED_BOX = RenderType.create("debug_filled_box", RenderSetup.builder(RenderPipelines.DEBUG_FILLED_BOX)
/* 635 */       .sortOnUpload()
/* 636 */       .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
/* 637 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugFilledBox() {
/* 640 */     return DEBUG_FILLED_BOX;
/*     */   }
/*     */   
/* 643 */   private static final RenderType DEBUG_POINT = RenderType.create("debug_point", RenderSetup.builder(RenderPipelines.DEBUG_POINTS)
/* 644 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugPoint() {
/* 647 */     return DEBUG_POINT;
/*     */   }
/*     */   
/* 650 */   private static final RenderType DEBUG_QUADS = RenderType.create("debug_quads", RenderSetup.builder(RenderPipelines.DEBUG_QUADS)
/* 651 */       .sortOnUpload()
/* 652 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugQuads() {
/* 655 */     return DEBUG_QUADS;
/*     */   }
/*     */   
/* 658 */   private static final RenderType DEBUG_TRIANGLE_FAN = RenderType.create("debug_triangle_fan", RenderSetup.builder(RenderPipelines.DEBUG_TRIANGLE_FAN)
/* 659 */       .sortOnUpload()
/* 660 */       .createRenderSetup());
/*     */   
/*     */   public static RenderType debugTriangleFan() {
/* 663 */     return DEBUG_TRIANGLE_FAN;
/*     */   }
/*     */   
/* 666 */   private static final Function<Identifier, RenderType> WEATHER_DEPTH_WRITE = createWeather(RenderPipelines.WEATHER_DEPTH_WRITE);
/*     */   
/* 668 */   private static final Function<Identifier, RenderType> WEATHER_NO_DEPTH_WRITE = createWeather(RenderPipelines.WEATHER_NO_DEPTH_WRITE); private static final Function<Identifier, RenderType> BLOCK_SCREEN_EFFECT; private static final Function<Identifier, RenderType> FIRE_SCREEN_EFFECT;
/*     */   
/*     */   private static Function<Identifier, RenderType> createWeather(RenderPipeline renderPipeline) {
/* 671 */     return Util.memoize(texture -> RenderType.create("weather", RenderSetup.builder(renderPipeline).withTexture("Sampler0", texture).setOutputTarget(OutputTarget.WEATHER_TARGET).useLightmap().createRenderSetup()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RenderType weather(Identifier texture, boolean depthWrite) {
/* 681 */     return (depthWrite ? WEATHER_DEPTH_WRITE : WEATHER_NO_DEPTH_WRITE).apply(texture);
/*     */   }
/*     */   static {
/* 684 */     BLOCK_SCREEN_EFFECT = Util.memoize(texture -> RenderType.create("block_screen_effect", RenderSetup.builder(RenderPipelines.BLOCK_SCREEN_EFFECT).withTexture("Sampler0", texture).createRenderSetup()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 694 */     FIRE_SCREEN_EFFECT = Util.memoize(texture -> RenderType.create("fire_screen_effect", RenderSetup.builder(RenderPipelines.FIRE_SCREEN_EFFECT).withTexture("Sampler0", texture).createRenderSetup()));
/*     */   }
/*     */   public static RenderType blockScreenEffect(Identifier texture) {
/*     */     return BLOCK_SCREEN_EFFECT.apply(texture);
/*     */   }
/*     */   
/*     */   public static RenderType fireScreenEffect(Identifier texture) {
/* 701 */     return FIRE_SCREEN_EFFECT.apply(texture);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/rendertype/RenderTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */