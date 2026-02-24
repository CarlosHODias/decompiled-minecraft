/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.PowerParticleOption;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ public class DragonBreathParticle
/*     */   extends SingleQuadParticle
/*     */ {
/*     */   private static final int COLOR_MIN = 11993298;
/*     */   private static final int COLOR_MAX = 14614777;
/*     */   private static final float COLOR_MIN_RED = 0.7176471F;
/*     */   private static final float COLOR_MIN_GREEN = 0.0F;
/*     */   private static final float COLOR_MIN_BLUE = 0.8235294F;
/*     */   private static final float COLOR_MAX_RED = 0.8745098F;
/*     */   private static final float COLOR_MAX_GREEN = 0.0F;
/*     */   private static final float COLOR_MAX_BLUE = 0.9764706F;
/*     */   private boolean hasHitGround;
/*     */   private final SpriteSet sprites;
/*     */   
/*     */   private DragonBreathParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  25 */     super(level, x, y, z, sprites.first());
/*  26 */     this.friction = 0.96F;
/*  27 */     this.xd = xa;
/*  28 */     this.yd = ya;
/*  29 */     this.zd = za;
/*     */     
/*  31 */     this.rCol = Mth.nextFloat(this.random, 0.7176471F, 0.8745098F);
/*  32 */     this.gCol = Mth.nextFloat(this.random, 0.0F, 0.0F);
/*  33 */     this.bCol = Mth.nextFloat(this.random, 0.8235294F, 0.9764706F);
/*     */     
/*  35 */     this.quadSize *= 0.75F;
/*     */     
/*  37 */     this.lifetime = (int)(20.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*  38 */     this.hasHitGround = false;
/*  39 */     this.hasPhysics = false;
/*     */     
/*  41 */     this.sprites = sprites;
/*  42 */     setSpriteFromAge(sprites);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  47 */     this.xo = this.x;
/*  48 */     this.yo = this.y;
/*  49 */     this.zo = this.z;
/*     */     
/*  51 */     if (this.age++ >= this.lifetime) {
/*  52 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  56 */     setSpriteFromAge(this.sprites);
/*     */     
/*  58 */     if (this.onGround) {
/*  59 */       this.yd = 0.0D;
/*  60 */       this.hasHitGround = true;
/*     */     } 
/*     */     
/*  63 */     if (this.hasHitGround) {
/*  64 */       this.yd += 0.002D;
/*     */     }
/*     */     
/*  67 */     move(this.xd, this.yd, this.zd);
/*     */     
/*  69 */     if (this.y == this.yo) {
/*  70 */       this.xd *= 1.1D;
/*  71 */       this.zd *= 1.1D;
/*     */     } 
/*     */     
/*  74 */     this.xd *= this.friction;
/*  75 */     this.zd *= this.friction;
/*     */     
/*  77 */     if (this.hasHitGround) {
/*  78 */       this.yd *= this.friction;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  84 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getQuadSize(float a) {
/*  89 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<PowerParticleOption> {
/*     */     private final SpriteSet sprites;
/*     */     
/*     */     public Provider(SpriteSet sprites) {
/*  96 */       this.sprites = sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(PowerParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 101 */       DragonBreathParticle particle = new DragonBreathParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/* 102 */       particle.setPower(options.getPower());
/* 103 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DragonBreathParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */