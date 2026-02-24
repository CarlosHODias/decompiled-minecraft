/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DoubleBlockHalf implements StringRepresentable {
/*  7 */   UPPER(Direction.DOWN),
/*  8 */   LOWER(Direction.UP);
/*    */   
/*    */   private final Direction directionToOther;
/*    */ 
/*    */   
/*    */   DoubleBlockHalf(Direction directionToOther) {
/* 14 */     this.directionToOther = directionToOther;
/*    */   }
/*    */   
/*    */   public Direction getDirectionToOther() {
/* 18 */     return this.directionToOther;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 28 */     return (this == UPPER) ? "upper" : "lower";
/*    */   }
/*    */   
/*    */   public DoubleBlockHalf getOtherHalf() {
/* 32 */     return (this == UPPER) ? LOWER : UPPER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/DoubleBlockHalf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */