/*    */ package net.minecraft.world.level.chunk;
/*    */ import java.util.List;
/*    */ public interface Configuration { boolean alwaysRepack();
/*    */   
/*    */   int bitsInMemory();
/*    */   
/*    */   int bitsInStorage();
/*    */   
/*    */   <T> Palette<T> createPalette(Strategy<T> paramStrategy, List<T> paramList);
/*    */   
/*    */   public static final class Simple extends Record implements Configuration { private final Palette.Factory factory;
/*    */     private final int bits;
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/Configuration$Simple;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/Configuration$Simple;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple;
/*    */     }
/*    */     
/* 22 */     public Simple(Palette.Factory factory, int bits) { this.factory = factory; this.bits = bits; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/Configuration$Simple;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #22	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/Configuration$Simple;
/* 22 */       //   0	8	1	o	Ljava/lang/Object; } public Palette.Factory factory() { return this.factory; } public int bits() { return this.bits; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean alwaysRepack() {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> Palette<T> createPalette(Strategy<T> strategy, List<T> paletteEntries) {
/* 33 */       return this.factory.create(this.bits, paletteEntries);
/*    */     }
/*    */ 
/*    */     
/*    */     public int bitsInMemory() {
/* 38 */       return this.bits;
/*    */     }
/*    */ 
/*    */     
/*    */     public int bitsInStorage() {
/* 43 */       return this.bits;
/*    */     } }
/*    */   public static final class Global extends Record implements Configuration { private final int bitsInMemory; private final int bitsInStorage;
/*    */     
/* 47 */     public Global(int bitsInMemory, int bitsInStorage) { this.bitsInMemory = bitsInMemory; this.bitsInStorage = bitsInStorage; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/Configuration$Global;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 47 */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global; } public int bitsInMemory() { return this.bitsInMemory; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/Configuration$Global;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/Configuration$Global;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #47	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/Configuration$Global;
/* 47 */       //   0	8	1	o	Ljava/lang/Object; } public int bitsInStorage() { return this.bitsInStorage; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean alwaysRepack() {
/* 54 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> Palette<T> createPalette(Strategy<T> strategy, List<T> paletteEntries) {
/* 59 */       return strategy.globalPalette();
/*    */     } }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/Configuration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */