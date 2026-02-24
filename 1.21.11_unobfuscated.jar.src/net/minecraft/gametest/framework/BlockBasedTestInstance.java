/*    */ package net.minecraft.gametest.framework;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TestBlock;
/*    */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*    */ 
/*    */ public class BlockBasedTestInstance extends GameTestInstance {
/*    */   static {
/* 20 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TestData.CODEC.forGetter(GameTestInstance::info)).apply((Applicative)i, BlockBasedTestInstance::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<BlockBasedTestInstance> CODEC;
/*    */   
/*    */   public BlockBasedTestInstance(TestData<Holder<TestEnvironmentDefinition>> testData) {
/* 25 */     super(testData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(GameTestHelper helper) {
/* 30 */     BlockPos startPos = findStartBlock(helper);
/* 31 */     TestBlockEntity blockEntity = helper.<TestBlockEntity>getBlockEntity(startPos, TestBlockEntity.class);
/* 32 */     blockEntity.trigger();
/*    */     
/* 34 */     helper.onEachTick(() -> {
/*    */           List<BlockPos> acceptBlocks = findTestBlocks(helper, TestBlockMode.ACCEPT);
/*    */           if (acceptBlocks.isEmpty()) {
/*    */             helper.fail((Component)Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.ACCEPT.getDisplayName() }));
/*    */           }
/*    */           boolean acceptTriggered = acceptBlocks.stream().map(()).anyMatch(TestBlockEntity::hasTriggered);
/*    */           if (acceptTriggered) {
/*    */             helper.succeed();
/*    */           } else {
/*    */             forAllTriggeredTestBlocks(helper, TestBlockMode.FAIL, ());
/*    */             forAllTriggeredTestBlocks(helper, TestBlockMode.LOG, TestBlockEntity::trigger);
/*    */           } 
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void forAllTriggeredTestBlocks(GameTestHelper helper, TestBlockMode mode, Consumer<TestBlockEntity> action) {
/* 52 */     List<BlockPos> failBlocks = findTestBlocks(helper, mode);
/* 53 */     for (BlockPos failBlock : failBlocks) {
/* 54 */       TestBlockEntity blockEntity = helper.<TestBlockEntity>getBlockEntity(failBlock, TestBlockEntity.class);
/* 55 */       if (blockEntity.hasTriggered()) {
/* 56 */         action.accept(blockEntity);
/* 57 */         blockEntity.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private BlockPos findStartBlock(GameTestHelper helper) {
/* 63 */     List<BlockPos> testBlocks = findTestBlocks(helper, TestBlockMode.START);
/* 64 */     if (testBlocks.isEmpty()) {
/* 65 */       helper.fail((Component)Component.translatable("test_block.error.missing", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 67 */     if (testBlocks.size() != 1) {
/* 68 */       helper.fail((Component)Component.translatable("test_block.error.too_many", new Object[] { TestBlockMode.START.getDisplayName() }));
/*    */     }
/* 70 */     return testBlocks.getFirst();
/*    */   }
/*    */   
/*    */   private List<BlockPos> findTestBlocks(GameTestHelper helper, TestBlockMode mode) {
/* 74 */     List<BlockPos> blocks = new java.util.ArrayList<>();
/* 75 */     helper.forEveryBlockInStructure(pos -> {
/*    */           BlockState state = helper.getBlockState(pos);
/*    */           if (state.is(Blocks.TEST_BLOCK) && state.getValue((Property)TestBlock.MODE) == mode) {
/*    */             blocks.add(pos.immutable());
/*    */           }
/*    */         });
/* 81 */     return blocks;
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<BlockBasedTestInstance> codec() {
/* 86 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   protected net.minecraft.network.chat.MutableComponent typeDescription() {
/* 91 */     return Component.translatable("test_instance.type.block_based");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/BlockBasedTestInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */