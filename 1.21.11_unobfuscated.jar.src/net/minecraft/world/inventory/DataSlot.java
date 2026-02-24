/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ public abstract class DataSlot {
/*    */   public static DataSlot forContainer(final ContainerData container, final int dataId) {
/*  5 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/*  8 */           return container.get(dataId);
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int value) {
/* 13 */           container.set(dataId, value);
/*    */         }
/*    */       };
/*    */   }
/*    */   private int prevValue;
/*    */   public static DataSlot shared(final int[] storage, final int index) {
/* 19 */     return new DataSlot()
/*    */       {
/*    */         public int get() {
/* 22 */           return storage[index];
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int value) {
/* 27 */           storage[index] = value;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static DataSlot standalone() {
/* 33 */     return new DataSlot()
/*    */       {
/*    */         private int value;
/*    */         
/*    */         public int get() {
/* 38 */           return this.value;
/*    */         }
/*    */ 
/*    */         
/*    */         public void set(int value) {
/* 43 */           this.value = value;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract int get();
/*    */ 
/*    */   
/*    */   public abstract void set(int paramInt);
/*    */   
/*    */   public boolean checkAndClearUpdateFlag() {
/* 55 */     int currentValue = get();
/* 56 */     boolean result = (currentValue != this.prevValue);
/* 57 */     this.prevValue = currentValue;
/* 58 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/DataSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */