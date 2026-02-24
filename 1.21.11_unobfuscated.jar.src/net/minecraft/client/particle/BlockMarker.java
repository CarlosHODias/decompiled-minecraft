/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.core.particles.BlockParticleOption;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMarker
/*    */   extends SingleQuadParticle {
/*    */   private BlockMarker(ClientLevel level, double x, double y, double z, BlockState state) {
/* 14 */     super(level, x, y, z, Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
/* 15 */     this.gravity = 0.0F;
/* 16 */     this.lifetime = 80;
/* 17 */     this.hasPhysics = false;
/* 18 */     this.layer = this.sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS) ? SingleQuadParticle.Layer.TERRAIN : SingleQuadParticle.Layer.ITEMS;
/*    */   }
/*    */   private final SingleQuadParticle.Layer layer;
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 23 */     return this.layer;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getQuadSize(float a) {
/* 28 */     return 0.5F;
/*    */   }
/*    */   
/*    */   public static class Provider
/*    */     implements ParticleProvider<BlockParticleOption> {
/*    */     public Particle createParticle(BlockParticleOption option, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 34 */       return new BlockMarker(level, x, y, z, option.getState());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/BlockMarker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */