/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceSphereConfiguration implements FeatureConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("target").forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(()), (App)IntProvider.codec(0, 12).fieldOf("radius").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, ReplaceSphereConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<ReplaceSphereConfiguration> CODEC;
/*    */   
/*    */   public final BlockState targetState;
/*    */   
/*    */   public final BlockState replaceState;
/*    */   private final IntProvider radius;
/*    */   
/*    */   public ReplaceSphereConfiguration(BlockState targetState, BlockState replaceState, IntProvider radius) {
/* 21 */     this.targetState = targetState;
/* 22 */     this.replaceState = replaceState;
/* 23 */     this.radius = radius;
/*    */   }
/*    */   
/*    */   public IntProvider radius() {
/* 27 */     return this.radius;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/ReplaceSphereConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */