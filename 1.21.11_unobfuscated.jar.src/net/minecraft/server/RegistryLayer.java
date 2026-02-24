/*    */ package net.minecraft.server;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public enum RegistryLayer
/*    */ {
/* 10 */   STATIC,
/* 11 */   WORLDGEN,
/* 12 */   DIMENSIONS,
/* 13 */   RELOADABLE;
/*    */ 
/*    */   
/* 16 */   private static final List<RegistryLayer> VALUES = List.of(values());
/*    */   
/* 18 */   private static final RegistryAccess.Frozen STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */   
/*    */   public static LayeredRegistryAccess<RegistryLayer> createRegistryAccess() {
/* 21 */     return new LayeredRegistryAccess(VALUES).replaceFrom(STATIC, new RegistryAccess.Frozen[] { STATIC_ACCESS });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/RegistryLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */