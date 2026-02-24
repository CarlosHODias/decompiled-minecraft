/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateMatchTest extends RuleTest {
/*    */   public static final com.mojang.serialization.MapCodec<BlockStateMatchTest> CODEC;
/*    */   
/*    */   static {
/*  8 */     CODEC = BlockState.CODEC.fieldOf("block_state").xmap(BlockStateMatchTest::new, t -> t.blockState);
/*    */   }
/*    */   private final BlockState blockState;
/*    */   
/*    */   public BlockStateMatchTest(BlockState blockState) {
/* 13 */     this.blockState = blockState;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState blockState, net.minecraft.util.RandomSource random) {
/* 18 */     return (blockState == this.blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 23 */     return RuleTestType.BLOCKSTATE_TEST;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/BlockStateMatchTest.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */