/*     */ package net.minecraft.util.profiling.jfr;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeFormatterBuilder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Function;
/*     */ import jdk.jfr.Configuration;
/*     */ import jdk.jfr.Event;
/*     */ import jdk.jfr.FlightRecorder;
/*     */ import jdk.jfr.FlightRecorderListener;
/*     */ import jdk.jfr.Recording;
/*     */ import jdk.jfr.RecordingState;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.util.profiling.jfr.event.ChunkGenerationEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.ChunkRegionReadEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.ChunkRegionWriteEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.ClientFpsEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.PacketSentEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.StructureGenerationEvent;
/*     */ import net.minecraft.util.profiling.jfr.event.WorldLoadFinishedEvent;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class JfrProfiler implements JvmProfiler {
/*  56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String ROOT_CATEGORY = "Minecraft";
/*     */   
/*     */   public static final String WORLD_GEN_CATEGORY = "World Generation";
/*     */   
/*     */   public static final String TICK_CATEGORY = "Ticking";
/*     */   public static final String NETWORK_CATEGORY = "Network";
/*     */   public static final String STORAGE_CATEGORY = "Storage";
/*  65 */   private static final List<Class<? extends Event>> CUSTOM_EVENTS = (List)List.of(ChunkGenerationEvent.class, ChunkRegionReadEvent.class, ChunkRegionWriteEvent.class, PacketReceivedEvent.class, PacketSentEvent.class, NetworkSummaryEvent.class, ServerTickTimeEvent.class, ClientFpsEvent.class, StructureGenerationEvent.class, WorldLoadFinishedEvent.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FLIGHT_RECORDER_CONFIG = "/flightrecorder-config.jfc";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd-HHmmss").toFormatter(Locale.ROOT).withZone(ZoneId.systemDefault());
/*     */   
/*  81 */   private static final JfrProfiler INSTANCE = new JfrProfiler();
/*     */   
/*     */   private Recording recording;
/*     */   
/*     */   private int currentFPS;
/*     */   private float currentAverageTickTimeServer;
/*  87 */   private final Map<String, NetworkSummaryEvent.SumAggregation> networkTrafficByAddress = new ConcurrentHashMap<>(); private final Runnable periodicClientFps = () -> new ClientFpsEvent(this.currentFPS).commit(); private final Runnable periodicServerTickTime = () -> new ServerTickTimeEvent(this.currentAverageTickTimeServer).commit();
/*     */   private final Runnable periodicNetworkSummary;
/*     */   
/*     */   private JfrProfiler() {
/*  91 */     this.periodicNetworkSummary = (() -> {
/*     */         Iterator<NetworkSummaryEvent.SumAggregation> iterator = this.networkTrafficByAddress.values().iterator();
/*     */         
/*     */         while (iterator.hasNext()) {
/*     */           ((NetworkSummaryEvent.SumAggregation)iterator.next()).commitEvent();
/*     */           
/*     */           iterator.remove();
/*     */         } 
/*     */       });
/*     */     
/* 101 */     CUSTOM_EVENTS.forEach(FlightRecorder::register);
/* 102 */     registerPeriodicEvents();
/*     */ 
/*     */     
/* 105 */     FlightRecorder.addListener(new FlightRecorderListener()
/*     */         {
/*     */           public void recordingStateChanged(Recording rec) {
/*     */             // Byte code:
/*     */             //   0: getstatic net/minecraft/util/profiling/jfr/JfrProfiler$3.$SwitchMap$jdk$jfr$RecordingState : [I
/*     */             //   3: aload_1
/*     */             //   4: invokevirtual getState : ()Ljdk/jfr/RecordingState;
/*     */             //   7: invokevirtual ordinal : ()I
/*     */             //   10: iaload
/*     */             //   11: tableswitch default -> 54, 1 -> 44, 2 -> 54, 3 -> 54, 4 -> 54, 5 -> 54
/*     */             //   44: aload_0
/*     */             //   45: getfield this$0 : Lnet/minecraft/util/profiling/jfr/JfrProfiler;
/*     */             //   48: invokevirtual registerPeriodicEvents : ()V
/*     */             //   51: goto -> 54
/*     */             //   54: return
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #108	-> 0
/*     */             //   #109	-> 44
/*     */             //   #113	-> 54
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   0	55	0	this	Lnet/minecraft/util/profiling/jfr/JfrProfiler$1;
/*     */             //   0	55	1	rec	Ljdk/jfr/Recording;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerPeriodicEvents() {
/* 118 */     addPeriodicEvent((Class)ClientFpsEvent.class, this.periodicClientFps);
/* 119 */     addPeriodicEvent((Class)ServerTickTimeEvent.class, this.periodicServerTickTime);
/* 120 */     addPeriodicEvent((Class)NetworkSummaryEvent.class, this.periodicNetworkSummary);
/*     */   }
/*     */   
/*     */   private static void addPeriodicEvent(Class<? extends Event> eventClass, Runnable runnable) {
/* 124 */     FlightRecorder.removePeriodicEvent(runnable);
/* 125 */     FlightRecorder.addPeriodicEvent(eventClass, runnable);
/*     */   }
/*     */   
/*     */   public static JfrProfiler getInstance() {
/* 129 */     return INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean start(Environment environment) {
/* 134 */     URL resource = JfrProfiler.class.getResource("/flightrecorder-config.jfc");
/* 135 */     if (resource == null) {
/* 136 */       LOGGER.warn("Could not find default flight recorder config at {}", "/flightrecorder-config.jfc");
/* 137 */       return false;
/*     */     } 
/*     */     
/* 140 */     try { BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)); 
/* 141 */       try { boolean bool = start(reader, environment);
/* 142 */         reader.close(); return bool; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 143 */     { LOGGER.warn("Failed to start flight recorder using configuration at {}", resource, e);
/* 144 */       return false; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public Path stop() {
/* 150 */     if (this.recording == null) {
/* 151 */       throw new IllegalStateException("Not currently profiling");
/*     */     }
/*     */     
/* 154 */     this.networkTrafficByAddress.clear();
/*     */     
/* 156 */     Path report = this.recording.getDestination();
/* 157 */     this.recording.stop();
/*     */     
/* 159 */     return report;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 164 */     return (this.recording != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAvailable() {
/* 169 */     return FlightRecorder.isAvailable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean start(Reader configurationFile, Environment environment) {
/* 181 */     if (isRunning()) {
/* 182 */       LOGGER.warn("Profiling already in progress");
/* 183 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 187 */       Configuration jfrConfig = Configuration.create(configurationFile);
/* 188 */       String startTimestamp = DATE_TIME_FORMATTER.format(Instant.now());
/* 189 */       this.recording = (Recording)Util.make(new Recording(jfrConfig), self -> {
/*     */             Objects.requireNonNull(self); CUSTOM_EVENTS.forEach(self::enable);
/*     */             self.setDumpOnExit(true);
/*     */             self.setToDisk(true);
/*     */             self.setName(String.format(Locale.ROOT, "%s-%s-%s", new Object[] { environment.getDescription(), SharedConstants.getCurrentVersion().name(), startTimestamp }));
/*     */           });
/* 195 */       Path destination = Paths.get(String.format(Locale.ROOT, "debug/%s-%s.jfr", new Object[] { environment.getDescription(), startTimestamp }), new String[0]);
/* 196 */       FileUtil.createDirectoriesSafe(destination.getParent());
/* 197 */       this.recording.setDestination(destination);
/* 198 */       this.recording.start();
/*     */       
/* 200 */       setupSummaryListener();
/* 201 */     } catch (IOException|java.text.ParseException exception) {
/* 202 */       LOGGER.warn("Failed to start jfr profiling", exception);
/* 203 */       return false;
/*     */     } 
/* 205 */     LOGGER.info("Started flight recorder profiling id({}):name({}) - will dump to {} on exit or stop command", new Object[] { this.recording.getId(), this.recording.getName(), this.recording.getDestination() });
/* 206 */     return true;
/*     */   }
/*     */   
/*     */   private void setupSummaryListener() {
/* 210 */     FlightRecorder.addListener(new FlightRecorderListener() { final SummaryReporter summaryReporter; {
/* 211 */             this.summaryReporter = new SummaryReporter(() -> JfrProfiler.this.recording = null);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void recordingStateChanged(Recording rec) {
/*     */             // Byte code:
/*     */             //   0: aload_1
/*     */             //   1: aload_0
/*     */             //   2: getfield this$0 : Lnet/minecraft/util/profiling/jfr/JfrProfiler;
/*     */             //   5: getfield recording : Ljdk/jfr/Recording;
/*     */             //   8: if_acmpeq -> 12
/*     */             //   11: return
/*     */             //   12: getstatic net/minecraft/util/profiling/jfr/JfrProfiler$3.$SwitchMap$jdk$jfr$RecordingState : [I
/*     */             //   15: aload_1
/*     */             //   16: invokevirtual getState : ()Ljdk/jfr/RecordingState;
/*     */             //   19: invokevirtual ordinal : ()I
/*     */             //   22: iaload
/*     */             //   23: tableswitch default -> 75, 1 -> 56, 2 -> 75, 3 -> 75, 4 -> 75, 5 -> 75
/*     */             //   56: aload_0
/*     */             //   57: getfield summaryReporter : Lnet/minecraft/util/profiling/jfr/SummaryReporter;
/*     */             //   60: aload_1
/*     */             //   61: invokevirtual getDestination : ()Ljava/nio/file/Path;
/*     */             //   64: invokevirtual recordingStopped : (Ljava/nio/file/Path;)V
/*     */             //   67: aload_0
/*     */             //   68: invokestatic removeListener : (Ljdk/jfr/FlightRecorderListener;)Z
/*     */             //   71: pop
/*     */             //   72: goto -> 75
/*     */             //   75: return
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #215	-> 0
/*     */             //   #216	-> 11
/*     */             //   #218	-> 12
/*     */             //   #220	-> 56
/*     */             //   #221	-> 67
/*     */             //   #222	-> 72
/*     */             //   #226	-> 75
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   0	76	0	this	Lnet/minecraft/util/profiling/jfr/JfrProfiler$2;
/*     */             //   0	76	1	rec	Ljdk/jfr/Recording;
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClientTick(int fps) {
/* 232 */     if (ClientFpsEvent.TYPE.isEnabled()) {
/* 233 */       this.currentFPS = fps;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onServerTick(float currentAverageTickTime) {
/* 239 */     if (ServerTickTimeEvent.TYPE.isEnabled()) {
/* 240 */       this.currentAverageTickTimeServer = currentAverageTickTime;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPacketReceived(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int readableBytes) {
/* 246 */     if (PacketReceivedEvent.TYPE.isEnabled()) {
/* 247 */       new PacketReceivedEvent(protocol.id(), packetId.flow().id(), packetId.id().toString(), remoteAddress, readableBytes).commit();
/*     */     }
/*     */     
/* 250 */     if (NetworkSummaryEvent.TYPE.isEnabled()) {
/* 251 */       networkStatFor(remoteAddress).trackReceivedPacket(readableBytes);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPacketSent(ConnectionProtocol protocol, PacketType<?> packetId, SocketAddress remoteAddress, int writtenBytes) {
/* 257 */     if (PacketSentEvent.TYPE.isEnabled()) {
/* 258 */       new PacketSentEvent(protocol.id(), packetId.flow().id(), packetId.id().toString(), remoteAddress, writtenBytes).commit();
/*     */     }
/*     */     
/* 261 */     if (NetworkSummaryEvent.TYPE.isEnabled()) {
/* 262 */       networkStatFor(remoteAddress).trackSentPacket(writtenBytes);
/*     */     }
/*     */   }
/*     */   
/*     */   private NetworkSummaryEvent.SumAggregation networkStatFor(SocketAddress remoteAddress) {
/* 267 */     return this.networkTrafficByAddress.computeIfAbsent(remoteAddress.toString(), net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent.SumAggregation::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRegionFileRead(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int readBytes) {
/* 272 */     if (ChunkRegionReadEvent.TYPE.isEnabled()) {
/* 273 */       new ChunkRegionReadEvent(info, pos, version, readBytes).commit();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRegionFileWrite(RegionStorageInfo info, ChunkPos pos, RegionFileVersion version, int writtenBytes) {
/* 279 */     if (ChunkRegionWriteEvent.TYPE.isEnabled()) {
/* 280 */       new ChunkRegionWriteEvent(info, pos, version, writtenBytes).commit();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfiledDuration onWorldLoadedStarted() {
/* 286 */     if (!WorldLoadFinishedEvent.TYPE.isEnabled()) {
/* 287 */       return null;
/*     */     }
/* 289 */     WorldLoadFinishedEvent event = new WorldLoadFinishedEvent();
/* 290 */     event.begin();
/* 291 */     return ignored -> event.commit();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfiledDuration onChunkGenerate(ChunkPos pos, ResourceKey<Level> dimension, String name) {
/* 296 */     if (!ChunkGenerationEvent.TYPE.isEnabled()) {
/* 297 */       return null;
/*     */     }
/* 299 */     ChunkGenerationEvent event = new ChunkGenerationEvent(pos, dimension, name);
/* 300 */     event.begin();
/* 301 */     return ignored -> event.commit();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfiledDuration onStructureGenerate(ChunkPos sourceChunkPos, ResourceKey<Level> dimension, Holder<Structure> structure) {
/* 306 */     if (!StructureGenerationEvent.TYPE.isEnabled()) {
/* 307 */       return null;
/*     */     }
/* 309 */     StructureGenerationEvent event = new StructureGenerationEvent(sourceChunkPos, structure, dimension);
/* 310 */     event.begin();
/* 311 */     return success -> {
/*     */         event.success = success;
/*     */         event.commit();
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/JfrProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */