/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockStateMatchTest extends RuleTest {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockState.CODEC.fieldOf("block_state").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)i, RandomBlockStateMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<RandomBlockStateMatchTest> CODEC;
/*    */   private final BlockState blockState;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockStateMatchTest(BlockState blockState, float probability) {
/* 19 */     this.blockState = blockState;
/* 20 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState blockState, net.minecraft.util.RandomSource random) {
/* 25 */     return (blockState == this.blockState && random.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 30 */     return RuleTestType.RANDOM_BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/RandomBlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */