/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BannerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public abstract class AbstractBannerBlock
/*    */   extends BaseEntityBlock {
/*    */   protected AbstractBannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
/* 16 */     super(properties);
/* 17 */     this.color = color;
/*    */   }
/*    */   
/*    */   private final DyeColor color;
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractBannerBlock> codec();
/*    */   
/*    */   public boolean isPossibleToRespawnInThis(BlockState state) {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 30 */     return (BlockEntity)new BannerBlockEntity(worldPosition, blockState, this.color);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 35 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof BannerBlockEntity) { BannerBlockEntity banner = (BannerBlockEntity)blockEntity;
/* 36 */       return banner.getItem(); }
/*    */ 
/*    */     
/* 39 */     return super.getCloneItemStack(level, pos, state, includeData);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 43 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AbstractBannerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */