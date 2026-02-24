/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SimpleRegionStorage
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final IOWorker worker;
/*     */   private final DataFixer fixerUpper;
/*     */   private final DataFixTypes dataFixType;
/*     */   private final Supplier<LegacyTagFixer> legacyFixer;
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType) {
/*  31 */     this(info, folder, fixerUpper, syncWrites, dataFixType, LegacyTagFixer.EMPTY);
/*     */   }
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType, Supplier<LegacyTagFixer> legacyFixer) {
/*  35 */     this.fixerUpper = fixerUpper;
/*  36 */     this.dataFixType = dataFixType;
/*  37 */     this.worker = new IOWorker(info, folder, syncWrites);
/*  38 */     Objects.requireNonNull(legacyFixer); this.legacyFixer = (Supplier<LegacyTagFixer>)Suppliers.memoize(legacyFixer::get);
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/*  42 */     return this.worker.isOldChunkAround(pos, range);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<CompoundTag>> read(ChunkPos pos) {
/*  46 */     return this.worker.loadAsync(pos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos pos, CompoundTag value) {
/*  50 */     return write(pos, () -> value);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
/*  54 */     markChunkDone(pos);
/*  55 */     return this.worker.store(pos, supplier);
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion, CompoundTag dataFixContextTag) {
/*  59 */     int version = NbtUtils.getDataVersion(chunkTag, defaultVersion);
/*  60 */     if (version == SharedConstants.getCurrentVersion().dataVersion().version()) {
/*  61 */       return chunkTag;
/*     */     }
/*     */     
/*     */     try {
/*  65 */       chunkTag = ((LegacyTagFixer)this.legacyFixer.get()).applyFix(chunkTag);
/*     */ 
/*     */       
/*  68 */       injectDatafixingContext(chunkTag, dataFixContextTag);
/*  69 */       chunkTag = this.dataFixType.updateToCurrentVersion(this.fixerUpper, chunkTag, Math.max(((LegacyTagFixer)this.legacyFixer.get()).targetDataVersion(), version));
/*  70 */       removeDatafixingContext(chunkTag);
/*     */ 
/*     */       
/*  73 */       NbtUtils.addCurrentDataVersion(chunkTag);
/*     */       
/*  75 */       return chunkTag;
/*  76 */     } catch (Exception e) {
/*  77 */       CrashReport report = CrashReport.forThrowable(e, "Updated chunk");
/*  78 */       CrashReportCategory details = report.addCategory("Updated chunk details");
/*  79 */       details.setDetail("Data version", version);
/*  80 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion) {
/*  85 */     return upgradeChunkTag(chunkTag, defaultVersion, null);
/*     */   }
/*     */   
/*     */   public Dynamic<Tag> upgradeChunkTag(Dynamic<Tag> chunkTag, int defaultVersion) {
/*  89 */     return new Dynamic(chunkTag.getOps(), upgradeChunkTag((CompoundTag)chunkTag.getValue(), defaultVersion, null));
/*     */   }
/*     */   
/*     */   public static void injectDatafixingContext(CompoundTag chunkTag, CompoundTag contextTag) {
/*  93 */     if (contextTag != null) {
/*  94 */       chunkTag.put("__context", (Tag)contextTag);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void removeDatafixingContext(CompoundTag chunkTag) {
/*  99 */     chunkTag.remove("__context");
/*     */   }
/*     */   
/*     */   protected void markChunkDone(ChunkPos pos) {
/* 103 */     ((LegacyTagFixer)this.legacyFixer.get()).markChunkDone(pos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> synchronize(boolean flush) {
/* 107 */     return this.worker.synchronize(flush);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 112 */     this.worker.close();
/*     */   }
/*     */   
/*     */   public ChunkScanAccess chunkScanner() {
/* 116 */     return this.worker;
/*     */   }
/*     */   
/*     */   public RegionStorageInfo storageInfo() {
/* 120 */     return this.worker.storageInfo();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/storage/SimpleRegionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */