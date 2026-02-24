/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ 
/*     */ public class MineshaftStructure extends Structure {
/*     */   static {
/*  27 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)settingsCodec(i), (App)Type.CODEC.fieldOf("mineshaft_type").forGetter(())).apply((Applicative)i, MineshaftStructure::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<MineshaftStructure> CODEC;
/*     */   private final Type type;
/*     */   
/*     */   public MineshaftStructure(Structure.StructureSettings settings, Type type) {
/*  35 */     super(settings);
/*  36 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/*  42 */     context.random().nextDouble();
/*     */     
/*  44 */     ChunkPos chunkPos = context.chunkPos();
/*     */     
/*  46 */     BlockPos startPos = new BlockPos(chunkPos.getMiddleBlockX(), 50, chunkPos.getMinBlockZ());
/*  47 */     StructurePiecesBuilder mineshaftPiecesBuilder = new StructurePiecesBuilder();
/*  48 */     int yOffset = generatePiecesAndAdjust(mineshaftPiecesBuilder, context);
/*  49 */     return Optional.of(new Structure.GenerationStub(startPos.offset(0, yOffset, 0), Either.right(mineshaftPiecesBuilder)));
/*     */   }
/*     */   
/*     */   private int generatePiecesAndAdjust(StructurePiecesBuilder builder, Structure.GenerationContext context) {
/*  53 */     ChunkPos chunkPos = context.chunkPos();
/*  54 */     WorldgenRandom random = context.random();
/*  55 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/*  56 */     MineshaftPieces.MineShaftRoom mineShaftRoom = new MineshaftPieces.MineShaftRoom(0, (RandomSource)random, chunkPos.getBlockX(2), chunkPos.getBlockZ(2), this.type);
/*  57 */     builder.addPiece(mineShaftRoom);
/*  58 */     mineShaftRoom.addChildren(mineShaftRoom, (net.minecraft.world.level.levelgen.structure.StructurePieceAccessor)builder, (RandomSource)random);
/*     */     
/*  60 */     int seaLevel = chunkGenerator.getSeaLevel();
/*  61 */     if (this.type == Type.MESA) {
/*     */       
/*  63 */       BlockPos center = builder.getBoundingBox().getCenter();
/*  64 */       int surfaceHeight = chunkGenerator.getBaseHeight(center.getX(), center.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
/*  65 */       int targetYForCenter = (surfaceHeight <= seaLevel) ? seaLevel : Mth.randomBetweenInclusive((RandomSource)random, seaLevel, surfaceHeight);
/*  66 */       int dy = targetYForCenter - center.getY();
/*     */       
/*  68 */       builder.offsetPiecesVertically(dy);
/*  69 */       return dy;
/*     */     } 
/*  71 */     return builder.moveBelowSeaLevel(seaLevel, chunkGenerator.getMinY(), (RandomSource)random, 10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureType<?> type() {
/*  77 */     return StructureType.MINESHAFT;
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/*  81 */     NORMAL("normal", Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.OAK_FENCE),
/*  82 */     MESA("mesa", Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE);
/*     */ 
/*     */     
/*  85 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*  86 */     private static final java.util.function.IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/*     */     private final String name;
/*     */     private final BlockState woodState;
/*     */     private final BlockState planksState;
/*     */     private final BlockState fenceState;
/*     */     
/*     */     Type(String name, Block wood, Block plank, Block fence) {
/*  94 */       this.name = name;
/*  95 */       this.woodState = wood.defaultBlockState();
/*  96 */       this.planksState = plank.defaultBlockState();
/*  97 */       this.fenceState = fence.defaultBlockState();
/*     */     }
/*     */     
/*     */     public String getName() {
/* 101 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Type byId(int id) {
/* 105 */       return BY_ID.apply(id);
/*     */     }
/*     */     
/*     */     public BlockState getWoodState() {
/* 109 */       return this.woodState;
/*     */     }
/*     */     
/*     */     public BlockState getPlanksState() {
/* 113 */       return this.planksState;
/*     */     }
/*     */     
/*     */     public BlockState getFenceState() {
/* 117 */       return this.fenceState;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 122 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/structures/MineshaftStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */