/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.Octree;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ 
/*    */ public class OctreeDebugRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public OctreeDebugRenderer(Minecraft minecraft) {
/* 22 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 27 */     Octree octree = this.minecraft.levelRenderer.getSectionOcclusionGraph().getOctree();
/* 28 */     MutableInt count = new MutableInt(0);
/* 29 */     octree.visitNodes((node, fullyVisible, depth, isClose) -> renderNode(count, depth, fullyVisible, count, isClose), frustum, 32);
/*    */   }
/*    */   
/*    */   private void renderNode(Octree.Node node, int depth, boolean fullyVisible, MutableInt count, boolean isClose) {
/* 33 */     AABB aabb = node.getAABB();
/* 34 */     double xSize = aabb.getXsize();
/* 35 */     long size = Math.round(xSize / 16.0D);
/* 36 */     if (size == 1L) {
/* 37 */       count.add(1);
/* 38 */       int color = isClose ? -16711936 : -1;
/* 39 */       Gizmos.billboardText(String.valueOf(count.intValue()), aabb.getCenter(), TextGizmo.Style.forColorAndCentered(color).withScale(4.8F));
/*    */     } 
/* 41 */     long colorNum = size + 5L;
/* 42 */     Gizmos.cuboid(aabb.deflate(0.1D * depth), GizmoStyle.stroke(ARGB.colorFromFloat(fullyVisible ? 0.4F : 1.0F, getColorComponent(colorNum, 0.3F), getColorComponent(colorNum, 0.8F), getColorComponent(colorNum, 0.5F))));
/*    */   }
/*    */   
/*    */   private static float getColorComponent(long size, float multiplier) {
/* 46 */     float minColor = 0.1F;
/* 47 */     return Mth.frac(multiplier * (float)size) * 0.9F + 0.1F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/OctreeDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */