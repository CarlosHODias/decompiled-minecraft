/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceBlockConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(())).apply((Applicative)i, ReplaceBlockConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final Codec<ReplaceBlockConfiguration> CODEC;
/*    */   public final List<OreConfiguration.TargetBlockState> targetStates;
/*    */   
/*    */   public ReplaceBlockConfiguration(BlockState targetState, BlockState state) {
/* 19 */     this((List<OreConfiguration.TargetBlockState>)com.google.common.collect.ImmutableList.of(OreConfiguration.target((net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest)new net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest(targetState), state)));
/*    */   }
/*    */   
/*    */   public ReplaceBlockConfiguration(List<OreConfiguration.TargetBlockState> targetBlockStates) {
/* 23 */     this.targetStates = targetBlockStates;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/ReplaceBlockConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */