/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BubbleColumnUpParticle extends SingleQuadParticle {
/*    */   private BubbleColumnUpParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/* 12 */     super(level, x, y, z, sprite);
/* 13 */     this.gravity = -0.125F;
/* 14 */     this.friction = 0.85F;
/* 15 */     setSize(0.02F, 0.02F);
/*    */     
/* 17 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
/*    */     
/* 19 */     this.xd = xa * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 20 */     this.yd = ya * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 21 */     this.zd = za * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/*    */     
/* 23 */     this.lifetime = (int)(40.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     super.tick();
/* 29 */     if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
/* 30 */       remove();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 36 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 43 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 48 */       BubbleColumnUpParticle particle = new BubbleColumnUpParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 49 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BubbleColumnUpParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */