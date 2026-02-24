/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class VinesFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public VinesFeature(Codec<NoneFeatureConfiguration> codec) {
/* 14 */     super(codec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 31 */     WorldGenLevel level = context.level();
/* 32 */     BlockPos origin = context.origin();
/* 33 */     context.config();
/* 34 */     if (!level.isEmptyBlock(origin)) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     for (Direction direction : Direction.values()) {
/* 39 */       if (direction != Direction.DOWN)
/*    */       {
/*    */ 
/*    */         
/* 43 */         if (VineBlock.isAcceptableNeighbour((BlockGetter)level, origin.relative(direction), direction)) {
/* 44 */           level.setBlock(origin, (net.minecraft.world.level.block.state.BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.getPropertyForFace(direction), true), 2);
/* 45 */           return true;
/*    */         }  } 
/*    */     } 
/* 48 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/VinesFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */