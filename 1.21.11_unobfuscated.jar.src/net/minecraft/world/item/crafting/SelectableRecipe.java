/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ public final class SelectableRecipe<T extends Recipe<?>> extends Record {
/*    */   private final net.minecraft.world.item.crafting.display.SlotDisplay optionDisplay;
/*    */   private final java.util.Optional<RecipeHolder<T>> recipe;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe<TT;>;
/*    */   }
/*    */   
/* 15 */   public SelectableRecipe(net.minecraft.world.item.crafting.display.SlotDisplay optionDisplay, java.util.Optional<RecipeHolder<T>> recipe) { this.optionDisplay = optionDisplay; this.recipe = recipe; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 15 */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe<TT;>; } public net.minecraft.world.item.crafting.display.SlotDisplay optionDisplay() { return this.optionDisplay; } public java.util.Optional<RecipeHolder<T>> recipe() { return this.recipe; }
/*    */    public static <T extends Recipe<?>> net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SelectableRecipe<T>> noRecipeCodec() {
/* 17 */     return net.minecraft.network.codec.StreamCodec.composite(net.minecraft.world.item.crafting.display.SlotDisplay.STREAM_CODEC, SelectableRecipe::optionDisplay, slotDisplay -> new SelectableRecipe(slotDisplay, java.util.Optional.empty()));
/*    */   }
/*    */   
/*    */   public static final class SingleInputEntry<T extends Recipe<?>> extends Record { private final Ingredient input;
/*    */     private final SelectableRecipe<T> recipe;
/*    */     
/* 23 */     public SingleInputEntry(Ingredient input, SelectableRecipe<T> recipe) { this.input = input; this.recipe = recipe; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #23	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 23 */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputEntry<TT;>; } public Ingredient input() { return this.input; } public SelectableRecipe<T> recipe() { return this.recipe; }
/*    */      public static <T extends Recipe<?>> net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SingleInputEntry<T>> noRecipeCodec() {
/* 25 */       return net.minecraft.network.codec.StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, SingleInputEntry::input, 
/*    */           
/* 27 */           SelectableRecipe.noRecipeCodec(), SingleInputEntry::recipe, SingleInputEntry::new);
/*    */     } }
/*    */ 
/*    */   
/*    */   public static final class SingleInputSet<T extends Recipe<?>> extends Record { private final java.util.List<SelectableRecipe.SingleInputEntry<T>> entries;
/*    */     
/* 33 */     public SingleInputSet(java.util.List<SelectableRecipe.SingleInputEntry<T>> entries) { this.entries = entries; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 33 */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/SelectableRecipe$SingleInputSet<TT;>; } public java.util.List<SelectableRecipe.SingleInputEntry<T>> entries() { return this.entries; }
/*    */      public static <T extends Recipe<?>> SingleInputSet<T> empty() {
/* 35 */       return new SingleInputSet<>(java.util.List.of());
/*    */     }
/*    */     
/*    */     public static <T extends Recipe<?>> net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SingleInputSet<T>> noRecipeCodec() {
/* 39 */       return net.minecraft.network.codec.StreamCodec.composite(
/* 40 */           SelectableRecipe.SingleInputEntry.<T>noRecipeCodec().apply(net.minecraft.network.codec.ByteBufCodecs.list()), SingleInputSet::entries, SingleInputSet::new);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean acceptsInput(net.minecraft.world.item.ItemStack input) {
/* 46 */       return this.entries.stream().anyMatch(e -> e.input.test(input));
/*    */     }
/*    */     
/*    */     public SingleInputSet<T> selectByInput(net.minecraft.world.item.ItemStack input) {
/* 50 */       return new SingleInputSet(this.entries.stream().filter(e -> e.input.test(input)).toList());
/*    */     }
/*    */     
/*    */     public boolean isEmpty() {
/* 54 */       return this.entries.isEmpty();
/*    */     }
/*    */     
/*    */     public int size() {
/* 58 */       return this.entries.size();
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SelectableRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */