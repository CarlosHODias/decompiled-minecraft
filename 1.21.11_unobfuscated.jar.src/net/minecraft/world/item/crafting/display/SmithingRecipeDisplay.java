/*    */ package net.minecraft.world.item.crafting.display;public final class SmithingRecipeDisplay extends Record implements RecipeDisplay { private final SlotDisplay template;
/*    */   private final SlotDisplay base;
/*    */   private final SlotDisplay addition;
/*    */   private final SlotDisplay result;
/*    */   private final SlotDisplay craftingStation;
/*    */   public static final com.mojang.serialization.MapCodec<SmithingRecipeDisplay> MAP_CODEC;
/*    */   
/*  8 */   public SmithingRecipeDisplay(SlotDisplay template, SlotDisplay base, SlotDisplay addition, SlotDisplay result, SlotDisplay craftingStation) { this.template = template; this.base = base; this.addition = addition; this.result = result; this.craftingStation = craftingStation; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay; } public SlotDisplay template() { return this.template; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay base() { return this.base; } public SlotDisplay addition() { return this.addition; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("template").forGetter(SmithingRecipeDisplay::template), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("base").forGetter(SmithingRecipeDisplay::base), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("addition").forGetter(SmithingRecipeDisplay::addition), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("result").forGetter(SmithingRecipeDisplay::result), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(SmithingRecipeDisplay::craftingStation)).apply((com.mojang.datafixers.kinds.Applicative)i, SmithingRecipeDisplay::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SmithingRecipeDisplay> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::template, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::base, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::addition, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::craftingStation, SmithingRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static final RecipeDisplay.Type<SmithingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */   
/*    */   public RecipeDisplay.Type<SmithingRecipeDisplay> type() {
/* 38 */     return TYPE;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/SmithingRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */