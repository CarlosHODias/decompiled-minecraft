/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class ComparatorBlockEntity
/*    */   extends BlockEntity {
/*    */   private static final int DEFAULT_OUTPUT = 0;
/* 11 */   private int output = 0;
/*    */   
/*    */   public ComparatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 14 */     super(BlockEntityType.COMPARATOR, worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveAdditional(ValueOutput output) {
/* 19 */     super.saveAdditional(output);
/* 20 */     output.putInt("OutputSignal", this.output);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void loadAdditional(ValueInput input) {
/* 25 */     super.loadAdditional(input);
/* 26 */     this.output = input.getIntOr("OutputSignal", 0);
/*    */   }
/*    */   
/*    */   public int getOutputSignal() {
/* 30 */     return this.output;
/*    */   }
/*    */   
/*    */   public void setOutputSignal(int value) {
/* 34 */     this.output = value;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/ComparatorBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */