/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class RarityFilter extends PlacementFilter {
/*    */   public static final MapCodec<RarityFilter> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, c -> c.chance);
/*    */   }
/*    */   private final int chance;
/*    */   
/*    */   private RarityFilter(int chance) {
/* 17 */     this.chance = chance;
/*    */   }
/*    */   
/*    */   public static RarityFilter onAverageOnceEvery(int chance) {
/* 21 */     return new RarityFilter(chance);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos origin) {
/* 26 */     return (random.nextFloat() < 1.0F / this.chance);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 31 */     return PlacementModifierType.RARITY_FILTER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/RarityFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */