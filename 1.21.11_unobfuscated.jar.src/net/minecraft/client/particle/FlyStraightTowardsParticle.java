/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class FlyStraightTowardsParticle
/*    */   extends SingleQuadParticle {
/*    */   private final double xStart;
/*    */   private final double yStart;
/*    */   private final double zStart;
/*    */   private final int startColor;
/*    */   private final int endColor;
/*    */   
/*    */   private FlyStraightTowardsParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, int startColor, int endColor, TextureAtlasSprite sprite) {
/* 20 */     super(level, x, y, z, sprite);
/*    */     
/* 22 */     this.xd = xd;
/* 23 */     this.yd = yd;
/* 24 */     this.zd = zd;
/* 25 */     this.xStart = x;
/* 26 */     this.yStart = y;
/* 27 */     this.zStart = z;
/* 28 */     this.xo = x + xd;
/* 29 */     this.yo = y + yd;
/* 30 */     this.zo = z + zd;
/* 31 */     this.x = this.xo;
/* 32 */     this.y = this.yo;
/* 33 */     this.z = this.zo;
/*    */     
/* 35 */     this.quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.2F);
/* 36 */     this.hasPhysics = false;
/*    */     
/* 38 */     this.lifetime = (int)(this.random.nextFloat() * 5.0F) + 25;
/*    */     
/* 40 */     this.startColor = startColor;
/* 41 */     this.endColor = endColor;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 46 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void move(double xa, double ya, double za) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 56 */     return 240;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 61 */     this.xo = this.x;
/* 62 */     this.yo = this.y;
/* 63 */     this.zo = this.z;
/*    */     
/* 65 */     if (this.age++ >= this.lifetime) {
/* 66 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 70 */     float normalizedAge = this.age / this.lifetime;
/* 71 */     float posAlpha = 1.0F - normalizedAge;
/*    */     
/* 73 */     this.x = this.xStart + this.xd * posAlpha;
/* 74 */     this.y = this.yStart + this.yd * posAlpha;
/* 75 */     this.z = this.zStart + this.zd * posAlpha;
/*    */     
/* 77 */     int color = ARGB.srgbLerp(normalizedAge, this.startColor, this.endColor);
/*    */     
/* 79 */     setColor(
/* 80 */         ARGB.red(color) / 255.0F, 
/* 81 */         ARGB.green(color) / 255.0F, 
/* 82 */         ARGB.blue(color) / 255.0F);
/*    */     
/* 84 */     setAlpha(ARGB.alpha(color) / 255.0F);
/*    */   }
/*    */   
/*    */   public static class OminousSpawnProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public OminousSpawnProvider(SpriteSet sprite) {
/* 91 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 96 */       FlyStraightTowardsParticle particle = new FlyStraightTowardsParticle(level, x, y, z, xAux, yAux, zAux, -12210434, -1, this.sprite.get(random));
/* 97 */       particle.scale(Mth.randomBetween(level.getRandom(), 3.0F, 5.0F));
/* 98 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FlyStraightTowardsParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */