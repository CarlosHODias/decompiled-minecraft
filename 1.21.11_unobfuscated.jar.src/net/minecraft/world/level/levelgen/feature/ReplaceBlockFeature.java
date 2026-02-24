/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
/*    */ 
/*    */ public class ReplaceBlockFeature
/*    */   extends Feature<ReplaceBlockConfiguration> {
/*    */   public ReplaceBlockFeature(Codec<ReplaceBlockConfiguration> codec) {
/* 12 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ReplaceBlockConfiguration> context) {
/* 17 */     WorldGenLevel level = context.level();
/* 18 */     BlockPos origin = context.origin();
/* 19 */     ReplaceBlockConfiguration config = context.config();
/* 20 */     for (OreConfiguration.TargetBlockState targetState : (Iterable<OreConfiguration.TargetBlockState>)config.targetStates) {
/* 21 */       if (targetState.target.test(level.getBlockState(origin), context.random())) {
/* 22 */         level.setBlock(origin, targetState.state, 2);
/*    */         break;
/*    */       } 
/*    */     } 
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/ReplaceBlockFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */