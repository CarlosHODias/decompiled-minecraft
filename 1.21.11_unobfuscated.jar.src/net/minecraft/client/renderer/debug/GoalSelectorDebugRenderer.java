/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.debug.DebugGoalInfo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoalSelectorDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/*    */   private static final int MAX_RENDER_DIST = 160;
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public GoalSelectorDebugRenderer(Minecraft minecraft) {
/* 21 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 26 */     Camera camera = this.minecraft.gameRenderer.getMainCamera();
/*    */     
/* 28 */     BlockPos playerPos = BlockPos.containing((camera.position()).x, 0.0D, (camera.position()).z);
/*    */     
/* 30 */     debugValues.forEachEntity(DebugSubscriptions.GOAL_SELECTORS, (entity, goalInfo) -> {
/*    */           if (playerPos.closerThan((Vec3i)entity.blockPosition(), 160.0D))
/*    */             for (int i = 0; i < goalInfo.goals().size(); i++) {
/*    */               DebugGoalInfo.DebugGoal goal = goalInfo.goals().get(i);
/*    */               double x = entity.getBlockX() + 0.5D, y = entity.getY() + 2.0D + i * 0.25D, z = entity.getBlockZ() + 0.5D;
/*    */               int color = goal.isRunning() ? -16711936 : -3355444;
/*    */               Gizmos.billboardText(goal.name(), new Vec3(x, y, z), TextGizmo.Style.forColorAndCentered(color));
/*    */             }  
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/GoalSelectorDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */