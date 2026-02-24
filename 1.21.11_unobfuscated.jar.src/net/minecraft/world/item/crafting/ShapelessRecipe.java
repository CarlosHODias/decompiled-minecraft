/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapelessRecipe implements CraftingRecipe {
/*     */   private final String group;
/*     */   private final CraftingBookCategory category;
/*     */   private final ItemStack result;
/*     */   private final List<Ingredient> ingredients;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public ShapelessRecipe(String group, CraftingBookCategory category, ItemStack result, List<Ingredient> ingredients) {
/*  29 */     this.group = group;
/*  30 */     this.category = category;
/*  31 */     this.result = result;
/*  32 */     this.ingredients = ingredients;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<ShapelessRecipe> getSerializer() {
/*  37 */     return RecipeSerializer.SHAPELESS_RECIPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String group() {
/*  42 */     return this.group;
/*     */   }
/*     */ 
/*     */   
/*     */   public CraftingBookCategory category() {
/*  47 */     return this.category;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  52 */     if (this.placementInfo == null) {
/*  53 */       this.placementInfo = PlacementInfo.create(this.ingredients);
/*     */     }
/*  55 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  60 */     if (input.ingredientCount() != this.ingredients.size()) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (input.size() == 1 && this.ingredients.size() == 1) {
/*  64 */       return ((Ingredient)this.ingredients.getFirst()).test(input.getItem(0));
/*     */     }
/*  66 */     return input.stackedContents().canCraft(this, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  71 */     return this.result.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  76 */     return (List)List.of(new ShapelessCraftingRecipeDisplay(
/*  77 */           this.ingredients.stream().map(Ingredient::display).toList(), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer implements RecipeSerializer<ShapelessRecipe> {
/*     */     private static final MapCodec<ShapelessRecipe> CODEC;
/*     */     
/*     */     static {
/*  84 */       CODEC = RecordCodecBuilder.mapCodec(r -> r.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(()), (App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(()), (App)Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(())).apply((Applicative)r, ShapelessRecipe::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  91 */       STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, r -> r.group, CraftingBookCategory.STREAM_CODEC, r -> r.category, ItemStack.STREAM_CODEC, r -> r.result, 
/*     */ 
/*     */ 
/*     */           
/*  95 */           Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.ingredients, ShapelessRecipe::new);
/*     */     }
/*     */     
/*     */     public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC;
/*     */     
/*     */     public MapCodec<ShapelessRecipe> codec() {
/* 101 */       return CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> streamCodec() {
/* 106 */       return STREAM_CODEC;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/ShapelessRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */