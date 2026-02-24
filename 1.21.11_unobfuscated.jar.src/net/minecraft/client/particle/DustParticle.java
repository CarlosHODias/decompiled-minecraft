/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.particles.DustParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustParticle extends DustParticleBase<DustParticleOptions> {
/*    */   protected DustParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, DustParticleOptions options, SpriteSet sprites) {
/* 10 */     super(level, x, y, z, xAux, yAux, zAux, options, sprites);
/*    */     
/* 12 */     float baseFactor = this.random.nextFloat() * 0.4F + 0.6F;
/* 13 */     Vector3f color = options.getColor();
/* 14 */     this.rCol = randomizeColor(color.x(), baseFactor);
/* 15 */     this.gCol = randomizeColor(color.y(), baseFactor);
/* 16 */     this.bCol = randomizeColor(color.z(), baseFactor);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<DustParticleOptions> {
/*    */     private final SpriteSet sprites;
/*    */     
/*    */     public Provider(SpriteSet sprites) {
/* 23 */       this.sprites = sprites;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(DustParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 28 */       return new DustParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/DustParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */