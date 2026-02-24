/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.util.ClassInstanceMultiMap;
/*    */ import net.minecraft.util.VisibleForDebug;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntitySection<T extends EntityAccess>
/*    */ {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ClassInstanceMultiMap<T> storage;
/*    */   private Visibility chunkStatus;
/*    */   
/*    */   public EntitySection(Class<T> entityClass, Visibility chunkStatus) {
/* 20 */     this.chunkStatus = chunkStatus;
/* 21 */     this.storage = new ClassInstanceMultiMap(entityClass);
/*    */   }
/*    */   
/*    */   public void add(T entity) {
/* 25 */     this.storage.add(entity);
/*    */   }
/*    */   
/*    */   public boolean remove(T entity) {
/* 29 */     return this.storage.remove(entity);
/*    */   }
/*    */   
/*    */   public AbortableIterationConsumer.Continuation getEntities(AABB bb, AbortableIterationConsumer<T> entities) {
/* 33 */     for (EntityAccess entityAccess : this.storage) {
/* 34 */       if (entityAccess.getBoundingBox().intersects(bb) && 
/* 35 */         entities.accept(entityAccess).shouldAbort()) {
/* 36 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */   
/*    */   public <U extends T> AbortableIterationConsumer.Continuation getEntities(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<? super U> consumer) {
/* 44 */     Collection<? extends T> foundEntities = this.storage.find(type.getBaseClass());
/* 45 */     if (foundEntities.isEmpty()) {
/* 46 */       return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */     }
/* 48 */     for (EntityAccess entityAccess1 : foundEntities) {
/* 49 */       EntityAccess entityAccess2 = (EntityAccess)type.tryCast((T)entityAccess1);
/* 50 */       if (entityAccess2 != null && entityAccess1.getBoundingBox().intersects(bb) && 
/* 51 */         consumer.accept(entityAccess2).shouldAbort()) {
/* 52 */         return AbortableIterationConsumer.Continuation.ABORT;
/*    */       }
/*    */     } 
/*    */     
/* 56 */     return AbortableIterationConsumer.Continuation.CONTINUE;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 60 */     return this.storage.isEmpty();
/*    */   }
/*    */   
/*    */   public Stream<T> getEntities() {
/* 64 */     return this.storage.stream();
/*    */   }
/*    */   
/*    */   public Visibility getStatus() {
/* 68 */     return this.chunkStatus;
/*    */   }
/*    */   
/*    */   public Visibility updateChunkStatus(Visibility chunkStatus) {
/* 72 */     Visibility prev = this.chunkStatus;
/* 73 */     this.chunkStatus = chunkStatus;
/* 74 */     return prev;
/*    */   }
/*    */   
/*    */   @VisibleForDebug
/*    */   public int size() {
/* 79 */     return this.storage.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/EntitySection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */