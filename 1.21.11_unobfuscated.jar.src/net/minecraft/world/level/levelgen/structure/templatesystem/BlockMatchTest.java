/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockMatchTest extends RuleTest {
/*    */   public static final com.mojang.serialization.MapCodec<BlockMatchTest> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").xmap(BlockMatchTest::new, t -> t.block);
/*    */   }
/*    */   private final Block block;
/*    */   
/*    */   public BlockMatchTest(Block block) {
/* 15 */     this.block = block;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState blockState, net.minecraft.util.RandomSource random) {
/* 20 */     return blockState.is(this.block);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 25 */     return RuleTestType.BLOCK_TEST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/BlockMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */