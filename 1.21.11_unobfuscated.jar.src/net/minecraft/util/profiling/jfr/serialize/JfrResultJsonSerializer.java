/*     */ package net.minecraft.util.profiling.jfr.serialize;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.LongSerializationPolicy;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.time.Duration;
/*     */ import java.util.DoubleSummaryStatistics;
/*     */ import java.util.IntSummaryStatistics;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.DoubleStream;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.jfr.Percentiles;
/*     */ import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ChunkIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FileIOStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.FpsStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.IoSummary;
/*     */ import net.minecraft.util.profiling.jfr.stats.PacketIdentification;
/*     */ import net.minecraft.util.profiling.jfr.stats.StructureGenStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
/*     */ import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class JfrResultJsonSerializer
/*     */ {
/*     */   private static final String BYTES_PER_SECOND = "bytesPerSecond";
/*     */   private static final String COUNT = "count";
/*     */   private static final String DURATION_NANOS_TOTAL = "durationNanosTotal";
/*     */   private static final String TOTAL_BYTES = "totalBytes";
/*     */   private static final String COUNT_PER_SECOND = "countPerSecond";
/*  49 */   final Gson gson = new GsonBuilder()
/*  50 */     .setPrettyPrinting()
/*  51 */     .setLongSerializationPolicy(LongSerializationPolicy.DEFAULT)
/*  52 */     .create();
/*     */   
/*     */   private static void serializePacketId(PacketIdentification identifier, JsonObject output) {
/*  55 */     output.addProperty("protocolId", identifier.protocolId());
/*  56 */     output.addProperty("packetId", identifier.packetId());
/*     */   }
/*     */   
/*     */   private static void serializeChunkId(ChunkIdentification identifier, JsonObject output) {
/*  60 */     output.addProperty("level", identifier.level());
/*  61 */     output.addProperty("dimension", identifier.dimension());
/*  62 */     output.addProperty("x", identifier.x());
/*  63 */     output.addProperty("z", identifier.z());
/*     */   }
/*     */   
/*     */   public String format(JfrStatsResult jfrStats) {
/*  67 */     JsonObject root = new JsonObject();
/*     */     
/*  69 */     root.addProperty("startedEpoch", jfrStats.recordingStarted().toEpochMilli());
/*  70 */     root.addProperty("endedEpoch", jfrStats.recordingEnded().toEpochMilli());
/*  71 */     root.addProperty("durationMs", jfrStats.recordingDuration().toMillis());
/*  72 */     Duration worldCreationDuration = jfrStats.worldCreationDuration();
/*  73 */     if (worldCreationDuration != null) {
/*  74 */       root.addProperty("worldGenDurationMs", worldCreationDuration.toMillis());
/*     */     }
/*  76 */     root.add("heap", heap(jfrStats.heapSummary()));
/*  77 */     root.add("cpuPercent", cpu(jfrStats.cpuLoadStats()));
/*  78 */     root.add("network", network(jfrStats));
/*  79 */     root.add("fileIO", fileIO(jfrStats));
/*  80 */     root.add("fps", fps(jfrStats.fps()));
/*  81 */     root.add("serverTick", serverTicks(jfrStats.serverTickTimes()));
/*  82 */     root.add("threadAllocation", threadAllocations(jfrStats.threadAllocationSummary()));
/*  83 */     root.add("chunkGen", chunkGen(jfrStats.chunkGenSummary()));
/*  84 */     root.add("structureGen", structureGen(jfrStats.structureGenStats()));
/*     */     
/*  86 */     return this.gson.toJson((JsonElement)root);
/*     */   }
/*     */   
/*     */   private JsonElement heap(GcHeapStat.Summary heapSummary) {
/*  90 */     JsonObject json = new JsonObject();
/*  91 */     json.addProperty("allocationRateBytesPerSecond", heapSummary.allocationRateBytesPerSecond());
/*  92 */     json.addProperty("gcCount", heapSummary.totalGCs());
/*  93 */     json.addProperty("gcOverHeadPercent", heapSummary.gcOverHead());
/*  94 */     json.addProperty("gcTotalDurationMs", heapSummary.gcTotalDuration().toMillis());
/*  95 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement structureGen(List<StructureGenStat> structureGenStats) {
/*  99 */     JsonObject root = new JsonObject();
/* 100 */     Optional<TimedStatSummary<StructureGenStat>> optionalSummary = TimedStatSummary.summary(structureGenStats);
/* 101 */     if (optionalSummary.isEmpty()) {
/* 102 */       return (JsonElement)root;
/*     */     }
/* 104 */     TimedStatSummary<StructureGenStat> summary = optionalSummary.get();
/*     */     
/* 106 */     JsonArray structureJsonArray = new JsonArray();
/* 107 */     root.add("structure", (JsonElement)structureJsonArray);
/*     */     
/* 109 */     ((Map)structureGenStats.stream()
/* 110 */       .collect(Collectors.groupingBy(StructureGenStat::structureName)))
/* 111 */       .forEach((structureName, timedStat) -> {
/*     */           Optional<TimedStatSummary<StructureGenStat>> optionalStatSummary = TimedStatSummary.summary(timedStat);
/*     */ 
/*     */           
/*     */           if (optionalStatSummary.isEmpty()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           TimedStatSummary<StructureGenStat> statSummary = optionalStatSummary.get();
/*     */           
/*     */           JsonObject structureJson = new JsonObject();
/*     */           
/*     */           structureJsonArray.add((JsonElement)structureJson);
/*     */           
/*     */           structureJson.addProperty("name", structureName);
/*     */           
/*     */           structureJson.addProperty("count", statSummary.count());
/*     */           
/*     */           structureJson.addProperty("durationNanosTotal", statSummary.totalDuration().toNanos());
/*     */           
/*     */           structureJson.addProperty("durationNanosAvg", statSummary.totalDuration().toNanos() / statSummary.count());
/*     */           
/*     */           JsonObject percentiles = (JsonObject)Util.make(new JsonObject(), ());
/*     */           
/*     */           statSummary.percentilesNanos().forEach(());
/*     */           
/*     */           Function<StructureGenStat, JsonElement> structureGenStatJsonGenerator = ();
/*     */           
/*     */           root.add("fastest", structureGenStatJsonGenerator.apply((StructureGenStat)summary.fastest()));
/*     */           
/*     */           root.add("slowest", structureGenStatJsonGenerator.apply((StructureGenStat)summary.slowest()));
/*     */           
/*     */           root.add("secondSlowest", (summary.secondSlowest() != null) ? structureGenStatJsonGenerator.apply((StructureGenStat)summary.secondSlowest()) : (JsonElement)JsonNull.INSTANCE);
/*     */         });
/*     */     
/* 146 */     return (JsonElement)root;
/*     */   }
/*     */   
/*     */   private JsonElement chunkGen(List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary) {
/* 150 */     JsonObject json = new JsonObject();
/* 151 */     if (chunkGenSummary.isEmpty()) {
/* 152 */       return (JsonElement)json;
/*     */     }
/* 154 */     json.addProperty("durationNanosTotal", chunkGenSummary.stream().mapToDouble(it -> ((TimedStatSummary)it.getSecond()).totalDuration().toNanos()).sum()); JsonArray chunkJsonArray = (JsonArray)Util.make(new JsonArray(), self -> json.add("status", (JsonElement)self));
/*     */     
/* 156 */     for (Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>> summaryByStatus : chunkGenSummary) {
/* 157 */       TimedStatSummary<ChunkGenStat> chunkStat = (TimedStatSummary<ChunkGenStat>)summaryByStatus.getSecond();
/* 158 */       Objects.requireNonNull(chunkJsonArray); JsonObject chunkStatusJson = (JsonObject)Util.make(new JsonObject(), chunkJsonArray::add);
/* 159 */       chunkStatusJson.addProperty("state", ((ChunkStatus)summaryByStatus.getFirst()).toString());
/* 160 */       chunkStatusJson.addProperty("count", chunkStat.count());
/* 161 */       chunkStatusJson.addProperty("durationNanosTotal", chunkStat.totalDuration().toNanos());
/* 162 */       chunkStatusJson.addProperty("durationNanosAvg", chunkStat.totalDuration().toNanos() / chunkStat.count());
/* 163 */       JsonObject percentiles = (JsonObject)Util.make(new JsonObject(), self -> chunkStatusJson.add("durationNanosPercentiles", (JsonElement)self));
/* 164 */       chunkStat.percentilesNanos().forEach((percentile, value) -> percentiles.addProperty("p" + percentile, value));
/*     */       
/*     */       Function<ChunkGenStat, JsonElement> chunkGenStatJsonGenerator = chunk -> {
/*     */           JsonObject chunkGenStatJson = new JsonObject();
/*     */           chunkGenStatJson.addProperty("durationNanos", chunk.duration().toNanos());
/*     */           chunkGenStatJson.addProperty("level", chunk.level());
/*     */           chunkGenStatJson.addProperty("chunkPosX", (chunk.chunkPos()).x);
/*     */           chunkGenStatJson.addProperty("chunkPosZ", (chunk.chunkPos()).z);
/*     */           chunkGenStatJson.addProperty("worldPosX", chunk.worldPos().x());
/*     */           chunkGenStatJson.addProperty("worldPosZ", chunk.worldPos().z());
/*     */           return chunkGenStatJson;
/*     */         };
/* 176 */       chunkStatusJson.add("fastest", chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.fastest()));
/* 177 */       chunkStatusJson.add("slowest", chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.slowest()));
/* 178 */       chunkStatusJson.add("secondSlowest", (chunkStat.secondSlowest() != null) ? 
/* 179 */           chunkGenStatJsonGenerator.apply((ChunkGenStat)chunkStat.secondSlowest()) : 
/* 180 */           (JsonElement)JsonNull.INSTANCE);
/*     */     } 
/*     */     
/* 183 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement threadAllocations(ThreadAllocationStat.Summary threadAllocationSummary) {
/* 187 */     JsonArray threads = new JsonArray();
/* 188 */     threadAllocationSummary.allocationsPerSecondByThread().forEach((threadName, bytesPerSecond) -> threads.add((JsonElement)Util.make(new JsonObject(), ())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     return (JsonElement)threads;
/*     */   }
/*     */   
/*     */   private JsonElement serverTicks(List<TickTimeStat> tickTimeStats) {
/* 198 */     if (tickTimeStats.isEmpty()) {
/* 199 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/* 201 */     JsonObject json = new JsonObject();
/* 202 */     double[] tickTimesMs = tickTimeStats.stream().mapToDouble(tickTimeStat -> tickTimeStat.currentAverage().toNanos() / 1000000.0D).toArray();
/*     */     
/* 204 */     DoubleSummaryStatistics summary = DoubleStream.of(tickTimesMs).summaryStatistics();
/* 205 */     json.addProperty("minMs", summary.getMin());
/* 206 */     json.addProperty("averageMs", summary.getAverage());
/* 207 */     json.addProperty("maxMs", summary.getMax());
/* 208 */     Map<Integer, Double> percentiles = Percentiles.evaluate(tickTimesMs);
/* 209 */     percentiles.forEach((percentile, value) -> json.addProperty("p" + percentile, value));
/* 210 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fps(List<FpsStat> fpsStats) {
/* 214 */     if (fpsStats.isEmpty()) {
/* 215 */       return (JsonElement)JsonNull.INSTANCE;
/*     */     }
/* 217 */     JsonObject json = new JsonObject();
/* 218 */     int[] fps = fpsStats.stream().mapToInt(FpsStat::fps).toArray();
/* 219 */     IntSummaryStatistics summary = IntStream.of(fps).summaryStatistics();
/* 220 */     json.addProperty("minFPS", summary.getMin());
/* 221 */     json.addProperty("averageFPS", summary.getAverage());
/* 222 */     json.addProperty("maxFPS", summary.getMax());
/* 223 */     Map<Integer, Double> percentiles = Percentiles.evaluate(fps);
/* 224 */     percentiles.forEach((percentile, value) -> json.addProperty("p" + percentile, value));
/* 225 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fileIO(JfrStatsResult jfrStats) {
/* 229 */     JsonObject json = new JsonObject();
/* 230 */     json.add("write", fileIoSummary(jfrStats.fileWrites()));
/* 231 */     json.add("read", fileIoSummary(jfrStats.fileReads()));
/* 232 */     json.add("chunksRead", ioSummary(jfrStats.readChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 233 */     json.add("chunksWritten", ioSummary(jfrStats.writtenChunks(), JfrResultJsonSerializer::serializeChunkId));
/* 234 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement fileIoSummary(FileIOStat.Summary io) {
/* 238 */     JsonObject json = new JsonObject();
/* 239 */     json.addProperty("totalBytes", io.totalBytes());
/* 240 */     json.addProperty("count", io.counts());
/* 241 */     json.addProperty("bytesPerSecond", io.bytesPerSecond());
/* 242 */     json.addProperty("countPerSecond", io.countsPerSecond());
/* 243 */     JsonArray topContributors = new JsonArray();
/* 244 */     json.add("topContributors", (JsonElement)topContributors);
/* 245 */     io.topTenContributorsByTotalBytes().forEach(contributor -> {
/*     */           JsonObject contributorJson = new JsonObject();
/*     */           topContributors.add((JsonElement)contributorJson);
/*     */           contributorJson.addProperty("path", (String)contributor.getFirst());
/*     */           contributorJson.addProperty("totalBytes", (Number)contributor.getSecond());
/*     */         });
/* 251 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement network(JfrStatsResult jfrStats) {
/* 255 */     JsonObject json = new JsonObject();
/* 256 */     json.add("sent", ioSummary(jfrStats.sentPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 257 */     json.add("received", ioSummary(jfrStats.receivedPacketsSummary(), JfrResultJsonSerializer::serializePacketId));
/* 258 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private <T> JsonElement ioSummary(IoSummary<T> summary, BiConsumer<T, JsonObject> elementWriter) {
/* 262 */     JsonObject json = new JsonObject();
/* 263 */     json.addProperty("totalBytes", summary.getTotalSize());
/* 264 */     json.addProperty("count", summary.getTotalCount());
/* 265 */     json.addProperty("bytesPerSecond", summary.getSizePerSecond());
/* 266 */     json.addProperty("countPerSecond", summary.getCountsPerSecond());
/* 267 */     JsonArray topContributors = new JsonArray();
/* 268 */     json.add("topContributors", (JsonElement)topContributors);
/* 269 */     summary.largestSizeContributors().forEach(contributor -> {
/*     */           JsonObject contributorJson = new JsonObject();
/*     */           topContributors.add((JsonElement)contributorJson);
/*     */           T identifier = (T)contributor.getFirst();
/*     */           IoSummary.CountAndSize countAndSize = (IoSummary.CountAndSize)contributor.getSecond();
/*     */           elementWriter.accept(identifier, contributorJson);
/*     */           contributorJson.addProperty("totalBytes", countAndSize.totalSize());
/*     */           contributorJson.addProperty("count", countAndSize.totalCount());
/*     */           contributorJson.addProperty("averageSize", countAndSize.averageSize());
/*     */         });
/* 279 */     return (JsonElement)json;
/*     */   }
/*     */   
/*     */   private JsonElement cpu(List<CpuLoadStat> cpuStats) {
/* 283 */     JsonObject json = new JsonObject();
/*     */     
/*     */     BiFunction<List<CpuLoadStat>, ToDoubleFunction<CpuLoadStat>, JsonObject> transformer = (cpuLoadStats, extractor) -> {
/*     */         JsonObject jsonGroup = new JsonObject();
/*     */         DoubleSummaryStatistics stats = cpuLoadStats.stream().mapToDouble(extractor).summaryStatistics();
/*     */         jsonGroup.addProperty("min", stats.getMin());
/*     */         jsonGroup.addProperty("average", stats.getAverage());
/*     */         jsonGroup.addProperty("max", stats.getMax());
/*     */         return jsonGroup;
/*     */       };
/* 293 */     json.add("jvm", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::jvm));
/* 294 */     json.add("userJvm", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::userJvm));
/* 295 */     json.add("system", (JsonElement)transformer.apply(cpuStats, CpuLoadStat::system));
/*     */     
/* 297 */     return (JsonElement)json;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/serialize/JfrResultJsonSerializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */