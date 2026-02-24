/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class Containers
/*    */ {
/*    */   public static void dropContents(Level level, BlockPos pos, Container container) {
/* 15 */     dropContents(level, pos.getX(), pos.getY(), pos.getZ(), container);
/*    */   }
/*    */   
/*    */   public static void dropContents(Level level, Entity entity, Container container) {
/* 19 */     dropContents(level, entity.getX(), entity.getY(), entity.getZ(), container);
/*    */   }
/*    */   
/*    */   private static void dropContents(Level level, double x, double y, double z, Container container) {
/* 23 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 24 */       dropItemStack(level, x, y, z, container.getItem(i));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void dropContents(Level level, BlockPos pos, NonNullList<ItemStack> list) {
/* 29 */     list.forEach(itemStack -> dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemStack));
/*    */   }
/*    */   
/*    */   public static void dropItemStack(Level level, double x, double y, double z, ItemStack itemStack) {
/* 33 */     double size = EntityType.ITEM.getWidth();
/* 34 */     double centerRange = 1.0D - size;
/* 35 */     double halfSize = size / 2.0D;
/* 36 */     double xo = Math.floor(x) + level.random.nextDouble() * centerRange + halfSize;
/* 37 */     double yo = Math.floor(y) + level.random.nextDouble() * centerRange;
/* 38 */     double zo = Math.floor(z) + level.random.nextDouble() * centerRange + halfSize;
/*    */     
/* 40 */     while (!itemStack.isEmpty()) {
/* 41 */       ItemEntity entity = new ItemEntity(level, xo, yo, zo, itemStack.split(level.random.nextInt(21) + 10));
/*    */       
/* 43 */       float pow = 0.05F;
/* 44 */       entity.setDeltaMovement(
/* 45 */           level.random.triangle(0.0D, 0.11485000171139836D), 
/* 46 */           level.random.triangle(0.2D, 0.11485000171139836D), 
/* 47 */           level.random.triangle(0.0D, 0.11485000171139836D));
/*    */ 
/*    */       
/* 50 */       level.addFreshEntity((Entity)entity);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void updateNeighboursAfterDestroy(BlockState state, Level level, BlockPos pos) {
/* 55 */     level.updateNeighbourForOutputSignal(pos, state.getBlock());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/Containers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */