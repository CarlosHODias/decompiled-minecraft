/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ public interface ChunkResult<T>
/*     */ {
/*     */   static <T> ChunkResult<T> of(T value) {
/*  11 */     return new Success<>(value);
/*     */   }
/*     */   
/*     */   static <T> ChunkResult<T> error(String error) {
/*  15 */     return error(() -> error);
/*     */   }
/*     */   
/*     */   static <T> ChunkResult<T> error(Supplier<String> errorSupplier) {
/*  19 */     return new Fail<>(errorSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R> R orElse(ChunkResult<? extends R> chunkResult, R orElse) {
/*  27 */     R result = chunkResult.orElse(null);
/*  28 */     return (result != null) ? result : orElse;
/*     */   }
/*     */   boolean isSuccess();
/*     */   T orElse(T paramT);
/*     */   String getError();
/*     */   ChunkResult<T> ifSuccess(Consumer<T> paramConsumer);
/*     */   <R> ChunkResult<R> map(Function<T, R> paramFunction);
/*     */   <E extends Throwable> T orElseThrow(Supplier<E> paramSupplier) throws E;
/*     */   
/*     */   public static final class Success<T> extends Record implements ChunkResult<T> { private final T value;
/*     */     
/*  39 */     public Success(T value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Success;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  39 */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; } public T value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Success;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; }
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Success;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>; } public boolean isSuccess() {
/*  42 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public T orElse(T orElse) {
/*  47 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getError() {
/*  52 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChunkResult<T> ifSuccess(Consumer<T> consumer) {
/*  57 */       consumer.accept(this.value);
/*  58 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <R> ChunkResult<R> map(Function<T, R> map) {
/*  63 */       return new Success((T)map.apply(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E {
/*  68 */       return this.value;
/*     */     } }
/*     */   public static final class Fail<T> extends Record implements ChunkResult<T> { private final Supplier<String> error;
/*     */     
/*  72 */     public Fail(Supplier<String> error) { this.error = error; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Fail;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Fail;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Fail;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #72	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  72 */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>; } public Supplier<String> error() { return this.error; }
/*     */     
/*     */     public boolean isSuccess() {
/*  75 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T orElse(T orElse) {
/*  80 */       return orElse;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getError() {
/*  85 */       return this.error.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChunkResult<T> ifSuccess(Consumer<T> consumer) {
/*  90 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public <R> ChunkResult<R> map(Function<T, R> map) {
/*  95 */       return new Fail(this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E {
/* 100 */       throw (E)exceptionSupplier.get();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ChunkResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */