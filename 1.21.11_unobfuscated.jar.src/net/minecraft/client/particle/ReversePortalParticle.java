/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class ReversePortalParticle extends PortalParticle {
/*    */   private ReversePortalParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, TextureAtlasSprite sprite) {
/* 10 */     super(level, x, y, z, xd, yd, zd, sprite);
/*    */     
/* 12 */     this.quadSize *= 1.5F;
/* 13 */     this.lifetime = (int)(this.random.nextFloat() * 2.0F) + 60;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 18 */     float s = 1.0F - (this.age + a) / this.lifetime * 1.5F;
/* 19 */     return this.quadSize * s;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 24 */     this.xo = this.x;
/* 25 */     this.yo = this.y;
/* 26 */     this.zo = this.z;
/*    */     
/* 28 */     if (this.age++ >= this.lifetime) {
/* 29 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     float speedMultiplier = this.age / this.lifetime;
/*    */     
/* 35 */     this.x += this.xd * speedMultiplier;
/* 36 */     this.y += this.yd * speedMultiplier;
/* 37 */     this.z += this.zd * speedMultiplier;
/*    */   }
/*    */   
/*    */   public static class ReversePortalProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public ReversePortalProvider(SpriteSet sprite) {
/* 44 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */       ReversePortalParticle particle = new ReversePortalParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 50 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ReversePortalParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */