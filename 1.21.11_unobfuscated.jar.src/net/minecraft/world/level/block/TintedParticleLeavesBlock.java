/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ColorParticleOption;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class TintedParticleLeavesBlock extends LeavesBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, TintedParticleLeavesBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<TintedParticleLeavesBlock> CODEC;
/*    */   
/*    */   public TintedParticleLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/* 20 */     super(leafParticleChance, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
/* 25 */     ColorParticleOption particle = ColorParticleOption.create(net.minecraft.core.particles.ParticleTypes.TINTED_LEAVES, level.getClientLeafTintColor(pos));
/* 26 */     net.minecraft.util.ParticleUtils.spawnParticleBelow(level, pos, random, (net.minecraft.core.particles.ParticleOptions)particle);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<? extends TintedParticleLeavesBlock> codec() {
/* 31 */     return CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/TintedParticleLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */