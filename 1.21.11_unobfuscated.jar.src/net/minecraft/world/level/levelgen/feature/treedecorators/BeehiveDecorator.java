/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BeehiveDecorator extends TreeDecorator {
/*    */   static {
/* 20 */     CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveDecorator::new, d -> d.probability);
/*    */   }
/* 22 */   public static final com.mojang.serialization.MapCodec<BeehiveDecorator> CODEC; private static final Direction WORLDGEN_FACING = Direction.SOUTH; private static final Direction[] SPAWN_DIRECTIONS; private final float probability; static {
/* 23 */     SPAWN_DIRECTIONS = (Direction[])Direction.Plane.HORIZONTAL.stream().filter(dir -> (dir != WORLDGEN_FACING.getOpposite())).toArray(x$0 -> new Direction[x$0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public BeehiveDecorator(float probability) {
/* 28 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TreeDecoratorType<?> type() {
/* 33 */     return TreeDecoratorType.BEEHIVE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 41 */     ObjectArrayList<BlockPos> objectArrayList1 = context.leaves();
/* 42 */     ObjectArrayList<BlockPos> objectArrayList2 = context.logs();
/*    */     
/* 44 */     if (objectArrayList2.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     RandomSource random = context.random();
/* 49 */     if (random.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     int hiveY = !objectArrayList1.isEmpty() ? Math.max(((BlockPos)objectArrayList1.getFirst()).getY() - 1, ((BlockPos)objectArrayList2.getFirst()).getY() + 1) : Math.min(((BlockPos)objectArrayList2.getFirst()).getY() + 1 + random.nextInt(3), ((BlockPos)objectArrayList2.getLast()).getY());
/*    */     
/* 55 */     List<BlockPos> hivePlacements = (List<BlockPos>)objectArrayList2.stream()
/* 56 */       .filter(pos -> (pos.getY() == hiveY))
/* 57 */       .flatMap(pos -> { Objects.requireNonNull(pos); return Stream.<Direction>of(SPAWN_DIRECTIONS).map(pos::relative);
/* 58 */         }).collect(Collectors.toList());
/* 59 */     if (hivePlacements.isEmpty()) {
/*    */       return;
/*    */     }
/* 62 */     Util.shuffle(hivePlacements, random);
/* 63 */     Optional<BlockPos> hivePos = hivePlacements.stream()
/* 64 */       .filter(pos -> (context.isAir(pos) && context.isAir(pos.relative(WORLDGEN_FACING))))
/* 65 */       .findFirst();
/* 66 */     if (hivePos.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 70 */     context.setBlock(hivePos.get(), (net.minecraft.world.level.block.state.BlockState)Blocks.BEE_NEST.defaultBlockState().setValue((Property)net.minecraft.world.level.block.BeehiveBlock.FACING, (Comparable)WORLDGEN_FACING));
/* 71 */     context.level().getBlockEntity(hivePos.get(), net.minecraft.world.level.block.entity.BlockEntityType.BEEHIVE).ifPresent(beehive -> {
/*    */           int numBees = 2 + random.nextInt(2);
/*    */           for (int count = 0; count < numBees; count++)
/*    */             beehive.storeBee(BeehiveBlockEntity.Occupant.create(random.nextInt(599))); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */