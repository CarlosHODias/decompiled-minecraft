/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.HeadedModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.object.skull.SkullModelBase;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ import net.minecraft.world.level.block.SkullBlock;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CustomHeadLayer<S extends LivingEntityRenderState, M extends EntityModel<S> & HeadedModel> extends RenderLayer<S, M> {
/*    */   private static final float ITEM_SCALE = 0.625F;
/*    */   private static final float SKULL_SCALE = 1.1875F;
/*    */   private final Transforms transforms;
/*    */   private final Function<SkullBlock.Type, SkullModelBase> skullModels;
/*    */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*    */   
/*    */   public CustomHeadLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, PlayerSkinRenderCache playerSkinRenderCache) {
/* 31 */     this(renderer, modelSet, playerSkinRenderCache, Transforms.DEFAULT);
/*    */   }
/*    */   
/*    */   public CustomHeadLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet, PlayerSkinRenderCache playerSkinRenderCache, Transforms transforms) {
/* 35 */     super(renderer);
/* 36 */     this.transforms = transforms;
/* 37 */     this.skullModels = Util.memoize(type -> SkullBlockRenderer.createModel(modelSet, type));
/* 38 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 43 */     if (((LivingEntityRenderState)state).headItem.isEmpty() && ((LivingEntityRenderState)state).wornHeadType == null) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     poseStack.pushPose();
/* 48 */     poseStack.scale(this.transforms.horizontalScale(), 1.0F, this.transforms.horizontalScale());
/*    */     
/* 50 */     M parentModel = getParentModel();
/* 51 */     parentModel.root().translateAndRotate(poseStack);
/* 52 */     ((HeadedModel)parentModel).translateToHead(poseStack);
/*    */     
/* 54 */     if (((LivingEntityRenderState)state).wornHeadType != null) {
/* 55 */       poseStack.translate(0.0F, this.transforms.skullYOffset(), 0.0F);
/* 56 */       poseStack.scale(1.1875F, -1.1875F, -1.1875F);
/*    */       
/* 58 */       poseStack.translate(-0.5D, 0.0D, -0.5D);
/* 59 */       SkullBlock.Type type = ((LivingEntityRenderState)state).wornHeadType;
/* 60 */       SkullModelBase skullModel = this.skullModels.apply(type);
/* 61 */       RenderType renderType = resolveSkullRenderType((LivingEntityRenderState)state, type);
/*    */       
/* 63 */       SkullBlockRenderer.submitSkull(null, 180.0F, ((LivingEntityRenderState)state).wornHeadAnimationPos, poseStack, submitNodeCollector, lightCoords, skullModel, renderType, ((LivingEntityRenderState)state).outlineColor, null);
/*    */     } else {
/* 65 */       translateToHead(poseStack, this.transforms);
/* 66 */       ((LivingEntityRenderState)state).headItem.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((LivingEntityRenderState)state).outlineColor);
/*    */     } 
/* 68 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private RenderType resolveSkullRenderType(LivingEntityRenderState state, SkullBlock.Type type) {
/* 72 */     if (type == SkullBlock.Types.PLAYER) {
/* 73 */       ResolvableProfile profile = state.wornHeadProfile;
/* 74 */       if (profile != null) {
/* 75 */         return this.playerSkinRenderCache.getOrDefault(profile).renderType();
/*    */       }
/*    */     } 
/*    */     
/* 79 */     return SkullBlockRenderer.getSkullRenderType(type, null);
/*    */   }
/*    */   
/*    */   public static void translateToHead(PoseStack poseStack, Transforms transforms) {
/* 83 */     poseStack.translate(0.0F, -0.25F + transforms.yOffset(), 0.0F);
/* 84 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 85 */     poseStack.scale(0.625F, -0.625F, -0.625F);
/*    */   }
/*    */   public static final class Transforms extends Record { private final float yOffset; private final float skullYOffset; private final float horizontalScale;
/* 88 */     public Transforms(float yOffset, float skullYOffset, float horizontalScale) { this.yOffset = yOffset; this.skullYOffset = skullYOffset; this.horizontalScale = horizontalScale; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 88 */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms; } public float yOffset() { return this.yOffset; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/CustomHeadLayer$Transforms;
/* 88 */       //   0	8	1	o	Ljava/lang/Object; } public float skullYOffset() { return this.skullYOffset; } public float horizontalScale() { return this.horizontalScale; }
/* 89 */      public static final Transforms DEFAULT = new Transforms(0.0F, 0.0F, 1.0F); }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/CustomHeadLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */