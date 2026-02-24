/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.ChunkSkyLightSources;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.ticks.BlackholeTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ 
/*     */ public class ImposterProtoChunk
/*     */   extends ProtoChunk {
/*     */   private final LevelChunk wrapped;
/*     */   private final boolean allowWrites;
/*     */   
/*     */   public ImposterProtoChunk(LevelChunk wrapped, boolean allowWrites) {
/*  38 */     super(wrapped.getPos(), UpgradeData.EMPTY, wrapped.levelHeightAccessor, wrapped.getLevel().palettedContainerFactory(), wrapped.getBlendingData());
/*     */     
/*  40 */     this.wrapped = wrapped;
/*  41 */     this.allowWrites = allowWrites;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/*  46 */     return this.wrapped.getBlockEntity(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/*  51 */     return this.wrapped.getBlockState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/*  56 */     return this.wrapped.getFluidState(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection getSection(int sectionIndex) {
/*  61 */     if (this.allowWrites) {
/*  62 */       return this.wrapped.getSection(sectionIndex);
/*     */     }
/*  64 */     return super.getSection(sectionIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
/*  69 */     if (this.allowWrites) {
/*  70 */       return this.wrapped.setBlockState(pos, state, flags);
/*     */     }
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/*  77 */     if (this.allowWrites) {
/*  78 */       this.wrapped.setBlockEntity(blockEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntity(Entity entity) {
/*  84 */     if (this.allowWrites) {
/*  85 */       this.wrapped.addEntity(entity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistedStatus(ChunkStatus status) {
/*  91 */     if (this.allowWrites) {
/*  92 */       super.setPersistedStatus(status);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunkSection[] getSections() {
/*  98 */     return this.wrapped.getSections();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeightmap(Heightmap.Types key, long[] data) {}
/*     */ 
/*     */   
/*     */   private Heightmap.Types fixType(Heightmap.Types type) {
/* 106 */     if (type == Heightmap.Types.WORLD_SURFACE_WG) {
/* 107 */       return Heightmap.Types.WORLD_SURFACE;
/*     */     }
/*     */     
/* 110 */     if (type == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 111 */       return Heightmap.Types.OCEAN_FLOOR;
/*     */     }
/*     */     
/* 114 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Heightmap getOrCreateHeightmapUnprimed(Heightmap.Types type) {
/* 119 */     return this.wrapped.getOrCreateHeightmapUnprimed(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 124 */     return this.wrapped.getHeight(fixType(type), x, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 129 */     return this.wrapped.getNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkPos getPos() {
/* 134 */     return this.wrapped.getPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureStart getStartForStructure(Structure structure) {
/* 139 */     return this.wrapped.getStartForStructure(structure);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartForStructure(Structure structure, StructureStart structureStart) {}
/*     */ 
/*     */   
/*     */   public Map<Structure, StructureStart> getAllStarts() {
/* 148 */     return this.wrapped.getAllStarts();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllStarts(Map<Structure, StructureStart> starts) {}
/*     */ 
/*     */   
/*     */   public LongSet getReferencesForStructure(Structure structure) {
/* 157 */     return this.wrapped.getReferencesForStructure(structure);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReferenceForStructure(Structure structure, long reference) {}
/*     */ 
/*     */   
/*     */   public Map<Structure, LongSet> getAllReferences() {
/* 166 */     return this.wrapped.getAllReferences();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllReferences(Map<Structure, LongSet> data) {}
/*     */ 
/*     */   
/*     */   public void markUnsaved() {
/* 175 */     this.wrapped.markUnsaved();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeSerialized() {
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryMarkSaved() {
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnsaved() {
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 196 */     return this.wrapped.getPersistedStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void markPosForPostprocessing(BlockPos blockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockEntityNbt(CompoundTag entityTag) {}
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbt(BlockPos blockPos) {
/* 213 */     return this.wrapped.getBlockEntityNbt(blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider registryAccess) {
/* 218 */     return this.wrapped.getBlockEntityNbtForSaving(blockPos, registryAccess);
/*     */   }
/*     */ 
/*     */   
/*     */   public void findBlocks(Predicate<BlockState> predicate, BiConsumer<BlockPos, BlockState> consumer) {
/* 223 */     this.wrapped.findBlocks(predicate, consumer);
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/* 228 */     if (this.allowWrites) {
/* 229 */       return this.wrapped.getBlockTicks();
/*     */     }
/* 231 */     return BlackholeTickAccess.emptyContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/* 236 */     if (this.allowWrites) {
/* 237 */       return this.wrapped.getFluidTicks();
/*     */     }
/* 239 */     return BlackholeTickAccess.emptyContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long currentTick) {
/* 244 */     return this.wrapped.getTicksForSerialization(currentTick);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlendingData getBlendingData() {
/* 249 */     return this.wrapped.getBlendingData();
/*     */   }
/*     */ 
/*     */   
/*     */   public CarvingMask getCarvingMask() {
/* 254 */     if (this.allowWrites) {
/* 255 */       return super.getCarvingMask();
/*     */     }
/* 257 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */ 
/*     */   
/*     */   public CarvingMask getOrCreateCarvingMask() {
/* 262 */     if (this.allowWrites) {
/* 263 */       return super.getOrCreateCarvingMask();
/*     */     }
/* 265 */     throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Meaningless in this context"));
/*     */   }
/*     */   
/*     */   public LevelChunk getWrapped() {
/* 269 */     return this.wrapped;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLightCorrect() {
/* 274 */     return this.wrapped.isLightCorrect();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightCorrect(boolean isLightCorrect) {
/* 279 */     this.wrapped.setLightCorrect(isLightCorrect);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillBiomesFromNoise(BiomeResolver biomeResolver, Climate.Sampler sampler) {
/* 284 */     if (this.allowWrites) {
/* 285 */       this.wrapped.fillBiomesFromNoise(biomeResolver, sampler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void initializeLightSources() {
/* 291 */     this.wrapped.initializeLightSources();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChunkSkyLightSources getSkyLightSources() {
/* 296 */     return this.wrapped.getSkyLightSources();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/ImposterProtoChunk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */