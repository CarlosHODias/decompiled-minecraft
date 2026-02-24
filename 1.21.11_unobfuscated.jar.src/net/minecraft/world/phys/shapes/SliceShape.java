/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class SliceShape extends VoxelShape {
/*    */   private final VoxelShape delegate;
/*    */   private final Direction.Axis axis;
/*  9 */   private static final DoubleList SLICE_COORDS = (DoubleList)new CubePointRange(1);
/*    */   
/*    */   public SliceShape(VoxelShape delegate, Direction.Axis axis, int point) {
/* 12 */     super(makeSlice(delegate.shape, axis, point));
/* 13 */     this.delegate = delegate;
/* 14 */     this.axis = axis;
/*    */   }
/*    */   
/*    */   private static DiscreteVoxelShape makeSlice(DiscreteVoxelShape delegate, Direction.Axis axis, int point) {
/* 18 */     return new SubShape(delegate, 
/* 19 */         axis.choose(point, 0, 0), 
/* 20 */         axis.choose(0, point, 0), 
/* 21 */         axis.choose(0, 0, point), 
/* 22 */         axis.choose(point + 1, delegate.xSize, delegate.xSize), 
/* 23 */         axis.choose(delegate.ySize, point + 1, delegate.ySize), 
/* 24 */         axis.choose(delegate.zSize, delegate.zSize, point + 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 30 */     if (axis == this.axis) {
/* 31 */       return SLICE_COORDS;
/*    */     }
/* 33 */     return this.delegate.getCoords(axis);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/SliceShape.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */