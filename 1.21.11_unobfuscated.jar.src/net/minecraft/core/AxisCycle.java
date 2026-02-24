/*    */ package net.minecraft.core;
/*    */ 
/*    */ public enum AxisCycle {
/*  4 */   NONE
/*    */   {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/*  7 */       return axis.choose(x, y, z);
/*    */     }
/*    */ 
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 12 */       return axis.choose(x, y, z);
/*    */     }
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 17 */       return axis;
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 22 */       return this;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */   
/* 28 */   FORWARD
/*    */   {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 31 */       return axis.choose(z, x, y);
/*    */     }
/*    */ 
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 36 */       return axis.choose(z, x, y);
/*    */     }
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 41 */       return AXIS_VALUES[Math.floorMod(axis.ordinal() + 1, 3)];
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 46 */       return BACKWARD;
/*    */     }
/*    */   },
/* 49 */   BACKWARD
/*    */   {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 52 */       return axis.choose(y, z, x);
/*    */     }
/*    */ 
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 57 */       return axis.choose(y, z, x);
/*    */     }
/*    */ 
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 62 */       return AXIS_VALUES[Math.floorMod(axis.ordinal() - 1, 3)];
/*    */     }
/*    */ 
/*    */     
/*    */     public AxisCycle inverse() {
/* 67 */       return FORWARD;
/*    */     }
/*    */   };
/*    */ 
/*    */   
/* 72 */   public static final Direction.Axis[] AXIS_VALUES = Direction.Axis.values();
/* 73 */   public static final AxisCycle[] VALUES = values();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static AxisCycle between(Direction.Axis from, Direction.Axis to) {
/* 88 */     return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)];
/*    */   }
/*    */   
/*    */   public abstract int cycle(int paramInt1, int paramInt2, int paramInt3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract double cycle(double paramDouble1, double paramDouble2, double paramDouble3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract Direction.Axis cycle(Direction.Axis paramAxis);
/*    */   
/*    */   public abstract AxisCycle inverse();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/AxisCycle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */