/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class LightDebugRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer {
/*    */   private final Minecraft minecraft;
/*    */   private final boolean showBlockLight;
/*    */   private final boolean showSkyLight;
/*    */   private static final int MAX_RENDER_DIST = 10;
/*    */   
/*    */   public LightDebugRenderer(Minecraft minecraft, boolean showBlockLight, boolean showSkyLight) {
/* 25 */     this.minecraft = minecraft;
/* 26 */     this.showBlockLight = showBlockLight;
/* 27 */     this.showSkyLight = showSkyLight;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 32 */     ClientLevel clientLevel = this.minecraft.level;
/*    */     
/* 34 */     BlockPos playerPos = BlockPos.containing(camX, camY, camZ);
/*    */     
/* 36 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/*    */     
/* 38 */     for (BlockPos blockPos : (Iterable<BlockPos>)BlockPos.betweenClosed(playerPos.offset(-10, -10, -10), playerPos.offset(10, 10, 10))) {
/* 39 */       int skyBrightness = clientLevel.getBrightness(LightLayer.SKY, blockPos);
/* 40 */       long sectionNode = SectionPos.blockToSection(blockPos.asLong());
/* 41 */       if (longOpenHashSet.add(sectionNode)) {
/* 42 */         Gizmos.billboardText(clientLevel.getChunkSource().getLightEngine().getDebugData(LightLayer.SKY, SectionPos.of(sectionNode)), new Vec3(SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode), 8), SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode), 8), SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode), 8)), TextGizmo.Style.forColorAndCentered(-65536).withScale(4.8F));
/*    */       }
/* 44 */       if (skyBrightness != 15 && this.showSkyLight) {
/* 45 */         int color = ARGB.srgbLerp(skyBrightness / 15.0F, -16776961, -16711681);
/* 46 */         Gizmos.billboardText(String.valueOf(skyBrightness), Vec3.atLowerCornerWithOffset((Vec3i)blockPos, 0.5D, 0.25D, 0.5D), TextGizmo.Style.forColorAndCentered(color));
/*    */       } 
/*    */       
/* 49 */       if (this.showBlockLight) {
/* 50 */         int blockBrightness = clientLevel.getBrightness(LightLayer.BLOCK, blockPos);
/* 51 */         if (blockBrightness != 0) {
/* 52 */           int color = ARGB.srgbLerp(blockBrightness / 15.0F, -5636096, -256);
/* 53 */           Gizmos.billboardText(String.valueOf(clientLevel.getBrightness(LightLayer.BLOCK, blockPos)), Vec3.atCenterOf((Vec3i)blockPos), TextGizmo.Style.forColorAndCentered(color));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/LightDebugRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */