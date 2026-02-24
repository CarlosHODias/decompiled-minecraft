/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ 
/*    */ public class VillageSectionsDebugRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 17 */     debugValues.forEachBlock(DebugSubscriptions.VILLAGE_SECTIONS, (pos, ignored) -> {
/*    */           SectionPos villageSection = SectionPos.of(pos);
/*    */           Gizmos.cuboid(villageSection.center(), GizmoStyle.fill(ARGB.colorFromFloat(0.15F, 0.2F, 1.0F, 0.2F)));
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/VillageSectionsDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */