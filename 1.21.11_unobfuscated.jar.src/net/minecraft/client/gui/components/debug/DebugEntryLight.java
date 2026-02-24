/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LightLayer;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ 
/*    */ public class DebugEntryLight
/*    */   implements DebugScreenEntry
/*    */ {
/* 18 */   public static final Identifier GROUP = Identifier.withDefaultNamespace("light");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 22 */     Minecraft minecraft = Minecraft.getInstance();
/* 23 */     Entity entity = minecraft.getCameraEntity();
/*    */     
/* 25 */     if (entity == null || minecraft.level == null) {
/*    */       return;
/*    */     }
/*    */     
/* 29 */     BlockPos feetPos = entity.blockPosition();
/* 30 */     int rawBrightness = minecraft.level.getChunkSource().getLightEngine().getRawBrightness(feetPos, 0);
/* 31 */     int sky = minecraft.level.getBrightness(LightLayer.SKY, feetPos);
/* 32 */     int block = minecraft.level.getBrightness(LightLayer.BLOCK, feetPos);
/* 33 */     String clientLight = "Client Light: " + rawBrightness + " (" + sky + " sky, " + block + " block)";
/*    */     
/* 35 */     if (SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES) {
/*    */       String serverLight;
/* 37 */       if (serverChunk != null) {
/* 38 */         LevelLightEngine lightEngine = serverChunk.getLevel().getLightEngine();
/* 39 */         serverLight = "Server Light: (" + lightEngine.getLayerListener(LightLayer.SKY).getLightValue(feetPos) + " sky, " + lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(feetPos) + " block)";
/*    */       } else {
/* 41 */         serverLight = "Server Light: (?? sky, ?? block)";
/*    */       } 
/* 43 */       displayer.addToGroup(GROUP, List.of(clientLight, serverLight));
/*    */     } else {
/* 45 */       displayer.addToGroup(GROUP, clientLight);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryLight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */