/*    */ package net.minecraft.world.item.crafting;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public abstract class CustomRecipe implements CraftingRecipe {
/*    */   public CustomRecipe(CraftingBookCategory category) {
/* 12 */     this.category = category;
/*    */   }
/*    */   private final CraftingBookCategory category;
/*    */   
/*    */   public boolean isSpecial() {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public CraftingBookCategory category() {
/* 22 */     return this.category;
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementInfo placementInfo() {
/* 27 */     return PlacementInfo.NOT_PLACEABLE;
/*    */   }
/*    */   
/*    */   public abstract RecipeSerializer<? extends CustomRecipe> getSerializer();
/*    */   
/*    */   public static class Serializer<T extends CraftingRecipe>
/*    */     implements RecipeSerializer<T> {
/*    */     private final MapCodec<T> codec;
/*    */     private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;
/*    */     
/*    */     public Serializer(Factory<T> constructor) {
/* 38 */       this.codec = RecordCodecBuilder.mapCodec(r -> {
/*    */             Objects.requireNonNull(constructor);
/*    */             
/*    */             return r.group((App)CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category)).apply((Applicative)r, constructor::create);
/*    */           });
/*    */       
/* 44 */       Objects.requireNonNull(constructor); this.streamCodec = StreamCodec.composite(CraftingBookCategory.STREAM_CODEC, CraftingRecipe::category, constructor::create);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public MapCodec<T> codec() {
/* 50 */       return this.codec;
/*    */     }
/*    */ 
/*    */     
/*    */     public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
/* 55 */       return this.streamCodec;
/*    */     }
/*    */     
/*    */     @FunctionalInterface
/*    */     public static interface Factory<T extends CraftingRecipe> {
/*    */       T create(CraftingBookCategory param2CraftingBookCategory);
/*    */     }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends CraftingRecipe> {
/*    */     T create(CraftingBookCategory param1CraftingBookCategory);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/CustomRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */