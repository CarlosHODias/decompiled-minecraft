/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SolidFaceRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer {
/*    */   public SolidFaceRenderer(Minecraft minecraft) {
/* 21 */     this.minecraft = minecraft;
/*    */   }
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 26 */     Level level = this.minecraft.player.level();
/*    */     
/* 28 */     BlockPos playerPos = BlockPos.containing(camX, camY, camZ);
/*    */     
/* 30 */     for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(playerPos.offset(-6, -6, -6), playerPos.offset(6, 6, 6))) {
/* 31 */       BlockState blockState = level.getBlockState(blockPos);
/*    */       
/* 33 */       if (blockState.is(Blocks.AIR)) {
/*    */         continue;
/*    */       }
/*    */       
/* 37 */       VoxelShape shape = blockState.getShape((BlockGetter)level, blockPos);
/* 38 */       for (AABB outlineBox : (Iterable<AABB>)shape.toAabbs()) {
/* 39 */         AABB aabb = outlineBox.move(blockPos).inflate(0.002D);
/*    */         
/* 41 */         int color = -2130771968;
/* 42 */         Vec3 min = aabb.getMinPosition();
/* 43 */         Vec3 max = aabb.getMaxPosition();
/* 44 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.WEST, min, max, -2130771968);
/* 45 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.SOUTH, min, max, -2130771968);
/* 46 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.EAST, min, max, -2130771968);
/* 47 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.NORTH, min, max, -2130771968);
/* 48 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.DOWN, min, max, -2130771968);
/* 49 */         addFaceIfSturdy(blockPos, blockState, (BlockGetter)level, Direction.UP, min, max, -2130771968);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void addFaceIfSturdy(BlockPos blockPos, BlockState blockState, BlockGetter level, Direction direction, Vec3 cornerA, Vec3 cornerB, int color) {
/* 55 */     if (blockState.isFaceSturdy(level, blockPos, direction))
/* 56 */       Gizmos.rect(cornerA, cornerB, direction, GizmoStyle.fill(color)); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/SolidFaceRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */