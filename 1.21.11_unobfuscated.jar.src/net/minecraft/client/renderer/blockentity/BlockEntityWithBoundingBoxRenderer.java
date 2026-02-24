/*     */ package net.minecraft.client.renderer.blockentity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityWithBoundingBoxRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.state.CameraRenderState;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoStyle;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BoundingBoxRenderable;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockEntityWithBoundingBoxRenderer<T extends BlockEntity & BoundingBoxRenderable>
/*     */   implements BlockEntityRenderer<T, BlockEntityWithBoundingBoxRenderState>
/*     */ {
/*  30 */   public static final int STRUCTURE_VOIDS_COLOR = ARGB.colorFromFloat(0.2F, 0.75F, 0.75F, 1.0F);
/*     */ 
/*     */   
/*     */   public BlockEntityWithBoundingBoxRenderState createRenderState() {
/*  34 */     return new BlockEntityWithBoundingBoxRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(T blockEntity, BlockEntityWithBoundingBoxRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/*  39 */     super.extractRenderState((BlockEntity)blockEntity, state, partialTicks, cameraPosition, breakProgress);
/*  40 */     extract(blockEntity, state);
/*     */   }
/*     */   
/*     */   public static <T extends BlockEntity & BoundingBoxRenderable> void extract(T blockEntity, BlockEntityWithBoundingBoxRenderState state) {
/*  44 */     LocalPlayer player = (Minecraft.getInstance()).player;
/*  45 */     state.isVisible = (player.canUseGameMasterBlocks() || player.isSpectator());
/*  46 */     state.box = ((BoundingBoxRenderable)blockEntity).getRenderableBox();
/*  47 */     state.mode = ((BoundingBoxRenderable)blockEntity).renderMode();
/*     */     
/*  49 */     BlockPos pos = state.box.localPos();
/*  50 */     Vec3i size = state.box.size();
/*  51 */     BlockPos entityPos = state.blockPos;
/*  52 */     BlockPos startingPos = entityPos.offset((Vec3i)pos);
/*  53 */     if (state.isVisible && blockEntity.getLevel() != null && state.mode == BoundingBoxRenderable.Mode.BOX_AND_INVISIBLE_BLOCKS) {
/*  54 */       state.invisibleBlocks = new BlockEntityWithBoundingBoxRenderState.InvisibleBlockType[size.getX() * size.getY() * size.getZ()];
/*  55 */       for (int x = 0; x < size.getX(); x++) {
/*  56 */         for (int y = 0; y < size.getY(); y++) {
/*  57 */           for (int z = 0; z < size.getZ(); z++) {
/*  58 */             int index = z * size.getX() * size.getY() + y * size.getX() + x;
/*  59 */             BlockState blockState = blockEntity.getLevel().getBlockState(startingPos.offset(x, y, z));
/*  60 */             if (blockState.isAir()) {
/*  61 */               state.invisibleBlocks[index] = BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.AIR;
/*  62 */             } else if (blockState.is(Blocks.STRUCTURE_VOID)) {
/*  63 */               state.invisibleBlocks[index] = BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.STRUCTURE_VOID;
/*  64 */             } else if (blockState.is(Blocks.BARRIER)) {
/*  65 */               state.invisibleBlocks[index] = BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.BARRIER;
/*  66 */             } else if (blockState.is(Blocks.LIGHT)) {
/*  67 */               state.invisibleBlocks[index] = BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.LIGHT;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } else {
/*  73 */       state.invisibleBlocks = null;
/*     */     } 
/*     */     
/*  76 */     if (state.isVisible);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  95 */     state.structureVoids = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void submit(BlockEntityWithBoundingBoxRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 101 */     if (!state.isVisible) {
/*     */       return;
/*     */     }
/* 104 */     BoundingBoxRenderable.Mode mode = state.mode;
/* 105 */     if (mode == BoundingBoxRenderable.Mode.NONE) {
/*     */       return;
/*     */     }
/* 108 */     BoundingBoxRenderable.RenderableBox box = state.box;
/* 109 */     BlockPos pos = box.localPos();
/* 110 */     Vec3i size = box.size();
/*     */     
/* 112 */     if (size.getX() < 1 || size.getY() < 1 || size.getZ() < 1) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     float lineAlpha = 1.0F;
/* 117 */     float lineRGB = 0.9F;
/*     */     
/* 119 */     BlockPos far = pos.offset(size);
/* 120 */     Gizmos.cuboid(new AABB(pos.getX(), pos.getY(), pos.getZ(), far.getX(), far.getY(), far.getZ()).move(state.blockPos), GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.9F, 0.9F, 0.9F)), true);
/*     */     
/* 122 */     renderInvisibleBlocks(state, pos, size);
/*     */   }
/*     */   
/*     */   private void renderInvisibleBlocks(BlockEntityWithBoundingBoxRenderState state, BlockPos localPos, Vec3i size) {
/* 126 */     if (state.invisibleBlocks == null) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     BlockPos entityPos = state.blockPos;
/* 131 */     BlockPos startingPos = entityPos.offset((Vec3i)localPos);
/*     */     
/* 133 */     for (int x = 0; x < size.getX(); x++) {
/* 134 */       for (int y = 0; y < size.getY(); y++) {
/* 135 */         for (int z = 0; z < size.getZ(); z++) {
/* 136 */           int index = z * size.getX() * size.getY() + y * size.getX() + x;
/* 137 */           BlockEntityWithBoundingBoxRenderState.InvisibleBlockType invisibleBlockType = state.invisibleBlocks[index];
/* 138 */           if (invisibleBlockType != null) {
/* 139 */             float scale = (invisibleBlockType == BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.AIR) ? 0.05F : 0.0F;
/*     */             
/* 141 */             double renderX0 = ((startingPos.getX() + x) + 0.45F - scale);
/* 142 */             double renderY0 = ((startingPos.getY() + y) + 0.45F - scale);
/* 143 */             double renderZ0 = ((startingPos.getZ() + z) + 0.45F - scale);
/* 144 */             double renderX1 = ((startingPos.getX() + x) + 0.55F + scale);
/* 145 */             double renderY1 = ((startingPos.getY() + y) + 0.55F + scale);
/* 146 */             double renderZ1 = ((startingPos.getZ() + z) + 0.55F + scale);
/*     */             
/* 148 */             AABB aabb = new AABB(renderX0, renderY0, renderZ0, renderX1, renderY1, renderZ1);
/* 149 */             if (invisibleBlockType == BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.AIR) {
/*     */               
/* 151 */               Gizmos.cuboid(aabb, GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 0.5F, 0.5F, 1.0F)));
/*     */             }
/* 153 */             else if (invisibleBlockType == BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.STRUCTURE_VOID) {
/*     */               
/* 155 */               Gizmos.cuboid(aabb, GizmoStyle.stroke(ARGB.colorFromFloat(1.0F, 1.0F, 0.75F, 0.75F)));
/*     */             }
/* 157 */             else if (invisibleBlockType == BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.BARRIER) {
/* 158 */               Gizmos.cuboid(aabb, GizmoStyle.stroke(-65536));
/* 159 */             } else if (invisibleBlockType == BlockEntityWithBoundingBoxRenderState.InvisibleBlockType.LIGHT) {
/* 160 */               Gizmos.cuboid(aabb, GizmoStyle.stroke(-256));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderStructureVoids(BlockEntityWithBoundingBoxRenderState state, BlockPos startingPosition, Vec3i size) {
/* 173 */     if (state.structureVoids == null) {
/*     */       return;
/*     */     }
/*     */     
/* 177 */     BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(size.getX(), size.getY(), size.getZ());
/*     */     
/* 179 */     for (int x = 0; x < size.getX(); x++) {
/* 180 */       for (int y = 0; y < size.getY(); y++) {
/* 181 */         for (int z = 0; z < size.getZ(); z++) {
/* 182 */           int index = z * size.getX() * size.getY() + y * size.getX() + x;
/* 183 */           if (state.structureVoids[index]) {
/* 184 */             bitSetDiscreteVoxelShape.fill(x, y, z);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     bitSetDiscreteVoxelShape.forAllFaces((direction, x, y, z) -> {
/*     */           float scale = 0.48F, x0 = (x + startingPosition.getX()) + 0.5F - 0.48F, y0 = (y + startingPosition.getY()) + 0.5F - 0.48F, z0 = (z + startingPosition.getZ()) + 0.5F - 0.48F, x1 = (x + startingPosition.getX()) + 0.5F + 0.48F, y1 = (y + startingPosition.getY()) + 0.5F + 0.48F, z1 = (z + startingPosition.getZ()) + 0.5F + 0.48F;
/*     */           Gizmos.rect(new Vec3(x0, y0, z0), new Vec3(x1, y1, z1), direction, GizmoStyle.fill(STRUCTURE_VOIDS_COLOR));
/*     */         });
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
/*     */   public boolean shouldRenderOffScreen() {
/* 209 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getViewDistance() {
/* 214 */     return 96;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BlockEntityWithBoundingBoxRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */