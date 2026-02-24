/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ class PlayerScores
/*    */ {
/* 14 */   private final Reference2ObjectOpenHashMap<Objective, Score> scores = new Reference2ObjectOpenHashMap(16, 0.5F);
/*    */   
/*    */   public Score get(Objective objective) {
/* 17 */     return (Score)this.scores.get(objective);
/*    */   }
/*    */   
/*    */   public Score getOrCreate(Objective objective, Consumer<Score> newResultCallback) {
/* 21 */     return (Score)this.scores.computeIfAbsent(objective, obj -> {
/*    */           Score newScore = new Score();
/*    */           newResultCallback.accept(newScore);
/*    */           return newScore;
/*    */         });
/*    */   }
/*    */   
/*    */   public boolean remove(Objective objective) {
/* 29 */     return (this.scores.remove(objective) != null);
/*    */   }
/*    */   
/*    */   public boolean hasScores() {
/* 33 */     return !this.scores.isEmpty();
/*    */   }
/*    */   
/*    */   public Object2IntMap<Objective> listScores() {
/* 37 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 38 */     this.scores.forEach((objective, score) -> result.put(objective, score.value()));
/* 39 */     return (Object2IntMap<Objective>)object2IntOpenHashMap;
/*    */   }
/*    */   
/*    */   void setScore(Objective objective, Score score) {
/* 43 */     this.scores.put(objective, score);
/*    */   }
/*    */   
/*    */   Map<Objective, Score> listRawScores() {
/* 47 */     return Collections.unmodifiableMap((Map<? extends Objective, ? extends Score>)this.scores);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/scores/PlayerScores.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */