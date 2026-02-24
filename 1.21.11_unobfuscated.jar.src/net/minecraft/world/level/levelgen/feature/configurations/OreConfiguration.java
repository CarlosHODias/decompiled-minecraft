/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class OreConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("size").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(())).apply((Applicative)i, OreConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<OreConfiguration> CODEC;
/*    */   
/*    */   public final List<TargetBlockState> targetStates;
/*    */   public final int size;
/*    */   public final float discardChanceOnAirExposure;
/*    */   
/*    */   public OreConfiguration(List<TargetBlockState> targetBlockStates, int size, float discardChanceOnAirExposure) {
/* 23 */     this.size = size;
/* 24 */     this.targetStates = targetBlockStates;
/* 25 */     this.discardChanceOnAirExposure = discardChanceOnAirExposure;
/*    */   }
/*    */   
/*    */   public OreConfiguration(List<TargetBlockState> targetBlockStates, int size) {
/* 29 */     this(targetBlockStates, size, 0.0F);
/*    */   }
/*    */   
/*    */   public OreConfiguration(RuleTest target, BlockState state, int size, float discardChanceOnAirExposure) {
/* 33 */     this((List<TargetBlockState>)com.google.common.collect.ImmutableList.of(new TargetBlockState(target, state)), size, discardChanceOnAirExposure);
/*    */   }
/*    */   
/*    */   public OreConfiguration(RuleTest target, BlockState state, int size) {
/* 37 */     this((List<TargetBlockState>)com.google.common.collect.ImmutableList.of(new TargetBlockState(target, state)), size, 0.0F);
/*    */   }
/*    */   
/*    */   public static TargetBlockState target(RuleTest rule, BlockState state) {
/* 41 */     return new TargetBlockState(rule, state);
/*    */   }
/*    */   public static class TargetBlockState { public static final Codec<TargetBlockState> CODEC;
/*    */     static {
/* 45 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)RuleTest.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)i, TargetBlockState::new));
/*    */     }
/*    */ 
/*    */     
/*    */     public final RuleTest target;
/*    */     
/*    */     public final BlockState state;
/*    */     
/*    */     private TargetBlockState(RuleTest target, BlockState state) {
/* 54 */       this.target = target;
/* 55 */       this.state = state;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */