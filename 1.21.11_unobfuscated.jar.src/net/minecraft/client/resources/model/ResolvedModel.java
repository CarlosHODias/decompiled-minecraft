/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.block.model.ItemTransform;
/*     */ import net.minecraft.client.renderer.block.model.ItemTransforms;
/*     */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ 
/*     */ public interface ResolvedModel
/*     */   extends ModelDebugName
/*     */ {
/*     */   public static final boolean DEFAULT_AMBIENT_OCCLUSION = true;
/*  13 */   public static final UnbakedModel.GuiLight DEFAULT_GUI_LIGHT = UnbakedModel.GuiLight.SIDE;
/*     */   
/*     */   UnbakedModel wrapped();
/*     */   
/*     */   ResolvedModel parent();
/*     */   
/*     */   static TextureSlots findTopTextureSlots(ResolvedModel top) {
/*  20 */     ResolvedModel current = top;
/*  21 */     TextureSlots.Resolver resolver = new TextureSlots.Resolver();
/*  22 */     while (current != null) {
/*  23 */       resolver.addLast(current.wrapped().textureSlots());
/*  24 */       current = current.parent();
/*     */     } 
/*     */     
/*  27 */     return resolver.resolve(top);
/*     */   }
/*     */   
/*     */   default TextureSlots getTopTextureSlots() {
/*  31 */     return findTopTextureSlots(this);
/*     */   }
/*     */   
/*     */   static boolean findTopAmbientOcclusion(ResolvedModel current) {
/*  35 */     while (current != null) {
/*  36 */       Boolean hasAmbientOcclusion = current.wrapped().ambientOcclusion();
/*  37 */       if (hasAmbientOcclusion != null) {
/*  38 */         return hasAmbientOcclusion;
/*     */       }
/*  40 */       current = current.parent();
/*     */     } 
/*  42 */     return true;
/*     */   }
/*     */   
/*     */   default boolean getTopAmbientOcclusion() {
/*  46 */     return findTopAmbientOcclusion(this);
/*     */   }
/*     */   
/*     */   static UnbakedModel.GuiLight findTopGuiLight(ResolvedModel current) {
/*  50 */     while (current != null) {
/*  51 */       UnbakedModel.GuiLight guiLight = current.wrapped().guiLight();
/*  52 */       if (guiLight != null) {
/*  53 */         return guiLight;
/*     */       }
/*  55 */       current = current.parent();
/*     */     } 
/*  57 */     return DEFAULT_GUI_LIGHT;
/*     */   }
/*     */   
/*     */   default UnbakedModel.GuiLight getTopGuiLight() {
/*  61 */     return findTopGuiLight(this);
/*     */   }
/*     */   
/*     */   static UnbakedGeometry findTopGeometry(ResolvedModel current) {
/*  65 */     while (current != null) {
/*  66 */       UnbakedGeometry geometry = current.wrapped().geometry();
/*  67 */       if (geometry != null) {
/*  68 */         return geometry;
/*     */       }
/*  70 */       current = current.parent();
/*     */     } 
/*  72 */     return UnbakedGeometry.EMPTY;
/*     */   }
/*     */   
/*     */   default UnbakedGeometry getTopGeometry() {
/*  76 */     return findTopGeometry(this);
/*     */   }
/*     */   
/*     */   default QuadCollection bakeTopGeometry(TextureSlots textureSlots, ModelBaker baker, ModelState state) {
/*  80 */     return getTopGeometry().bake(textureSlots, baker, state, this);
/*     */   }
/*     */   
/*     */   static TextureAtlasSprite resolveParticleSprite(TextureSlots textureSlots, ModelBaker baker, ModelDebugName resolvedModel) {
/*  84 */     return baker.sprites().resolveSlot(textureSlots, "particle", resolvedModel);
/*     */   }
/*     */   
/*     */   default TextureAtlasSprite resolveParticleSprite(TextureSlots textureSlots, ModelBaker baker) {
/*  88 */     return resolveParticleSprite(textureSlots, baker, this);
/*     */   }
/*     */   
/*     */   static ItemTransform findTopTransform(ResolvedModel current, ItemDisplayContext type) {
/*  92 */     while (current != null) {
/*  93 */       ItemTransforms transforms = current.wrapped().transforms();
/*  94 */       if (transforms != null) {
/*  95 */         ItemTransform transform = transforms.getTransform(type);
/*  96 */         if (transform != ItemTransform.NO_TRANSFORM) {
/*  97 */           return transform;
/*     */         }
/*     */       } 
/* 100 */       current = current.parent();
/*     */     } 
/* 102 */     return ItemTransform.NO_TRANSFORM;
/*     */   }
/*     */   
/*     */   static ItemTransforms findTopTransforms(ResolvedModel top) {
/* 106 */     ItemTransform thirdPersonLeftHand = findTopTransform(top, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
/* 107 */     ItemTransform thirdPersonRightHand = findTopTransform(top, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
/* 108 */     ItemTransform firstPersonLeftHand = findTopTransform(top, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
/* 109 */     ItemTransform firstPersonRightHand = findTopTransform(top, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
/* 110 */     ItemTransform head = findTopTransform(top, ItemDisplayContext.HEAD);
/* 111 */     ItemTransform gui = findTopTransform(top, ItemDisplayContext.GUI);
/* 112 */     ItemTransform ground = findTopTransform(top, ItemDisplayContext.GROUND);
/* 113 */     ItemTransform fixed = findTopTransform(top, ItemDisplayContext.FIXED);
/* 114 */     ItemTransform fixedFromBottom = findTopTransform(top, ItemDisplayContext.ON_SHELF);
/* 115 */     return new ItemTransforms(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed, fixedFromBottom);
/*     */   }
/*     */   
/*     */   default ItemTransforms getTopTransforms() {
/* 119 */     return findTopTransforms(this);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ResolvedModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */