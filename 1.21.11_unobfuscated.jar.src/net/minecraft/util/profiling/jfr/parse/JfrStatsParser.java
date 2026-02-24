/*     */ package net.minecraft.util.profiling.jfr.parse;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import jdk.jfr.consumer.RecordedEvent;
/*     */ import jdk.jfr.consumer.RecordingFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JfrStatsParser
/*     */ {
/*  47 */   private Instant recordingStarted = Instant.EPOCH;
/*  48 */   private Instant recordingEnded = Instant.EPOCH;
/*     */   
/*  50 */   private final List<ChunkGenStat> chunkGenStats = new ArrayList<>();
/*  51 */   private final List<StructureGenStat> structureGenStats = new ArrayList<>();
/*  52 */   private final List<CpuLoadStat> cpuLoadStat = new ArrayList<>();
/*  53 */   private final Map<PacketIdentification, MutableCountAndSize> receivedPackets = new HashMap<>();
/*  54 */   private final Map<PacketIdentification, MutableCountAndSize> sentPackets = new HashMap<>();
/*  55 */   private final Map<ChunkIdentification, MutableCountAndSize> readChunks = new HashMap<>();
/*  56 */   private final Map<ChunkIdentification, MutableCountAndSize> writtenChunks = new HashMap<>();
/*  57 */   private final List<FileIOStat> fileWrites = new ArrayList<>();
/*  58 */   private final List<FileIOStat> fileReads = new ArrayList<>();
/*     */   private int garbageCollections;
/*  60 */   private Duration gcTotalDuration = Duration.ZERO;
/*  61 */   private final List<GcHeapStat> gcHeapStats = new ArrayList<>();
/*  62 */   private final List<ThreadAllocationStat> threadAllocationStats = new ArrayList<>();
/*     */   
/*  64 */   private final List<FpsStat> fps = new ArrayList<>();
/*  65 */   private final List<TickTimeStat> serverTickTimes = new ArrayList<>();
/*     */   
/*  67 */   private Duration worldCreationDuration = null;
/*     */   
/*     */   private JfrStatsParser(Stream<RecordedEvent> events) {
/*  70 */     capture(events);
/*     */   }
/*     */   public static JfrStatsResult parse(Path path) {
/*     */     
/*  74 */     try { final RecordingFile recordingFile = new RecordingFile(path); 
/*  75 */       try { Iterator<RecordedEvent> iterator = new Iterator<RecordedEvent>()
/*     */           {
/*     */             public boolean hasNext() {
/*  78 */               return recordingFile.hasMoreEvents();
/*     */             }
/*     */ 
/*     */             
/*     */             public RecordedEvent next() {
/*  83 */               if (!hasNext()) {
/*  84 */                 throw new NoSuchElementException();
/*     */               }
/*     */               try {
/*  87 */                 return recordingFile.readEvent();
/*  88 */               } catch (IOException e) {
/*  89 */                 throw new UncheckedIOException(e);
/*     */               } 
/*     */             }
/*     */           };
/*  93 */         Stream<RecordedEvent> events = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 1297), false);
/*  94 */         JfrStatsResult jfrStatsResult = new JfrStatsParser(events).results();
/*  95 */         recordingFile.close(); return jfrStatsResult; } catch (Throwable throwable) { try { recordingFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*  96 */     { throw new UncheckedIOException(e); }
/*     */   
/*     */   }
/*     */   
/*     */   private JfrStatsResult results() {
/* 101 */     Duration recordingDuration = Duration.between(this.recordingStarted, this.recordingEnded);
/* 102 */     return new JfrStatsResult(this.recordingStarted, this.recordingEnded, recordingDuration, this.worldCreationDuration, this.fps, this.serverTickTimes, this.cpuLoadStat, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         GcHeapStat.summary(recordingDuration, this.gcHeapStats, this.gcTotalDuration, this.garbageCollections), 
/* 111 */         ThreadAllocationStat.summary(this.threadAllocationStats), 
/* 112 */         collectIoStats(recordingDuration, this.receivedPackets), 
/* 113 */         collectIoStats(recordingDuration, this.sentPackets), 
/* 114 */         collectIoStats(recordingDuration, this.writtenChunks), 
/* 115 */         collectIoStats(recordingDuration, this.readChunks), 
/* 116 */         FileIOStat.summary(recordingDuration, this.fileWrites), 
/* 117 */         FileIOStat.summary(recordingDuration, this.fileReads), this.chunkGenStats, this.structureGenStats);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void capture(Stream<RecordedEvent> events) {
/* 124 */     events.forEach(event -> { if (event.getEndTime().isAfter(this.recordingEnded) || this.recordingEnded.equals(Instant.EPOCH))
/*     */             this.recordingEnded = event.getEndTime();  if (event.getStartTime().isBefore(this.recordingStarted) || this.recordingStarted.equals(Instant.EPOCH))
/*     */             this.recordingStarted = event.getStartTime();  switch (event.getEventType().getName()) {
/*     */             case "minecraft.ChunkGeneration":
/*     */               this.chunkGenStats.add(ChunkGenStat.from(event)); break;
/*     */             case "minecraft.StructureGeneration":
/*     */               this.structureGenStats.add(StructureGenStat.from(event)); break;
/*     */             case "minecraft.LoadWorld":
/*     */               this.worldCreationDuration = event.getDuration(); break;
/*     */             case "minecraft.ClientFps":
/*     */               this.fps.add(FpsStat.from(event, "fps")); break;
/*     */             case "minecraft.ServerTickTime":
/*     */               this.serverTickTimes.add(TickTimeStat.from(event)); break;
/*     */             case "minecraft.PacketReceived":
/*     */               incrementPacket(event, event.getInt("bytes"), this.receivedPackets); break;
/*     */             case "minecraft.PacketSent":
/*     */               incrementPacket(event, event.getInt("bytes"), this.sentPackets); break;
/*     */             case "minecraft.ChunkRegionRead":
/*     */               incrementChunk(event, event.getInt("bytes"), this.readChunks); break;
/*     */             case "minecraft.ChunkRegionWrite":
/*     */               incrementChunk(event, event.getInt("bytes"), this.writtenChunks); break;
/*     */             case "jdk.ThreadAllocationStatistics":
/*     */               this.threadAllocationStats.add(ThreadAllocationStat.from(event)); break;
/*     */             case "jdk.GCHeapSummary":
/*     */               this.gcHeapStats.add(GcHeapStat.from(event)); break;
/*     */             case "jdk.CPULoad":
/*     */               this.cpuLoadStat.add(CpuLoadStat.from(event)); break;
/*     */             case "jdk.FileWrite":
/*     */               appendFileIO(event, this.fileWrites, "bytesWritten"); break;
/*     */             case "jdk.FileRead":
/*     */               appendFileIO(event, this.fileReads, "bytesRead"); break;
/*     */             case "jdk.GarbageCollection":
/*     */               this.garbageCollections++; this.gcTotalDuration = this.gcTotalDuration.plus(event.getDuration()); break;
/*     */           } 
/* 158 */         }); } private void incrementPacket(RecordedEvent event, int packetSize, Map<PacketIdentification, MutableCountAndSize> packets) { ((MutableCountAndSize)packets.computeIfAbsent(PacketIdentification.from(event), ignored -> new MutableCountAndSize())).increment(packetSize); }
/*     */ 
/*     */   
/*     */   private void incrementChunk(RecordedEvent event, int chunkSize, Map<ChunkIdentification, MutableCountAndSize> packets) {
/* 162 */     ((MutableCountAndSize)packets.computeIfAbsent(ChunkIdentification.from(event), ignored -> new MutableCountAndSize())).increment(chunkSize);
/*     */   }
/*     */   
/*     */   private void appendFileIO(RecordedEvent event, List<FileIOStat> stats, String sizeField) {
/* 166 */     stats.add(new FileIOStat(event.getDuration(), event.getString("path"), event.getLong(sizeField)));
/*     */   }
/*     */   
/*     */   private static <T> IoSummary<T> collectIoStats(Duration recordingDuration, Map<T, MutableCountAndSize> packetStats) {
/* 170 */     List<Pair<T, IoSummary.CountAndSize>> summaryStats = packetStats.entrySet().stream()
/* 171 */       .map(e -> Pair.of(e.getKey(), ((MutableCountAndSize)e.getValue()).toCountAndSize()))
/* 172 */       .toList();
/* 173 */     return new IoSummary(recordingDuration, summaryStats);
/*     */   }
/*     */   
/*     */   public static final class MutableCountAndSize {
/*     */     private long count;
/*     */     private long totalSize;
/*     */     
/*     */     public void increment(int bytes) {
/* 181 */       this.totalSize += bytes;
/* 182 */       this.count++;
/*     */     }
/*     */     
/*     */     public IoSummary.CountAndSize toCountAndSize() {
/* 186 */       return new IoSummary.CountAndSize(this.count, this.totalSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/parse/JfrStatsParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */