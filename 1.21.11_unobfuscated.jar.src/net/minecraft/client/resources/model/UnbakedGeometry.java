/*   */ package net.minecraft.client.resources.model;
/*   */ 
/*   */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface UnbakedGeometry {
/*   */   public static final UnbakedGeometry EMPTY = (textureSlots, modelBaker, modelState, name) -> QuadCollection.EMPTY;
/*   */   
/*   */   QuadCollection bake(TextureSlots paramTextureSlots, ModelBaker paramModelBaker, ModelState paramModelState, ModelDebugName paramModelDebugName);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/UnbakedGeometry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */