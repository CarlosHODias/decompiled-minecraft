/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public class FailedTestTracker
/*    */ {
/* 10 */   private static final Set<Holder.Reference<GameTestInstance>> LAST_FAILED_TESTS = Sets.newHashSet();
/*    */   
/*    */   public static Stream<Holder.Reference<GameTestInstance>> getLastFailedTests() {
/* 13 */     return LAST_FAILED_TESTS.stream();
/*    */   }
/*    */   
/*    */   public static void rememberFailedTest(Holder.Reference<GameTestInstance> test) {
/* 17 */     LAST_FAILED_TESTS.add(test);
/*    */   }
/*    */   
/*    */   public static void forgetFailedTests() {
/* 21 */     LAST_FAILED_TESTS.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/FailedTestTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */