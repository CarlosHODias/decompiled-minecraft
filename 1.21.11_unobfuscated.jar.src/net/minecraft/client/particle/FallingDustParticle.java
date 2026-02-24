/*    */ package net.minecraft.client.particle;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.FallingBlock;
/*    */ import net.minecraft.world.level.block.RenderShape;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FallingDustParticle extends SingleQuadParticle {
/*    */   private final float rotSpeed;
/*    */   
/*    */   private FallingDustParticle(ClientLevel level, double x, double y, double z, float r, float g, float b, SpriteSet sprites) {
/* 19 */     super(level, x, y, z, sprites.first());
/* 20 */     this.sprites = sprites;
/*    */     
/* 22 */     this.rCol = r;
/* 23 */     this.gCol = g;
/* 24 */     this.bCol = b;
/*    */     
/* 26 */     float scale = 0.9F;
/*    */     
/* 28 */     this.quadSize *= 0.67499995F;
/*    */     
/* 30 */     int baseLifetime = (int)(32.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/* 31 */     this.lifetime = (int)Math.max(baseLifetime * 0.9F, 1.0F);
/* 32 */     setSpriteFromAge(sprites);
/*    */     
/* 34 */     this.rotSpeed = (this.random.nextFloat() - 0.5F) * 0.1F;
/* 35 */     this.roll = this.random.nextFloat() * 6.2831855F;
/*    */   }
/*    */   private final SpriteSet sprites;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 40 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 45 */     return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 50 */     this.xo = this.x;
/* 51 */     this.yo = this.y;
/* 52 */     this.zo = this.z;
/*    */     
/* 54 */     if (this.age++ >= this.lifetime) {
/* 55 */       remove();
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     setSpriteFromAge(this.sprites);
/*    */     
/* 61 */     this.oRoll = this.roll;
/* 62 */     this.roll += 3.1415927F * this.rotSpeed * 2.0F;
/* 63 */     if (this.onGround) {
/* 64 */       this.oRoll = this.roll = 0.0F;
/*    */     }
/*    */     
/* 67 */     move(this.xd, this.yd, this.zd);
/* 68 */     this.yd -= 0.003000000026077032D;
/* 69 */     this.yd = Math.max(this.yd, -0.14000000059604645D);
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<BlockParticleOption> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 76 */       this.sprite = sprite;
/*    */     }
/*    */ 
/*    */     
/*    */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 81 */       BlockState blockState = options.getState();
/* 82 */       if (!blockState.isAir() && blockState.getRenderShape() == RenderShape.INVISIBLE) {
/* 83 */         return null;
/*    */       }
/*    */       
/* 86 */       BlockPos pos = BlockPos.containing(x, y, z);
/* 87 */       int col = Minecraft.getInstance().getBlockColors().getColor(blockState, (Level)level, pos);
/* 88 */       if (blockState.getBlock() instanceof FallingBlock) {
/* 89 */         col = ((FallingBlock)blockState.getBlock()).getDustColor(blockState, (BlockGetter)level, pos);
/*    */       }
/* 91 */       float r = (col >> 16 & 0xFF) / 255.0F;
/* 92 */       float g = (col >> 8 & 0xFF) / 255.0F;
/* 93 */       float b = (col & 0xFF) / 255.0F;
/*    */       
/* 95 */       return new FallingDustParticle(level, x, y, z, r, g, b, this.sprite);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/FallingDustParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */