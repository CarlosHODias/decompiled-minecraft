/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.LayeredRegistryAccess;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public enum ClientRegistryLayer
/*    */ {
/* 10 */   STATIC,
/* 11 */   REMOTE;
/*    */ 
/*    */   
/* 14 */   private static final List<ClientRegistryLayer> VALUES = List.of(values());
/*    */   
/* 16 */   private static final RegistryAccess.Frozen STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/*    */   
/*    */   public static LayeredRegistryAccess<ClientRegistryLayer> createRegistryAccess() {
/* 19 */     return new LayeredRegistryAccess(VALUES).replaceFrom(STATIC, new RegistryAccess.Frozen[] { STATIC_ACCESS });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientRegistryLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */