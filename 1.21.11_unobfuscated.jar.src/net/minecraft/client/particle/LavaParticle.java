/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class LavaParticle extends SingleQuadParticle {
/*    */   private LavaParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 11 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 12 */     this.gravity = 0.75F;
/* 13 */     this.friction = 0.999F;
/* 14 */     this.xd *= 0.800000011920929D;
/* 15 */     this.yd *= 0.800000011920929D;
/* 16 */     this.zd *= 0.800000011920929D;
/* 17 */     this.yd = (this.random.nextFloat() * 0.4F + 0.05F);
/*    */     
/* 19 */     this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
/*    */     
/* 21 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 26 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLightColor(float a) {
/* 31 */     int br = super.getLightColor(a);
/*    */     
/* 33 */     int br1 = 240;
/* 34 */     int br2 = br >> 16 & 0xFF;
/* 35 */     return 0xF0 | br2 << 16;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 40 */     float s = (this.age + a) / this.lifetime;
/* 41 */     return this.quadSize * (1.0F - s * s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     super.tick();
/* 47 */     if (!this.removed) {
/* 48 */       float odds = this.age / this.lifetime;
/* 49 */       if (this.random.nextFloat() > odds)
/* 50 */         this.level.addParticle((ParticleOptions)ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 59 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 64 */       LavaParticle particle = new LavaParticle(level, x, y, z, this.sprite.get(random));
/* 65 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/LavaParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */