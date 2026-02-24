/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SnowflakeParticle
/*    */   extends SingleQuadParticle {
/*    */   protected SnowflakeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/* 11 */     super(level, x, y, z, sprites.first());
/* 12 */     this.gravity = 0.225F;
/* 13 */     this.friction = 1.0F;
/* 14 */     this.sprites = sprites;
/* 15 */     this.xd = xa + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/* 16 */     this.yd = ya + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/* 17 */     this.zd = za + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F);
/*    */     
/* 19 */     this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
/*    */     
/* 21 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D)) + 2;
/* 22 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 27 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 32 */     super.tick();
/* 33 */     setSpriteFromAge(this.sprites);
/*    */     
/* 35 */     this.xd *= 0.949999988079071D;
/* 36 */     this.yd *= 0.8999999761581421D;
/* 37 */     this.zd *= 0.949999988079071D;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 44 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 49 */       SnowflakeParticle snowflakeParticle = new SnowflakeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/* 50 */       snowflakeParticle.setColor(0.923F, 0.964F, 0.999F);
/* 51 */       return snowflakeParticle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SnowflakeParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */