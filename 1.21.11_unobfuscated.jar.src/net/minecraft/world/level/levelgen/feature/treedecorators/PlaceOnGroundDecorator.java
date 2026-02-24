/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class PlaceOnGroundDecorator extends TreeDecorator {
/*    */   static {
/* 19 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter(()), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(())).apply((Applicative)i, PlaceOnGroundDecorator::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<PlaceOnGroundDecorator> CODEC;
/*    */   
/*    */   private final int tries;
/*    */   
/*    */   private final int radius;
/*    */   private final int height;
/*    */   private final BlockStateProvider blockStateProvider;
/*    */   
/*    */   public PlaceOnGroundDecorator(int tries, int radius, int height, BlockStateProvider blockStateProvider) {
/* 32 */     this.tries = tries;
/* 33 */     this.radius = radius;
/* 34 */     this.height = height;
/* 35 */     this.blockStateProvider = blockStateProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 40 */     return TreeDecoratorType.PLACE_ON_GROUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 45 */     List<BlockPos> blockPositions = net.minecraft.world.level.levelgen.feature.TreeFeature.getLowestTrunkOrRootOfTree(context);
/*    */     
/* 47 */     if (blockPositions.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     BlockPos origin = blockPositions.getFirst();
/* 52 */     int minY = origin.getY();
/* 53 */     int minX = origin.getX();
/* 54 */     int maxX = origin.getX();
/* 55 */     int minZ = origin.getZ();
/* 56 */     int maxZ = origin.getZ();
/* 57 */     for (BlockPos position : blockPositions) {
/* 58 */       if (position.getY() == minY) {
/* 59 */         minX = Math.min(minX, position.getX());
/* 60 */         maxX = Math.max(maxX, position.getX());
/* 61 */         minZ = Math.min(minZ, position.getZ());
/* 62 */         maxZ = Math.max(maxZ, position.getZ());
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     RandomSource random = context.random();
/* 67 */     BoundingBox bb = new BoundingBox(minX, minY, minZ, maxX, minY, maxZ).inflatedBy(this.radius, this.height, this.radius);
/* 68 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*    */     
/* 70 */     for (int i = 0; i < this.tries; i++) {
/* 71 */       pos.set(random.nextIntBetweenInclusive(bb.minX(), bb.maxX()), random.nextIntBetweenInclusive(bb.minY(), bb.maxY()), random.nextIntBetweenInclusive(bb.minZ(), bb.maxZ()));
/* 72 */       attemptToPlaceBlockAbove(context, (BlockPos)pos);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void attemptToPlaceBlockAbove(TreeDecorator.Context context, BlockPos pos) {
/* 77 */     BlockPos abovePos = pos.above();
/* 78 */     if (context.level().isStateAtPosition(abovePos, state -> (state.isAir() || state.is(Blocks.VINE))) && 
/* 79 */       context.checkBlock(pos, BlockBehaviour.BlockStateBase::isSolidRender) && 
/* 80 */       context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() <= abovePos.getY())
/* 81 */       context.setBlock(abovePos, this.blockStateProvider.getState(context.random(), abovePos)); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/PlaceOnGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */