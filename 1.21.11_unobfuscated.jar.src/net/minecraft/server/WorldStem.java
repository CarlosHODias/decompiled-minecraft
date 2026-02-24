/*    */ package net.minecraft.server;
/*    */ public final class WorldStem extends Record implements AutoCloseable { private final net.minecraft.server.packs.resources.CloseableResourceManager resourceManager;
/*    */   private final ReloadableServerResources dataPackResources;
/*    */   private final net.minecraft.core.LayeredRegistryAccess<RegistryLayer> registries;
/*    */   private final net.minecraft.world.level.storage.WorldData worldData;
/*    */   
/*  7 */   public WorldStem(net.minecraft.server.packs.resources.CloseableResourceManager resourceManager, ReloadableServerResources dataPackResources, net.minecraft.core.LayeredRegistryAccess<RegistryLayer> registries, net.minecraft.world.level.storage.WorldData worldData) { this.resourceManager = resourceManager; this.dataPackResources = dataPackResources; this.registries = registries; this.worldData = worldData; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/WorldStem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/server/WorldStem; } public net.minecraft.server.packs.resources.CloseableResourceManager resourceManager() { return this.resourceManager; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/WorldStem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/WorldStem; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/WorldStem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/WorldStem;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public ReloadableServerResources dataPackResources() { return this.dataPackResources; } public net.minecraft.core.LayeredRegistryAccess<RegistryLayer> registries() { return this.registries; } public net.minecraft.world.level.storage.WorldData worldData() { return this.worldData; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 15 */     this.resourceManager.close();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/WorldStem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */