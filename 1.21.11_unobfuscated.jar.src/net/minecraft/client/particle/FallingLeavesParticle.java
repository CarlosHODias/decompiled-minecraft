/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ public class FallingLeavesParticle
/*     */   extends SingleQuadParticle
/*     */ {
/*     */   private static final float ACCELERATION_SCALE = 0.0025F;
/*     */   private static final int INITIAL_LIFETIME = 300;
/*     */   private static final int CURVE_ENDPOINT_TIME = 300;
/*     */   private float rotSpeed;
/*     */   private final float spinAcceleration;
/*     */   private final float windBig;
/*     */   private final boolean swirl;
/*     */   private final boolean flowAway;
/*     */   private final double xaFlowScale;
/*     */   private final double zaFlowScale;
/*     */   private final double swirlPeriod;
/*     */   
/*     */   protected FallingLeavesParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite, float fallAcceleration, float sideAcceleration, boolean swirl, boolean flowAway, float scale, float startVelocity) {
/*  35 */     super(level, x, y, z, sprite);
/*     */ 
/*     */     
/*  38 */     this.rotSpeed = (float)Math.toRadians(this.random.nextBoolean() ? -30.0D : 30.0D);
/*  39 */     this.spinAcceleration = (float)Math.toRadians(this.random.nextBoolean() ? -5.0D : 5.0D);
/*  40 */     this.windBig = sideAcceleration;
/*  41 */     this.swirl = swirl;
/*  42 */     this.flowAway = flowAway;
/*     */ 
/*     */     
/*  45 */     this.lifetime = 300;
/*     */     
/*  47 */     this.gravity = fallAcceleration * 1.2F * 0.0025F;
/*     */ 
/*     */     
/*  50 */     float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
/*  51 */     this.quadSize = size;
/*  52 */     setSize(size, size);
/*     */ 
/*     */     
/*  55 */     this.friction = 1.0F;
/*  56 */     this.yd = -startVelocity;
/*     */ 
/*     */     
/*  59 */     float particleRandom = this.random.nextFloat();
/*  60 */     this.xaFlowScale = Math.cos(Math.toRadians((particleRandom * 60.0F))) * this.windBig;
/*  61 */     this.zaFlowScale = Math.sin(Math.toRadians((particleRandom * 60.0F))) * this.windBig;
/*  62 */     this.swirlPeriod = Math.toRadians((1000.0F + particleRandom * 3000.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  67 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  73 */     this.xo = this.x;
/*  74 */     this.yo = this.y;
/*  75 */     this.zo = this.z;
/*     */     
/*  77 */     if (this.lifetime-- <= 0) {
/*  78 */       remove();
/*     */     }
/*  80 */     if (this.removed) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  85 */     float aliveTicks = (300 - this.lifetime);
/*  86 */     float relativeAge = Math.min(aliveTicks / 300.0F, 1.0F);
/*     */     
/*  88 */     double xa = 0.0D;
/*  89 */     double za = 0.0D;
/*  90 */     if (this.flowAway) {
/*  91 */       xa += this.xaFlowScale * Math.pow(relativeAge, 1.25D);
/*  92 */       za += this.zaFlowScale * Math.pow(relativeAge, 1.25D);
/*     */     } 
/*  94 */     if (this.swirl) {
/*  95 */       xa += relativeAge * Math.cos(relativeAge * this.swirlPeriod) * this.windBig;
/*  96 */       za += relativeAge * Math.sin(relativeAge * this.swirlPeriod) * this.windBig;
/*     */     } 
/*     */     
/*  99 */     this.xd += xa * 0.0024999999441206455D;
/* 100 */     this.zd += za * 0.0024999999441206455D;
/*     */ 
/*     */     
/* 103 */     this.yd -= this.gravity;
/*     */     
/* 105 */     this.rotSpeed += this.spinAcceleration / 20.0F;
/*     */     
/* 107 */     this.oRoll = this.roll;
/* 108 */     this.roll += this.rotSpeed / 20.0F;
/*     */ 
/*     */     
/* 111 */     move(this.xd, this.yd, this.zd);
/*     */ 
/*     */     
/* 114 */     if (this.onGround || (this.lifetime < 299 && (this.xd == 0.0D || this.zd == 0.0D))) {
/* 115 */       remove();
/*     */     }
/* 117 */     if (this.removed) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     this.xd *= this.friction;
/* 122 */     this.yd *= this.friction;
/* 123 */     this.zd *= this.friction;
/*     */   }
/*     */   
/*     */   public static class CherryProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public CherryProvider(SpriteSet sprites) {
/* 130 */       this.sprites = sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 135 */       return new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.25F, 2.0F, false, true, 1.0F, 0.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PaleOakProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public PaleOakProvider(SpriteSet sprites) {
/* 143 */       this.sprites = sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 148 */       return new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.07F, 10.0F, true, false, 2.0F, 0.021F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TintedLeavesProvider implements ParticleProvider<ColorParticleOption> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public TintedLeavesProvider(SpriteSet sprites) {
/* 156 */       this.sprites = sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 161 */       FallingLeavesParticle particle = new FallingLeavesParticle(level, x, y, z, this.sprites.get(random), 0.07F, 10.0F, true, false, 2.0F, 0.021F);
/* 162 */       particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 163 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FallingLeavesParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */