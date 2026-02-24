/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class OffsetDoubleList extends AbstractDoubleList {
/*    */   private final DoubleList delegate;
/*    */   private final double offset;
/*    */   
/*    */   public OffsetDoubleList(DoubleList delegate, double offset) {
/* 11 */     this.delegate = delegate;
/* 12 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int index) {
/* 17 */     return this.delegate.getDouble(index) + this.offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 22 */     return this.delegate.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/OffsetDoubleList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */