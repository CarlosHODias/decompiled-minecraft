/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.DoubleSupplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SupportBlockRenderer implements DebugRenderer.SimpleDebugRenderer {
/* 25 */   private double lastUpdateTime = Double.MIN_VALUE; private final Minecraft minecraft;
/* 26 */   private List<Entity> surroundEntities = Collections.emptyList();
/*    */   
/*    */   public SupportBlockRenderer(Minecraft minecraft) {
/* 29 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 34 */     double time = Util.getNanos();
/* 35 */     if (time - this.lastUpdateTime > 1.0E8D) {
/* 36 */       this.lastUpdateTime = time;
/* 37 */       Entity cameraEntity = this.minecraft.gameRenderer.getMainCamera().entity();
/* 38 */       this.surroundEntities = (List<Entity>)ImmutableList.copyOf(cameraEntity.level().getEntities(cameraEntity, cameraEntity.getBoundingBox().inflate(16.0D)));
/*    */     } 
/*    */     
/* 41 */     LocalPlayer localPlayer = this.minecraft.player;
/* 42 */     if (localPlayer != null && ((Player)localPlayer).mainSupportingBlockPos.isPresent()) {
/* 43 */       drawHighlights((Entity)localPlayer, () -> 0.0D, -65536);
/*    */     }
/*    */     
/* 46 */     for (Entity entity : this.surroundEntities) {
/* 47 */       if (entity == localPlayer) {
/*    */         continue;
/*    */       }
/* 50 */       drawHighlights(entity, () -> getBias(entity), -16711936);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void drawHighlights(Entity entity, DoubleSupplier biasGetter, int color) {
/* 55 */     entity.mainSupportingBlockPos.ifPresent(bp -> {
/*    */           double bias = biasGetter.getAsDouble();
/*    */           BlockPos supportingBlock = biasGetter.getOnPos();
/*    */           highlightPosition(supportingBlock, 0.02D + bias, entity);
/*    */           BlockPos effect = biasGetter.getOnPosLegacy();
/*    */           if (!effect.equals(supportingBlock)) {
/*    */             highlightPosition(effect, 0.04D + bias, -16711681);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private double getBias(Entity entity) {
/* 69 */     return 0.02D * (String.valueOf(entity.getId() + 0.132453657D).hashCode() % 1000) / 1000.0D;
/*    */   }
/*    */   
/*    */   private void highlightPosition(BlockPos pos, double offset, int color) {
/* 73 */     double fromX = pos.getX() - 2.0D * offset;
/* 74 */     double fromY = pos.getY() - 2.0D * offset;
/* 75 */     double fromZ = pos.getZ() - 2.0D * offset;
/* 76 */     double toX = fromX + 1.0D + 4.0D * offset;
/* 77 */     double toY = fromY + 1.0D + 4.0D * offset;
/* 78 */     double toZ = fromZ + 1.0D + 4.0D * offset;
/* 79 */     Gizmos.cuboid(new AABB(fromX, fromY, fromZ, toX, toY, toZ), GizmoStyle.stroke(ARGB.color(0.4F, color)));
/* 80 */     VoxelShape shape = this.minecraft.level.getBlockState(pos).getCollisionShape((BlockGetter)this.minecraft.level, pos, CollisionContext.empty()).move((Vec3i)pos);
/* 81 */     GizmoStyle style = GizmoStyle.stroke(color);
/* 82 */     for (AABB aabb : (Iterable<AABB>)shape.toAabbs())
/* 83 */       Gizmos.cuboid(aabb, style); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/SupportBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */