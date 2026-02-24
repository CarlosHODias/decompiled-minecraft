/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ 
/*    */ public class BlockPredicateFilter extends PlacementFilter {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.fieldOf("predicate").forGetter(())).apply((Applicative)i, BlockPredicateFilter::new));
/*    */   }
/*    */   
/*    */   public static final MapCodec<BlockPredicateFilter> CODEC;
/*    */   private final BlockPredicate predicate;
/*    */   
/*    */   private BlockPredicateFilter(BlockPredicate predicate) {
/* 21 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public static BlockPredicateFilter forPredicate(BlockPredicate predicate) {
/* 25 */     return new BlockPredicateFilter(predicate);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 30 */     return this.predicate.test(context.getLevel(), origin);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 35 */     return PlacementModifierType.BLOCK_PREDICATE_FILTER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/BlockPredicateFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */