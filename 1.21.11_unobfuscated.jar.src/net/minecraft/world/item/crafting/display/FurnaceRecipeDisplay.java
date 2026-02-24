/*    */ package net.minecraft.world.item.crafting.display;
/*    */ public final class FurnaceRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay ingredient;
/*    */   private final SlotDisplay fuel;
/*    */   private final SlotDisplay result;
/*    */   private final SlotDisplay craftingStation;
/*    */   private final int duration;
/*    */   private final float experience;
/*    */   public static final com.mojang.serialization.MapCodec<FurnaceRecipeDisplay> MAP_CODEC;
/*    */   
/* 11 */   public FurnaceRecipeDisplay(SlotDisplay ingredient, SlotDisplay fuel, SlotDisplay result, SlotDisplay craftingStation, int duration, float experience) { this.ingredient = ingredient; this.fuel = fuel; this.result = result; this.craftingStation = craftingStation; this.duration = duration; this.experience = experience; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay; } public SlotDisplay ingredient() { return this.ingredient; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public SlotDisplay fuel() { return this.fuel; } public SlotDisplay result() { return this.result; } public SlotDisplay craftingStation() { return this.craftingStation; } public int duration() { return this.duration; } public float experience() { return this.experience; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("ingredient").forGetter(FurnaceRecipeDisplay::ingredient), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("fuel").forGetter(FurnaceRecipeDisplay::fuel), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("result").forGetter(FurnaceRecipeDisplay::result), (com.mojang.datafixers.kinds.App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(FurnaceRecipeDisplay::craftingStation), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.INT.fieldOf("duration").forGetter(FurnaceRecipeDisplay::duration), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.FLOAT.fieldOf("experience").forGetter(FurnaceRecipeDisplay::experience)).apply((com.mojang.datafixers.kinds.Applicative)i, FurnaceRecipeDisplay::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FurnaceRecipeDisplay> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::ingredient, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::fuel, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::result, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::craftingStation, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, FurnaceRecipeDisplay::duration, net.minecraft.network.codec.ByteBufCodecs.FLOAT, FurnaceRecipeDisplay::experience, FurnaceRecipeDisplay::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static final RecipeDisplay.Type<FurnaceRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */ 
/*    */   
/*    */   public RecipeDisplay.Type<FurnaceRecipeDisplay> type() {
/* 42 */     return TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled(net.minecraft.world.flag.FeatureFlagSet enabledFeatures) {
/* 47 */     return (this.ingredient.isEnabled(enabledFeatures) && fuel().isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/display/FurnaceRecipeDisplay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */