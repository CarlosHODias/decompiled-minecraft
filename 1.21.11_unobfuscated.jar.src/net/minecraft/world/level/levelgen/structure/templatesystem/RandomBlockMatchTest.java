/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RandomBlockMatchTest extends RuleTest {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(()), (App)Codec.FLOAT.fieldOf("probability").forGetter(())).apply((Applicative)i, RandomBlockMatchTest::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<RandomBlockMatchTest> CODEC;
/*    */   private final Block block;
/*    */   private final float probability;
/*    */   
/*    */   public RandomBlockMatchTest(Block block, float probability) {
/* 21 */     this.block = block;
/* 22 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState blockState, net.minecraft.util.RandomSource random) {
/* 27 */     return (blockState.is(this.block) && random.nextFloat() < this.probability);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 32 */     return RuleTestType.RANDOM_BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/RandomBlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */