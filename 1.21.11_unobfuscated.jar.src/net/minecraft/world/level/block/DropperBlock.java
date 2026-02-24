/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.dispenser.BlockSource;
/*    */ import net.minecraft.core.dispenser.DispenseItemBehavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.DropperBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DropperBlock extends DispenserBlock {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 25 */   public static final MapCodec<DropperBlock> CODEC = simpleCodec(DropperBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<DropperBlock> codec() {
/* 29 */     return CODEC;
/*    */   }
/*    */   
/* 32 */   private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = (DispenseItemBehavior)new net.minecraft.core.dispenser.DefaultDispenseItemBehavior();
/*    */   
/*    */   public DropperBlock(BlockBehaviour.Properties properties) {
/* 35 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected DispenseItemBehavior getDispenseMethod(Level level, ItemStack itemStack) {
/* 40 */     return DISPENSE_BEHAVIOUR;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 45 */     return (BlockEntity)new DropperBlockEntity(worldPosition, blockState);
/*    */   }
/*    */   
/*    */   protected void dispenseFrom(ServerLevel level, BlockState state, BlockPos pos) {
/*    */     ItemStack remaining;
/* 50 */     DispenserBlockEntity blockEntity = level.getBlockEntity(pos, net.minecraft.world.level.block.entity.BlockEntityType.DROPPER).orElse(null);
/* 51 */     if (blockEntity == null) {
/* 52 */       LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", pos);
/*    */       return;
/*    */     } 
/* 55 */     BlockSource source = new BlockSource(level, pos, state, blockEntity);
/*    */     
/* 57 */     int slot = blockEntity.getRandomSlot(level.random);
/* 58 */     if (slot < 0) {
/* 59 */       level.levelEvent(1001, pos, 0);
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     ItemStack itemStack = blockEntity.getItem(slot);
/* 64 */     if (itemStack.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 68 */     Direction direction = (Direction)level.getBlockState(pos).getValue((Property)FACING);
/* 69 */     Container into = HopperBlockEntity.getContainerAt((Level)level, pos.relative(direction));
/*    */ 
/*    */     
/* 72 */     if (into == null) {
/* 73 */       remaining = DISPENSE_BEHAVIOUR.dispense(source, itemStack);
/*    */     } else {
/* 75 */       remaining = HopperBlockEntity.addItem((Container)blockEntity, into, itemStack.copyWithCount(1), direction.getOpposite());
/*    */       
/* 77 */       if (remaining.isEmpty()) {
/* 78 */         remaining = itemStack.copy();
/* 79 */         remaining.shrink(1);
/*    */       } else {
/*    */         
/* 82 */         remaining = itemStack.copy();
/*    */       } 
/*    */     } 
/*    */     
/* 86 */     blockEntity.setItem(slot, remaining);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DropperBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */