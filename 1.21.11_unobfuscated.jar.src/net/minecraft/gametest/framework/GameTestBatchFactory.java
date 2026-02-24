/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Streams;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GameTestBatchFactory
/*    */ {
/*    */   private static final int MAX_TESTS_PER_BATCH = 50;
/*    */   public static final TestDecorator DIRECT;
/*    */   
/*    */   static {
/* 24 */     DIRECT = ((test, level) -> Stream.of(new GameTestInfo(test, Rotation.NONE, level, RetryOptions.noRetries())));
/*    */   }
/*    */   public static List<GameTestBatch> divideIntoBatches(Collection<Holder.Reference<GameTestInstance>> allTests, TestDecorator decorator, ServerLevel level) {
/* 27 */     Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>> testsPerBatch = (Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>>)allTests.stream()
/* 28 */       .flatMap(test -> decorator.decorate(test, level))
/* 29 */       .collect(Collectors.groupingBy(info -> info.getTest().batch()));
/*    */     
/* 31 */     return testsPerBatch.entrySet().stream().flatMap(e -> {
/*    */           Holder<TestEnvironmentDefinition> batchKey = (Holder<TestEnvironmentDefinition>)e.getKey();
/*    */ 
/*    */           
/*    */           List<GameTestInfo> testsInBatch = (List<GameTestInfo>)e.getValue();
/*    */           
/*    */           return Streams.mapWithIndex(Lists.partition(testsInBatch, 50).stream(), ());
/* 38 */         }).toList();
/*    */   }
/*    */   
/*    */   public static GameTestRunner.GameTestBatcher fromGameTestInfo() {
/* 42 */     return fromGameTestInfo(50);
/*    */   }
/*    */   
/*    */   public static GameTestRunner.GameTestBatcher fromGameTestInfo(int maxTestsPerBatch) {
/* 46 */     return gameTestInfos -> {
/*    */         Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>> testFunctionsPerBatch = (Map<Holder<TestEnvironmentDefinition>, List<GameTestInfo>>)gameTestInfos.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(()));
/*    */         return testFunctionsPerBatch.entrySet().stream().flatMap(()).toList();
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GameTestBatch toGameTestBatch(Collection<GameTestInfo> tests, Holder<TestEnvironmentDefinition> batch, int counter) {
/* 61 */     return new GameTestBatch(counter, tests, batch);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TestDecorator {
/*    */     Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> param1Reference, ServerLevel param1ServerLevel);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestBatchFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */