/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class LevelEntityGetterAdapter<T extends EntityAccess>
/*    */   implements LevelEntityGetter<T>
/*    */ {
/*    */   private final EntityLookup<T> visibleEntities;
/*    */   private final EntitySectionStorage<T> sectionStorage;
/*    */   
/*    */   public LevelEntityGetterAdapter(EntityLookup<T> visibleEntities, EntitySectionStorage<T> sectionStorage) {
/* 15 */     this.visibleEntities = visibleEntities;
/* 16 */     this.sectionStorage = sectionStorage;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(int id) {
/* 21 */     return this.visibleEntities.getEntity(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(UUID id) {
/* 26 */     return this.visibleEntities.getEntity(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterable<T> getAll() {
/* 31 */     return this.visibleEntities.getAllEntities();
/*    */   }
/*    */ 
/*    */   
/*    */   public <U extends T> void get(EntityTypeTest<T, U> type, AbortableIterationConsumer<U> consumer) {
/* 36 */     this.visibleEntities.getEntities(type, consumer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void get(AABB bb, Consumer<T> output) {
/* 41 */     this.sectionStorage.getEntities(bb, AbortableIterationConsumer.forConsumer(output));
/*    */   }
/*    */ 
/*    */   
/*    */   public <U extends T> void get(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<U> consumer) {
/* 46 */     this.sectionStorage.getEntities(type, bb, consumer);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/LevelEntityGetterAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */