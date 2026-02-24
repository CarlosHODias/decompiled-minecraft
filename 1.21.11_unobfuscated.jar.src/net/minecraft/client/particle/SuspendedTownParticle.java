/*     */ package net.minecraft.client.particle;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SuspendedTownParticle extends SingleQuadParticle {
/*     */   private SuspendedTownParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/*  10 */     super(level, x, y, z, xa, ya, za, sprite);
/*     */     
/*  12 */     float br = this.random.nextFloat() * 0.1F + 0.2F;
/*  13 */     this.rCol = br;
/*  14 */     this.gCol = br;
/*  15 */     this.bCol = br;
/*  16 */     setSize(0.02F, 0.02F);
/*     */     
/*  18 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
/*     */     
/*  20 */     this.xd *= 0.019999999552965164D;
/*  21 */     this.yd *= 0.019999999552965164D;
/*  22 */     this.zd *= 0.019999999552965164D;
/*     */     
/*  24 */     this.lifetime = (int)(20.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  29 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/*  34 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/*  35 */     setLocationFromBoundingbox();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  40 */     this.xo = this.x;
/*  41 */     this.yo = this.y;
/*  42 */     this.zo = this.z;
/*     */     
/*  44 */     if (this.lifetime-- <= 0) {
/*  45 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     move(this.xd, this.yd, this.zd);
/*  50 */     this.xd *= 0.99D;
/*  51 */     this.yd *= 0.99D;
/*  52 */     this.zd *= 0.99D;
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/*  59 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  64 */       return new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HappyVillagerProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public HappyVillagerProvider(SpriteSet sprite) {
/*  72 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  77 */       SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/*  78 */       particle.setColor(1.0F, 1.0F, 1.0F);
/*  79 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ComposterFillProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public ComposterFillProvider(SpriteSet sprite) {
/*  87 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  92 */       SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/*  93 */       particle.setColor(1.0F, 1.0F, 1.0F);
/*  94 */       particle.setLifetime(3 + level.getRandom().nextInt(5));
/*  95 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DolphinSpeedProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public DolphinSpeedProvider(SpriteSet sprite) {
/* 103 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 108 */       SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 109 */       particle.setColor(0.3F, 0.5F, 1.0F);
/* 110 */       particle.setAlpha(1.0F - random.nextFloat() * 0.7F);
/* 111 */       particle.setLifetime(particle.getLifetime() / 2);
/* 112 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class EggCrackProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public EggCrackProvider(SpriteSet sprite) {
/* 120 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 125 */       SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 126 */       particle.setColor(1.0F, 1.0F, 1.0F);
/* 127 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SuspendedTownParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */