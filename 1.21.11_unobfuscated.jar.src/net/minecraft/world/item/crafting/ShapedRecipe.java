/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
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
/*     */ import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ShapedRecipe implements CraftingRecipe {
/*     */   private final ShapedRecipePattern pattern;
/*     */   private final ItemStack result;
/*     */   private final String group;
/*     */   private final CraftingBookCategory category;
/*     */   private final boolean showNotification;
/*     */   private PlacementInfo placementInfo;
/*     */   
/*     */   public ShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
/*  31 */     this.group = group;
/*  32 */     this.category = category;
/*  33 */     this.pattern = pattern;
/*  34 */     this.result = result;
/*  35 */     this.showNotification = showNotification;
/*     */   }
/*     */   
/*     */   public ShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
/*  39 */     this(group, category, pattern, result, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public RecipeSerializer<? extends ShapedRecipe> getSerializer() {
/*  44 */     return RecipeSerializer.SHAPED_RECIPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String group() {
/*  49 */     return this.group;
/*     */   }
/*     */ 
/*     */   
/*     */   public CraftingBookCategory category() {
/*  54 */     return this.category;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<Optional<Ingredient>> getIngredients() {
/*  59 */     return this.pattern.ingredients();
/*     */   }
/*     */ 
/*     */   
/*     */   public PlacementInfo placementInfo() {
/*  64 */     if (this.placementInfo == null) {
/*  65 */       this.placementInfo = PlacementInfo.createFromOptionals(this.pattern.ingredients());
/*     */     }
/*  67 */     return this.placementInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean showNotification() {
/*  72 */     return this.showNotification;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  77 */     return this.pattern.matches(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
/*  82 */     return this.result.copy();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  86 */     return this.pattern.width();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  90 */     return this.pattern.height();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  95 */     return (List)List.of(new ShapedCraftingRecipeDisplay(
/*  96 */           this.pattern.width(), 
/*  97 */           this.pattern.height(), 
/*  98 */           this.pattern.ingredients().stream().map(e -> (SlotDisplay)e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */   
/*     */   public static class Serializer implements RecipeSerializer<ShapedRecipe> {
/*     */     public static final MapCodec<ShapedRecipe> CODEC;
/*     */     
/*     */     static {
/* 105 */       CODEC = RecordCodecBuilder.mapCodec(r -> r.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(()), (App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(()), (App)ShapedRecipePattern.MAP_CODEC.forGetter(()), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(()), (App)Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(())).apply((Applicative)r, ShapedRecipe::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
/*     */ 
/*     */     
/*     */     public MapCodec<ShapedRecipe> codec() {
/* 117 */       return CODEC;
/*     */     }
/*     */ 
/*     */     
/*     */     public StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> streamCodec() {
/* 122 */       return STREAM_CODEC;
/*     */     }
/*     */     
/*     */     private static ShapedRecipe fromNetwork(RegistryFriendlyByteBuf input) {
/* 126 */       String group = input.readUtf();
/* 127 */       CraftingBookCategory category = (CraftingBookCategory)input.readEnum(CraftingBookCategory.class);
/* 128 */       ShapedRecipePattern pattern = (ShapedRecipePattern)ShapedRecipePattern.STREAM_CODEC.decode(input);
/* 129 */       ItemStack result = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 130 */       boolean showNotification = input.readBoolean();
/* 131 */       return new ShapedRecipe(group, category, pattern, result, showNotification);
/*     */     }
/*     */     
/*     */     private static void toNetwork(RegistryFriendlyByteBuf output, ShapedRecipe recipe) {
/* 135 */       output.writeUtf(recipe.group);
/* 136 */       output.writeEnum(recipe.category);
/* 137 */       ShapedRecipePattern.STREAM_CODEC.encode(output, recipe.pattern);
/* 138 */       ItemStack.STREAM_CODEC.encode(output, recipe.result);
/* 139 */       output.writeBoolean(recipe.showNotification);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/ShapedRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */