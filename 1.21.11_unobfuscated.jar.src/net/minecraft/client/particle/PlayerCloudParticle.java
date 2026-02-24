/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class PlayerCloudParticle
/*    */   extends SingleQuadParticle {
/*    */   private PlayerCloudParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites) {
/* 13 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprites.first());
/* 14 */     this.friction = 0.96F;
/* 15 */     this.sprites = sprites;
/*    */     
/* 17 */     float scale = 2.5F;
/* 18 */     this.xd *= 0.10000000149011612D;
/* 19 */     this.yd *= 0.10000000149011612D;
/* 20 */     this.zd *= 0.10000000149011612D;
/* 21 */     this.xd += xa;
/* 22 */     this.yd += ya;
/* 23 */     this.zd += za;
/*    */     
/* 25 */     float col = 1.0F - this.random.nextFloat() * 0.3F;
/* 26 */     this.rCol = col;
/* 27 */     this.gCol = col;
/* 28 */     this.bCol = col;
/* 29 */     this.quadSize *= 1.875F;
/*    */     
/* 31 */     int baseLifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.3D));
/* 32 */     this.lifetime = (int)Math.max(baseLifetime * 2.5F, 1.0F);
/*    */     
/* 34 */     this.hasPhysics = false;
/* 35 */     setSpriteFromAge(sprites);
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 40 */     return SingleQuadParticle.Layer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 45 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 50 */     super.tick();
/* 51 */     if (!this.removed) {
/* 52 */       setSpriteFromAge(this.sprites);
/* 53 */       Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0D, false);
/* 54 */       if (player != null) {
/* 55 */         double playerY = player.getY();
/* 56 */         if (this.y > playerY) {
/* 57 */           this.y += (playerY - this.y) * 0.2D;
/* 58 */           this.yd += ((player.getDeltaMovement()).y - this.yd) * 0.2D;
/* 59 */           setPos(this.x, this.y, this.z);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 69 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 74 */       return new PlayerCloudParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SneezeProvider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public SneezeProvider(SpriteSet sprites) {
/* 82 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 87 */       PlayerCloudParticle particle = new PlayerCloudParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
/* 88 */       particle.setColor(0.22F, 1.0F, 0.53F);
/* 89 */       particle.setAlpha(0.4F);
/* 90 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/PlayerCloudParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */