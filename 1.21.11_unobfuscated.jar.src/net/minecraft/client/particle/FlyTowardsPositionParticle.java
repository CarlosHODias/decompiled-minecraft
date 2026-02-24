/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.state.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class FlyTowardsPositionParticle extends SingleQuadParticle {
/*     */   private final double xStart;
/*     */   private final double yStart;
/*     */   private final double zStart;
/*     */   private final boolean isGlowing;
/*     */   private final Particle.LifetimeAlpha lifetimeAlpha;
/*     */   
/*     */   private FlyTowardsPositionParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*  19 */     this(level, x, y, z, xd, yd, zd, false, Particle.LifetimeAlpha.ALWAYS_OPAQUE, sprite);
/*     */   }
/*     */   
/*     */   private FlyTowardsPositionParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, boolean isGlowing, Particle.LifetimeAlpha lifetimeAlpha, TextureAtlasSprite sprite) {
/*  23 */     super(level, x, y, z, sprite);
/*  24 */     this.isGlowing = isGlowing;
/*  25 */     this.lifetimeAlpha = lifetimeAlpha;
/*  26 */     setAlpha(lifetimeAlpha.startAlpha());
/*     */     
/*  28 */     this.xd = xd;
/*  29 */     this.yd = yd;
/*  30 */     this.zd = zd;
/*  31 */     this.xStart = x;
/*  32 */     this.yStart = y;
/*  33 */     this.zStart = z;
/*  34 */     this.xo = x + xd;
/*  35 */     this.yo = y + yd;
/*  36 */     this.zo = z + zd;
/*  37 */     this.x = this.xo;
/*  38 */     this.y = this.yo;
/*  39 */     this.z = this.zo;
/*     */     
/*  41 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
/*     */     
/*  43 */     float br = this.random.nextFloat() * 0.6F + 0.4F;
/*  44 */     this.rCol = 0.9F * br;
/*  45 */     this.gCol = 0.9F * br;
/*  46 */     this.bCol = br;
/*     */     
/*  48 */     this.hasPhysics = false;
/*     */     
/*  50 */     this.lifetime = (int)(this.random.nextFloat() * 10.0F) + 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  55 */     if (this.lifetimeAlpha.isOpaque()) {
/*  56 */       return SingleQuadParticle.Layer.OPAQUE;
/*     */     }
/*  58 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/*  63 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/*  64 */     setLocationFromBoundingbox();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/*  69 */     if (this.isGlowing) {
/*  70 */       return 240;
/*     */     }
/*  72 */     int br = super.getLightColor(a);
/*     */     
/*  74 */     float pos = this.age / this.lifetime;
/*  75 */     pos *= pos;
/*  76 */     pos *= pos;
/*     */     
/*  78 */     int br1 = br & 0xFF;
/*  79 */     int br2 = br >> 16 & 0xFF;
/*  80 */     br2 += (int)(pos * 15.0F * 16.0F);
/*  81 */     if (br2 > 240) {
/*  82 */       br2 = 240;
/*     */     }
/*  84 */     return br1 | br2 << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  89 */     this.xo = this.x;
/*  90 */     this.yo = this.y;
/*  91 */     this.zo = this.z;
/*     */     
/*  93 */     if (this.age++ >= this.lifetime) {
/*  94 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     float pos = this.age / this.lifetime;
/*  99 */     pos = 1.0F - pos;
/*     */     
/* 101 */     float pp = 1.0F - pos;
/* 102 */     pp *= pp;
/* 103 */     pp *= pp;
/* 104 */     this.x = this.xStart + this.xd * pos;
/* 105 */     this.y = this.yStart + this.yd * pos - (pp * 1.2F);
/* 106 */     this.z = this.zStart + this.zd * pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
/* 111 */     setAlpha(this.lifetimeAlpha.currentAlphaForAge(this.age, this.lifetime, partialTickTime));
/* 112 */     super.extract(particleTypeRenderState, camera, partialTickTime);
/*     */   }
/*     */   
/*     */   public static class EnchantProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public EnchantProvider(SpriteSet sprite) {
/* 119 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 124 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 125 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NautilusProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public NautilusProvider(SpriteSet sprite) {
/* 133 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 138 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 139 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VaultConnectionProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public VaultConnectionProvider(SpriteSet sprite) {
/* 147 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 152 */       FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(level, x, y, z, xAux, yAux, zAux, true, new Particle.LifetimeAlpha(0.0F, 0.6F, 0.25F, 1.0F), this.sprite.get(random));
/* 153 */       particle.scale(1.5F);
/* 154 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FlyTowardsPositionParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */