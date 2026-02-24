/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class LinearPosTest extends PosRuleTest {
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("min_chance").orElse(0.0F).forGetter(()), (App)Codec.FLOAT.fieldOf("max_chance").orElse(0.0F).forGetter(()), (App)Codec.INT.fieldOf("min_dist").orElse(0).forGetter(()), (App)Codec.INT.fieldOf("max_dist").orElse(0).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, LinearPosTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<LinearPosTest> CODEC;
/*    */   
/*    */   private final float minChance;
/*    */   
/*    */   private final float maxChance;
/*    */   private final int minDist;
/*    */   private final int maxDist;
/*    */   
/*    */   public LinearPosTest(float minChance, float maxChance, int minDist, int maxDist) {
/* 24 */     if (minDist >= maxDist) {
/* 25 */       throw new IllegalArgumentException("Invalid range: [" + minDist + "," + maxDist + "]");
/*    */     }
/*    */     
/* 28 */     this.minChance = minChance;
/* 29 */     this.maxChance = maxChance;
/* 30 */     this.minDist = minDist;
/* 31 */     this.maxDist = maxDist;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockPos inTemplatePos, BlockPos worldPos, BlockPos worldReference, net.minecraft.util.RandomSource random) {
/* 36 */     int dist = worldPos.distManhattan((net.minecraft.core.Vec3i)worldReference);
/*    */     
/* 38 */     float rnd = random.nextFloat();
/* 39 */     return (rnd <= Mth.clampedLerp(Mth.inverseLerp(dist, this.minDist, this.maxDist), this.minChance, this.maxChance));
/*    */   }
/*    */ 
/*    */   
/*    */   protected PosRuleTestType<?> getType() {
/* 44 */     return PosRuleTestType.LINEAR_POS_TEST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/LinearPosTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */