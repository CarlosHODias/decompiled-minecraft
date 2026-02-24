/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ 
/*    */ public final class StructurePieceSerializationContext extends Record {
/*    */   private final net.minecraft.server.packs.resources.ResourceManager resourceManager;
/*    */   private final net.minecraft.core.RegistryAccess registryAccess;
/*    */   private final net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureTemplateManager;
/*    */   
/*  9 */   public StructurePieceSerializationContext(net.minecraft.server.packs.resources.ResourceManager resourceManager, net.minecraft.core.RegistryAccess registryAccess, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureTemplateManager) { this.resourceManager = resourceManager; this.registryAccess = registryAccess; this.structureTemplateManager = structureTemplateManager; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext; } public net.minecraft.server.packs.resources.ResourceManager resourceManager() { return this.resourceManager; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.RegistryAccess registryAccess() { return this.registryAccess; } public net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static StructurePieceSerializationContext fromLevel(net.minecraft.server.level.ServerLevel level) {
/* 15 */     net.minecraft.server.MinecraftServer server = level.getServer();
/* 16 */     return new StructurePieceSerializationContext(
/* 17 */         server.getResourceManager(), (net.minecraft.core.RegistryAccess)
/* 18 */         server.registryAccess(), 
/* 19 */         server.getStructureManager());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */