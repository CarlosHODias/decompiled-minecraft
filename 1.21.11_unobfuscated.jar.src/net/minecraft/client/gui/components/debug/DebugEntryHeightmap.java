/*    */ package net.minecraft.client.gui.components.debug;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class DebugEntryHeightmap
/*    */   implements DebugScreenEntry
/*    */ {
/* 18 */   private static final Map<Heightmap.Types, String> HEIGHTMAP_NAMES = Maps.newEnumMap(Map.of(Heightmap.Types.WORLD_SURFACE_WG, "SW", Heightmap.Types.WORLD_SURFACE, "S", Heightmap.Types.OCEAN_FLOOR_WG, "OW", Heightmap.Types.OCEAN_FLOOR, "O", Heightmap.Types.MOTION_BLOCKING, "M", Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, "ML"));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   private static final Identifier GROUP = Identifier.withDefaultNamespace("heightmaps");
/*    */ 
/*    */   
/*    */   public void display(DebugScreenDisplayer displayer, Level serverOrClientLevel, LevelChunk clientChunk, LevelChunk serverChunk) {
/* 30 */     Minecraft minecraft = Minecraft.getInstance();
/* 31 */     Entity entity = minecraft.getCameraEntity();
/*    */     
/* 33 */     if (entity == null || minecraft.level == null || clientChunk == null) {
/*    */       return;
/*    */     }
/*    */     
/* 37 */     BlockPos feetPos = entity.blockPosition();
/* 38 */     List<String> result = new ArrayList<>();
/*    */     
/* 40 */     StringBuilder heightmaps = new StringBuilder("CH");
/* 41 */     for (Heightmap.Types type : Heightmap.Types.values()) {
/* 42 */       if (type.sendToClient()) {
/* 43 */         heightmaps.append(" ").append(HEIGHTMAP_NAMES.get(type)).append(": ").append(clientChunk.getHeight(type, feetPos.getX(), feetPos.getZ()));
/*    */       }
/*    */     } 
/* 46 */     result.add(heightmaps.toString());
/*    */     
/* 48 */     heightmaps.setLength(0);
/* 49 */     heightmaps.append("SH");
/* 50 */     for (Heightmap.Types type : Heightmap.Types.values()) {
/* 51 */       if (type.keepAfterWorldgen()) {
/* 52 */         heightmaps.append(" ").append(HEIGHTMAP_NAMES.get(type)).append(": ");
/* 53 */         if (serverChunk != null) {
/* 54 */           heightmaps.append(serverChunk.getHeight(type, feetPos.getX(), feetPos.getZ()));
/*    */         } else {
/* 56 */           heightmaps.append("??");
/*    */         } 
/*    */       } 
/*    */     } 
/* 60 */     result.add(heightmaps.toString());
/*    */     
/* 62 */     displayer.addToGroup(GROUP, result);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryHeightmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */