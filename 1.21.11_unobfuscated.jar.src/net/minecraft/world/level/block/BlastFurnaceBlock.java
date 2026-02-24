/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.MenuProvider;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlastFurnaceBlock extends AbstractFurnaceBlock {
/* 22 */   public static final MapCodec<BlastFurnaceBlock> CODEC = simpleCodec(BlastFurnaceBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<BlastFurnaceBlock> codec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/*    */   protected BlastFurnaceBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 35 */     return (BlockEntity)new BlastFurnaceBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 40 */     return createFurnaceTicker(level, type, BlockEntityType.BLAST_FURNACE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void openContainer(Level level, BlockPos pos, Player player) {
/* 45 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 46 */     if (blockEntity instanceof BlastFurnaceBlockEntity) {
/* 47 */       player.openMenu((MenuProvider)blockEntity);
/* 48 */       player.awardStat(Stats.INTERACT_WITH_BLAST_FURNACE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 54 */     if (!((Boolean)state.getValue((Property)LIT))) {
/*    */       return;
/*    */     }
/*    */     
/* 58 */     double x = pos.getX() + 0.5D;
/* 59 */     double y = pos.getY();
/* 60 */     double z = pos.getZ() + 0.5D;
/*    */     
/* 62 */     if (random.nextDouble() < 0.1D) {
/* 63 */       level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
/*    */     }
/*    */     
/* 66 */     Direction direction = (Direction)state.getValue((Property)FACING);
/* 67 */     Direction.Axis axis = direction.getAxis();
/*    */     
/* 69 */     double r = 0.52D;
/* 70 */     double ss = random.nextDouble() * 0.6D - 0.3D;
/*    */     
/* 72 */     double dx = (axis == Direction.Axis.X) ? (direction.getStepX() * 0.52D) : ss;
/* 73 */     double dy = random.nextDouble() * 9.0D / 16.0D;
/* 74 */     double dz = (axis == Direction.Axis.Z) ? (direction.getStepZ() * 0.52D) : ss;
/*    */     
/* 76 */     level.addParticle((ParticleOptions)net.minecraft.core.particles.ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/BlastFurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */