/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipleTestTracker
/*     */ {
/*     */   private static final char NOT_STARTED_TEST_CHAR = ' ';
/*     */   private static final char ONGOING_TEST_CHAR = '_';
/*     */   private static final char SUCCESSFUL_TEST_CHAR = '+';
/*     */   private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';
/*     */   private static final char FAILED_REQUIRED_TEST_CHAR = 'X';
/*  18 */   private final Collection<GameTestInfo> tests = Lists.newArrayList();
/*  19 */   private final Collection<GameTestListener> listeners = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipleTestTracker(Collection<GameTestInfo> tests) {
/*  25 */     this.tests.addAll(tests);
/*     */   }
/*     */   
/*     */   public void addTestToTrack(GameTestInfo testInfo) {
/*  29 */     this.tests.add(testInfo);
/*  30 */     Objects.requireNonNull(testInfo); this.listeners.forEach(testInfo::addListener);
/*     */   }
/*     */   
/*     */   public void addListener(GameTestListener listener) {
/*  34 */     this.listeners.add(listener);
/*  35 */     this.tests.forEach(testInfo -> testInfo.addListener(listener));
/*     */   }
/*     */   
/*     */   public void addFailureListener(final Consumer<GameTestInfo> listener) {
/*  39 */     addListener(new GameTestListener(this)
/*     */         {
/*     */           public void testStructureLoaded(GameTestInfo testInfo) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void testPassed(GameTestInfo testInfo, GameTestRunner runner) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void testFailed(GameTestInfo testInfo, GameTestRunner runner) {
/*  50 */             listener.accept(testInfo);
/*     */           }
/*     */ 
/*     */           
/*     */           public void testAddedForRerun(GameTestInfo original, GameTestInfo copy, GameTestRunner runner) {}
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFailedRequiredCount() {
/*  60 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).count();
/*     */   }
/*     */   
/*     */   public int getFailedOptionalCount() {
/*  64 */     return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).count();
/*     */   }
/*     */   
/*     */   public int getDoneCount() {
/*  68 */     return (int)this.tests.stream().filter(GameTestInfo::isDone).count();
/*     */   }
/*     */   
/*     */   public boolean hasFailedRequired() {
/*  72 */     return (getFailedRequiredCount() > 0);
/*     */   }
/*     */   
/*     */   public boolean hasFailedOptional() {
/*  76 */     return (getFailedOptionalCount() > 0);
/*     */   }
/*     */   
/*     */   public Collection<GameTestInfo> getFailedRequired() {
/*  80 */     return (Collection<GameTestInfo>)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public Collection<GameTestInfo> getFailedOptional() {
/*  84 */     return (Collection<GameTestInfo>)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public int getTotalCount() {
/*  88 */     return this.tests.size();
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  92 */     return (getDoneCount() == getTotalCount());
/*     */   }
/*     */   
/*     */   public String getProgressBar() {
/*  96 */     StringBuffer buf = new StringBuffer();
/*  97 */     buf.append('[');
/*  98 */     this.tests.forEach(test -> {
/*     */           if (!test.hasStarted()) {
/*     */             buf.append(' ');
/*     */           } else if (test.hasSucceeded()) {
/*     */             buf.append('+');
/*     */           } else if (test.hasFailed()) {
/*     */             buf.append(test.isRequired() ? 88 : 120);
/*     */           } else {
/*     */             buf.append('_');
/*     */           } 
/*     */         });
/* 109 */     buf.append(']');
/* 110 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return getProgressBar();
/*     */   }
/*     */   
/*     */   public void remove(GameTestInfo testInfo) {
/* 119 */     this.tests.remove(testInfo);
/*     */   }
/*     */   
/*     */   public MultipleTestTracker() {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/MultipleTestTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */