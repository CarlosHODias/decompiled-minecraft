/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class CollisionBoxRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   private final Minecraft minecraft;
/* 20 */   private double lastUpdateTime = Double.MIN_VALUE;
/* 21 */   private List<VoxelShape> shapes = Collections.emptyList();
/*    */   
/*    */   public CollisionBoxRenderer(Minecraft minecraft) {
/* 24 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 29 */     double time = Util.getNanos();
/* 30 */     if (time - this.lastUpdateTime > 1.0E8D) {
/* 31 */       this.lastUpdateTime = time;
/* 32 */       Entity cameraEntity = this.minecraft.gameRenderer.getMainCamera().entity();
/* 33 */       this.shapes = (List<VoxelShape>)ImmutableList.copyOf(cameraEntity.level().getCollisions(cameraEntity, cameraEntity.getBoundingBox().inflate(6.0D)));
/*    */     } 
/*    */     
/* 36 */     for (VoxelShape shape : this.shapes) {
/* 37 */       GizmoStyle style = GizmoStyle.stroke(-1);
/* 38 */       for (AABB aabb : (Iterable<AABB>)shape.toAabbs())
/* 39 */         Gizmos.cuboid(aabb, style); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/CollisionBoxRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */