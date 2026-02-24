/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.data.worldgen.Pools;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.StructureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JigsawBlock;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public class FeaturePoolElement extends StructurePoolElement {
/*    */   static {
/* 30 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)PlacedFeature.CODEC.fieldOf("feature").forGetter(()), (App)projectionCodec()).apply((Applicative)i, FeaturePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<FeaturePoolElement> CODEC;
/* 35 */   private static final Identifier DEFAULT_JIGSAW_NAME = Identifier.withDefaultNamespace("bottom");
/*    */   
/*    */   private final Holder<PlacedFeature> feature;
/*    */   private final CompoundTag defaultJigsawNBT;
/*    */   
/*    */   protected FeaturePoolElement(Holder<PlacedFeature> feature, StructureTemplatePool.Projection projection) {
/* 41 */     super(projection);
/* 42 */     this.feature = feature;
/* 43 */     this.defaultJigsawNBT = fillDefaultJigsawNBT();
/*    */   }
/*    */   
/*    */   private CompoundTag fillDefaultJigsawNBT() {
/* 47 */     CompoundTag tag = new CompoundTag();
/* 48 */     tag.store("name", Identifier.CODEC, DEFAULT_JIGSAW_NAME);
/* 49 */     tag.putString("final_state", "minecraft:air");
/*    */ 
/*    */     
/* 52 */     tag.store("pool", JigsawBlockEntity.POOL_CODEC, Pools.EMPTY);
/* 53 */     tag.store("target", Identifier.CODEC, JigsawBlockEntity.EMPTY_ID);
/* 54 */     tag.store("joint", (com.mojang.serialization.Codec)JigsawBlockEntity.JointType.CODEC, JigsawBlockEntity.JointType.ROLLABLE);
/*    */     
/* 56 */     return tag;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
/* 61 */     return Vec3i.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
/* 66 */     return List.of(StructureTemplate.JigsawBlockInfo.of(new StructureTemplate.StructureBlockInfo(position, (net.minecraft.world.level.block.state.BlockState)Blocks.JIGSAW.defaultBlockState().setValue((net.minecraft.world.level.block.state.properties.Property)JigsawBlock.ORIENTATION, (Comparable)net.minecraft.core.FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
/* 71 */     Vec3i size = getSize(structureTemplateManager, rotation);
/* 72 */     return new BoundingBox(position.getX(), position.getY(), position.getZ(), position.getX() + size.getX(), position.getY() + size.getY(), position.getZ() + size.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 77 */     return ((PlacedFeature)this.feature.value()).place(level, generator, random, position);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 82 */     return StructurePoolElementType.FEATURE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return "Feature[" + String.valueOf(this.feature) + "]";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/FeaturePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */