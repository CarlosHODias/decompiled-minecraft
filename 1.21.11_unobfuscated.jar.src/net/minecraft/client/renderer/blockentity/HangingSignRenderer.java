/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.geom.PartPose;
/*     */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*     */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*     */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*     */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WoodType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HangingSignRenderer
/*     */   extends AbstractSignRenderer
/*     */ {
/*     */   private static final String PLANK = "plank";
/*     */   private static final String V_CHAINS = "vChains";
/*     */   private static final String NORMAL_CHAINS = "normalChains";
/*     */   private static final String CHAIN_L_1 = "chainL1";
/*     */   private static final String CHAIN_L_2 = "chainL2";
/*     */   private static final String CHAIN_R_1 = "chainR1";
/*     */   private static final String CHAIN_R_2 = "chainR2";
/*     */   private static final String BOARD = "board";
/*     */   public static final float MODEL_RENDER_SCALE = 1.0F;
/*     */   private static final float TEXT_RENDER_SCALE = 0.9F;
/*  53 */   private static final Vec3 TEXT_OFFSET = new Vec3(0.0D, -0.3199999928474426D, 0.0729999989271164D);
/*     */   private final Map<ModelKey, Model.Simple> hangingSignModels;
/*     */   
/*     */   public HangingSignRenderer(BlockEntityRendererProvider.Context context) {
/*  57 */     super(context);
/*  58 */     Stream<ModelKey> modelKeys = WoodType.values()
/*  59 */       .flatMap(woodType -> Arrays.<AttachmentType>stream(AttachmentType.values()).map(()));
/*     */ 
/*     */     
/*  62 */     this.hangingSignModels = modelKeys.collect(ImmutableMap.toImmutableMap(type -> type, type -> createSignModel(context.entityModelSet(), type.woodType, type.attachmentType)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Model.Simple createSignModel(EntityModelSet entityModelSet, WoodType woodType, AttachmentType attachmentType) {
/*  69 */     return new Model.Simple(entityModelSet.bakeLayer(ModelLayers.createHangingSignModelName(woodType, attachmentType)), RenderTypes::entityCutoutNoCull);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSignModelRenderScale() {
/*  74 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSignTextRenderScale() {
/*  79 */     return 0.9F;
/*     */   }
/*     */   
/*     */   public static void translateBase(PoseStack poseStack, float angle) {
/*  83 */     poseStack.translate(0.5D, 0.9375D, 0.5D);
/*  84 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(angle));
/*  85 */     poseStack.translate(0.0F, -0.3125F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void translateSign(PoseStack poseStack, float angle, BlockState blockState) {
/*  90 */     translateBase(poseStack, angle);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Model.Simple getSignModel(BlockState blockState, WoodType type) {
/*  95 */     AttachmentType attachmentType = AttachmentType.byBlockState(blockState);
/*  96 */     return this.hangingSignModels.get(new ModelKey(type, attachmentType));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Material getSignMaterial(WoodType type) {
/* 101 */     return Sheets.getHangingSignMaterial(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTextOffset() {
/* 106 */     return TEXT_OFFSET;
/*     */   }
/*     */   
/*     */   public static void submitSpecial(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, Model.Simple model, Material material) {
/* 110 */     poseStack.pushPose();
/* 111 */     translateBase(poseStack, 0.0F);
/* 112 */     poseStack.scale(1.0F, -1.0F, -1.0F);
/* 113 */     Objects.requireNonNull(model); submitNodeCollector.submitModel((Model)model, Unit.INSTANCE, poseStack, material.renderType(model::renderType), lightCoords, overlayCoords, -1, materials.get(material), OverlayTexture.NO_OVERLAY, null);
/* 114 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static LayerDefinition createHangingSignLayer(AttachmentType type) {
/* 118 */     MeshDefinition mesh = new MeshDefinition();
/* 119 */     PartDefinition root = mesh.getRoot();
/*     */     
/* 121 */     root.addOrReplaceChild("board", 
/* 122 */         CubeListBuilder.create()
/* 123 */         .texOffs(0, 12).addBox(-7.0F, 0.0F, -1.0F, 14.0F, 10.0F, 2.0F), PartPose.ZERO);
/*     */ 
/*     */     
/* 126 */     if (type == AttachmentType.WALL) {
/* 127 */       root.addOrReplaceChild("plank", 
/* 128 */           CubeListBuilder.create()
/* 129 */           .texOffs(0, 0).addBox(-8.0F, -6.0F, -2.0F, 16.0F, 2.0F, 4.0F), PartPose.ZERO);
/*     */     }
/*     */ 
/*     */     
/* 133 */     if (type == AttachmentType.WALL || type == AttachmentType.CEILING) {
/* 134 */       PartDefinition normalChains = root.addOrReplaceChild("normalChains", 
/* 135 */           CubeListBuilder.create(), PartPose.ZERO);
/*     */ 
/*     */       
/* 138 */       normalChains.addOrReplaceChild("chainL1", 
/* 139 */           CubeListBuilder.create()
/* 140 */           .texOffs(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), 
/* 141 */           PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
/*     */       
/* 143 */       normalChains.addOrReplaceChild("chainL2", 
/* 144 */           CubeListBuilder.create()
/* 145 */           .texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), 
/* 146 */           PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));
/*     */       
/* 148 */       normalChains.addOrReplaceChild("chainR1", 
/* 149 */           CubeListBuilder.create()
/* 150 */           .texOffs(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), 
/* 151 */           PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
/*     */       
/* 153 */       normalChains.addOrReplaceChild("chainR2", 
/* 154 */           CubeListBuilder.create()
/* 155 */           .texOffs(6, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F), 
/* 156 */           PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));
/*     */     } 
/*     */     
/* 159 */     if (type == AttachmentType.CEILING_MIDDLE) {
/* 160 */       root.addOrReplaceChild("vChains", 
/* 161 */           CubeListBuilder.create()
/* 162 */           .texOffs(14, 6).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 0.0F), PartPose.ZERO);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 167 */     return LayerDefinition.create(mesh, 64, 32);
/*     */   }
/*     */   
/*     */   public enum AttachmentType implements StringRepresentable {
/* 171 */     WALL("wall"),
/* 172 */     CEILING("ceiling"),
/* 173 */     CEILING_MIDDLE("ceiling_middle");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     AttachmentType(String name) {
/* 179 */       this.name = name;
/*     */     }
/*     */     
/*     */     public static AttachmentType byBlockState(BlockState blockState) {
/* 183 */       if (blockState.getBlock() instanceof net.minecraft.world.level.block.CeilingHangingSignBlock) {
/* 184 */         return (Boolean)blockState.getValue((Property)BlockStateProperties.ATTACHED) ? CEILING_MIDDLE : CEILING;
/*     */       }
/* 186 */       return WALL;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 191 */       return this.name;
/*     */     } }
/*     */   public static final class ModelKey extends Record { private final WoodType woodType; private final HangingSignRenderer.AttachmentType attachmentType;
/*     */     
/* 195 */     public ModelKey(WoodType woodType, HangingSignRenderer.AttachmentType attachmentType) { this.woodType = woodType; this.attachmentType = attachmentType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 195 */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey; } public WoodType woodType() { return this.woodType; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #195	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/blockentity/HangingSignRenderer$ModelKey;
/* 195 */       //   0	8	1	o	Ljava/lang/Object; } public HangingSignRenderer.AttachmentType attachmentType() { return this.attachmentType; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/HangingSignRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */