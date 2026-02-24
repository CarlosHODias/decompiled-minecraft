/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ 
/*     */ public class FireworkParticles {
/*     */   public static class Starter extends NoRenderParticle {
/*  25 */     private static final double[][] CREEPER_PARTICLE_COORDS = new double[][] { { 0.0D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.6D }, { 0.6D, 0.6D }, { 0.6D, 0.2D }, { 0.2D, 0.2D }, { 0.2D, 0.0D }, { 0.4D, 0.0D }, { 0.4D, -0.6D }, { 0.2D, -0.6D }, { 0.2D, -0.4D }, { 0.0D, -0.4D } };
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
/*  39 */     private static final double[][] STAR_PARTICLE_COORDS = new double[][] { { 0.0D, 1.0D }, { 0.3455D, 0.309D }, { 0.9511D, 0.309D }, { 0.3795918367346939D, -0.12653061224489795D }, { 0.6122448979591837D, -0.8040816326530612D }, { 0.0D, -0.35918367346938773D } };
/*     */ 
/*     */     
/*     */     private int life;
/*     */ 
/*     */     
/*     */     private final ParticleEngine engine;
/*     */ 
/*     */     
/*     */     private final List<FireworkExplosion> explosions;
/*     */     
/*     */     private boolean twinkleDelay;
/*     */ 
/*     */     
/*     */     public Starter(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, ParticleEngine engine, List<FireworkExplosion> explosions) {
/*  54 */       super(level, x, y, z);
/*  55 */       this.xd = xd;
/*  56 */       this.yd = yd;
/*  57 */       this.zd = zd;
/*  58 */       this.engine = engine;
/*     */       
/*  60 */       if (explosions.isEmpty()) {
/*  61 */         throw new IllegalArgumentException("Cannot create firework starter with no explosions");
/*     */       }
/*     */       
/*  64 */       this.explosions = explosions;
/*  65 */       this.lifetime = explosions.size() * 2 - 1;
/*     */       
/*  67 */       for (FireworkExplosion explosion : explosions) {
/*  68 */         if (explosion.hasTwinkle()) {
/*  69 */           this.twinkleDelay = true;
/*  70 */           this.lifetime += 15;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/*  78 */       if (this.life == 0) {
/*  79 */         SoundEvent sound; boolean farEffect = isFarAwayFromCamera();
/*     */         
/*     */         boolean largeExplosion = false;
/*  82 */         if (this.explosions.size() >= 3) {
/*  83 */           largeExplosion = true;
/*     */         } else {
/*  85 */           for (FireworkExplosion explosion : this.explosions) {
/*  86 */             if (explosion.shape() == FireworkExplosion.Shape.LARGE_BALL) {
/*  87 */               largeExplosion = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  94 */         if (largeExplosion) {
/*  95 */           sound = farEffect ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST;
/*     */         } else {
/*  97 */           sound = farEffect ? SoundEvents.FIREWORK_ROCKET_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_BLAST;
/*     */         } 
/*  99 */         this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.AMBIENT, 20.0F, 0.95F + this.random.nextFloat() * 0.1F, true);
/*     */       } 
/*     */       
/* 102 */       if (this.life % 2 == 0 && this.life / 2 < this.explosions.size()) {
/* 103 */         int eIndex = this.life / 2;
/* 104 */         FireworkExplosion explosion = this.explosions.get(eIndex);
/*     */         
/* 106 */         boolean trail = explosion.hasTrail();
/* 107 */         boolean twinkle = explosion.hasTwinkle();
/* 108 */         IntList colors = explosion.colors();
/* 109 */         IntList fadeColors = explosion.fadeColors();
/*     */         
/* 111 */         if (colors.isEmpty()) {
/* 112 */           colors = IntList.of(DyeColor.BLACK.getFireworkColor());
/*     */         }
/*     */         
/* 115 */         switch (explosion.shape()) { case SMALL_BALL:
/* 116 */             createParticleBall(0.25D, 2, colors, fadeColors, trail, twinkle); break;
/* 117 */           case LARGE_BALL: createParticleBall(0.5D, 4, colors, fadeColors, trail, twinkle); break;
/* 118 */           case STAR: createParticleShape(0.5D, STAR_PARTICLE_COORDS, colors, fadeColors, trail, twinkle, false);
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case CREEPER:
/* 127 */             createParticleShape(0.5D, CREEPER_PARTICLE_COORDS, colors, fadeColors, trail, twinkle, true);
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case BURST:
/* 136 */             createParticleBurst(colors, fadeColors, trail, twinkle);
/*     */             break; }
/*     */         
/* 139 */         int color = colors.getInt(0);
/* 140 */         this.engine.createParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.FLASH, color), this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*     */       } 
/* 142 */       this.life++;
/* 143 */       if (this.life > this.lifetime) {
/* 144 */         if (this.twinkleDelay) {
/* 145 */           boolean farEffect = isFarAwayFromCamera();
/* 146 */           SoundEvent sound = farEffect ? SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.FIREWORK_ROCKET_TWINKLE;
/* 147 */           this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.AMBIENT, 20.0F, 0.9F + this.random.nextFloat() * 0.15F, true);
/*     */         } 
/* 149 */         remove();
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean isFarAwayFromCamera() {
/* 154 */       Minecraft instance = Minecraft.getInstance();
/* 155 */       return (instance.gameRenderer.getMainCamera().position().distanceToSqr(this.x, this.y, this.z) >= 256.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     private void createParticle(double x, double y, double z, double xa, double ya, double za, IntList rgbColors, IntList fadeColors, boolean trail, boolean twinkle) {
/* 160 */       FireworkParticles.SparkParticle sparkParticle = (FireworkParticles.SparkParticle)this.engine.createParticle((ParticleOptions)ParticleTypes.FIREWORK, x, y, z, xa, ya, za);
/* 161 */       sparkParticle.setTrail(trail);
/* 162 */       sparkParticle.setTwinkle(twinkle);
/* 163 */       sparkParticle.setAlpha(0.99F);
/*     */       
/* 165 */       sparkParticle.setColor((Integer)Util.getRandom((List)rgbColors, this.random));
/* 166 */       if (!fadeColors.isEmpty()) {
/* 167 */         sparkParticle.setFadeColor((Integer)Util.getRandom((List)fadeColors, this.random));
/*     */       }
/*     */     }
/*     */     
/*     */     private void createParticleBall(double baseSpeed, int steps, IntList rgbColors, IntList fadeColors, boolean trail, boolean twinkle) {
/* 172 */       double xx = this.x;
/* 173 */       double yy = this.y;
/* 174 */       double zz = this.z;
/*     */       
/* 176 */       for (int yStep = -steps; yStep <= steps; yStep++) {
/* 177 */         for (int xStep = -steps; xStep <= steps; xStep++) {
/* 178 */           for (int zStep = -steps; zStep <= steps; zStep++) {
/* 179 */             double xa = xStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
/* 180 */             double ya = yStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
/* 181 */             double za = zStep + (this.random.nextDouble() - this.random.nextDouble()) * 0.5D;
/* 182 */             double len = Math.sqrt(xa * xa + ya * ya + za * za) / baseSpeed + this.random.nextGaussian() * 0.05D;
/*     */             
/* 184 */             createParticle(xx, yy, zz, xa / len, ya / len, za / len, rgbColors, fadeColors, trail, twinkle);
/*     */             
/* 186 */             if (yStep != -steps && yStep != steps && xStep != -steps && xStep != steps) {
/* 187 */               zStep += steps * 2 - 1;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void createParticleShape(double baseSpeed, double[][] coords, IntList rgbColors, IntList fadeColors, boolean trail, boolean twinkle, boolean flat) {
/* 195 */       double sx = coords[0][0];
/* 196 */       double sy = coords[0][1];
/*     */       
/* 198 */       createParticle(this.x, this.y, this.z, sx * baseSpeed, sy * baseSpeed, 0.0D, rgbColors, fadeColors, trail, twinkle);
/*     */       
/* 200 */       float baseAngle = this.random.nextFloat() * 3.1415927F;
/* 201 */       double angleMod = flat ? 0.034D : 0.34D;
/* 202 */       for (int angleStep = 0; angleStep < 3; angleStep++) {
/* 203 */         double angle = baseAngle + (angleStep * 3.1415927F) * angleMod;
/*     */         
/* 205 */         double ox = sx;
/* 206 */         double oy = sy;
/*     */         
/* 208 */         for (int c = 1; c < coords.length; c++) {
/* 209 */           double tx = coords[c][0];
/* 210 */           double ty = coords[c][1];
/*     */           
/* 212 */           for (double subStep = 0.25D; subStep <= 1.0D; subStep += 0.25D) {
/* 213 */             double xa = Mth.lerp(subStep, ox, tx) * baseSpeed;
/* 214 */             double ya = Mth.lerp(subStep, oy, ty) * baseSpeed;
/*     */             
/* 216 */             double za = xa * Math.sin(angle);
/* 217 */             xa *= Math.cos(angle);
/*     */             
/* 219 */             for (double flip = -1.0D; flip <= 1.0D; flip += 2.0D) {
/* 220 */               createParticle(this.x, this.y, this.z, xa * flip, ya, za * flip, rgbColors, fadeColors, trail, twinkle);
/*     */             }
/*     */           } 
/* 223 */           ox = tx;
/* 224 */           oy = ty;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void createParticleBurst(IntList rgbColors, IntList fadeColors, boolean trail, boolean twinkle) {
/* 230 */       double baseOffX = this.random.nextGaussian() * 0.05D;
/* 231 */       double baseOffZ = this.random.nextGaussian() * 0.05D;
/*     */       
/* 233 */       for (int i = 0; i < 70; i++) {
/* 234 */         double xa = this.xd * 0.5D + this.random.nextGaussian() * 0.15D + baseOffX;
/* 235 */         double za = this.zd * 0.5D + this.random.nextGaussian() * 0.15D + baseOffZ;
/* 236 */         double ya = this.yd * 0.5D + this.random.nextDouble() * 0.5D;
/*     */         
/* 238 */         createParticle(this.x, this.y, this.z, xa, ya, za, rgbColors, fadeColors, trail, twinkle);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SparkParticle
/*     */     extends SimpleAnimatedParticle {
/*     */     private boolean trail;
/*     */     private boolean twinkle;
/*     */     private final ParticleEngine engine;
/*     */     private float fadeR;
/*     */     private float fadeG;
/*     */     private float fadeB;
/*     */     private boolean hasFade;
/*     */     
/*     */     private SparkParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, ParticleEngine engine, SpriteSet sprites) {
/* 254 */       super(level, x, y, z, sprites, 0.1F);
/* 255 */       this.xd = xa;
/* 256 */       this.yd = ya;
/* 257 */       this.zd = za;
/* 258 */       this.engine = engine;
/*     */       
/* 260 */       this.quadSize *= 0.75F;
/*     */       
/* 262 */       this.lifetime = 48 + this.random.nextInt(12);
/* 263 */       setSpriteFromAge(sprites);
/*     */     }
/*     */     
/*     */     public void setTrail(boolean trail) {
/* 267 */       this.trail = trail;
/*     */     }
/*     */     
/*     */     public void setTwinkle(boolean twinkle) {
/* 271 */       this.twinkle = twinkle;
/*     */     }
/*     */ 
/*     */     
/*     */     public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 276 */       if (!this.twinkle || this.age < this.lifetime / 3 || (this.age + this.lifetime) / 3 % 2 == 0) {
/* 277 */         super.extract(particleTypeRenderState, camera, partialTickTime);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 283 */       super.tick();
/*     */       
/* 285 */       if (this.trail && this.age < this.lifetime / 2 && (this.age + this.lifetime) % 2 == 0) {
/* 286 */         SparkParticle sparkParticle = new SparkParticle(this.level, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D, this.engine, this.sprites);
/* 287 */         sparkParticle.setAlpha(0.99F);
/* 288 */         sparkParticle.setColor(this.rCol, this.gCol, this.bCol);
/* 289 */         sparkParticle.age = sparkParticle.lifetime / 2;
/* 290 */         if (this.hasFade) {
/* 291 */           sparkParticle.hasFade = true;
/* 292 */           sparkParticle.fadeR = this.fadeR;
/* 293 */           sparkParticle.fadeG = this.fadeG;
/* 294 */           sparkParticle.fadeB = this.fadeB;
/*     */         } 
/* 296 */         sparkParticle.twinkle = this.twinkle;
/* 297 */         this.engine.add(sparkParticle);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class OverlayParticle extends SingleQuadParticle {
/*     */     private OverlayParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 304 */       super(level, x, y, z, sprite);
/* 305 */       this.lifetime = 4;
/*     */     }
/*     */ 
/*     */     
/*     */     public SingleQuadParticle.Layer getLayer() {
/* 310 */       return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */     }
/*     */ 
/*     */     
/*     */     public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 315 */       setAlpha(0.6F - (this.age + partialTickTime - 1.0F) * 0.25F * 0.5F);
/* 316 */       super.extract(particleTypeRenderState, camera, partialTickTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public float getQuadSize(float a) {
/* 321 */       return 7.1F * Mth.sin(((this.age + a - 1.0F) * 0.25F * 3.1415927F));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FlashProvider implements ParticleProvider<ColorParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public FlashProvider(SpriteSet sprite) {
/* 329 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 334 */       FireworkParticles.OverlayParticle particle = new FireworkParticles.OverlayParticle(level, x, y, z, this.sprite.get(random));
/* 335 */       particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 336 */       particle.setAlpha(options.getAlpha());
/* 337 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SparkProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public SparkProvider(SpriteSet sprites) {
/* 345 */       this.sprites = sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 350 */       FireworkParticles.SparkParticle particle = new FireworkParticles.SparkParticle(level, x, y, z, xAux, yAux, zAux, (Minecraft.getInstance()).particleEngine, this.sprites);
/* 351 */       particle.setAlpha(0.99F);
/* 352 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FireworkParticles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */