/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PaintingVariantTags
/*    */ {
/* 11 */   public static final TagKey<PaintingVariant> PLACEABLE = create("placeable");
/*    */   
/*    */   private static TagKey<PaintingVariant> create(String name) {
/* 14 */     return TagKey.create(Registries.PAINTING_VARIANT, Identifier.withDefaultNamespace(name));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/PaintingVariantTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */