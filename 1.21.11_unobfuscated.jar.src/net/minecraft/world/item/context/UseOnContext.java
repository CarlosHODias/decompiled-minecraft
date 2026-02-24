/*    */ package net.minecraft.world.item.context;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class UseOnContext
/*    */ {
/*    */   private final Player player;
/*    */   private final InteractionHand hand;
/*    */   private final BlockHitResult hitResult;
/*    */   private final Level level;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public UseOnContext(Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 22 */     this(player.level(), player, hand, player.getItemInHand(hand), hitResult);
/*    */   }
/*    */   
/*    */   protected UseOnContext(Level level, Player player, InteractionHand hand, ItemStack itemStack, BlockHitResult hitResult) {
/* 26 */     this.player = player;
/* 27 */     this.hand = hand;
/* 28 */     this.hitResult = hitResult;
/*    */     
/* 30 */     this.itemStack = itemStack;
/* 31 */     this.level = level;
/*    */   }
/*    */   
/*    */   protected final BlockHitResult getHitResult() {
/* 35 */     return this.hitResult;
/*    */   }
/*    */   
/*    */   public BlockPos getClickedPos() {
/* 39 */     return this.hitResult.getBlockPos();
/*    */   }
/*    */   
/*    */   public Direction getClickedFace() {
/* 43 */     return this.hitResult.getDirection();
/*    */   }
/*    */   
/*    */   public Vec3 getClickLocation() {
/* 47 */     return this.hitResult.getLocation();
/*    */   }
/*    */   
/*    */   public boolean isInside() {
/* 51 */     return this.hitResult.isInside();
/*    */   }
/*    */   
/*    */   public ItemStack getItemInHand() {
/* 55 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public Player getPlayer() {
/* 59 */     return this.player;
/*    */   }
/*    */   
/*    */   public InteractionHand getHand() {
/* 63 */     return this.hand;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 67 */     return this.level;
/*    */   }
/*    */   
/*    */   public Direction getHorizontalDirection() {
/* 71 */     return (this.player == null) ? Direction.NORTH : this.player.getDirection();
/*    */   }
/*    */   
/*    */   public boolean isSecondaryUseActive() {
/* 75 */     return (this.player != null && this.player.isSecondaryUseActive());
/*    */   }
/*    */   
/*    */   public float getRotation() {
/* 79 */     return (this.player == null) ? 0.0F : this.player.getYRot();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/context/UseOnContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */