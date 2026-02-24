/*    */ package net.minecraft.world.item.crafting;
/*    */ public final class SmithingRecipeInput extends Record implements RecipeInput { private final net.minecraft.world.item.ItemStack template;
/*    */   private final net.minecraft.world.item.ItemStack base;
/*    */   private final net.minecraft.world.item.ItemStack addition;
/*    */   
/*  6 */   public SmithingRecipeInput(net.minecraft.world.item.ItemStack template, net.minecraft.world.item.ItemStack base, net.minecraft.world.item.ItemStack addition) { this.template = template; this.base = base; this.addition = addition; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput; } public net.minecraft.world.item.ItemStack template() { return this.template; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SmithingRecipeInput;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.item.ItemStack base() { return this.base; } public net.minecraft.world.item.ItemStack addition() { return this.addition; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.world.item.ItemStack getItem(int index) {
/*    */     // Byte code:
/*    */     //   0: iload_1
/*    */     //   1: tableswitch default -> 49, 0 -> 28, 1 -> 35, 2 -> 42
/*    */     //   28: aload_0
/*    */     //   29: getfield template : Lnet/minecraft/world/item/ItemStack;
/*    */     //   32: goto -> 63
/*    */     //   35: aload_0
/*    */     //   36: getfield base : Lnet/minecraft/world/item/ItemStack;
/*    */     //   39: goto -> 63
/*    */     //   42: aload_0
/*    */     //   43: getfield addition : Lnet/minecraft/world/item/ItemStack;
/*    */     //   46: goto -> 63
/*    */     //   49: new java/lang/IllegalArgumentException
/*    */     //   52: dup
/*    */     //   53: iload_1
/*    */     //   54: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
/*    */     //   59: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   62: athrow
/*    */     //   63: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     //   #10	-> 28
/*    */     //   #11	-> 35
/*    */     //   #12	-> 42
/*    */     //   #13	-> 49
/*    */     //   #9	-> 63
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	64	0	this	Lnet/minecraft/world/item/crafting/SmithingRecipeInput;
/*    */     //   0	64	1	index	I
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 19 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 24 */     return (this.template.isEmpty() && this.base.isEmpty() && this.addition.isEmpty());
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SmithingRecipeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */