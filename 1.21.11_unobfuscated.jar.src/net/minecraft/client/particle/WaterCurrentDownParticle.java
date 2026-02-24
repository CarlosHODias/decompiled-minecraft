/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class WaterCurrentDownParticle
/*    */   extends SingleQuadParticle {
/*    */   private WaterCurrentDownParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 15 */     super(level, x, y, z, sprite);
/*    */     
/* 17 */     this.lifetime = (int)(this.random.nextFloat() * 60.0F) + 30;
/*    */     
/* 19 */     this.hasPhysics = false;
/*    */     
/* 21 */     this.xd = 0.0D;
/* 22 */     this.yd = -0.05D;
/* 23 */     this.zd = 0.0D;
/*    */     
/* 25 */     setSize(0.02F, 0.02F);
/* 26 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
/* 27 */     this.gravity = 0.002F;
/*    */   }
/*    */   private float angle;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 32 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 37 */     this.xo = this.x;
/* 38 */     this.yo = this.y;
/* 39 */     this.zo = this.z;
/*    */     
/* 41 */     if (this.age++ >= this.lifetime) {
/* 42 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     float radius = 0.6F;
/* 47 */     this.xd += (0.6F * Mth.cos(this.angle));
/* 48 */     this.zd += (0.6F * Mth.sin(this.angle));
/* 49 */     this.xd *= 0.07D;
/* 50 */     this.zd *= 0.07D;
/* 51 */     move(this.xd, this.yd, this.zd);
/*    */     
/* 53 */     if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER) || this.onGround) {
/* 54 */       remove();
/*    */     }
/*    */     
/* 57 */     this.angle += 0.08F;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 64 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 69 */       return new WaterCurrentDownParticle(level, x, y, z, this.sprite.get(random));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/WaterCurrentDownParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */