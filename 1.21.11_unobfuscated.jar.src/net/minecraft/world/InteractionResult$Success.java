/*    */ package net.minecraft.world;
/*    */ 
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
/*    */ public final class Success
/*    */   extends Record
/*    */   implements InteractionResult
/*    */ {
/*    */   private final InteractionResult.SwingSource swingSource;
/*    */   private final InteractionResult.ItemContext itemContext;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/InteractionResult$Success;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/InteractionResult$Success;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/InteractionResult$Success;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/InteractionResult$Success;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Success(InteractionResult.SwingSource swingSource, InteractionResult.ItemContext itemContext) {
/* 51 */     this.swingSource = swingSource; this.itemContext = itemContext; } public InteractionResult.SwingSource swingSource() { return this.swingSource; } public InteractionResult.ItemContext itemContext() { return this.itemContext; }
/*    */ 
/*    */   
/*    */   public boolean consumesAction() {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   public Success heldItemTransformedTo(ItemStack itemStack) {
/* 59 */     return new Success(this.swingSource, new InteractionResult.ItemContext(true, itemStack));
/*    */   }
/*    */   
/*    */   public Success withoutItem() {
/* 63 */     return new Success(this.swingSource, InteractionResult.ItemContext.NONE);
/*    */   }
/*    */   
/*    */   public boolean wasItemInteraction() {
/* 67 */     return this.itemContext.wasItemInteraction;
/*    */   }
/*    */   
/*    */   public ItemStack heldItemTransformedTo() {
/* 71 */     return this.itemContext.heldItemTransformedTo;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/InteractionResult$Success.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */