/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ShapelessCraftingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final java.util.List<SlotDisplay> ingredients;
/*    */   private final SlotDisplay result;
/*    */   private final SlotDisplay craftingStation;
/*    */   public static final com.mojang.serialization.MapCodec<ShapelessCraftingRecipeDisplay> MAP_CODEC;
/*    */   
/* 12 */   public ShapelessCraftingRecipeDisplay(java.util.List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) { this.ingredients = ingredients; this.result = result; this.craftingStation = craftingStation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay; } public java.util.List<SlotDisplay> ingredients() { return this.ingredients; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ShapelessCraftingRecipeDisplay::ingredients), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(ShapelessCraftingRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ShapelessCraftingRecipeDisplay::craftingStation)).apply((com.mojang.datafixers.kinds.Applicative)i, ShapelessCraftingRecipeDisplay::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ShapelessCraftingRecipeDisplay> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 24 */       SlotDisplay.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), ShapelessCraftingRecipeDisplay::ingredients, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::craftingStation, ShapelessCraftingRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static final RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */   
/*    */   public RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> type() {
/* 34 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled(net.minecraft.world.flag.FeatureFlagSet enabledFeatures) {
/* 39 */     return (this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && super.isEnabled(enabledFeatures));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */