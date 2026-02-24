/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class PoolElementStructurePiece extends StructurePiece {
/*     */   protected final StructurePoolElement element;
/*     */   protected BlockPos position;
/*     */   private final int groundLevelDelta;
/*     */   protected final Rotation rotation;
/*  33 */   private final List<JigsawJunction> junctions = Lists.newArrayList();
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final LiquidSettings liquidSettings;
/*     */   
/*     */   public PoolElementStructurePiece(StructureTemplateManager structureTemplateManager, StructurePoolElement element, BlockPos position, int groundLevelDelta, Rotation rotation, BoundingBox boundingBox, LiquidSettings liquidSettings) {
/*  38 */     super(StructurePieceType.JIGSAW, 0, boundingBox);
/*  39 */     this.structureTemplateManager = structureTemplateManager;
/*  40 */     this.element = element;
/*  41 */     this.position = position;
/*  42 */     this.groundLevelDelta = groundLevelDelta;
/*  43 */     this.rotation = rotation;
/*  44 */     this.liquidSettings = liquidSettings;
/*     */   }
/*     */   
/*     */   public PoolElementStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
/*  48 */     super(StructurePieceType.JIGSAW, tag);
/*  49 */     this.structureTemplateManager = context.structureTemplateManager();
/*  50 */     this.position = new BlockPos(tag.getIntOr("PosX", 0), tag.getIntOr("PosY", 0), tag.getIntOr("PosZ", 0));
/*  51 */     this.groundLevelDelta = tag.getIntOr("ground_level_delta", 0);
/*     */     
/*  53 */     RegistryOps registryOps = context.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  54 */     this.element = (StructurePoolElement)tag.read("pool_element", StructurePoolElement.CODEC, (DynamicOps)registryOps).orElseThrow(() -> new IllegalStateException("Invalid pool element found"));
/*     */     
/*  56 */     this.rotation = tag.read("rotation", Rotation.LEGACY_CODEC).orElseThrow();
/*  57 */     this.boundingBox = this.element.getBoundingBox(this.structureTemplateManager, this.position, this.rotation);
/*     */     
/*  59 */     ListTag junctionsTag = tag.getListOrEmpty("junctions");
/*  60 */     this.junctions.clear();
/*  61 */     junctionsTag.forEach(junctionTag -> this.junctions.add(JigsawJunction.deserialize(new Dynamic(ops, junctionTag))));
/*     */     
/*  63 */     this.liquidSettings = tag.read("liquid_settings", LiquidSettings.CODEC).orElse(JigsawStructure.DEFAULT_LIQUID_SETTINGS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  68 */     tag.putInt("PosX", this.position.getX());
/*  69 */     tag.putInt("PosY", this.position.getY());
/*  70 */     tag.putInt("PosZ", this.position.getZ());
/*  71 */     tag.putInt("ground_level_delta", this.groundLevelDelta);
/*     */     
/*  73 */     RegistryOps registryOps = context.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/*  74 */     tag.store("pool_element", StructurePoolElement.CODEC, (DynamicOps)registryOps, this.element);
/*     */     
/*  76 */     tag.store("rotation", Rotation.LEGACY_CODEC, this.rotation);
/*  77 */     ListTag junctionsTag = new ListTag();
/*  78 */     for (JigsawJunction junction : this.junctions) {
/*  79 */       junctionsTag.add(junction.serialize((DynamicOps)registryOps).getValue());
/*     */     }
/*  81 */     tag.put("junctions", (Tag)junctionsTag);
/*     */     
/*  83 */     if (this.liquidSettings != JigsawStructure.DEFAULT_LIQUID_SETTINGS) {
/*  84 */       tag.store("liquid_settings", LiquidSettings.CODEC, (DynamicOps)registryOps, this.liquidSettings);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  90 */     place(level, structureManager, generator, random, chunkBB, referencePos, false);
/*     */   }
/*     */   
/*     */   public void place(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, BlockPos referencePos, boolean keepJigsaws) {
/*  94 */     this.element.place(this.structureTemplateManager, level, structureManager, generator, this.position, referencePos, this.rotation, chunkBB, random, this.liquidSettings, keepJigsaws);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(int dx, int dy, int dz) {
/*  99 */     super.move(dx, dy, dz);
/* 100 */     this.position = this.position.offset(dx, dy, dz);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 105 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 110 */     return String.format(Locale.ROOT, "<%s | %s | %s | %s>", new Object[] { getClass().getSimpleName(), this.position, this.rotation, this.element });
/*     */   }
/*     */   
/*     */   public StructurePoolElement getElement() {
/* 114 */     return this.element;
/*     */   }
/*     */   
/*     */   public BlockPos getPosition() {
/* 118 */     return this.position;
/*     */   }
/*     */   
/*     */   public int getGroundLevelDelta() {
/* 122 */     return this.groundLevelDelta;
/*     */   }
/*     */   
/*     */   public void addJunction(JigsawJunction junction) {
/* 126 */     this.junctions.add(junction);
/*     */   }
/*     */   
/*     */   public List<JigsawJunction> getJunctions() {
/* 130 */     return this.junctions;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/PoolElementStructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */