/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class PortalParticle
/*     */   extends SingleQuadParticle {
/*     */   private final double xStart;
/*     */   
/*     */   protected PortalParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/*  14 */     super(level, x, y, z, sprite);
/*     */     
/*  16 */     this.xd = xd;
/*  17 */     this.yd = yd;
/*  18 */     this.zd = zd;
/*  19 */     this.x = x;
/*  20 */     this.y = y;
/*  21 */     this.z = z;
/*  22 */     this.xStart = this.x;
/*  23 */     this.yStart = this.y;
/*  24 */     this.zStart = this.z;
/*     */     
/*  26 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
/*     */     
/*  28 */     float br = this.random.nextFloat() * 0.6F + 0.4F;
/*  29 */     this.rCol = br * 0.9F;
/*  30 */     this.gCol = br * 0.3F;
/*  31 */     this.bCol = br;
/*     */     
/*  33 */     this.lifetime = (int)(this.random.nextFloat() * 10.0F) + 40;
/*     */   }
/*     */   private final double yStart; private final double zStart;
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  38 */     return SingleQuadParticle.Layer.OPAQUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(double xa, double ya, double za) {
/*  43 */     setBoundingBox(getBoundingBox().move(xa, ya, za));
/*  44 */     setLocationFromBoundingbox();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getQuadSize(float a) {
/*  49 */     float s = (this.age + a) / this.lifetime;
/*  50 */     s = 1.0F - s;
/*  51 */     s *= s;
/*  52 */     s = 1.0F - s;
/*  53 */     return this.quadSize * s;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/*  58 */     int br = super.getLightColor(a);
/*     */     
/*  60 */     float pos = this.age / this.lifetime;
/*  61 */     pos *= pos;
/*  62 */     pos *= pos;
/*     */     
/*  64 */     int br1 = br & 0xFF;
/*  65 */     int br2 = br >> 16 & 0xFF;
/*  66 */     br2 += (int)(pos * 15.0F * 16.0F);
/*  67 */     if (br2 > 240) {
/*  68 */       br2 = 240;
/*     */     }
/*  70 */     return br1 | br2 << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  75 */     this.xo = this.x;
/*  76 */     this.yo = this.y;
/*  77 */     this.zo = this.z;
/*     */     
/*  79 */     if (this.age++ >= this.lifetime) {
/*  80 */       remove();
/*     */       
/*     */       return;
/*     */     } 
/*  84 */     float pos = this.age / this.lifetime;
/*  85 */     float a = pos;
/*  86 */     pos = -pos + pos * pos * 2.0F;
/*  87 */     pos = 1.0F - pos;
/*     */     
/*  89 */     this.x = this.xStart + this.xd * pos;
/*  90 */     this.y = this.yStart + this.yd * pos + (1.0F - a);
/*  91 */     this.z = this.zStart + this.zd * pos;
/*     */   }
/*     */   
/*     */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*     */     private final SpriteSet sprite;
/*     */     
/*     */     public Provider(SpriteSet sprite) {
/*  98 */       this.sprite = sprite;
/*     */     }
/*     */ 
/*     */     
/*     */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 103 */       PortalParticle particle = new PortalParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 104 */       return particle;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/PortalParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */