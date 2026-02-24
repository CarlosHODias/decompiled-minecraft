/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SmokerBlock extends AbstractFurnaceBlock {
/* 21 */   public static final MapCodec<SmokerBlock> CODEC = simpleCodec(SmokerBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SmokerBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected SmokerBlock(BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 34 */     return (BlockEntity)new net.minecraft.world.level.block.entity.SmokerBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 39 */     return createFurnaceTicker(level, type, BlockEntityType.SMOKER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level level, BlockPos pos, Player player) {
/* 44 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 45 */     if (blockEntity instanceof net.minecraft.world.level.block.entity.SmokerBlockEntity) {
/* 46 */       player.openMenu((MenuProvider)blockEntity);
/* 47 */       player.awardStat(Stats.INTERACT_WITH_SMOKER);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 53 */     if (!((Boolean)state.getValue((Property)LIT))) {
/*    */       return;
/*    */     }
/*    */     
/* 57 */     double x = pos.getX() + 0.5D;
/* 58 */     double y = pos.getY();
/* 59 */     double z = pos.getZ() + 0.5D;
/*    */     
/* 61 */     if (random.nextDouble() < 0.1D) {
/* 62 */       level.playLocalSound(x, y, z, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 65 */     level.addParticle((ParticleOptions)ParticleTypes.SMOKE, x, y + 1.1D, z, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SmokerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */