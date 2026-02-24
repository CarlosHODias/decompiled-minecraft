/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ 
/*    */ public class DebugEntryBiome
/*    */   implements DebugScreenEntry
/*    */ {
/* 18 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("biome");
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
/*    */     
/* 31 */     if (minecraft.level.isInsideBuildHeight(feetPos.getY())) {
/* 32 */       if (SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES && serverOrClientLevel instanceof net.minecraft.server.level.ServerLevel) {
/* 33 */         displayer.addToGroup(GROUP, List.of("Biome: " + 
/* 34 */               printBiome(minecraft.level.getBiome(feetPos)), "Server Biome: " + 
/* 35 */               printBiome(serverOrClientLevel.getBiome(feetPos))));
/*    */       } else {
/*    */         
/* 38 */         displayer.addLine("Biome: " + printBiome(minecraft.level.getBiome(feetPos)));
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private static String printBiome(Holder<Biome> biome) {
/* 44 */     return (String)biome.unwrap().map(key -> key.identifier().toString(), l -> "[unregistered " + String.valueOf(l) + "]");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryBiome.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */