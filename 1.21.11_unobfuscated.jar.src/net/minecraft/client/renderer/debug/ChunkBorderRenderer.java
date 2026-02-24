/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ChunkBorderRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   private static final float THICK_WIDTH = 4.0F;
/*    */   private static final float THIN_WIDTH = 1.0F;
/*    */   private final Minecraft minecraft;
/* 20 */   private static final int CELL_BORDER = ARGB.color(255, 0, 155, 155);
/* 21 */   private static final int YELLOW = ARGB.color(255, 255, 255, 0);
/* 22 */   private static final int MAJOR_LINES = ARGB.colorFromFloat(1.0F, 0.25F, 0.25F, 1.0F);
/*    */   
/*    */   public ChunkBorderRenderer(Minecraft minecraft) {
/* 25 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 30 */     Entity cameraEntity = this.minecraft.gameRenderer.getMainCamera().entity();
/*    */     
/* 32 */     float ymin = this.minecraft.level.getMinY();
/* 33 */     float ymax = (this.minecraft.level.getMaxY() + 1);
/*    */     
/* 35 */     SectionPos cameraPos = SectionPos.of(cameraEntity.blockPosition());
/* 36 */     double xstart = cameraPos.minBlockX();
/* 37 */     double zstart = cameraPos.minBlockZ();
/*    */ 
/*    */     
/* 40 */     for (int k = -16; k <= 32; k += 16) {
/* 41 */       for (int m = -16; m <= 32; m += 16) {
/* 42 */         Gizmos.line(new Vec3(xstart + k, ymin, zstart + m), new Vec3(xstart + k, ymax, zstart + m), ARGB.colorFromFloat(0.5F, 1.0F, 0.0F, 0.0F), 4.0F);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 47 */     for (int i = 2; i < 16; i += 2) {
/* 48 */       int color = (i % 4 == 0) ? CELL_BORDER : YELLOW;
/* 49 */       Gizmos.line(new Vec3(xstart + i, ymin, zstart), new Vec3(xstart + i, ymax, zstart), color, 1.0F);
/* 50 */       Gizmos.line(new Vec3(xstart + i, ymin, zstart + 16.0D), new Vec3(xstart + i, ymax, zstart + 16.0D), color, 1.0F);
/*    */     } 
/*    */ 
/*    */     
/* 54 */     for (int z = 2; z < 16; z += 2) {
/* 55 */       int color = (z % 4 == 0) ? CELL_BORDER : YELLOW;
/* 56 */       Gizmos.line(new Vec3(xstart, ymin, zstart + z), new Vec3(xstart, ymax, zstart + z), color, 1.0F);
/*    */       
/* 58 */       Gizmos.line(new Vec3(xstart + 16.0D, ymin, zstart + z), new Vec3(xstart + 16.0D, ymax, zstart + z), color, 1.0F);
/*    */     } 
/*    */ 
/*    */     
/* 62 */     for (int j = this.minecraft.level.getMinY(); j <= this.minecraft.level.getMaxY() + 1; j += 2) {
/* 63 */       float yline = j;
/* 64 */       int color = (j % 8 == 0) ? CELL_BORDER : YELLOW;
/* 65 */       Gizmos.line(new Vec3(xstart, yline, zstart), new Vec3(xstart, yline, zstart + 16.0D), color, 1.0F);
/* 66 */       Gizmos.line(new Vec3(xstart, yline, zstart + 16.0D), new Vec3(xstart + 16.0D, yline, zstart + 16.0D), color, 1.0F);
/* 67 */       Gizmos.line(new Vec3(xstart + 16.0D, yline, zstart + 16.0D), new Vec3(xstart + 16.0D, yline, zstart), color, 1.0F);
/* 68 */       Gizmos.line(new Vec3(xstart + 16.0D, yline, zstart), new Vec3(xstart, yline, zstart), color, 1.0F);
/*    */     } 
/*    */ 
/*    */     
/* 72 */     for (int x = 0; x <= 16; x += 16) {
/* 73 */       for (int m = 0; m <= 16; m += 16) {
/* 74 */         Gizmos.line(new Vec3(xstart + x, ymin, zstart + m), new Vec3(xstart + x, ymax, zstart + m), MAJOR_LINES, 4.0F);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 79 */     Gizmos.cuboid(new AABB(
/*    */           
/* 81 */           cameraPos.minBlockX(), 
/* 82 */           cameraPos.minBlockY(), 
/* 83 */           cameraPos.minBlockZ(), (
/* 84 */           cameraPos.maxBlockX() + 1), (
/* 85 */           cameraPos.maxBlockY() + 1), (
/* 86 */           cameraPos.maxBlockZ() + 1)), 
/*    */         
/* 88 */         GizmoStyle.stroke(MAJOR_LINES, 1.0F))
/* 89 */       .setAlwaysOnTop();
/*    */ 
/*    */     
/* 92 */     for (int y = this.minecraft.level.getMinY(); y <= this.minecraft.level.getMaxY() + 1; y += 16) {
/* 93 */       Gizmos.line(new Vec3(xstart, y, zstart), new Vec3(xstart, y, zstart + 16.0D), MAJOR_LINES, 4.0F);
/* 94 */       Gizmos.line(new Vec3(xstart, y, zstart + 16.0D), new Vec3(xstart + 16.0D, y, zstart + 16.0D), MAJOR_LINES, 4.0F);
/* 95 */       Gizmos.line(new Vec3(xstart + 16.0D, y, zstart + 16.0D), new Vec3(xstart + 16.0D, y, zstart), MAJOR_LINES, 4.0F);
/* 96 */       Gizmos.line(new Vec3(xstart + 16.0D, y, zstart), new Vec3(xstart, y, zstart), MAJOR_LINES, 4.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/ChunkBorderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */