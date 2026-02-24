/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BedBlockEntity
/*    */   extends BlockEntity {
/*    */   public BedBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 13 */     this(worldPosition, blockState, ((BedBlock)blockState.getBlock()).getColor());
/*    */   }
/*    */   private final DyeColor color;
/*    */   public BedBlockEntity(BlockPos worldPosition, BlockState blockState, DyeColor color) {
/* 17 */     super(BlockEntityType.BED, worldPosition, blockState);
/* 18 */     this.color = color;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 23 */     return ClientboundBlockEntityDataPacket.create(this);
/*    */   }
/*    */   
/*    */   public DyeColor getColor() {
/* 27 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BedBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */