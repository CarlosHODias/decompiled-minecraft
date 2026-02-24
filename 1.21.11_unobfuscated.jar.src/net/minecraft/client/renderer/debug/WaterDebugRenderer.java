/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterDebugRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer {
/*    */   public WaterDebugRenderer(Minecraft minecraft) {
/* 22 */     this.minecraft = minecraft;
/*    */   }
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 27 */     BlockPos pos = this.minecraft.player.blockPosition();
/* 28 */     Level level = this.minecraft.player.level();
/*    */     
/* 30 */     for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(pos.offset(-10, -10, -10), pos.offset(10, 10, 10))) {
/* 31 */       FluidState fluidState = level.getFluidState(blockPos);
/* 32 */       if (fluidState.is(FluidTags.WATER)) {
/* 33 */         double height = (blockPos.getY() + fluidState.getHeight((BlockGetter)level, blockPos));
/*    */         
/* 35 */         Gizmos.cuboid(new AABB((
/* 36 */               blockPos.getX() + 0.01F), (
/* 37 */               blockPos.getY() + 0.01F), (
/* 38 */               blockPos.getZ() + 0.01F), (
/* 39 */               blockPos.getX() + 0.99F), height, (
/*    */               
/* 41 */               blockPos.getZ() + 0.99F)), 
/* 42 */             GizmoStyle.fill(ARGB.colorFromFloat(0.15F, 0.0F, 1.0F, 0.0F)));
/*    */       } 
/*    */     } 
/*    */     
/* 46 */     for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(pos.offset(-10, -10, -10), pos.offset(10, 10, 10))) {
/* 47 */       FluidState fluidState = level.getFluidState(blockPos);
/* 48 */       if (fluidState.is(FluidTags.WATER))
/* 49 */         Gizmos.billboardText(
/* 50 */             String.valueOf(fluidState.getAmount()), Vec3.atLowerCornerWithOffset((Vec3i)blockPos, 0.5D, 
/*    */               
/* 52 */               fluidState.getHeight((BlockGetter)level, blockPos), 0.5D), 
/*    */             
/* 54 */             TextGizmo.Style.forColorAndCentered(-16777216)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/WaterDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */