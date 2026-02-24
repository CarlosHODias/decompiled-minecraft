/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RedstoneWireOrientationsRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 13 */     debugValues.forEachBlock(DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, (wirePos, orientation) -> {
/*    */           Vec3 center = wirePos.getBottomCenter().subtract(0.0D, 0.1D, 0.0D);
/*    */           Gizmos.arrow(center, center.add(orientation.getFront().getUnitVec3().scale(0.5D)), -16776961);
/*    */           Gizmos.arrow(center, center.add(orientation.getUp().getUnitVec3().scale(0.4D)), -65536);
/*    */           Gizmos.arrow(center, center.add(orientation.getSide().getUnitVec3().scale(0.3D)), -256);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/RedstoneWireOrientationsRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */