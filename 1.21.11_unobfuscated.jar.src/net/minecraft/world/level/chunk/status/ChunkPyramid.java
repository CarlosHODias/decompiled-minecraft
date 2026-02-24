/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public final class ChunkPyramid extends Record {
/*     */   private final ImmutableList<ChunkStep> steps;
/*     */   
/*   9 */   public ChunkPyramid(ImmutableList<ChunkStep> steps) { this.steps = steps; } public static final ChunkPyramid GENERATION_PYRAMID; public static final ChunkPyramid LOADING_PYRAMID; public ImmutableList<ChunkStep> steps() { return this.steps; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  62 */     GENERATION_PYRAMID = new Builder().step(ChunkStatus.EMPTY, s -> s).step(ChunkStatus.STRUCTURE_STARTS, s -> s.setTask(ChunkStatusTasks::generateStructureStarts)).step(ChunkStatus.STRUCTURE_REFERENCES, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).setTask(ChunkStatusTasks::generateStructureReferences)).step(ChunkStatus.BIOMES, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).setTask(ChunkStatusTasks::generateBiomes)).step(ChunkStatus.NOISE, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).addRequirement(ChunkStatus.BIOMES, 1).blockStateWriteRadius(0).setTask(ChunkStatusTasks::generateNoise)).step(ChunkStatus.SURFACE, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).addRequirement(ChunkStatus.BIOMES, 1).blockStateWriteRadius(0).setTask(ChunkStatusTasks::generateSurface)).step(ChunkStatus.CARVERS, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).blockStateWriteRadius(0).setTask(ChunkStatusTasks::generateCarvers)).step(ChunkStatus.FEATURES, s -> s.addRequirement(ChunkStatus.STRUCTURE_STARTS, 8).addRequirement(ChunkStatus.CARVERS, 1).blockStateWriteRadius(1).setTask(ChunkStatusTasks::generateFeatures)).step(ChunkStatus.INITIALIZE_LIGHT, s -> s.setTask(ChunkStatusTasks::initializeLight)).step(ChunkStatus.LIGHT, s -> s.addRequirement(ChunkStatus.INITIALIZE_LIGHT, 1).setTask(ChunkStatusTasks::light)).step(ChunkStatus.SPAWN, s -> s.addRequirement(ChunkStatus.BIOMES, 1).setTask(ChunkStatusTasks::generateSpawn)).step(ChunkStatus.FULL, s -> s.setTask(ChunkStatusTasks::full)).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     LOADING_PYRAMID = new Builder().step(ChunkStatus.EMPTY, s -> s).step(ChunkStatus.STRUCTURE_STARTS, s -> s.setTask(ChunkStatusTasks::loadStructureStarts)).step(ChunkStatus.STRUCTURE_REFERENCES, s -> s).step(ChunkStatus.BIOMES, s -> s).step(ChunkStatus.NOISE, s -> s).step(ChunkStatus.SURFACE, s -> s).step(ChunkStatus.CARVERS, s -> s).step(ChunkStatus.FEATURES, s -> s).step(ChunkStatus.INITIALIZE_LIGHT, s -> s.setTask(ChunkStatusTasks::initializeLight)).step(ChunkStatus.LIGHT, s -> s.addRequirement(ChunkStatus.INITIALIZE_LIGHT, 1).setTask(ChunkStatusTasks::light)).step(ChunkStatus.SPAWN, s -> s).step(ChunkStatus.FULL, s -> s.setTask(ChunkStatusTasks::full)).build();
/*     */   }
/*     */   public ChunkStep getStepTo(ChunkStatus status) {
/*  89 */     return (ChunkStep)this.steps.get(status.getIndex());
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  93 */     private final java.util.List<ChunkStep> steps = new java.util.ArrayList<>();
/*     */     
/*     */     public ChunkPyramid build() {
/*  96 */       return new ChunkPyramid(ImmutableList.copyOf(this.steps));
/*     */     }
/*     */     
/*     */     public Builder step(ChunkStatus status, UnaryOperator<ChunkStep.Builder> operator) {
/*     */       ChunkStep.Builder stepBuilder;
/* 101 */       if (this.steps.isEmpty()) {
/* 102 */         stepBuilder = new ChunkStep.Builder(status);
/*     */       } else {
/* 104 */         stepBuilder = new ChunkStep.Builder(status, this.steps.getLast());
/*     */       } 
/* 106 */       this.steps.add(((ChunkStep.Builder)operator.apply(stepBuilder)).build());
/* 107 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/status/ChunkPyramid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */