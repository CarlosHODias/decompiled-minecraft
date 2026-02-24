/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ public abstract class StructurePoolElement
/*     */ {
/*  31 */   public static final Codec<StructurePoolElement> CODEC = BuiltInRegistries.STRUCTURE_POOL_ELEMENT.byNameCodec().dispatch("element_type", StructurePoolElement::getType, StructurePoolElementType::codec);
/*     */   
/*  33 */   private static final Holder<StructureProcessorList> EMPTY = Holder.direct(new StructureProcessorList(List.of()));
/*     */   
/*     */   protected static <E extends StructurePoolElement> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() {
/*  36 */     return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(StructurePoolElement::getProjection);
/*     */   }
/*     */   
/*     */   private volatile StructureTemplatePool.Projection projection;
/*     */   
/*     */   protected StructurePoolElement(StructureTemplatePool.Projection projection) {
/*  42 */     this.projection = projection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleDataMarker(LevelAccessor level, StructureTemplate.StructureBlockInfo dataMarker, BlockPos position, Rotation rotation, RandomSource random, BoundingBox chunkBB) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructurePoolElement setProjection(StructureTemplatePool.Projection projection) {
/*  59 */     this.projection = projection;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public StructureTemplatePool.Projection getProjection() {
/*  64 */     StructureTemplatePool.Projection projection = this.projection;
/*  65 */     if (projection == null) {
/*  66 */       throw new IllegalStateException();
/*     */     }
/*  68 */     return projection;
/*     */   }
/*     */   
/*     */   public int getGroundLevelDelta() {
/*  72 */     return 1;
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, EmptyPoolElement> empty() {
/*  76 */     return p -> EmptyPoolElement.INSTANCE;
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String location) {
/*  80 */     return p -> new LegacySinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String location, Holder<StructureProcessorList> processors) {
/*  84 */     return p -> new LegacySinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location) {
/*  88 */     return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, Holder<StructureProcessorList> processors) {
/*  92 */     return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.empty());
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, LiquidSettings overrideLiquidSettings) {
/*  96 */     return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.of(overrideLiquidSettings));
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, Holder<StructureProcessorList> processors, LiquidSettings overrideLiquidSettings) {
/* 100 */     return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.of(overrideLiquidSettings));
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, FeaturePoolElement> feature(Holder<PlacedFeature> feature) {
/* 104 */     return p -> new FeaturePoolElement(feature, p);
/*     */   }
/*     */   
/*     */   public static Function<StructureTemplatePool.Projection, ListPoolElement> list(List<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> elements) {
/* 108 */     return p -> new ListPoolElement((List<StructurePoolElement>)elements.stream().map(()).collect(Collectors.toList()), p);
/*     */   }
/*     */   
/*     */   public abstract Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation);
/*     */   
/*     */   public abstract List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource);
/*     */   
/*     */   public abstract BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation);
/*     */   
/*     */   public abstract boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean);
/*     */   
/*     */   public abstract StructurePoolElementType<?> getType();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/StructurePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */