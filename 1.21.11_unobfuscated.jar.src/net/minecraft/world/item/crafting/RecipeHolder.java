/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class RecipeHolder<T extends Recipe<?>> extends Record {
/*    */   private final ResourceKey<Recipe<?>> id;
/*    */   
/*  9 */   public RecipeHolder(ResourceKey<Recipe<?>> id, T value) { this.id = id; this.value = value; } private final T value; public ResourceKey<Recipe<?>> id() { return this.id; } public T value() { return this.value; }
/* 10 */    public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, RecipeHolder<?>> STREAM_CODEC = StreamCodec.composite(
/* 11 */       ResourceKey.streamCodec(net.minecraft.core.registries.Registries.RECIPE), RecipeHolder::id, Recipe.STREAM_CODEC, RecipeHolder::value, RecipeHolder::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     if (this == obj) {
/* 19 */       return true;
/*    */     }
/* 21 */     if (obj instanceof RecipeHolder) { RecipeHolder<?> holder = (RecipeHolder)obj; if (this.id == holder.id); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 26 */     return this.id.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     return this.id.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/RecipeHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */