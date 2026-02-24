/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.lang.invoke.MethodHandle;
/*    */ import java.lang.invoke.MethodHandles;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import org.lwjgl.system.Pointer;
/*    */ 
/*    */ public class DebugMemoryUntracker
/*    */ {
/*    */   static {
/* 12 */     UNTRACK = GLX.<MethodHandle>make(() -> {
/*    */           try {
/*    */             MethodHandles.Lookup lookup = MethodHandles.lookup();
/*    */             
/*    */             Class<?> debugAllocator = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
/*    */             
/*    */             Method reflectionUntrack = debugAllocator.getDeclaredMethod("untrack", new Class<?>[] { long.class });
/*    */             
/*    */             reflectionUntrack.setAccessible(true);
/*    */             
/*    */             Field allocatorField = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
/*    */             
/*    */             allocatorField.setAccessible(true);
/*    */             
/*    */             Object allocator = allocatorField.get(null);
/*    */             return debugAllocator.isInstance(allocator) ? lookup.unreflect(reflectionUntrack) : null;
/* 28 */           } catch (ClassNotFoundException|NoSuchMethodException|NoSuchFieldException|IllegalAccessException e) {
/*    */             throw new RuntimeException(e);
/*    */           } 
/*    */         });
/*    */   } private static final MethodHandle UNTRACK;
/*    */   public static void untrack(long address) {
/* 34 */     if (UNTRACK == null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 38 */       UNTRACK.invoke(address);
/* 39 */     } catch (Throwable throwable) {
/* 40 */       throw new RuntimeException(throwable);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void untrack(Pointer ptr) {
/* 45 */     untrack(ptr.address());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/DebugMemoryUntracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */