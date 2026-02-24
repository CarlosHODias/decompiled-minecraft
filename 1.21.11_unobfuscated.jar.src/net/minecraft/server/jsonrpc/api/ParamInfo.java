/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ public final class ParamInfo<Param> extends Record {
/*    */   private final String name;
/*    */   private final Schema<Param> schema;
/*    */   private final boolean required;
/*    */   
/*  7 */   public ParamInfo(String name, Schema<Param> schema, boolean required) { this.name = name; this.schema = schema; this.required = required; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/api/ParamInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*  7 */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/api/ParamInfo<TParam;>; } public Schema<Param> schema() { return this.schema; } public boolean required() { return this.required; }
/*    */    public static <Param> com.mojang.serialization.MapCodec<ParamInfo<Param>> typedCodec() {
/*  9 */     return com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.fieldOf("name").forGetter(ParamInfo::name), (com.mojang.datafixers.kinds.App)Schema.<T>typedCodec().fieldOf("schema").forGetter(ParamInfo::schema), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.fieldOf("required").forGetter(ParamInfo::required)).apply((com.mojang.datafixers.kinds.Applicative)i, ParamInfo::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParamInfo(String name, Schema<Param> schema) {
/* 17 */     this(name, schema, true);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/api/ParamInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */