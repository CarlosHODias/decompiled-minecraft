/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public class DripParticle extends SingleQuadParticle {
/*     */   private final Fluid type;
/*     */   protected boolean isGlowing;
/*     */   
/*     */   private static class DripHangParticle extends DripParticle {
/*     */     private DripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/*  25 */       super(level, x, y, z, type, sprite);
/*  26 */       this.fallingParticle = fallingParticle;
/*  27 */       this.gravity *= 0.02F;
/*  28 */       this.lifetime = 40;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void preMoveUpdate() {
/*  33 */       if (this.lifetime-- <= 0) {
/*  34 */         remove();
/*  35 */         this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
/*     */       } 
/*     */     }
/*     */     private final ParticleOptions fallingParticle;
/*     */     
/*     */     protected void postMoveUpdate() {
/*  41 */       this.xd *= 0.02D;
/*  42 */       this.yd *= 0.02D;
/*  43 */       this.zd *= 0.02D;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CoolingDripHangParticle extends DripHangParticle {
/*     */     private CoolingDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/*  49 */       super(level, x, y, z, type, fallingParticle, sprite);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void preMoveUpdate() {
/*  54 */       this.rCol = 1.0F;
/*  55 */       this.gCol = 16.0F / (40 - this.lifetime + 16);
/*  56 */       this.bCol = 4.0F / (40 - this.lifetime + 8);
/*  57 */       super.preMoveUpdate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FallAndLandParticle extends FallingParticle {
/*     */     protected final ParticleOptions landParticle;
/*     */     
/*     */     private FallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  65 */       super(level, x, y, z, type, sprite);
/*  66 */       this.lifetime = (int)(64.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  67 */       this.landParticle = landParticle;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void postMoveUpdate() {
/*  72 */       if (this.onGround) {
/*  73 */         remove();
/*  74 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class HoneyFallAndLandParticle extends FallAndLandParticle {
/*     */     private HoneyFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  81 */       super(level, x, y, z, type, landParticle, sprite);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void postMoveUpdate() {
/*  86 */       if (this.onGround) {
/*  87 */         remove();
/*  88 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*  89 */         float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/*  90 */         this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, volume, 1.0F, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DripstoneFallAndLandParticle extends FallAndLandParticle {
/*     */     private DripstoneFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  97 */       super(level, x, y, z, type, landParticle, sprite);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void postMoveUpdate() {
/* 102 */       if (this.onGround) {
/* 103 */         remove();
/* 104 */         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/* 105 */         SoundEvent sound = (getType() == Fluids.LAVA) ? SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA : SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
/* 106 */         float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/* 107 */         this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.BLOCKS, volume, 1.0F, false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FallingParticle extends DripParticle {
/*     */     private FallingParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 114 */       super(level, x, y, z, type, sprite);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void postMoveUpdate() {
/* 119 */       if (this.onGround)
/* 120 */         remove(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DripLandParticle
/*     */     extends DripParticle {
/*     */     private DripLandParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 127 */       super(level, x, y, z, type, sprite);
/* 128 */       this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DripParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 136 */     super(level, x, y, z, sprite);
/* 137 */     setSize(0.01F, 0.01F);
/* 138 */     this.gravity = 0.06F;
/* 139 */     this.type = type;
/*     */   }
/*     */   
/*     */   protected Fluid getType() {
/* 143 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/* 148 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/* 153 */     if (this.isGlowing) {
/* 154 */       return 240;
/*     */     }
/*     */     
/* 157 */     return super.getLightColor(a);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 162 */     this.xo = this.x;
/* 163 */     this.yo = this.y;
/* 164 */     this.zo = this.z;
/*     */     
/* 166 */     preMoveUpdate();
/* 167 */     if (this.removed) {
/*     */       return;
/*     */     }
/*     */     
/* 171 */     this.yd -= this.gravity;
/* 172 */     move(this.xd, this.yd, this.zd);
/* 173 */     postMoveUpdate();
/* 174 */     if (this.removed) {
/*     */       return;
/*     */     }
/*     */     
/* 178 */     this.xd *= 0.9800000190734863D;
/* 179 */     this.yd *= 0.9800000190734863D;
/* 180 */     this.zd *= 0.9800000190734863D;
/*     */     
/* 182 */     if (this.type == Fluids.EMPTY) {
/*     */       return;
/*     */     }
/*     */     
/* 186 */     BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
/* 187 */     FluidState fluidState = this.level.getFluidState(pos);
/* 188 */     if (fluidState.getType() == this.type && 
/* 189 */       this.y < (pos.getY() + fluidState.getHeight((BlockGetter)this.level, pos))) {
/* 190 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void preMoveUpdate() {
/* 196 */     if (this.lifetime-- <= 0)
/* 197 */       remove(); 
/*     */   }
/*     */   
/*     */   protected void postMoveUpdate() {}
/*     */   
/*     */   public static class WaterHangProvider
/*     */     implements ParticleProvider<SimpleParticleType>
/*     */   {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaterHangProvider(SpriteSet sprite) {
/* 208 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 213 */       DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_WATER, this.sprite.get(random));
/* 214 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 215 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaterFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaterFallProvider(SpriteSet sprite) {
/* 223 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 228 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.SPLASH, this.sprite.get(random));
/* 229 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 230 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaHangProvider(SpriteSet sprite) {
/* 238 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 243 */       DripParticle.CoolingDripHangParticle particle = new DripParticle.CoolingDripHangParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.FALLING_LAVA, this.sprite.get(random));
/* 244 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaFallProvider(SpriteSet sprite) {
/* 252 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 257 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA, this.sprite.get(random));
/* 258 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 259 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LavaLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public LavaLandProvider(SpriteSet sprite) {
/* 267 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 272 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, this.sprite.get(random));
/* 273 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 274 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyHangProvider(SpriteSet sprite) {
/* 282 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 287 */       DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_HONEY, this.sprite.get(random));
/* 288 */       particle.gravity *= 0.01F;
/* 289 */       particle.lifetime = 100;
/* 290 */       particle.setColor(0.622F, 0.508F, 0.082F);
/* 291 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyFallProvider(SpriteSet sprite) {
/* 299 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 304 */       DripParticle particle = new DripParticle.HoneyFallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_HONEY, this.sprite.get(random));
/* 305 */       particle.gravity = 0.01F;
/* 306 */       particle.setColor(0.582F, 0.448F, 0.082F);
/* 307 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HoneyLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HoneyLandProvider(SpriteSet sprite) {
/* 315 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 320 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 321 */       particle.lifetime = (int)(128.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 322 */       particle.setColor(0.522F, 0.408F, 0.082F);
/* 323 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneWaterHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneWaterHangProvider(SpriteSet sprite) {
/* 331 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 336 */       DripParticle particle = new DripParticle.DripHangParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_DRIPSTONE_WATER, this.sprite.get(random));
/* 337 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 338 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneWaterFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneWaterFallProvider(SpriteSet sprite) {
/* 346 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 351 */       DripParticle particle = new DripParticle.DripstoneFallAndLandParticle(level, x, y, z, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.SPLASH, this.sprite.get(random));
/* 352 */       particle.setColor(0.2F, 0.3F, 1.0F);
/* 353 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneLavaHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneLavaHangProvider(SpriteSet sprite) {
/* 361 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 366 */       DripParticle.CoolingDripHangParticle particle = new DripParticle.CoolingDripHangParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.FALLING_DRIPSTONE_LAVA, this.sprite.get(random));
/* 367 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DripstoneLavaFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DripstoneLavaFallProvider(SpriteSet sprite) {
/* 375 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 380 */       DripParticle particle = new DripParticle.DripstoneFallAndLandParticle(level, x, y, z, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA, this.sprite.get(random));
/* 381 */       particle.setColor(1.0F, 0.2857143F, 0.083333336F);
/* 382 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NectarFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public NectarFallProvider(SpriteSet sprite) {
/* 390 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 395 */       DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 396 */       particle.lifetime = (int)(16.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 397 */       particle.gravity = 0.007F;
/* 398 */       particle.setColor(0.92F, 0.782F, 0.72F);
/* 399 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SporeBlossomFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public SporeBlossomFallProvider(SpriteSet sprite) {
/* 407 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 412 */       DripParticle particle = new DripParticle.FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 413 */       particle.lifetime = (int)(64.0F / Mth.randomBetween(particle.random, 0.1F, 0.9F));
/* 414 */       particle.gravity = 0.005F;
/* 415 */       particle.setColor(0.32F, 0.5F, 0.22F);
/* 416 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearHangProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearHangProvider(SpriteSet sprite) {
/* 424 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 429 */       DripParticle.DripHangParticle particle = new DripParticle.DripHangParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 430 */       particle.isGlowing = true;
/* 431 */       particle.gravity *= 0.01F;
/* 432 */       particle.lifetime = 100;
/* 433 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 434 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearFallProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearFallProvider(SpriteSet sprite) {
/* 442 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 447 */       DripParticle particle = new DripParticle.FallAndLandParticle(level, x, y, z, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_OBSIDIAN_TEAR, this.sprite.get(random));
/* 448 */       particle.isGlowing = true;
/* 449 */       particle.gravity = 0.01F;
/* 450 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 451 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObsidianTearLandProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ObsidianTearLandProvider(SpriteSet sprite) {
/* 459 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 464 */       DripParticle particle = new DripParticle.DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
/* 465 */       particle.isGlowing = true;
/* 466 */       particle.lifetime = (int)(28.0D / (random.nextFloat() * 0.8D + 0.2D));
/* 467 */       particle.setColor(0.51171875F, 0.03125F, 0.890625F);
/* 468 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DripParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */