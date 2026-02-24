/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ 
/*    */ public interface CollisionContext
/*    */ {
/*    */   static CollisionContext empty() {
/* 17 */     return EntityCollisionContext.Empty.WITHOUT_FLUID_COLLISIONS;
/*    */   }
/*    */   
/*    */   static CollisionContext emptyWithFluidCollisions() {
/* 21 */     return EntityCollisionContext.Empty.WITH_FLUID_COLLISIONS;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity entity) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: dup
/*    */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*    */     //   5: pop
/*    */     //   6: astore_1
/*    */     //   7: iconst_0
/*    */     //   8: istore_2
/*    */     //   9: aload_1
/*    */     //   10: iload_2
/*    */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*    */     //   16: lookupswitch default -> 76, 0 -> 36
/*    */     //   36: aload_1
/*    */     //   37: checkcast net/minecraft/world/entity/vehicle/minecart/AbstractMinecart
/*    */     //   40: astore_3
/*    */     //   41: aload_3
/*    */     //   42: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*    */     //   45: invokestatic useExperimentalMovement : (Lnet/minecraft/world/level/Level;)Z
/*    */     //   48: ifeq -> 63
/*    */     //   51: new net/minecraft/world/phys/shapes/MinecartCollisionContext
/*    */     //   54: dup
/*    */     //   55: aload_3
/*    */     //   56: iconst_0
/*    */     //   57: invokespecial <init> : (Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;Z)V
/*    */     //   60: goto -> 86
/*    */     //   63: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   66: dup
/*    */     //   67: aload_0
/*    */     //   68: iconst_0
/*    */     //   69: iconst_0
/*    */     //   70: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   73: goto -> 86
/*    */     //   76: new net/minecraft/world/phys/shapes/EntityCollisionContext
/*    */     //   79: dup
/*    */     //   80: aload_0
/*    */     //   81: iconst_0
/*    */     //   82: iconst_0
/*    */     //   83: invokespecial <init> : (Lnet/minecraft/world/entity/Entity;ZZ)V
/*    */     //   86: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     //   #27	-> 36
/*    */     //   #28	-> 41
/*    */     //   #29	-> 51
/*    */     //   #31	-> 63
/*    */     //   #33	-> 76
/*    */     //   #26	-> 86
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   41	35	3	minecart	Lnet/minecraft/world/entity/vehicle/minecart/AbstractMinecart;
/*    */     //   0	87	0	entity	Lnet/minecraft/world/entity/Entity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext of(Entity entity, boolean alwaysCollideWithFluid) {
/* 39 */     return new EntityCollisionContext(entity, alwaysCollideWithFluid, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext placementContext(Player player) {
/* 47 */     Player player1 = player; return new EntityCollisionContext((player != null) ? player.isDescending() : false, true, (player != null) ? player.getY() : -1.7976931348623157E308D, (player instanceof LivingEntity) ? player1.getMainHandItem() : ItemStack.EMPTY, false, (Entity)player);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static CollisionContext withPosition(Entity entity, double position) {
/* 58 */     LivingEntity livingEntity = (LivingEntity)entity; return new EntityCollisionContext((entity != null) ? entity.isDescending() : false, true, (entity != null) ? position : -1.7976931348623157E308D, (entity instanceof LivingEntity) ? livingEntity.getMainHandItem() : ItemStack.EMPTY, false, entity);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isDescending();
/*    */ 
/*    */   
/*    */   boolean isAbove(VoxelShape paramVoxelShape, BlockPos paramBlockPos, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   boolean isHoldingItem(Item paramItem);
/*    */   
/*    */   boolean alwaysCollideWithFluid();
/*    */   
/*    */   boolean canStandOnFluid(FluidState paramFluidState1, FluidState paramFluidState2);
/*    */   
/*    */   VoxelShape getCollisionShape(BlockState paramBlockState, CollisionGetter paramCollisionGetter, BlockPos paramBlockPos);
/*    */   
/*    */   default boolean isPlacement() {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/CollisionContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */