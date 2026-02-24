/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.TrailParticleOption;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TrailParticle extends SingleQuadParticle {
/*    */   private final Vec3 target;
/*    */   
/*    */   private TrailParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, Vec3 target, int color, TextureAtlasSprite sprite) {
/* 16 */     super(level, x, y, z, xAux, yAux, zAux, sprite);
/*    */     
/* 18 */     color = ARGB.scaleRGB(color, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F);
/*    */     
/* 20 */     this.rCol = ARGB.red(color) / 255.0F;
/* 21 */     this.gCol = ARGB.green(color) / 255.0F;
/* 22 */     this.bCol = ARGB.blue(color) / 255.0F;
/*    */     
/* 24 */     this.quadSize = 0.26F;
/*    */     
/* 26 */     this.target = target;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 31 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     this.xo = this.x;
/* 37 */     this.yo = this.y;
/* 38 */     this.zo = this.z;
/*    */     
/* 40 */     if (this.age++ >= this.lifetime) {
/* 41 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     int ticksRemaining = this.lifetime - this.age;
/* 46 */     double alpha = 1.0D / ticksRemaining;
/*    */     
/* 48 */     this.x = Mth.lerp(alpha, this.x, this.target.x());
/* 49 */     this.y = Mth.lerp(alpha, this.y, this.target.y());
/* 50 */     this.z = Mth.lerp(alpha, this.z, this.target.z());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 55 */     return 15728880;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<TrailParticleOption> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 62 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(TrailParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 67 */       TrailParticle particle = new TrailParticle(level, x, y, z, xAux, yAux, zAux, options.target(), options.color(), this.sprite.get(random));
/* 68 */       particle.setLifetime(options.duration());
/*    */       
/* 70 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/TrailParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */