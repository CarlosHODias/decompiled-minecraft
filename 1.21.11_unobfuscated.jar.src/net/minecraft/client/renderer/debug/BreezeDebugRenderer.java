/*    */ package net.minecraft.client.renderer.debug;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugBreezeInfo;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreezeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
/* 17 */   private static final int JUMP_TARGET_LINE_COLOR = ARGB.color(255, 255, 100, 255);
/* 18 */   private static final int TARGET_LINE_COLOR = ARGB.color(255, 100, 255, 255);
/* 19 */   private static final int INNER_CIRCLE_COLOR = ARGB.color(255, 0, 255, 0);
/* 20 */   private static final int MIDDLE_CIRCLE_COLOR = ARGB.color(255, 255, 165, 0);
/* 21 */   private static final int OUTER_CIRCLE_COLOR = ARGB.color(255, 255, 0, 0);
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public BreezeDebugRenderer(Minecraft minecraft) {
/* 26 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 31 */     ClientLevel level = this.minecraft.level;
/* 32 */     debugValues.forEachEntity(DebugSubscriptions.BREEZES, (entity, info) -> {
/*    */           Objects.requireNonNull(level);
/*    */           info.attackTarget().map(level::getEntity).map(()).ifPresent(());
/*    */           info.jumpTarget().ifPresent(());
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/BreezeDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */