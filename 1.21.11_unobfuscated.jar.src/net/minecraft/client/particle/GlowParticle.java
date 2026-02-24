/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class GlowParticle extends SingleQuadParticle {
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   private GlowParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  13 */     super(level, x, y, z, xa, ya, za, sprites.first());
/*  14 */     this.friction = 0.96F;
/*  15 */     this.speedUpWhenYMotionIsBlocked = true;
/*  16 */     this.sprites = sprites;
/*     */     
/*  18 */     this.quadSize *= 0.75F;
/*     */     
/*  20 */     this.hasPhysics = false;
/*  21 */     setSpriteFromAge(sprites);
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  26 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/*  31 */     float l = (this.age + a) / this.lifetime;
/*  32 */     l = Mth.clamp(l, 0.0F, 1.0F);
/*  33 */     int br = super.getLightColor(a);
/*     */     
/*  35 */     int br1 = br & 0xFF;
/*  36 */     int br2 = br >> 16 & 0xFF;
/*  37 */     br1 += (int)(l * 15.0F * 16.0F);
/*  38 */     if (br1 > 240) {
/*  39 */       br1 = 240;
/*     */     }
/*  41 */     return br1 | br2 << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  46 */     super.tick();
/*  47 */     setSpriteFromAge(this.sprites);
/*     */   }
/*     */   
/*     */   public static class GlowSquidProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public GlowSquidProvider(SpriteSet sprite) {
/*  54 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  59 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.5D - random.nextDouble(), yAux, 0.5D - random.nextDouble(), this.sprite);
/*  60 */       if (random.nextBoolean()) {
/*  61 */         glowParticle.setColor(0.6F, 1.0F, 0.8F);
/*     */       } else {
/*  63 */         glowParticle.setColor(0.08F, 0.4F, 0.4F);
/*     */       } 
/*  65 */       glowParticle.yd *= 0.20000000298023224D;
/*  66 */       if (xAux == 0.0D && zAux == 0.0D) {
/*  67 */         glowParticle.xd *= 0.10000000149011612D;
/*  68 */         glowParticle.zd *= 0.10000000149011612D;
/*     */       } 
/*  70 */       glowParticle.setLifetime((int)(8.0D / (random.nextDouble() * 0.8D + 0.2D)));
/*  71 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaxOnProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaxOnProvider(SpriteSet sprite) {
/*  80 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  85 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/*  86 */       glowParticle.setColor(0.91F, 0.55F, 0.08F);
/*     */       
/*  88 */       glowParticle.setParticleSpeed(xAux * 0.01D / 2.0D, yAux * 0.01D, zAux * 0.01D / 2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  94 */       int minLifespan = 10;
/*  95 */       int maxLifespan = 40;
/*  96 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/*  97 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaxOffProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WaxOffProvider(SpriteSet sprite) {
/* 106 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 111 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 112 */       glowParticle.setColor(1.0F, 0.9F, 1.0F);
/*     */       
/* 114 */       glowParticle.setParticleSpeed(xAux * 0.01D / 2.0D, yAux * 0.01D, zAux * 0.01D / 2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       int minLifespan = 10;
/* 121 */       int maxLifespan = 40;
/* 122 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/* 123 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ElectricSparkProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.25D;
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ElectricSparkProvider(SpriteSet sprite) {
/* 132 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 137 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 138 */       glowParticle.setColor(1.0F, 0.9F, 1.0F);
/*     */       
/* 140 */       glowParticle.setParticleSpeed(xAux * 0.25D, yAux * 0.25D, zAux * 0.25D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       int minLifespan = 2;
/* 147 */       int maxLifespan = 4;
/* 148 */       glowParticle.setLifetime(random.nextInt(2) + 2);
/* 149 */       return glowParticle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScrapeProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private static final double SPEED_FACTOR = 0.01D;
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ScrapeProvider(SpriteSet sprite) {
/* 158 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 163 */       GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0D, 0.0D, 0.0D, this.sprite);
/* 164 */       if (random.nextBoolean()) {
/* 165 */         glowParticle.setColor(0.29F, 0.58F, 0.51F);
/*     */       } else {
/* 167 */         glowParticle.setColor(0.43F, 0.77F, 0.62F);
/*     */       } 
/*     */       
/* 170 */       glowParticle.setParticleSpeed(xAux * 0.01D, yAux * 0.01D, zAux * 0.01D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 176 */       int minLifespan = 10;
/* 177 */       int maxLifespan = 40;
/* 178 */       glowParticle.setLifetime(random.nextInt(30) + 10);
/* 179 */       return glowParticle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/GlowParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */