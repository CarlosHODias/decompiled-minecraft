/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
/*     */ 
/*     */ public class SmithingTransformRecipe implements SmithingRecipe {
/*     */   private final Optional<Ingredient> template;
/*     */   private final Ingredient base;
/*     */   private final Optional<Ingredient> addition;
/*     */   private final TransmuteResult result;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public SmithingTransformRecipe(Optional<Ingredient> template, Ingredient base, Optional<Ingredient> addition, TransmuteResult result) {
/*  27 */     this.template = template;
/*  28 */     this.base = base;
/*  29 */     this.addition = addition;
/*  30 */     this.result = result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
/*  35 */     return this.result.apply(input.base());
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Ingredient> templateIngredient() {
/*  40 */     return this.template;
/*     */   }
/*     */ 
/*     */   
/*     */   public Ingredient baseIngredient() {
/*  45 */     return this.base;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Ingredient> additionIngredient() {
/*  50 */     return this.addition;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<SmithingTransformRecipe> getSerializer() {
/*  55 */     return RecipeSerializer.SMITHING_TRANSFORM;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  60 */     if (this.placementInfo == null) {
/*  61 */       this.placementInfo = PlacementInfo.createFromOptionals(List.of(this.template, Optional.of(this.base), this.addition));
/*     */     }
/*  63 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  69 */     return (List)List.of(new SmithingRecipeDisplay(
/*     */           
/*  71 */           Ingredient.optionalIngredientToDisplay(this.template), 
/*  72 */           this.base.display(), 
/*  73 */           Ingredient.optionalIngredientToDisplay(this.addition), 
/*  74 */           this.result.display(), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer implements RecipeSerializer<SmithingTransformRecipe> {
/*     */     private static final MapCodec<SmithingTransformRecipe> CODEC;
/*     */     
/*     */     static {
/*  81 */       CODEC = RecordCodecBuilder.mapCodec(r -> r.group((App)Ingredient.CODEC.optionalFieldOf("template").forGetter(()), (App)Ingredient.CODEC.fieldOf("base").forGetter(()), (App)Ingredient.CODEC.optionalFieldOf("addition").forGetter(()), (App)TransmuteResult.CODEC.fieldOf("result").forGetter(())).apply((Applicative)r, SmithingTransformRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  88 */       STREAM_CODEC = StreamCodec.composite(Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> r.template, Ingredient.CONTENTS_STREAM_CODEC, r -> r.base, Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, r -> r.addition, TransmuteResult.STREAM_CODEC, r -> r.result, SmithingTransformRecipe::new);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> STREAM_CODEC;
/*     */ 
/*     */ 
/*     */     
/*     */     public MapCodec<SmithingTransformRecipe> codec() {
/*  98 */       return CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public StreamCodec<RegistryFriendlyByteBuf, SmithingTransformRecipe> streamCodec() {
/* 103 */       return STREAM_CODEC;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SmithingTransformRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */