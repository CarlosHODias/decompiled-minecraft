/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.stream.LongStream;
/*    */ 
/*    */ public interface PalettedContainerRO<T> {
/*    */   T get(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void getAll(java.util.function.Consumer<T> paramConsumer);
/*    */   
/*    */   void write(net.minecraft.network.FriendlyByteBuf paramFriendlyByteBuf);
/*    */   
/*    */   int getSerializedSize();
/*    */   
/*    */   @com.google.common.annotations.VisibleForTesting
/*    */   int bitsPerEntry();
/*    */   
/*    */   boolean maybeHas(java.util.function.Predicate<T> paramPredicate);
/*    */   
/*    */   void count(PalettedContainer.CountConsumer<T> paramCountConsumer);
/*    */   
/*    */   PalettedContainer<T> copy();
/*    */   
/*    */   PalettedContainer<T> recreate();
/*    */   
/*    */   PackedData<T> pack(Strategy<T> paramStrategy);
/*    */   
/*    */   public static interface Unpacker<T, C extends PalettedContainerRO<T>> {
/*    */     com.mojang.serialization.DataResult<C> read(Strategy<T> param1Strategy, PalettedContainerRO.PackedData<T> param1PackedData);
/*    */   }
/*    */   
/*    */   public static final class PackedData<T> extends Record {
/*    */     private final java.util.List<T> paletteEntries;
/*    */     private final java.util.Optional<LongStream> storage;
/*    */     
/* 35 */     public int bitsPerEntry() { return this.bitsPerEntry; } private final int bitsPerEntry; public static final int UNKNOWN_BITS_PER_ENTRY = -1; public java.util.Optional<LongStream> storage() { return this.storage; } public java.util.List<T> paletteEntries() { return this.paletteEntries; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 35 */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>; } public PackedData(java.util.List<T> paletteEntries, java.util.Optional<LongStream> storage, int bitsPerEntry) { this.paletteEntries = paletteEntries; this.storage = storage; this.bitsPerEntry = bitsPerEntry; }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */     }
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #35	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */     }
/*    */     public PackedData(java.util.List<T> paletteEntries, java.util.Optional<LongStream> storage) {
/* 44 */       this(paletteEntries, storage, -1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/PalettedContainerRO.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */