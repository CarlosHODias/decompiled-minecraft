/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class UntintedParticleLeavesBlock extends LeavesBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.util.ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, UntintedParticleLeavesBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<UntintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   protected final ParticleOptions leafParticle;
/*    */   
/*    */   public UntintedParticleLeavesBlock(float leafParticleChance, ParticleOptions leafParticle, BlockBehaviour.Properties properties) {
/* 23 */     super(leafParticleChance, properties);
/* 24 */     this.leafParticle = leafParticle;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level level, net.minecraft.core.BlockPos pos, RandomSource random) {
/* 29 */     net.minecraft.util.ParticleUtils.spawnParticleBelow(level, pos, random, this.leafParticle);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<UntintedParticleLeavesBlock> codec() {
/* 34 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/UntintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */