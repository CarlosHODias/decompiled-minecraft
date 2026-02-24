/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BedPart;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CatSitOnBlockGoal extends MoveToBlockGoal {
/*    */   public CatSitOnBlockGoal(Cat cat, double speedModifier) {
/* 18 */     super((PathfinderMob)cat, speedModifier, 8);
/* 19 */     this.cat = cat;
/*    */   }
/*    */   private final Cat cat;
/*    */   
/*    */   public boolean canUse() {
/* 24 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 29 */     super.start();
/* 30 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 35 */     super.stop();
/* 36 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     super.tick();
/*    */     
/* 43 */     this.cat.setInSittingPose(isReachedTarget());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 48 */     if (!level.isEmptyBlock(pos.above())) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     BlockState blockState = level.getBlockState(pos);
/*    */ 
/*    */     
/* 55 */     if (blockState.is(Blocks.CHEST))
/* 56 */       return (ChestBlockEntity.getOpenCount((BlockGetter)level, pos) < 1); 
/* 57 */     if (blockState.is(Blocks.FURNACE) && (Boolean)blockState.getValue((Property)net.minecraft.world.level.block.FurnaceBlock.LIT)) {
/* 58 */       return true;
/*    */     }
/* 60 */     return blockState.is(BlockTags.BEDS, s -> (Boolean)s.getOptionalValue((Property)BedBlock.PART).map(()).orElse(true));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/CatSitOnBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */