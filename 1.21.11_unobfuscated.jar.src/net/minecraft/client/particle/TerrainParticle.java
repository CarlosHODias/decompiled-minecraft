/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class TerrainParticle
/*     */   extends SingleQuadParticle {
/*     */   private final SingleQuadParticle.Layer layer;
/*     */   private final BlockPos pos;
/*     */   private final float uo;
/*     */   private final float vo;
/*     */   
/*     */   public TerrainParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, BlockState blockState) {
/*  23 */     this(level, x, y, z, xa, ya, za, blockState, BlockPos.containing(x, y, z));
/*     */   }
/*     */   
/*     */   public TerrainParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, BlockState blockState, BlockPos pos) {
/*  27 */     super(level, x, y, z, xa, ya, za, Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState));
/*  28 */     this.pos = pos;
/*  29 */     this.gravity = 1.0F;
/*  30 */     this.rCol = 0.6F;
/*  31 */     this.gCol = 0.6F;
/*  32 */     this.bCol = 0.6F;
/*     */     
/*  34 */     if (!blockState.is(Blocks.GRASS_BLOCK)) {
/*  35 */       int col = Minecraft.getInstance().getBlockColors().getColor(blockState, (BlockAndTintGetter)level, pos, 0);
/*  36 */       this.rCol *= (col >> 16 & 0xFF) / 255.0F;
/*  37 */       this.gCol *= (col >> 8 & 0xFF) / 255.0F;
/*  38 */       this.bCol *= (col & 0xFF) / 255.0F;
/*     */     } 
/*     */     
/*  41 */     this.quadSize /= 2.0F;
/*     */     
/*  43 */     this.uo = this.random.nextFloat() * 3.0F;
/*  44 */     this.vo = this.random.nextFloat() * 3.0F;
/*  45 */     this.layer = this.sprite.atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS) ? SingleQuadParticle.Layer.TERRAIN : SingleQuadParticle.Layer.ITEMS;
/*     */   }
/*     */ 
/*     */   
/*     */   public SingleQuadParticle.Layer getLayer() {
/*  50 */     return this.layer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getU0() {
/*  55 */     return this.sprite.getU((this.uo + 1.0F) / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getU1() {
/*  60 */     return this.sprite.getU(this.uo / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getV0() {
/*  65 */     return this.sprite.getV(this.vo / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getV1() {
/*  70 */     return this.sprite.getV((this.vo + 1.0F) / 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightColor(float a) {
/*  75 */     int rawPositionColor = super.getLightColor(a);
/*     */ 
/*     */     
/*  78 */     if (rawPositionColor == 0 && this.level.hasChunkAt(this.pos)) {
/*  79 */       return LevelRenderer.getLightColor((BlockAndTintGetter)this.level, this.pos);
/*     */     }
/*     */     
/*  82 */     return rawPositionColor;
/*     */   }
/*     */   
/*     */   public static class Provider
/*     */     implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  88 */       return TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class DustPillarProvider
/*     */     implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/*  95 */       Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/*  96 */       if (particle != null) {
/*  97 */         particle.setParticleSpeed(random.nextGaussian() / 30.0D, yAux + random.nextGaussian() / 2.0D, random.nextGaussian() / 30.0D);
/*  98 */         particle.setLifetime(random.nextInt(20) + 20);
/*     */       } 
/* 100 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CrumblingProvider
/*     */     implements ParticleProvider<BlockParticleOption> {
/*     */     public Particle createParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 107 */       Particle particle = TerrainParticle.createTerrainParticle(options, level, x, y, z, xAux, yAux, zAux);
/* 108 */       if (particle != null) {
/* 109 */         particle.setParticleSpeed(0.0D, 0.0D, 0.0D);
/* 110 */         particle.setLifetime(random.nextInt(10) + 1);
/*     */       } 
/* 112 */       return particle;
/*     */     }
/*     */   }
/*     */   
/*     */   private static TerrainParticle createTerrainParticle(BlockParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
/* 117 */     BlockState state = options.getState();
/* 118 */     if (state.isAir() || state.is(Blocks.MOVING_PISTON) || !state.shouldSpawnTerrainParticles()) {
/* 119 */       return null;
/*     */     }
/* 121 */     return new TerrainParticle(level, x, y, z, xAux, yAux, zAux, state);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/TerrainParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */