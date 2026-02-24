/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.model.object.chest.ChestModel;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.Material;
/*     */ import net.minecraft.client.resources.model.MaterialSet;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.CopperChestBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChestRenderer<T extends BlockEntity & LidBlockEntity>
/*     */   implements BlockEntityRenderer<T, ChestRenderState>
/*     */ {
/*     */   private final MaterialSet materials;
/*     */   private final ChestModel singleModel;
/*     */   private final ChestModel doubleLeftModel;
/*     */   private final ChestModel doubleRightModel;
/*     */   private final boolean xmasTextures;
/*     */   
/*     */   public ChestRenderer(BlockEntityRendererProvider.Context context) {
/*  45 */     this.materials = context.materials();
/*     */     
/*  47 */     this.xmasTextures = xmasTextures();
/*     */     
/*  49 */     this.singleModel = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
/*  50 */     this.doubleLeftModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
/*  51 */     this.doubleRightModel = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
/*     */   }
/*     */   
/*     */   public static boolean xmasTextures() {
/*  55 */     return SpecialDates.isExtendedChristmas();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChestRenderState createRenderState() {
/*  60 */     return new ChestRenderState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void extractRenderState(T blockEntity, ChestRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: aload_2
/*     */     //   3: fload_3
/*     */     //   4: aload #4
/*     */     //   6: aload #5
/*     */     //   8: invokespecial extractRenderState : (Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;FLnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V
/*     */     //   11: aload_1
/*     */     //   12: invokevirtual getLevel : ()Lnet/minecraft/world/level/Level;
/*     */     //   15: ifnull -> 22
/*     */     //   18: iconst_1
/*     */     //   19: goto -> 23
/*     */     //   22: iconst_0
/*     */     //   23: istore #6
/*     */     //   25: iload #6
/*     */     //   27: ifeq -> 37
/*     */     //   30: aload_1
/*     */     //   31: invokevirtual getBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   34: goto -> 55
/*     */     //   37: getstatic net/minecraft/world/level/block/Blocks.CHEST : Lnet/minecraft/world/level/block/Block;
/*     */     //   40: invokevirtual defaultBlockState : ()Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   43: getstatic net/minecraft/world/level/block/ChestBlock.FACING : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   46: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */     //   49: invokevirtual setValue : (Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
/*     */     //   52: checkcast net/minecraft/world/level/block/state/BlockState
/*     */     //   55: astore #7
/*     */     //   57: aload_2
/*     */     //   58: aload #7
/*     */     //   60: getstatic net/minecraft/world/level/block/ChestBlock.TYPE : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   63: invokevirtual hasProperty : (Lnet/minecraft/world/level/block/state/properties/Property;)Z
/*     */     //   66: ifeq -> 83
/*     */     //   69: aload #7
/*     */     //   71: getstatic net/minecraft/world/level/block/ChestBlock.TYPE : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   74: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   77: checkcast net/minecraft/world/level/block/state/properties/ChestType
/*     */     //   80: goto -> 86
/*     */     //   83: getstatic net/minecraft/world/level/block/state/properties/ChestType.SINGLE : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   86: putfield type : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   89: aload_2
/*     */     //   90: aload #7
/*     */     //   92: getstatic net/minecraft/world/level/block/ChestBlock.FACING : Lnet/minecraft/world/level/block/state/properties/EnumProperty;
/*     */     //   95: invokevirtual getValue : (Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;
/*     */     //   98: checkcast net/minecraft/core/Direction
/*     */     //   101: invokevirtual toYRot : ()F
/*     */     //   104: putfield angle : F
/*     */     //   107: aload_2
/*     */     //   108: aload_0
/*     */     //   109: aload_1
/*     */     //   110: aload_0
/*     */     //   111: getfield xmasTextures : Z
/*     */     //   114: invokevirtual getChestMaterial : (Lnet/minecraft/world/level/block/entity/BlockEntity;Z)Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;
/*     */     //   117: putfield material : Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState$ChestMaterialType;
/*     */     //   120: iload #6
/*     */     //   122: ifeq -> 168
/*     */     //   125: aload #7
/*     */     //   127: invokevirtual getBlock : ()Lnet/minecraft/world/level/block/Block;
/*     */     //   130: astore #10
/*     */     //   132: aload #10
/*     */     //   134: instanceof net/minecraft/world/level/block/ChestBlock
/*     */     //   137: ifeq -> 168
/*     */     //   140: aload #10
/*     */     //   142: checkcast net/minecraft/world/level/block/ChestBlock
/*     */     //   145: astore #9
/*     */     //   147: aload #9
/*     */     //   149: aload #7
/*     */     //   151: aload_1
/*     */     //   152: invokevirtual getLevel : ()Lnet/minecraft/world/level/Level;
/*     */     //   155: aload_1
/*     */     //   156: invokevirtual getBlockPos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   159: iconst_1
/*     */     //   160: invokevirtual combine : (Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   163: astore #8
/*     */     //   165: goto -> 175
/*     */     //   168: <illegal opcode> apply : ()Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   173: astore #8
/*     */     //   175: aload_2
/*     */     //   176: aload #8
/*     */     //   178: aload_1
/*     */     //   179: checkcast net/minecraft/world/level/block/entity/LidBlockEntity
/*     */     //   182: invokestatic opennessCombiner : (Lnet/minecraft/world/level/block/entity/LidBlockEntity;)Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;
/*     */     //   185: invokeinterface apply : (Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;)Ljava/lang/Object;
/*     */     //   190: checkcast it/unimi/dsi/fastutil/floats/Float2FloatFunction
/*     */     //   193: fload_3
/*     */     //   194: invokeinterface get : (F)F
/*     */     //   199: putfield open : F
/*     */     //   202: aload_2
/*     */     //   203: getfield type : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   206: getstatic net/minecraft/world/level/block/state/properties/ChestType.SINGLE : Lnet/minecraft/world/level/block/state/properties/ChestType;
/*     */     //   209: if_acmpeq -> 242
/*     */     //   212: aload_2
/*     */     //   213: aload #8
/*     */     //   215: new net/minecraft/client/renderer/blockentity/BrightnessCombiner
/*     */     //   218: dup
/*     */     //   219: invokespecial <init> : ()V
/*     */     //   222: invokeinterface apply : (Lnet/minecraft/world/level/block/DoubleBlockCombiner$Combiner;)Ljava/lang/Object;
/*     */     //   227: checkcast it/unimi/dsi/fastutil/ints/Int2IntFunction
/*     */     //   230: aload_2
/*     */     //   231: getfield lightCoords : I
/*     */     //   234: invokeinterface applyAsInt : (I)I
/*     */     //   239: putfield lightCoords : I
/*     */     //   242: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #65	-> 0
/*     */     //   #67	-> 11
/*     */     //   #68	-> 25
/*     */     //   #69	-> 57
/*     */     //   #70	-> 89
/*     */     //   #71	-> 107
/*     */     //   #74	-> 120
/*     */     //   #75	-> 147
/*     */     //   #77	-> 168
/*     */     //   #80	-> 175
/*     */     //   #82	-> 202
/*     */     //   #83	-> 212
/*     */     //   #85	-> 242
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   165	3	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     //   147	21	9	chestBlock	Lnet/minecraft/world/level/block/ChestBlock;
/*     */     //   0	243	0	this	Lnet/minecraft/client/renderer/blockentity/ChestRenderer;
/*     */     //   0	243	1	blockEntity	Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   0	243	2	state	Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;
/*     */     //   0	243	3	partialTicks	F
/*     */     //   0	243	4	cameraPosition	Lnet/minecraft/world/phys/Vec3;
/*     */     //   0	243	5	breakProgress	Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;
/*     */     //   25	218	6	hasLevel	Z
/*     */     //   57	186	7	blockState	Lnet/minecraft/world/level/block/state/BlockState;
/*     */     //   175	68	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   165	3	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult<+Lnet/minecraft/world/level/block/entity/ChestBlockEntity;>;
/*     */     //   0	243	0	this	Lnet/minecraft/client/renderer/blockentity/ChestRenderer<TT;>;
/*     */     //   0	243	1	blockEntity	TT;
/*     */     //   175	68	8	combineResult	Lnet/minecraft/world/level/block/DoubleBlockCombiner$NeighborCombineResult<+Lnet/minecraft/world/level/block/entity/ChestBlockEntity;>;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void submit(ChestRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  89 */     poseStack.pushPose();
/*     */     
/*  91 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/*  92 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-state.angle));
/*  93 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/*     */     
/*  95 */     float open = state.open;
/*     */     
/*  97 */     open = 1.0F - open;
/*  98 */     open = 1.0F - open * open * open;
/*     */     
/* 100 */     Material material = Sheets.chooseMaterial(state.material, state.type);
/*     */     
/* 102 */     RenderType renderType = material.renderType(RenderTypes::entityCutout);
/* 103 */     TextureAtlasSprite sprite = this.materials.get(material);
/*     */     
/* 105 */     if (state.type != ChestType.SINGLE) {
/* 106 */       if (state.type == ChestType.LEFT) {
/* 107 */         submitNodeCollector.submitModel((Model)this.doubleLeftModel, open, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
/*     */       } else {
/* 109 */         submitNodeCollector.submitModel((Model)this.doubleRightModel, open, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
/*     */       } 
/*     */     } else {
/* 112 */       submitNodeCollector.submitModel((Model)this.singleModel, open, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, sprite, 0, state.breakProgress);
/*     */     } 
/*     */     
/* 115 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private ChestRenderState.ChestMaterialType getChestMaterial(BlockEntity entity, boolean xmasTextures) {
/* 119 */     if (entity instanceof net.minecraft.world.level.block.entity.EnderChestBlockEntity)
/* 120 */       return ChestRenderState.ChestMaterialType.ENDER_CHEST; 
/* 121 */     if (xmasTextures)
/* 122 */       return ChestRenderState.ChestMaterialType.CHRISTMAS; 
/* 123 */     if (entity instanceof net.minecraft.world.level.block.entity.TrappedChestBlockEntity) {
/* 124 */       return ChestRenderState.ChestMaterialType.TRAPPED;
/*     */     }
/* 126 */     Block block = entity.getBlockState().getBlock(); if (block instanceof CopperChestBlock) { CopperChestBlock copperChestBlock = (CopperChestBlock)block;
/* 127 */       switch (copperChestBlock.getState()) { default: throw new MatchException(null, null);case UNAFFECTED: case EXPOSED: case WEATHERED: case OXIDIZED: break; }  return 
/*     */ 
/*     */ 
/*     */         
/* 131 */         ChestRenderState.ChestMaterialType.COPPER_OXIDIZED; }
/*     */ 
/*     */     
/* 134 */     return ChestRenderState.ChestMaterialType.REGULAR;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/ChestRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */