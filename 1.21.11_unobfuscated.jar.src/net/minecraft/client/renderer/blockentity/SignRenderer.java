/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class SignRenderer
/*     */   extends AbstractSignRenderer
/*     */ {
/*     */   public static final float RENDER_SCALE = 0.6666667F;
/*  32 */   private static final Vec3 TEXT_OFFSET = new Vec3(0.0D, 0.3333333432674408D, 0.046666666865348816D);
/*     */   
/*     */   private final Map<WoodType, Models> signModels;
/*     */   
/*     */   public SignRenderer(BlockEntityRendererProvider.Context context) {
/*  37 */     super(context);
/*  38 */     this.signModels = (Map<WoodType, Models>)WoodType.values().collect(ImmutableMap.toImmutableMap(type -> type, type -> new Models(createSignModel(context.entityModelSet(), type, true), createSignModel(context.entityModelSet(), type, false))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Model.Simple getSignModel(BlockState blockState, WoodType type) {
/*  49 */     Models models = this.signModels.get(type);
/*  50 */     return (blockState.getBlock() instanceof net.minecraft.world.level.block.StandingSignBlock) ? models.standing() : models.wall();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Material getSignMaterial(WoodType type) {
/*  55 */     return Sheets.getSignMaterial(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSignModelRenderScale() {
/*  60 */     return 0.6666667F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSignTextRenderScale() {
/*  65 */     return 0.6666667F;
/*     */   }
/*     */   
/*     */   private static void translateBase(PoseStack poseStack, float angle) {
/*  69 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/*  70 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void translateSign(PoseStack poseStack, float angle, BlockState blockState) {
/*  75 */     translateBase(poseStack, angle);
/*     */     
/*  77 */     if (!(blockState.getBlock() instanceof net.minecraft.world.level.block.StandingSignBlock)) {
/*  78 */       poseStack.translate(0.0F, -0.3125F, -0.4375F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTextOffset() {
/*  84 */     return TEXT_OFFSET;
/*     */   }
/*     */   
/*     */   public static void submitSpecial(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model.Simple model, Material material) {
/*  88 */     poseStack.pushPose();
/*  89 */     applyInHandTransforms(poseStack);
/*  90 */     Objects.requireNonNull(model); submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, material.renderType(model::renderType), lightCoords, overlayCoords, -1, materials.get(material), 0, null);
/*  91 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static void applyInHandTransforms(PoseStack poseStack) {
/*  95 */     translateBase(poseStack, 0.0F);
/*  96 */     poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
/*     */   }
/*     */   
/*     */   public static Model.Simple createSignModel(EntityModelSet entityModelSet, WoodType woodType, boolean standing) {
/* 100 */     ModelLayerLocation layer = standing ? ModelLayers.createStandingSignModelName(woodType) : ModelLayers.createWallSignModelName(woodType);
/* 101 */     return new Model.Simple(entityModelSet.bakeLayer(layer), RenderTypes::entityCutoutNoCull);
/*     */   }
/*     */   
/*     */   public static LayerDefinition createSignLayer(boolean standing) {
/* 105 */     MeshDefinition mesh = new MeshDefinition();
/* 106 */     PartDefinition root = mesh.getRoot();
/*     */     
/* 108 */     root.addOrReplaceChild("sign", 
/* 109 */         CubeListBuilder.create()
/* 110 */         .texOffs(0, 0).addBox(-12.0F, -14.0F, -1.0F, 24.0F, 12.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */ 
/*     */     
/* 114 */     if (standing) {
/* 115 */       root.addOrReplaceChild("stick", 
/* 116 */           CubeListBuilder.create()
/* 117 */           .texOffs(0, 14).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F), PartPose.ZERO);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   private static final class Models extends Record { private final Model.Simple standing; private final Model.Simple wall;
/* 125 */     private Models(Model.Simple standing, Model.Simple wall) { this.standing = standing; this.wall = wall; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 125 */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models; } public Model.Simple standing() { return this.standing; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #125	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/SignRenderer$Models;
/* 125 */       //   0	8	1	o	Ljava/lang/Object; } public Model.Simple wall() { return this.wall; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/SignRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */