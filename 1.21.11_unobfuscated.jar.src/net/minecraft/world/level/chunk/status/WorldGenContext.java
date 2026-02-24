/*    */ package net.minecraft.world.level.chunk.status;
/*    */ 
/*    */ public final class WorldGenContext extends Record {
/*    */   private final net.minecraft.server.level.ServerLevel level;
/*    */   private final net.minecraft.world.level.chunk.ChunkGenerator generator;
/*    */   private final net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureManager;
/*    */   private final net.minecraft.server.level.ThreadedLevelLightEngine lightEngine;
/*    */   private final java.util.concurrent.Executor mainThreadExecutor;
/*    */   private final net.minecraft.world.level.chunk.LevelChunk.UnsavedListener unsavedListener;
/*    */   
/* 11 */   public WorldGenContext(net.minecraft.server.level.ServerLevel level, net.minecraft.world.level.chunk.ChunkGenerator generator, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureManager, net.minecraft.server.level.ThreadedLevelLightEngine lightEngine, java.util.concurrent.Executor mainThreadExecutor, net.minecraft.world.level.chunk.LevelChunk.UnsavedListener unsavedListener) { this.level = level; this.generator = generator; this.structureManager = structureManager; this.lightEngine = lightEngine; this.mainThreadExecutor = mainThreadExecutor; this.unsavedListener = unsavedListener; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext; } public net.minecraft.server.level.ServerLevel level() { return this.level; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/status/WorldGenContext;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/status/WorldGenContext;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.chunk.ChunkGenerator generator() { return this.generator; } public net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager structureManager() { return this.structureManager; } public net.minecraft.server.level.ThreadedLevelLightEngine lightEngine() { return this.lightEngine; } public java.util.concurrent.Executor mainThreadExecutor() { return this.mainThreadExecutor; } public net.minecraft.world.level.chunk.LevelChunk.UnsavedListener unsavedListener() { return this.unsavedListener; }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/status/WorldGenContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */