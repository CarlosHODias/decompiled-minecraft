/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ChunkMap;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalMobCapCalculator
/*    */ {
/* 19 */   private final Long2ObjectMap<List<ServerPlayer>> playersNearChunk = (Long2ObjectMap<List<ServerPlayer>>)new Long2ObjectOpenHashMap();
/* 20 */   private final Map<ServerPlayer, MobCounts> playerMobCounts = Maps.newHashMap();
/*    */   private final ChunkMap chunkMap;
/*    */   
/*    */   public LocalMobCapCalculator(ChunkMap chunkMap) {
/* 24 */     this.chunkMap = chunkMap;
/*    */   }
/*    */   
/*    */   private List<ServerPlayer> getPlayersNear(ChunkPos pos) {
/* 28 */     return (List<ServerPlayer>)this.playersNearChunk.computeIfAbsent(pos.toLong(), key -> this.chunkMap.getPlayersCloseForSpawning(pos));
/*    */   }
/*    */   
/*    */   public void addMob(ChunkPos pos, MobCategory category) {
/* 32 */     for (ServerPlayer player : getPlayersNear(pos)) {
/* 33 */       ((MobCounts)this.playerMobCounts.computeIfAbsent(player, key -> new MobCounts())).add(category);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean canSpawn(MobCategory mobCategory, ChunkPos pos) {
/* 38 */     for (ServerPlayer serverPlayer : getPlayersNear(pos)) {
/* 39 */       MobCounts mobCounts = this.playerMobCounts.get(serverPlayer);
/* 40 */       if (mobCounts == null || mobCounts.canSpawn(mobCategory)) {
/* 41 */         return true;
/*    */       }
/*    */     } 
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   private static class MobCounts {
/* 48 */     private final Object2IntMap<MobCategory> counts = (Object2IntMap<MobCategory>)new Object2IntOpenHashMap((MobCategory.values()).length);
/*    */     
/*    */     public void add(MobCategory category) {
/* 51 */       this.counts.computeInt(category, (k, count) -> (count == null) ? 1 : (count + 1));
/*    */     }
/*    */     
/*    */     public boolean canSpawn(MobCategory category) {
/* 55 */       return (this.counts.getOrDefault(category, 0) < category.getMaxInstancesPerChunk());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/LocalMobCapCalculator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */