/*   */ package net.minecraft.server.jsonrpc.api;
/*   */ public final class ResultInfo<Result> extends Record {
/*   */   private final String name;
/*   */   private final Schema<Result> schema;
/*   */   
/* 6 */   public ResultInfo(String name, Schema<Result> schema) { this.name = name; this.schema = schema; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 6 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/ResultInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 6 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ResultInfo<TResult;>; } public Schema<Result> schema() { return this.schema; }
/*   */    public static <Result> com.mojang.serialization.Codec<ResultInfo<Result>> typedCodec() {
/* 8 */     return com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("name").forGetter(ResultInfo::name), (com.mojang.datafixers.kinds.App)Schema.<T>typedCodec().fieldOf("schema").forGetter(ResultInfo::schema)).apply((com.mojang.datafixers.kinds.Applicative)i, ResultInfo::new));
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/api/ResultInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */