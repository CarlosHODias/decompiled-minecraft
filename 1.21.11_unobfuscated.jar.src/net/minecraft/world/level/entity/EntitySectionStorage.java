/*     */ package net.minecraft.world.level.entity;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSortedSet;
/*     */ import java.util.Objects;
/*     */ import java.util.PrimitiveIterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.AbortableIterationConsumer;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntitySectionStorage<T extends EntityAccess>
/*     */ {
/*     */   public static final int CHONKY_ENTITY_SEARCH_GRACE = 2;
/*     */   public static final int MAX_NON_CHONKY_ENTITY_SIZE = 4;
/*     */   private final Class<T> entityClass;
/*     */   private final Long2ObjectFunction<Visibility> intialSectionVisibility;
/*  32 */   private final Long2ObjectMap<EntitySection<T>> sections = (Long2ObjectMap<EntitySection<T>>)new Long2ObjectOpenHashMap();
/*     */ 
/*     */   
/*  35 */   private final LongSortedSet sectionIds = (LongSortedSet)new LongAVLTreeSet();
/*     */   
/*     */   public EntitySectionStorage(Class<T> entityClass, Long2ObjectFunction<Visibility> intialSectionVisibility) {
/*  38 */     this.entityClass = entityClass;
/*  39 */     this.intialSectionVisibility = intialSectionVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachAccessibleNonEmptySection(AABB bb, AbortableIterationConsumer<EntitySection<T>> output) {
/*  44 */     int xMin = SectionPos.posToSectionCoord(bb.minX - 2.0D);
/*  45 */     int yMin = SectionPos.posToSectionCoord(bb.minY - 4.0D);
/*  46 */     int zMin = SectionPos.posToSectionCoord(bb.minZ - 2.0D);
/*     */     
/*  48 */     int xMax = SectionPos.posToSectionCoord(bb.maxX + 2.0D);
/*  49 */     int yMax = SectionPos.posToSectionCoord(bb.maxY + 0.0D);
/*  50 */     int zMax = SectionPos.posToSectionCoord(bb.maxZ + 2.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     for (int x = xMin; x <= xMax; x++) {
/*  56 */       long lowestAbsoluteSectionKey = SectionPos.asLong(x, 0, 0);
/*  57 */       long highestAbsoluteSectionKey = SectionPos.asLong(x, -1, -1);
/*  58 */       LongBidirectionalIterator longBidirectionalIterator = this.sectionIds.subSet(lowestAbsoluteSectionKey, highestAbsoluteSectionKey + 1L).iterator();
/*  59 */       while (longBidirectionalIterator.hasNext()) {
/*  60 */         long sectionKey = longBidirectionalIterator.nextLong();
/*  61 */         int y = SectionPos.y(sectionKey);
/*  62 */         int z = SectionPos.z(sectionKey);
/*  63 */         if (y >= yMin && y <= yMax && z >= zMin && z <= zMax) {
/*  64 */           EntitySection<T> entitySection = (EntitySection<T>)this.sections.get(sectionKey);
/*  65 */           if (entitySection != null && !entitySection.isEmpty() && entitySection.getStatus().isAccessible() && 
/*  66 */             output.accept(entitySection).shouldAbort()) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public LongStream getExistingSectionPositionsInChunk(long chunkKey) {
/*  76 */     int x = ChunkPos.getX(chunkKey);
/*  77 */     int z = ChunkPos.getZ(chunkKey);
/*  78 */     LongSortedSet chunkSections = getChunkSections(x, z);
/*  79 */     if (chunkSections.isEmpty()) {
/*  80 */       return LongStream.empty();
/*     */     }
/*  82 */     LongBidirectionalIterator longBidirectionalIterator = chunkSections.iterator();
/*  83 */     return StreamSupport.longStream(Spliterators.spliteratorUnknownSize((PrimitiveIterator.OfLong)longBidirectionalIterator, 1301), false);
/*     */   }
/*     */   
/*     */   private LongSortedSet getChunkSections(int x, int z) {
/*  87 */     long lowestAbsoluteSectionKey = SectionPos.asLong(x, 0, z);
/*  88 */     long highestAbsoluteSectionKey = SectionPos.asLong(x, -1, z);
/*  89 */     return this.sectionIds.subSet(lowestAbsoluteSectionKey, highestAbsoluteSectionKey + 1L);
/*     */   }
/*     */   
/*     */   public Stream<EntitySection<T>> getExistingSectionsInChunk(long chunkKey) {
/*  93 */     Objects.requireNonNull(this.sections); return getExistingSectionPositionsInChunk(chunkKey).<EntitySection<T>>mapToObj(this.sections::get).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   private static long getChunkKeyFromSectionKey(long sectionPos) {
/*  97 */     return ChunkPos.asLong(SectionPos.x(sectionPos), SectionPos.z(sectionPos));
/*     */   }
/*     */   
/*     */   public EntitySection<T> getOrCreateSection(long key) {
/* 101 */     return (EntitySection<T>)this.sections.computeIfAbsent(key, this::createSection);
/*     */   }
/*     */   
/*     */   public EntitySection<T> getSection(long key) {
/* 105 */     return (EntitySection<T>)this.sections.get(key);
/*     */   }
/*     */   
/*     */   private EntitySection<T> createSection(long sectionPos) {
/* 109 */     long chunkPos = getChunkKeyFromSectionKey(sectionPos);
/* 110 */     Visibility chunkStatus = (Visibility)this.intialSectionVisibility.get(chunkPos);
/* 111 */     this.sectionIds.add(sectionPos);
/* 112 */     return new EntitySection<>(this.entityClass, chunkStatus);
/*     */   }
/*     */   
/*     */   public LongSet getAllChunksWithExistingSections() {
/* 116 */     LongOpenHashSet longOpenHashSet = new LongOpenHashSet();
/* 117 */     this.sections.keySet().forEach(sectionKey -> chunks.add(getChunkKeyFromSectionKey(sectionKey)));
/* 118 */     return (LongSet)longOpenHashSet;
/*     */   }
/*     */   
/*     */   public void getEntities(AABB bb, AbortableIterationConsumer<T> output) {
/* 122 */     forEachAccessibleNonEmptySection(bb, section -> section.getEntities(bb, output));
/*     */   }
/*     */   
/*     */   public <U extends T> void getEntities(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<U> consumer) {
/* 126 */     forEachAccessibleNonEmptySection(bb, section -> section.getEntities(type, bb, consumer));
/*     */   }
/*     */   
/*     */   public void remove(long sectionKey) {
/* 130 */     this.sections.remove(sectionKey);
/* 131 */     this.sectionIds.remove(sectionKey);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public int count() {
/* 136 */     return this.sectionIds.size();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/EntitySectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */