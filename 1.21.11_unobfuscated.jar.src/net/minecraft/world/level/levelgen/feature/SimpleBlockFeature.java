/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.DoublePlantBlock;
/*    */ import net.minecraft.world.level.block.MossyCarpetBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
/*    */ 
/*    */ public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
/*    */   public SimpleBlockFeature(Codec<SimpleBlockConfiguration> codec) {
/* 14 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
/* 19 */     SimpleBlockConfiguration config = context.config();
/* 20 */     WorldGenLevel level = context.level();
/* 21 */     BlockPos origin = context.origin();
/* 22 */     BlockState stateToPlace = config.toPlace().getState(context.random(), origin);
/*    */     
/* 24 */     if (stateToPlace.canSurvive((LevelReader)level, origin)) {
/* 25 */       if (stateToPlace.getBlock() instanceof DoublePlantBlock) {
/* 26 */         if (level.isEmptyBlock(origin.above())) {
/* 27 */           DoublePlantBlock.placeAt((LevelAccessor)level, stateToPlace, origin, 2);
/*    */         } else {
/* 29 */           return false;
/*    */         } 
/* 31 */       } else if (stateToPlace.getBlock() instanceof MossyCarpetBlock) {
/* 32 */         MossyCarpetBlock.placeAt((LevelAccessor)level, origin, level.getRandom(), 2);
/*    */       } else {
/* 34 */         level.setBlock(origin, stateToPlace, 2);
/*    */       } 
/* 36 */       if (config.scheduleTick()) {
/* 37 */         level.scheduleTick(origin, level.getBlockState(origin).getBlock(), 1);
/*    */       }
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/SimpleBlockFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */