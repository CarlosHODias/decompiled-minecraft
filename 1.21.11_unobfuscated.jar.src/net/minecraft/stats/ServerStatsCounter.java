/*     */ package net.minecraft.stats;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerStatsCounter extends StatsCounter {
/*  43 */   private static final Gson GSON = new GsonBuilder()
/*  44 */     .setPrettyPrinting()
/*  45 */     .create();
/*     */   
/*  47 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final Codec<Map<Stat<?>, Integer>> STATS_CODEC;
/*     */   private final Path file;
/*     */   
/*     */   static {
/*  52 */     STATS_CODEC = Codec.dispatchedMap(BuiltInRegistries.STAT_TYPE.byNameCodec(), Util.memoize(ServerStatsCounter::createTypedStatsCodec)).xmap(groupedStats -> {
/*     */           Map<Stat<?>, Integer> stats = new HashMap<>();
/*     */           groupedStats.forEach(());
/*     */           return stats;
/*     */         }, map -> (Map)map.entrySet().stream().collect(Collectors.groupingBy((), Util.toMap())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> Codec<Map<Stat<?>, Integer>> createTypedStatsCodec(StatType<T> type) {
/*  67 */     Codec<T> valueCodec = type.getRegistry().byNameCodec();
/*  68 */     Objects.requireNonNull(type); Codec<Stat<?>> statCodec = valueCodec.flatComapMap(type::get, stat -> (stat.getType() == type) ? DataResult.success(stat.getValue()) : DataResult.error(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     return (Codec<Map<Stat<?>, Integer>>)Codec.unboundedMap(statCodec, (Codec)Codec.INT);
/*     */   }
/*     */ 
/*     */   
/*  79 */   private final Set<Stat<?>> dirty = Sets.newHashSet();
/*     */   
/*     */   public ServerStatsCounter(MinecraftServer server, Path file) {
/*  82 */     this.file = file;
/*  83 */     if (Files.isRegularFile(file, new java.nio.file.LinkOption[0])) {
/*  84 */       try { Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8); 
/*  85 */         try { JsonElement element = StrictJsonParser.parse(reader);
/*  86 */           parse(server.getFixerUpper(), element);
/*  87 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  88 */       { LOGGER.error("Couldn't read statistics file {}", file, e); }
/*  89 */       catch (JsonParseException e)
/*  90 */       { LOGGER.error("Couldn't parse statistics file {}", file, e); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public void save() {
/*     */     
/*  97 */     try { FileUtil.createDirectoriesSafe(this.file.getParent());
/*  98 */       Writer writer = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]); 
/*  99 */       try { GSON.toJson(toJson(), GSON.newJsonWriter(writer));
/* 100 */         if (writer != null) writer.close();  } catch (Throwable throwable) { if (writer != null)
/* 101 */           try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonIOException e)
/* 102 */     { LOGGER.error("Couldn't save stats to {}", this.file, e); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Player player, Stat<?> stat, int count) {
/* 108 */     super.setValue(player, stat, count);
/* 109 */     this.dirty.add(stat);
/*     */   }
/*     */   
/*     */   private Set<Stat<?>> getDirty() {
/* 113 */     Set<Stat<?>> result = Sets.newHashSet(this.dirty);
/* 114 */     this.dirty.clear();
/* 115 */     return result;
/*     */   }
/*     */   
/*     */   public void parse(DataFixer fixerUpper, JsonElement element) {
/* 119 */     Dynamic<JsonElement> data = new Dynamic((DynamicOps)JsonOps.INSTANCE, element);
/*     */     
/* 121 */     data = DataFixTypes.STATS.updateToCurrentVersion(fixerUpper, data, net.minecraft.nbt.NbtUtils.getDataVersion(data, 1343));
/*     */     
/* 123 */     this.stats.putAll(STATS_CODEC.parse(data.get("stats").orElseEmptyMap())
/* 124 */         .resultOrPartial(error -> LOGGER.error("Failed to parse statistics for {}: {}", this.file, error))
/* 125 */         .orElse(Map.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected JsonElement toJson() {
/* 130 */     JsonObject result = new JsonObject();
/* 131 */     result.add("stats", (JsonElement)STATS_CODEC.encodeStart((DynamicOps)JsonOps.INSTANCE, this.stats).getOrThrow());
/* 132 */     result.addProperty("DataVersion", SharedConstants.getCurrentVersion().dataVersion().version());
/* 133 */     return (JsonElement)result;
/*     */   }
/*     */   
/*     */   public void markAllDirty() {
/* 137 */     this.dirty.addAll((Collection<? extends Stat<?>>)this.stats.keySet());
/*     */   }
/*     */   
/*     */   public void sendStats(ServerPlayer player) {
/* 141 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/*     */     
/* 143 */     for (Stat<?> stat : getDirty()) {
/* 144 */       object2IntOpenHashMap.put(stat, getValue(stat));
/*     */     }
/*     */     
/* 147 */     player.connection.send((Packet)new ClientboundAwardStatsPacket((Object2IntMap)object2IntOpenHashMap));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/stats/ServerStatsCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */