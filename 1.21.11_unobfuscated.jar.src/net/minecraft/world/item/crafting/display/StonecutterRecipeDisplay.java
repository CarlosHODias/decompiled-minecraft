/*    */ package net.minecraft.world.item.crafting.display;
/*    */ public final class StonecutterRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay input;
/*    */   private final SlotDisplay result;
/*    */   private final SlotDisplay craftingStation;
/*    */   public static final com.mojang.serialization.MapCodec<StonecutterRecipeDisplay> MAP_CODEC;
/*    */   
/*  8 */   public StonecutterRecipeDisplay(SlotDisplay input, SlotDisplay result, SlotDisplay craftingStation) { this.input = input; this.result = result; this.craftingStation = craftingStation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay; } public SlotDisplay input() { return this.input; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("input").forGetter(StonecutterRecipeDisplay::input), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("result").forGetter(StonecutterRecipeDisplay::result), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(StonecutterRecipeDisplay::craftingStation)).apply((com.mojang.datafixers.kinds.Applicative)i, StonecutterRecipeDisplay::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StonecutterRecipeDisplay> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::input, SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::result, SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::craftingStation, StonecutterRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final RecipeDisplay.Type<StonecutterRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */   
/*    */   public RecipeDisplay.Type<StonecutterRecipeDisplay> type() {
/* 31 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/StonecutterRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */