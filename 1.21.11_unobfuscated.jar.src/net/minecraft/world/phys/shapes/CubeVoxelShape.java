/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class CubeVoxelShape extends VoxelShape {
/*    */   protected CubeVoxelShape(DiscreteVoxelShape shape) {
/*  9 */     super(shape);
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 14 */     return (DoubleList)new CubePointRange(this.shape.getSize(axis));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int findIndex(Direction.Axis axis, double coord) {
/* 19 */     int size = this.shape.getSize(axis);
/* 20 */     return Mth.floor(Mth.clamp(coord * size, -1.0D, size));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/CubeVoxelShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */