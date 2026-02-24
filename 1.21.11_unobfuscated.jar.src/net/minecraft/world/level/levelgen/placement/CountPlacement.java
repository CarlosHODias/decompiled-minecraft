/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ 
/*    */ public class CountPlacement extends RepeatingPlacement {
/*    */   public static final MapCodec<CountPlacement> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountPlacement::new, c -> c.count);
/*    */   }
/*    */   private final IntProvider count;
/*    */   
/*    */   private CountPlacement(IntProvider count) {
/* 19 */     this.count = count;
/*    */   }
/*    */   
/*    */   public static CountPlacement of(IntProvider count) {
/* 23 */     return new CountPlacement(count);
/*    */   }
/*    */   
/*    */   public static CountPlacement of(int count) {
/* 27 */     return of((IntProvider)ConstantInt.of(count));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 32 */     return this.count.sample(random);
/*    */   }
/*    */ 
/*    */   
/*    */   public PlacementModifierType<?> type() {
/* 37 */     return PlacementModifierType.COUNT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/placement/CountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */