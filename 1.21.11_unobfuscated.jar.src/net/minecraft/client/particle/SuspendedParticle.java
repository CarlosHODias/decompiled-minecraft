/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleLimit;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SuspendedParticle
/*     */   extends SingleQuadParticle {
/*     */   private SuspendedParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/*  15 */     super(level, x, y - 0.125D, z, sprite);
/*  16 */     setSize(0.01F, 0.01F);
/*     */     
/*  18 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
/*  19 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  20 */     this.hasPhysics = false;
/*     */     
/*  22 */     this.friction = 1.0F;
/*  23 */     this.gravity = 0.0F;
/*     */   }
/*     */   
/*     */   private SuspendedParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*  27 */     super(level, x, y - 0.125D, z, xd, yd, zd, sprite);
/*  28 */     setSize(0.01F, 0.01F);
/*     */     
/*  30 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
/*  31 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  32 */     this.hasPhysics = false;
/*     */     
/*  34 */     this.friction = 1.0F;
/*  35 */     this.gravity = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  40 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */   
/*     */   public static class UnderwaterProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public UnderwaterProvider(SpriteSet sprite) {
/*  47 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  52 */       SuspendedParticle particle = new SuspendedParticle(level, x, y, z, this.sprite.get(random));
/*  53 */       particle.setColor(0.4F, 0.4F, 0.7F);
/*  54 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SporeBlossomAirProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public SporeBlossomAirProvider(SpriteSet sprite) {
/*  62 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  67 */       SuspendedParticle particle = new SuspendedParticle(this, level, x, y, z, 0.0D, -0.800000011920929D, 0.0D, this.sprite.get(random))
/*     */         {
/*     */           public Optional<ParticleLimit> getParticleLimit() {
/*  70 */             return Optional.of(ParticleLimit.SPORE_BLOSSOM);
/*     */           }
/*     */         };
/*  73 */       particle.lifetime = Mth.randomBetweenInclusive(random, 500, 1000);
/*  74 */       particle.gravity = 0.01F;
/*  75 */       particle.setColor(0.32F, 0.5F, 0.22F);
/*  76 */       return particle;
/*     */     }
/*     */   } class null extends SuspendedParticle { null(SuspendedParticle.SporeBlossomAirProvider this$0, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*     */       super(level, x, y, z, xd, yd, zd, sprite);
/*     */     } public Optional<ParticleLimit> getParticleLimit() {
/*     */       return Optional.of(ParticleLimit.SPORE_BLOSSOM);
/*     */     } }
/*     */   public static class CrimsonSporeProvider implements ParticleProvider<SimpleParticleType> { public CrimsonSporeProvider(SpriteSet sprite) {
/*  84 */       this.sprite = sprite;
/*     */     }
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  89 */       double xa = random.nextGaussian() * 9.999999974752427E-7D;
/*  90 */       double ya = random.nextGaussian() * 9.999999747378752E-5D;
/*  91 */       double za = random.nextGaussian() * 9.999999974752427E-7D;
/*  92 */       SuspendedParticle particle = new SuspendedParticle(level, x, y, z, xa, ya, za, this.sprite.get(random));
/*  93 */       particle.setColor(0.9F, 0.4F, 0.5F);
/*  94 */       return particle;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class WarpedSporeProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WarpedSporeProvider(SpriteSet sprite) {
/* 102 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 107 */       double ya = random.nextFloat() * -1.9D * random.nextFloat() * 0.1D;
/* 108 */       SuspendedParticle particle = new SuspendedParticle(level, x, y, z, 0.0D, ya, 0.0D, this.sprite.get(random));
/* 109 */       particle.setColor(0.1F, 0.1F, 0.3F);
/* 110 */       particle.setSize(0.001F, 0.001F);
/* 111 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SuspendedParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */