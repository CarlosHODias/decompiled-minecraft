/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.Mirror;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ public class FossilFeature extends Feature<FossilFeatureConfiguration> {
/*    */   public FossilFeature(Codec<FossilFeatureConfiguration> codec) {
/* 25 */     super(codec);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<FossilFeatureConfiguration> context) {
/* 30 */     RandomSource random = context.random();
/* 31 */     WorldGenLevel level = context.level();
/* 32 */     BlockPos origin = context.origin();
/* 33 */     Rotation rotation = Rotation.getRandom(random);
/* 34 */     FossilFeatureConfiguration config = context.config();
/*    */     
/* 36 */     int fossilIndex = random.nextInt(config.fossilStructures.size());
/*    */ 
/*    */     
/* 39 */     StructureTemplateManager structureTemplateManager = level.getLevel().getServer().getStructureManager();
/* 40 */     StructureTemplate fossilBase = structureTemplateManager.getOrCreate(config.fossilStructures.get(fossilIndex));
/* 41 */     StructureTemplate fossilOverlay = structureTemplateManager.getOrCreate(config.overlayStructures.get(fossilIndex));
/* 42 */     ChunkPos chunkPos = new ChunkPos(origin);
/* 43 */     BoundingBox boundingBox = new BoundingBox(
/* 44 */         chunkPos.getMinBlockX() - 16, level.getMinY(), chunkPos.getMinBlockZ() - 16, 
/* 45 */         chunkPos.getMaxBlockX() + 16, level.getMaxY(), chunkPos.getMaxBlockZ() + 16);
/*    */     
/* 47 */     StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingBox).setRandom(random);
/*    */     
/* 49 */     Vec3i size = fossilBase.getSize(rotation);
/*    */     
/* 51 */     BlockPos lowCorner = origin.offset(-size.getX() / 2, 0, -size.getZ() / 2);
/*    */     
/* 53 */     int lowestSurfaceY = origin.getY();
/*    */     
/* 55 */     for (int xscan = 0; xscan < size.getX(); xscan++) {
/* 56 */       for (int zscan = 0; zscan < size.getZ(); zscan++) {
/* 57 */         lowestSurfaceY = Math.min(lowestSurfaceY, level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, lowCorner.getX() + xscan, lowCorner.getZ() + zscan));
/*    */       }
/*    */     } 
/* 60 */     int targetY = Math.max(lowestSurfaceY - 15 - random.nextInt(10), level.getMinY() + 10);
/*    */     
/* 62 */     BlockPos targetPos = fossilBase.getZeroPositionWithTransform(lowCorner.atY(targetY), Mirror.NONE, rotation);
/*    */     
/* 64 */     if (countEmptyCorners(level, fossilBase.getBoundingBox(settings, targetPos)) > config.maxEmptyCornersAllowed) {
/* 65 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 69 */     settings.clearProcessors();
/* 70 */     Objects.requireNonNull(settings); ((StructureProcessorList)config.fossilProcessors.value()).list().forEach(settings::addProcessor);
/* 71 */     fossilBase.placeInWorld((ServerLevelAccessor)level, targetPos, targetPos, settings, random, 260);
/*    */ 
/*    */     
/* 74 */     settings.clearProcessors();
/* 75 */     Objects.requireNonNull(settings); ((StructureProcessorList)config.overlayProcessors.value()).list().forEach(settings::addProcessor);
/* 76 */     fossilOverlay.placeInWorld((ServerLevelAccessor)level, targetPos, targetPos, settings, random, 260);
/*    */     
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   private static int countEmptyCorners(WorldGenLevel level, BoundingBox structureBounds) {
/* 82 */     MutableInt count = new MutableInt(0);
/* 83 */     structureBounds.forAllCorners(pos -> {
/*    */           BlockState state = level.getBlockState(pos);
/*    */           if (state.isAir() || state.is(Blocks.LAVA) || state.is(Blocks.WATER)) {
/*    */             count.add(1);
/*    */           }
/*    */         });
/* 89 */     return count.intValue();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/FossilFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */