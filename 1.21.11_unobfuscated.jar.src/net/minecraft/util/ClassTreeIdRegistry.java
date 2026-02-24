/*    */ package net.minecraft.util;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ 
/*    */ public class ClassTreeIdRegistry {
/*    */   public static final int NO_ID_VALUE = -1;
/*    */   
/*    */   public ClassTreeIdRegistry() {
/*  9 */     this.classToLastIdCache = (Object2IntMap<Class<?>>)Util.make(new Object2IntOpenHashMap(), map -> map.defaultReturnValue(-1));
/*    */   } private final Object2IntMap<Class<?>> classToLastIdCache;
/*    */   public int getLastIdFor(Class<?> clazz) {
/* 12 */     int id = this.classToLastIdCache.getInt(clazz);
/* 13 */     if (id != -1) {
/* 14 */       return id;
/*    */     }
/* 16 */     Class<?> superclass = clazz;
/* 17 */     while ((superclass = superclass.getSuperclass()) != Object.class) {
/* 18 */       int newId = this.classToLastIdCache.getInt(superclass);
/* 19 */       if (newId != -1) {
/* 20 */         return newId;
/*    */       }
/*    */     } 
/* 23 */     return -1;
/*    */   }
/*    */   
/*    */   public int getCount(Class<?> clazz) {
/* 27 */     return getLastIdFor(clazz) + 1;
/*    */   }
/*    */   
/*    */   public int define(Class<?> clazz) {
/* 31 */     int id = getLastIdFor(clazz);
/* 32 */     int nextId = (id == -1) ? 0 : (id + 1);
/* 33 */     this.classToLastIdCache.put(clazz, nextId);
/* 34 */     return nextId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ClassTreeIdRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */