/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.culling.Frustum;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.debug.DebugValueAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class HeightMapRenderer
/*    */   implements DebugRenderer.SimpleDebugRenderer
/*    */ {
/*    */   private final Minecraft minecraft;
/*    */   private static final int CHUNK_DIST = 2;
/*    */   private static final float BOX_HEIGHT = 0.09375F;
/*    */   
/*    */   public HeightMapRenderer(Minecraft minecraft) {
/* 27 */     this.minecraft = minecraft;
/*    */   }
/*    */ 
/*    */   
/*    */   public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
/* 32 */     ClientLevel clientLevel = this.minecraft.level;
/*    */     
/* 34 */     BlockPos playerPos = BlockPos.containing(camX, 0.0D, camZ);
/*    */     
/* 36 */     for (int chunkX = -2; chunkX <= 2; chunkX++) {
/* 37 */       for (int chunkZ = -2; chunkZ <= 2; chunkZ++) {
/* 38 */         ChunkAccess chunk = clientLevel.getChunk(playerPos.offset(chunkX * 16, 0, chunkZ * 16));
/* 39 */         for (Map.Entry<Heightmap.Types, Heightmap> heightmapEntry : (Iterable<Map.Entry<Heightmap.Types, Heightmap>>)chunk.getHeightmaps()) {
/* 40 */           Heightmap.Types type = heightmapEntry.getKey();
/* 41 */           ChunkPos chunkPos = chunk.getPos();
/* 42 */           Vector3f color = getColor(type);
/* 43 */           for (int relativeX = 0; relativeX < 16; relativeX++) {
/* 44 */             for (int relativeZ = 0; relativeZ < 16; relativeZ++) {
/* 45 */               int xx = SectionPos.sectionToBlockCoord(chunkPos.x, relativeX);
/* 46 */               int zz = SectionPos.sectionToBlockCoord(chunkPos.z, relativeZ);
/* 47 */               float height = clientLevel.getHeight(type, xx, zz) + type.ordinal() * 0.09375F;
/* 48 */               Gizmos.cuboid(new AABB((xx + 0.25F), height, (zz + 0.25F), (xx + 0.75F), (height + 0.09375F), (zz + 0.75F)), GizmoStyle.fill(ARGB.colorFromFloat(1.0F, color.x(), color.y(), color.z())));
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private Vector3f getColor(Heightmap.Types type) {
/* 57 */     switch (type) { default: throw new MatchException(null, null);case WORLD_SURFACE_WG: case OCEAN_FLOOR_WG: case WORLD_SURFACE: case OCEAN_FLOOR: case MOTION_BLOCKING: case MOTION_BLOCKING_NO_LEAVES: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       new Vector3f(0.0F, 0.5F, 0.5F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/debug/HeightMapRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */