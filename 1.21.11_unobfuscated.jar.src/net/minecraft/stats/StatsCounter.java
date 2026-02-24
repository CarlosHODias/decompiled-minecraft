/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class StatsCounter {
/*  9 */   protected final Object2IntMap<Stat<?>> stats = Object2IntMaps.synchronize((Object2IntMap)new Object2IntOpenHashMap());
/*    */   
/*    */   public StatsCounter() {
/* 12 */     this.stats.defaultReturnValue(0);
/*    */   }
/*    */   
/*    */   public void increment(Player player, Stat<?> stat, int count) {
/* 16 */     int result = (int)Math.min(getValue(stat) + count, 2147483647L);
/* 17 */     setValue(player, stat, result);
/*    */   }
/*    */   
/*    */   public void setValue(Player player, Stat<?> stat, int count) {
/* 21 */     this.stats.put(stat, count);
/*    */   }
/*    */   
/*    */   public <T> int getValue(StatType<T> type, T key) {
/* 25 */     return type.contains(key) ? getValue(type.get(key)) : 0;
/*    */   }
/*    */   
/*    */   public int getValue(Stat<?> stat) {
/* 29 */     return this.stats.getInt(stat);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/StatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */