/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class BubbleParticle extends SingleQuadParticle {
/*    */   private BubbleParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
/* 12 */     super(level, x, y, z, sprite);
/* 13 */     setSize(0.02F, 0.02F);
/*    */     
/* 15 */     this.quadSize *= this.random.nextFloat() * 0.6F + 0.2F;
/*    */     
/* 17 */     this.xd = xa * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 18 */     this.yd = ya * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 19 */     this.zd = za * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/*    */     
/* 21 */     this.lifetime = (int)(8.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 26 */     this.xo = this.x;
/* 27 */     this.yo = this.y;
/* 28 */     this.zo = this.z;
/*    */     
/* 30 */     if (this.lifetime-- <= 0) {
/* 31 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     this.yd += 0.002D;
/* 36 */     move(this.xd, this.yd, this.zd);
/* 37 */     this.xd *= 0.8500000238418579D;
/* 38 */     this.yd *= 0.8500000238418579D;
/* 39 */     this.zd *= 0.8500000238418579D;
/*    */     
/* 41 */     if (!this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
/* 42 */       remove();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 48 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 55 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 60 */       BubbleParticle particle = new BubbleParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
/* 61 */       return particle;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BubbleParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */