/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestVisibleLivingEntities
/*    */ {
/* 22 */   private static final NearestVisibleLivingEntities EMPTY = new NearestVisibleLivingEntities();
/*    */   private final List<LivingEntity> nearbyEntities;
/*    */   private final Predicate<LivingEntity> lineOfSightTest;
/*    */   
/*    */   private NearestVisibleLivingEntities() {
/* 27 */     this.nearbyEntities = List.of();
/* 28 */     this.lineOfSightTest = (ignored -> false);
/*    */   }
/*    */   
/*    */   public NearestVisibleLivingEntities(ServerLevel level, LivingEntity body, List<LivingEntity> livingEntities) {
/* 32 */     this.nearbyEntities = livingEntities;
/* 33 */     Object2BooleanOpenHashMap<LivingEntity> cache = new Object2BooleanOpenHashMap(livingEntities.size());
/*    */     Predicate<LivingEntity> targetTest = targetEntity -> Sensor.isEntityTargetable(level, body, targetEntity);
/* 35 */     this.lineOfSightTest = (otherEntity -> cache.computeIfAbsent(otherEntity, targetTest));
/*    */   }
/*    */   
/*    */   public static NearestVisibleLivingEntities empty() {
/* 39 */     return EMPTY;
/*    */   }
/*    */   
/*    */   public Optional<LivingEntity> findClosest(Predicate<LivingEntity> filter) {
/* 43 */     for (LivingEntity nearbyEntity : this.nearbyEntities) {
/* 44 */       if (filter.test(nearbyEntity) && this.lineOfSightTest.test(nearbyEntity)) {
/* 45 */         return Optional.of(nearbyEntity);
/*    */       }
/*    */     } 
/* 48 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterable<LivingEntity> findAll(Predicate<LivingEntity> filter) {
/* 57 */     return Iterables.filter(this.nearbyEntities, entity -> (filter.test(filter) && this.lineOfSightTest.test(filter)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Stream<LivingEntity> find(Predicate<LivingEntity> filter) {
/* 67 */     return this.nearbyEntities.stream()
/* 68 */       .filter(entity -> (filter.test(filter) && this.lineOfSightTest.test(filter)));
/*    */   }
/*    */   
/*    */   public boolean contains(LivingEntity targetEntity) {
/* 72 */     return (this.nearbyEntities.contains(targetEntity) && this.lineOfSightTest.test(targetEntity));
/*    */   }
/*    */   
/*    */   public boolean contains(Predicate<LivingEntity> filter) {
/* 76 */     for (LivingEntity nearbyEntity : this.nearbyEntities) {
/* 77 */       if (filter.test(nearbyEntity) && this.lineOfSightTest.test(nearbyEntity)) {
/* 78 */         return true;
/*    */       }
/*    */     } 
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/memory/NearestVisibleLivingEntities.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */