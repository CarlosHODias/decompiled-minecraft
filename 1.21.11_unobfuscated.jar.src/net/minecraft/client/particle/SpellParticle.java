/*     */ package net.minecraft.client.particle;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.core.particles.SpellParticleOption;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SpellParticle extends SingleQuadParticle {
/*  13 */   private static final RandomSource RANDOM = RandomSource.create();
/*     */   
/*     */   private final SpriteSet sprites;
/*  16 */   private float originalAlpha = 1.0F;
/*     */   
/*     */   private SpellParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/*  19 */     super(level, x, y, z, 0.5D - RANDOM.nextDouble(), ya, 0.5D - RANDOM.nextDouble(), sprites.first());
/*  20 */     this.friction = 0.96F;
/*  21 */     this.gravity = -0.1F;
/*  22 */     this.speedUpWhenYMotionIsBlocked = true;
/*  23 */     this.sprites = sprites;
/*  24 */     this.yd *= 0.20000000298023224D;
/*  25 */     if (xa == 0.0D && za == 0.0D) {
/*  26 */       this.xd *= 0.10000000149011612D;
/*  27 */       this.zd *= 0.10000000149011612D;
/*     */     } 
/*     */     
/*  30 */     this.quadSize *= 0.75F;
/*     */     
/*  32 */     this.lifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*     */     
/*  34 */     this.hasPhysics = false;
/*  35 */     setSpriteFromAge(sprites);
/*     */     
/*  37 */     if (isCloseToScopingPlayer()) {
/*  38 */       setAlpha(0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  44 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  49 */     super.tick();
/*  50 */     setSpriteFromAge(this.sprites);
/*     */     
/*  52 */     if (isCloseToScopingPlayer()) {
/*  53 */       this.alpha = 0.0F;
/*     */     } else {
/*  55 */       this.alpha = Mth.lerp(0.05F, this.alpha, this.originalAlpha);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setAlpha(float alpha) {
/*  61 */     super.setAlpha(alpha);
/*  62 */     this.originalAlpha = alpha;
/*     */   }
/*     */   
/*     */   private boolean isCloseToScopingPlayer() {
/*  66 */     Minecraft instance = Minecraft.getInstance();
/*  67 */     LocalPlayer player = instance.player;
/*  68 */     return (player != null && player.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 9.0D && instance.options.getCameraType().isFirstPerson() && player.isScoping());
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/*  75 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  80 */       return new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MobEffectProvider implements ParticleProvider<ColorParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public MobEffectProvider(SpriteSet sprite) {
/*  88 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  93 */       SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/*     */       
/*  95 */       particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/*  96 */       particle.setAlpha(options.getAlpha());
/*  97 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WitchProvider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public WitchProvider(SpriteSet sprite) {
/* 105 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 110 */       SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 111 */       float randBrightness = random.nextFloat() * 0.5F + 0.35F;
/* 112 */       particle.setColor(1.0F * randBrightness, 0.0F * randBrightness, 1.0F * randBrightness);
/* 113 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InstantProvider implements ParticleProvider<SpellParticleOption> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public InstantProvider(SpriteSet sprite) {
/* 121 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SpellParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 126 */       SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
/* 127 */       particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
/* 128 */       particle.setPower(options.getPower());
/* 129 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SpellParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */