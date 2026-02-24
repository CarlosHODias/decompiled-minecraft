/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.network.HashedPatchMap;
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.world.item.ItemStack;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface RemoteSlot
/*    */ {
/* 24 */   public static final RemoteSlot PLACEHOLDER = new RemoteSlot()
/*    */     {
/*    */       public void receive(HashedStack incoming) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public void force(ItemStack outgoing) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean matches(ItemStack local) {
/* 35 */         return true;
/*    */       }
/*    */     };
/*    */   void force(ItemStack paramItemStack);
/*    */   void receive(HashedStack paramHashedStack);
/*    */   boolean matches(ItemStack paramItemStack);
/* 41 */   public static class Synchronized implements RemoteSlot { private ItemStack remoteStack = null; private final HashedPatchMap.HashGenerator hasher;
/* 42 */     private HashedStack remoteHash = null;
/*    */     
/*    */     public Synchronized(HashedPatchMap.HashGenerator hasher) {
/* 45 */       this.hasher = hasher;
/*    */     }
/*    */ 
/*    */     
/*    */     public void force(ItemStack outgoing) {
/* 50 */       this.remoteStack = outgoing.copy();
/* 51 */       this.remoteHash = null;
/*    */     }
/*    */ 
/*    */     
/*    */     public void receive(HashedStack incoming) {
/* 56 */       this.remoteStack = null;
/* 57 */       this.remoteHash = incoming;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches(ItemStack local) {
/* 62 */       if (this.remoteStack != null) {
/* 63 */         return ItemStack.matches(this.remoteStack, local);
/*    */       }
/*    */       
/* 66 */       if (this.remoteHash != null && 
/* 67 */         this.remoteHash.matches(local, this.hasher)) {
/*    */         
/* 69 */         this.remoteStack = local.copy();
/* 70 */         return true;
/*    */       } 
/*    */ 
/*    */       
/* 74 */       return false;
/*    */     }
/*    */     
/*    */     public void copyFrom(Synchronized other) {
/* 78 */       this.remoteStack = other.remoteStack;
/* 79 */       this.remoteHash = other.remoteHash;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/RemoteSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */