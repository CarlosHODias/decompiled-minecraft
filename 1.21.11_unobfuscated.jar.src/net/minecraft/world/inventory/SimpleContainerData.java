/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public class SimpleContainerData implements ContainerData {
/*    */   private final int[] ints;
/*    */   
/*    */   public SimpleContainerData(int count) {
/*  7 */     this.ints = new int[count];
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int dataId) {
/* 12 */     return this.ints[dataId];
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int dataId, int value) {
/* 17 */     this.ints[dataId] = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCount() {
/* 22 */     return this.ints.length;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/SimpleContainerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */