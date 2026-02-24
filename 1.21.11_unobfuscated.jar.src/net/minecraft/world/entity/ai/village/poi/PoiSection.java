/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PoiSection {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  32 */   private final Short2ObjectMap<PoiRecord> records = (Short2ObjectMap<PoiRecord>)new Short2ObjectOpenHashMap();
/*  33 */   private final Map<Holder<PoiType>, Set<PoiRecord>> byType = Maps.newHashMap();
/*     */   private final Runnable setDirty;
/*     */   private boolean isValid;
/*     */   
/*     */   public PoiSection(Runnable setDirty) {
/*  38 */     this(setDirty, true, (List<PoiRecord>)ImmutableList.of());
/*     */   }
/*     */   
/*     */   private PoiSection(Runnable setDirty, boolean isValid, List<PoiRecord> records) {
/*  42 */     this.setDirty = setDirty;
/*  43 */     this.isValid = isValid;
/*  44 */     records.forEach(this::add);
/*     */   }
/*     */   
/*     */   public Packed pack() {
/*  48 */     return new Packed(this.isValid, this.records.values().stream().map(PoiRecord::pack).toList());
/*     */   }
/*     */   
/*     */   public Stream<PoiRecord> getRecords(Predicate<Holder<PoiType>> predicate, PoiManager.Occupancy occupancy) {
/*  52 */     return this.byType.entrySet()
/*  53 */       .stream()
/*  54 */       .filter(e -> predicate.test((Holder)e.getKey()))
/*  55 */       .flatMap(e -> ((Set)e.getValue()).stream())
/*  56 */       .filter(occupancy.getTest());
/*     */   }
/*     */ 
/*     */   
/*     */   public PoiRecord add(BlockPos blockPos, Holder<PoiType> type) {
/*  61 */     PoiRecord record = new PoiRecord(blockPos, type, this.setDirty);
/*  62 */     if (add(record)) {
/*  63 */       LOGGER.debug("Added POI of type {} @ {}", type.getRegisteredName(), blockPos);
/*  64 */       this.setDirty.run();
/*  65 */       return record;
/*     */     } 
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   private boolean add(PoiRecord record) {
/*  71 */     BlockPos blockPos = record.getPos();
/*  72 */     Holder<PoiType> poiType = record.getPoiType();
/*  73 */     short key = SectionPos.sectionRelativePos(blockPos);
/*  74 */     PoiRecord oldRecord = (PoiRecord)this.records.get(key);
/*     */     
/*  76 */     if (oldRecord != null) {
/*  77 */       if (poiType.equals(oldRecord.getPoiType())) {
/*  78 */         return false;
/*     */       }
/*  80 */       Util.logAndPauseIfInIde("POI data mismatch: already registered at " + String.valueOf(blockPos));
/*     */     } 
/*     */ 
/*     */     
/*  84 */     this.records.put(key, record);
/*  85 */     ((Set<PoiRecord>)this.byType.computeIfAbsent(poiType, k -> Sets.newHashSet())).add(record);
/*  86 */     return true;
/*     */   }
/*     */   
/*     */   public void remove(BlockPos pos) {
/*  90 */     PoiRecord poiRecord = (PoiRecord)this.records.remove(SectionPos.sectionRelativePos(pos));
/*  91 */     if (poiRecord == null) {
/*  92 */       LOGGER.error("POI data mismatch: never registered at {}", pos);
/*     */       return;
/*     */     } 
/*  95 */     ((Set)this.byType.get(poiRecord.getPoiType())).remove(poiRecord);
/*     */     
/*  97 */     Objects.requireNonNull(poiRecord); Objects.requireNonNull(poiRecord); LOGGER.debug("Removed POI of type {} @ {}", LogUtils.defer(poiRecord::getPoiType), LogUtils.defer(poiRecord::getPos));
/*  98 */     this.setDirty.run();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForDebug
/*     */   public int getFreeTickets(BlockPos pos) {
/* 104 */     return (Integer)getPoiRecord(pos).<Integer>map(PoiRecord::getFreeTickets).orElse(0);
/*     */   }
/*     */   
/*     */   public boolean release(BlockPos pos) {
/* 108 */     PoiRecord record = (PoiRecord)this.records.get(SectionPos.sectionRelativePos(pos));
/* 109 */     if (record == null) {
/* 110 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI never registered at " + String.valueOf(pos)));
/*     */     }
/* 112 */     boolean success = record.releaseTicket();
/* 113 */     this.setDirty.run();
/* 114 */     return success;
/*     */   }
/*     */   
/*     */   public boolean exists(BlockPos pos, Predicate<Holder<PoiType>> predicate) {
/* 118 */     return getType(pos).filter(predicate).isPresent();
/*     */   }
/*     */   
/*     */   public Optional<Holder<PoiType>> getType(BlockPos pos) {
/* 122 */     return getPoiRecord(pos).map(PoiRecord::getPoiType);
/*     */   }
/*     */   
/*     */   private Optional<PoiRecord> getPoiRecord(BlockPos pos) {
/* 126 */     return Optional.ofNullable((PoiRecord)this.records.get(SectionPos.sectionRelativePos(pos)));
/*     */   }
/*     */   
/*     */   public Optional<net.minecraft.util.debug.DebugPoiInfo> getDebugPoiInfo(BlockPos pos) {
/* 130 */     return getPoiRecord(pos).map(net.minecraft.util.debug.DebugPoiInfo::new);
/*     */   }
/*     */   
/*     */   public void refresh(Consumer<BiConsumer<BlockPos, Holder<PoiType>>> updater) {
/* 134 */     if (!this.isValid) {
/* 135 */       Short2ObjectOpenHashMap short2ObjectOpenHashMap = new Short2ObjectOpenHashMap(this.records);
/* 136 */       clear();
/* 137 */       updater.accept((blockPos, poiType) -> {
/*     */             short key = SectionPos.sectionRelativePos(blockPos);
/*     */             PoiRecord newRecord = (PoiRecord)oldRecords.computeIfAbsent(key, ());
/*     */             add(newRecord);
/*     */           });
/* 142 */       this.isValid = true;
/* 143 */       this.setDirty.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clear() {
/* 148 */     this.records.clear();
/* 149 */     this.byType.clear();
/*     */   }
/*     */   
/*     */   boolean isValid() {
/* 153 */     return this.isValid;
/*     */   }
/*     */   public static final class Packed extends Record { private final boolean isValid; private final List<PoiRecord.Packed> records; public static final Codec<Packed> CODEC;
/* 156 */     public Packed(boolean isValid, List<PoiRecord.Packed> records) { this.isValid = isValid; this.records = records; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 156 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed; } public boolean isValid() { return this.isValid; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #156	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/village/poi/PoiSection$Packed;
/* 156 */       //   0	8	1	o	Ljava/lang/Object; } public List<PoiRecord.Packed> records() { return this.records; }
/*     */ 
/*     */     
/*     */     static {
/* 160 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.BOOL.lenientOptionalFieldOf("Valid", false).forGetter(Packed::isValid), (App)PoiRecord.Packed.CODEC.listOf().fieldOf("Records").forGetter(Packed::records)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PoiSection unpack(Runnable setDirty) {
/* 166 */       return new PoiSection(setDirty, this.isValid, this.records.stream().map(record -> record.unpack(setDirty)).toList());
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/village/poi/PoiSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */