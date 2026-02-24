/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ 
/*    */ public class WaterDropParticle extends SingleQuadParticle {
/*    */   protected WaterDropParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
/* 12 */     super(level, x, y, z, 0.0D, 0.0D, 0.0D, sprite);
/* 13 */     this.xd *= 0.30000001192092896D;
/* 14 */     this.yd = (this.random.nextFloat() * 0.2F + 0.1F);
/* 15 */     this.zd *= 0.30000001192092896D;
/*    */     
/* 17 */     setSize(0.01F, 0.01F);
/* 18 */     this.gravity = 0.06F;
/*    */     
/* 20 */     this.lifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 25 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 30 */     this.xo = this.x;
/* 31 */     this.yo = this.y;
/* 32 */     this.zo = this.z;
/*    */     
/* 34 */     if (this.lifetime-- <= 0) {
/* 35 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     this.yd -= this.gravity;
/* 40 */     move(this.xd, this.yd, this.zd);
/* 41 */     this.xd *= 0.9800000190734863D;
/* 42 */     this.yd *= 0.9800000190734863D;
/* 43 */     this.zd *= 0.9800000190734863D;
/*    */     
/* 45 */     if (this.onGround) {
/* 46 */       if (this.random.nextFloat() < 0.5F) {
/* 47 */         remove();
/*    */       }
/* 49 */       this.xd *= 0.699999988079071D;
/* 50 */       this.zd *= 0.699999988079071D;
/*    */     } 
/*    */     
/* 53 */     BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
/* 54 */     double offset = Math.max(
/* 55 */         this.level.getBlockState(pos).getCollisionShape((BlockGetter)this.level, pos).max(net.minecraft.core.Direction.Axis.Y, this.x - pos.getX(), this.z - pos.getZ()), 
/* 56 */         this.level.getFluidState(pos).getHeight((BlockGetter)this.level, pos));
/*    */ 
/*    */     
/* 59 */     if (offset > 0.0D && this.y < pos.getY() + offset)
/* 60 */       remove(); 
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 68 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 73 */       return new WaterDropParticle(level, x, y, z, this.sprite.get(random));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/WaterDropParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */