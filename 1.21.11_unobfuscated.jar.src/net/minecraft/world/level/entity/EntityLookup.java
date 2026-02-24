/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.AbortableIterationConsumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class EntityLookup<T extends EntityAccess>
/*    */ {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 18 */   private final Int2ObjectMap<T> byId = (Int2ObjectMap<T>)new Int2ObjectLinkedOpenHashMap();
/* 19 */   private final Map<UUID, T> byUuid = Maps.newHashMap();
/*    */   
/*    */   public <U extends T> void getEntities(EntityTypeTest<T, U> type, AbortableIterationConsumer<U> consumer) {
/* 22 */     for (ObjectIterator<EntityAccess> objectIterator = this.byId.values().iterator(); objectIterator.hasNext(); ) { EntityAccess entityAccess1 = objectIterator.next();
/* 23 */       EntityAccess entityAccess2 = (EntityAccess)type.tryCast((T)entityAccess1);
/* 24 */       if (entityAccess2 != null && 
/* 25 */         consumer.accept(entityAccess2).shouldAbort()) {
/*    */         return;
/*    */       } }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterable<T> getAllEntities() {
/* 33 */     return Iterables.unmodifiableIterable((Iterable)this.byId.values());
/*    */   }
/*    */   
/*    */   public void add(T entity) {
/* 37 */     UUID uuid = entity.getUUID();
/* 38 */     if (this.byUuid.containsKey(uuid)) {
/* 39 */       LOGGER.warn("Duplicate entity UUID {}: {}", uuid, entity);
/*    */       return;
/*    */     } 
/* 42 */     this.byUuid.put(uuid, entity);
/* 43 */     this.byId.put(entity.getId(), entity);
/*    */   }
/*    */   
/*    */   public void remove(T entity) {
/* 47 */     this.byUuid.remove(entity.getUUID());
/* 48 */     this.byId.remove(entity.getId());
/*    */   }
/*    */   
/*    */   public T getEntity(int id) {
/* 52 */     return (T)this.byId.get(id);
/*    */   }
/*    */   
/*    */   public T getEntity(UUID id) {
/* 56 */     return this.byUuid.get(id);
/*    */   }
/*    */   
/*    */   public int count() {
/* 60 */     return this.byUuid.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/EntityLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */