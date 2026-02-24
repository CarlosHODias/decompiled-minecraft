/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.DispenserBlock;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class DefaultDispenseItemBehavior
/*    */   implements DispenseItemBehavior {
/*    */   private static final int DEFAULT_ACCURACY = 6;
/*    */   
/*    */   public final ItemStack dispense(BlockSource source, ItemStack dispensed) {
/* 17 */     ItemStack result = execute(source, dispensed);
/*    */     
/* 19 */     playSound(source);
/* 20 */     playAnimation(source, (Direction)source.state().getValue((Property)DispenserBlock.FACING));
/*    */     
/* 22 */     return result;
/*    */   }
/*    */   
/*    */   protected ItemStack execute(BlockSource source, ItemStack dispensed) {
/* 26 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 27 */     Position position = DispenserBlock.getDispensePosition(source);
/*    */     
/* 29 */     ItemStack itemStack = dispensed.split(1);
/*    */     
/* 31 */     spawnItem((Level)source.level(), itemStack, 6, direction, position);
/*    */     
/* 33 */     return dispensed;
/*    */   }
/*    */   
/*    */   public static void spawnItem(Level level, ItemStack itemStack, int accuracy, Direction direction, Position position) {
/* 37 */     double spawnX = position.x();
/* 38 */     double spawnY = position.y();
/* 39 */     double spawnZ = position.z();
/*    */     
/* 41 */     if (direction.getAxis() == Direction.Axis.Y) {
/*    */       
/* 43 */       spawnY -= 0.125D;
/*    */     } else {
/*    */       
/* 46 */       spawnY -= 0.15625D;
/*    */     } 
/*    */     
/* 49 */     ItemEntity itemEntity = new ItemEntity(level, spawnX, spawnY, spawnZ, itemStack);
/*    */     
/* 51 */     double pow = level.random.nextDouble() * 0.1D + 0.2D;
/* 52 */     itemEntity.setDeltaMovement(
/* 53 */         level.random.triangle(direction.getStepX() * pow, 0.0172275D * accuracy), 
/* 54 */         level.random.triangle(0.2D, 0.0172275D * accuracy), 
/* 55 */         level.random.triangle(direction.getStepZ() * pow, 0.0172275D * accuracy));
/*    */ 
/*    */     
/* 58 */     level.addFreshEntity((Entity)itemEntity);
/*    */   }
/*    */   
/*    */   protected void playSound(BlockSource source) {
/* 62 */     playDefaultSound(source);
/*    */   }
/*    */   
/*    */   protected void playAnimation(BlockSource source, Direction direction) {
/* 66 */     playDefaultAnimation(source, direction);
/*    */   }
/*    */   
/*    */   private static void playDefaultSound(BlockSource source) {
/* 70 */     source.level().levelEvent(1000, source.pos(), 0);
/*    */   }
/*    */   
/*    */   private static void playDefaultAnimation(BlockSource source, Direction direction) {
/* 74 */     source.level().levelEvent(2000, source.pos(), direction.get3DDataValue());
/*    */   }
/*    */   
/*    */   protected ItemStack consumeWithRemainder(BlockSource source, ItemStack dispensed, ItemStack remainder) {
/* 78 */     dispensed.shrink(1);
/* 79 */     if (dispensed.isEmpty())
/*    */     {
/* 81 */       return remainder;
/*    */     }
/* 83 */     addToInventoryOrDispense(source, remainder);
/* 84 */     return dispensed;
/*    */   }
/*    */   
/*    */   private void addToInventoryOrDispense(BlockSource source, ItemStack itemStack) {
/* 88 */     ItemStack remainder = source.blockEntity().insertItem(itemStack);
/* 89 */     if (remainder.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 93 */     Direction direction = (Direction)source.state().getValue((Property)DispenserBlock.FACING);
/* 94 */     spawnItem((Level)source.level(), remainder, 6, direction, DispenserBlock.getDispensePosition(source));
/*    */     
/* 96 */     playDefaultSound(source);
/* 97 */     playDefaultAnimation(source, direction);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/dispenser/DefaultDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */