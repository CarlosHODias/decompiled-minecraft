/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ 
/*    */ public class SpikeConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.fieldOf("crystal_invulnerable").orElse(false).forGetter(()), (App)SpikeFeature.EndSpike.CODEC.listOf().fieldOf("spikes").forGetter(()), (App)BlockPos.CODEC.optionalFieldOf("crystal_beam_target").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SpikeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpikeConfiguration> CODEC;
/*    */   
/*    */   private final boolean crystalInvulnerable;
/*    */   private final List<SpikeFeature.EndSpike> spikes;
/*    */   private final BlockPos crystalBeamTarget;
/*    */   
/*    */   public SpikeConfiguration(boolean crystalInvulnerable, List<SpikeFeature.EndSpike> spikes, BlockPos crystalBeamTarget) {
/* 24 */     this(crystalInvulnerable, spikes, Optional.ofNullable(crystalBeamTarget));
/*    */   }
/*    */   
/*    */   private SpikeConfiguration(boolean crystalInvulnerable, List<SpikeFeature.EndSpike> spikes, Optional<BlockPos> crystalBeamTarget) {
/* 28 */     this.crystalInvulnerable = crystalInvulnerable;
/* 29 */     this.spikes = spikes;
/* 30 */     this.crystalBeamTarget = crystalBeamTarget.orElse(null);
/*    */   }
/*    */   
/*    */   public boolean isCrystalInvulnerable() {
/* 34 */     return this.crystalInvulnerable;
/*    */   }
/*    */   
/*    */   public List<SpikeFeature.EndSpike> getSpikes() {
/* 38 */     return this.spikes;
/*    */   }
/*    */   
/*    */   public BlockPos getCrystalBeamTarget() {
/* 42 */     return this.crystalBeamTarget;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */