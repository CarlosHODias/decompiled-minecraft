/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class SingleItemRecipe
/*    */   implements Recipe<SingleRecipeInput> {
/*    */   private final Ingredient input;
/*    */   private final ItemStack result;
/*    */   
/*    */   public SingleItemRecipe(String group, Ingredient input, ItemStack result) {
/* 22 */     this.group = group;
/* 23 */     this.input = input;
/* 24 */     this.result = result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final String group;
/*    */   
/*    */   private PlacementInfo placementInfo;
/*    */ 
/*    */   
/*    */   public boolean matches(SingleRecipeInput input, Level level) {
/* 35 */     return this.input.test(input.item());
/*    */   }
/*    */ 
/*    */   
/*    */   public String group() {
/* 40 */     return this.group;
/*    */   }
/*    */   
/*    */   public Ingredient input() {
/* 44 */     return this.input;
/*    */   }
/*    */   public abstract RecipeSerializer<? extends SingleItemRecipe> getSerializer();
/*    */   protected ItemStack result() {
/* 48 */     return this.result;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 53 */     if (this.placementInfo == null) {
/* 54 */       this.placementInfo = PlacementInfo.create(this.input);
/*    */     }
/* 56 */     return this.placementInfo;
/*    */   }
/*    */   public abstract RecipeType<? extends SingleItemRecipe> getType();
/*    */   
/*    */   public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
/* 61 */     return this.result.copy();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends SingleItemRecipe> {
/*    */     T create(String param1String, Ingredient param1Ingredient, ItemStack param1ItemStack); }
/*    */   
/*    */   public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> { protected Serializer(SingleItemRecipe.Factory<T> factory) {
/* 69 */       this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */             Objects.requireNonNull(factory);
/*    */ 
/*    */ 
/*    */             
/*    */             return r.group((App)Codec.STRING.optionalFieldOf("group", "").forGetter(SingleItemRecipe::group), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SingleItemRecipe::result)).apply((Applicative)r, factory::create);
/*    */           });
/*    */ 
/*    */ 
/*    */       
/* 79 */       Objects.requireNonNull(factory); this.streamCodec = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SingleItemRecipe::group, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStack.STREAM_CODEC, SingleItemRecipe::result, factory::create);
/*    */     }
/*    */     private final MapCodec<T> codec;
/*    */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */     
/*    */     public MapCodec<T> codec() {
/* 85 */       return this.codec;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 90 */       return this.streamCodec;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/SingleItemRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */