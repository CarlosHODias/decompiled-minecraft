/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class ListPoolElement extends StructurePoolElement {
/*     */   static {
/*  24 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter(()), (App)projectionCodec()).apply((Applicative)i, ListPoolElement::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final com.mojang.serialization.MapCodec<ListPoolElement> CODEC;
/*     */   private final List<StructurePoolElement> elements;
/*     */   
/*     */   public ListPoolElement(List<StructurePoolElement> elements, StructureTemplatePool.Projection projection) {
/*  32 */     super(projection);
/*  33 */     if (elements.isEmpty()) {
/*  34 */       throw new IllegalArgumentException("Elements are empty");
/*     */     }
/*  36 */     this.elements = elements;
/*  37 */     setProjectionOnEachElement(projection);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
/*  42 */     int sizeX = 0;
/*  43 */     int sizeY = 0;
/*  44 */     int sizeZ = 0;
/*  45 */     for (StructurePoolElement element : this.elements) {
/*  46 */       Vec3i size = element.getSize(structureTemplateManager, rotation);
/*  47 */       sizeX = Math.max(sizeX, size.getX());
/*  48 */       sizeY = Math.max(sizeY, size.getY());
/*  49 */       sizeZ = Math.max(sizeZ, size.getZ());
/*     */     } 
/*     */     
/*  52 */     return new Vec3i(sizeX, sizeY, sizeZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
/*  57 */     return ((StructurePoolElement)this.elements.get(0)).getShuffledJigsawBlocks(structureTemplateManager, position, rotation, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
/*  62 */     Stream<BoundingBox> stream = this.elements.stream()
/*  63 */       .filter(e -> (e != EmptyPoolElement.INSTANCE))
/*  64 */       .map(e -> e.getBoundingBox(structureTemplateManager, position, rotation));
/*     */     
/*  66 */     java.util.Objects.requireNonNull(stream); return (BoundingBox)BoundingBox.encapsulatingBoxes(stream::iterator).orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox for ListPoolElement"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
/*  71 */     for (StructurePoolElement element : this.elements) {
/*  72 */       if (!element.place(structureTemplateManager, level, structureManager, generator, position, referencePos, rotation, chunkBB, random, liquidSettings, keepJigsaws)) {
/*  73 */         return false;
/*     */       }
/*     */     } 
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElementType<?> getType() {
/*  81 */     return StructurePoolElementType.LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructurePoolElement setProjection(StructureTemplatePool.Projection projection) {
/*  86 */     super.setProjection(projection);
/*  87 */     setProjectionOnEachElement(projection);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return "List[" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
/*     */   }
/*     */   
/*     */   private void setProjectionOnEachElement(StructureTemplatePool.Projection projection) {
/*  97 */     this.elements.forEach(k -> k.setProjection(projection));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public List<StructurePoolElement> getElements() {
/* 102 */     return this.elements;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pools/ListPoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */