/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ 
/*     */ public interface IncomingRpcMethod<Params, Result> {
/*     */   MethodInfo<Params, Result> info();
/*     */   
/*     */   Attributes attributes();
/*     */   
/*     */   JsonElement apply(MinecraftApi paramMinecraftApi, JsonElement paramJsonElement, ClientInfo paramClientInfo);
/*     */   
/*     */   public static final class Attributes extends Record {
/*     */     private final boolean runOnMainThread;
/*     */     private final boolean discoverable;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*     */     }
/*     */     
/*  28 */     public Attributes(boolean runOnMainThread, boolean discoverable) { this.runOnMainThread = runOnMainThread; this.discoverable = discoverable; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Attributes;
/*  28 */       //   0	8	1	o	Ljava/lang/Object; } public boolean runOnMainThread() { return this.runOnMainThread; } public boolean discoverable() { return this.discoverable; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ParameterlessMethod<Params, Result>
/*     */     extends Record
/*     */     implements IncomingRpcMethod<Params, Result>
/*     */   {
/*     */     private final MethodInfo<Params, Result> info;
/*     */     
/*     */     private final IncomingRpcMethod.Attributes attributes;
/*     */     
/*     */     private final IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier;
/*     */     
/*     */     public ParameterlessMethod(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier) {
/*  44 */       this.info = info; this.attributes = attributes; this.supplier = supplier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #44	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  44 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$ParameterlessMethod<TParams;TResult;>; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> supplier() { return this.supplier; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*  52 */       if (paramsJson != null && (!paramsJson.isJsonArray() || !paramsJson.getAsJsonArray().isEmpty())) {
/*  53 */         throw new net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException("Expected no params, or an empty array");
/*     */       }
/*  55 */       if (this.info.params().isPresent()) {
/*  56 */         throw new IllegalArgumentException("Parameterless method unexpectedly has parameter description");
/*     */       }
/*  58 */       Result result = this.supplier.apply(minecraftApi, clientInfo);
/*  59 */       if (this.info.result().isEmpty()) {
/*  60 */         throw new IllegalStateException("No result codec defined");
/*     */       }
/*  62 */       return (JsonElement)((net.minecraft.server.jsonrpc.api.ResultInfo)this.info.result().get()).schema().codec().encodeStart((com.mojang.serialization.DynamicOps)com.mojang.serialization.JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException::new);
/*     */     } }
/*     */   public static final class Method<Params, Result> extends Record implements IncomingRpcMethod<Params, Result> { private final MethodInfo<Params, Result> info; private final IncomingRpcMethod.Attributes attributes; private final IncomingRpcMethod.RpcMethodFunction<Params, Result> function;
/*     */     
/*  66 */     public Method(MethodInfo<Params, Result> info, IncomingRpcMethod.Attributes attributes, IncomingRpcMethod.RpcMethodFunction<Params, Result> function) { this.info = info; this.attributes = attributes; this.function = function; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  66 */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/IncomingRpcMethod$Method<TParams;TResult;>; } public MethodInfo<Params, Result> info() { return this.info; } public IncomingRpcMethod.Attributes attributes() { return this.attributes; } public IncomingRpcMethod.RpcMethodFunction<Params, Result> function() { return this.function; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement apply(MinecraftApi minecraftApi, JsonElement paramsJson, ClientInfo clientInfo) {
/*     */       JsonElement paramsJsonElement;
/*  74 */       if (paramsJson == null || (!paramsJson.isJsonArray() && !paramsJson.isJsonObject())) {
/*  75 */         throw new net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException("Expected params as array or named");
/*     */       }
/*     */       
/*  78 */       if (this.info.params().isEmpty()) {
/*  79 */         throw new IllegalArgumentException("Method defined as having parameters without describing them");
/*     */       }
/*     */ 
/*     */       
/*  83 */       if (paramsJson.isJsonObject()) {
/*  84 */         String parameterName = ((net.minecraft.server.jsonrpc.api.ParamInfo)this.info.params().get()).name();
/*  85 */         JsonElement jsonElement = paramsJson.getAsJsonObject().get(parameterName);
/*  86 */         if (jsonElement == null) {
/*  87 */           throw new net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException(String.format(java.util.Locale.ROOT, "Params passed by-name, but expected param [%s] does not exist", new Object[] { parameterName }));
/*     */         }
/*  89 */         paramsJsonElement = jsonElement;
/*     */       } else {
/*  91 */         com.google.gson.JsonArray jsonArray = paramsJson.getAsJsonArray();
/*  92 */         if (jsonArray.isEmpty() || jsonArray.size() > 1) {
/*  93 */           throw new net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException("Expected exactly one element in the params array");
/*     */         }
/*  95 */         paramsJsonElement = jsonArray.get(0);
/*     */       } 
/*  97 */       Params params = (Params)((net.minecraft.server.jsonrpc.api.ParamInfo)this.info.params().get()).schema().codec().parse((com.mojang.serialization.DynamicOps)com.mojang.serialization.JsonOps.INSTANCE, paramsJsonElement).getOrThrow(net.minecraft.server.jsonrpc.methods.InvalidParameterJsonRpcException::new);
/*  98 */       Result result = this.function.apply(minecraftApi, params, clientInfo);
/*  99 */       if (this.info.result().isEmpty()) {
/* 100 */         throw new IllegalStateException("No result codec defined");
/*     */       }
/* 102 */       return (JsonElement)((net.minecraft.server.jsonrpc.api.ResultInfo)this.info.result().get()).schema().codec().encodeStart((com.mojang.serialization.DynamicOps)com.mojang.serialization.JsonOps.INSTANCE, result).getOrThrow(net.minecraft.server.jsonrpc.methods.EncodeJsonRpcException::new);
/*     */     } }
/*     */    @FunctionalInterface
/*     */   public static interface RpcMethodFunction<Params, Result> { Result apply(MinecraftApi param1MinecraftApi, Params param1Params, ClientInfo param1ClientInfo); } @FunctionalInterface
/*     */   public static interface ParameterlessRpcMethodFunction<Result> { Result apply(MinecraftApi param1MinecraftApi, ClientInfo param1ClientInfo); }
/* 107 */   public static class IncomingRpcMethodBuilder<Params, Result> { private String description = "";
/*     */     private net.minecraft.server.jsonrpc.api.ParamInfo<Params> paramInfo;
/*     */     private net.minecraft.server.jsonrpc.api.ResultInfo<Result> resultInfo;
/*     */     private boolean discoverable = true;
/*     */     private boolean runOnMainThread = true;
/*     */     private IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> parameterlessFunction;
/*     */     private IncomingRpcMethod.RpcMethodFunction<Params, Result> parameterFunction;
/*     */     
/*     */     public IncomingRpcMethodBuilder(IncomingRpcMethod.ParameterlessRpcMethodFunction<Result> function) {
/* 116 */       this.parameterlessFunction = function;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder(IncomingRpcMethod.RpcMethodFunction<Params, Result> function) {
/* 120 */       this.parameterFunction = function;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder(Function<MinecraftApi, Result> supplier) {
/* 124 */       this.parameterlessFunction = ((apiService, clientInfo) -> supplier.apply(apiService));
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> description(String description) {
/* 128 */       this.description = description;
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> response(String resultName, net.minecraft.server.jsonrpc.api.Schema<Result> resultSchema) {
/* 133 */       this.resultInfo = new net.minecraft.server.jsonrpc.api.ResultInfo(resultName, resultSchema.info());
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> param(String paramName, net.minecraft.server.jsonrpc.api.Schema<Params> paramSchema) {
/* 138 */       this.paramInfo = new net.minecraft.server.jsonrpc.api.ParamInfo(paramName, paramSchema.info());
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> undiscoverable() {
/* 143 */       this.discoverable = false;
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethodBuilder<Params, Result> notOnMainThread() {
/* 148 */       this.runOnMainThread = false;
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public IncomingRpcMethod<Params, Result> build() {
/* 153 */       if (this.resultInfo == null) {
/* 154 */         throw new IllegalStateException("No response defined");
/*     */       }
/*     */       
/* 157 */       IncomingRpcMethod.Attributes attributes = new IncomingRpcMethod.Attributes(this.runOnMainThread, this.discoverable);
/* 158 */       MethodInfo<Params, Result> methodInfo = new MethodInfo(this.description, this.paramInfo, this.resultInfo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       if (this.parameterlessFunction != null) {
/* 165 */         return new IncomingRpcMethod.ParameterlessMethod<>(methodInfo, attributes, this.parameterlessFunction);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 170 */       if (this.parameterFunction != null) {
/* 171 */         if (this.paramInfo == null) {
/* 172 */           throw new IllegalStateException("No param schema defined");
/*     */         }
/* 174 */         return new IncomingRpcMethod.Method<>(methodInfo, attributes, this.parameterFunction);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 180 */       throw new IllegalStateException("No method defined");
/*     */     }
/*     */ 
/*     */     
/*     */     public IncomingRpcMethod<?, ?> register(net.minecraft.core.Registry<IncomingRpcMethod<?, ?>> methodRegistry, String key) {
/* 185 */       return register(methodRegistry, net.minecraft.resources.Identifier.withDefaultNamespace(key));
/*     */     }
/*     */     
/*     */     private IncomingRpcMethod<?, ?> register(net.minecraft.core.Registry<IncomingRpcMethod<?, ?>> methodRegistry, net.minecraft.resources.Identifier id) {
/* 189 */       return (IncomingRpcMethod<?, ?>)net.minecraft.core.Registry.register(methodRegistry, id, build());
/*     */     } }
/*     */ 
/*     */   
/*     */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(ParameterlessRpcMethodFunction<Result> function) {
/* 194 */     return new IncomingRpcMethodBuilder<>(function);
/*     */   }
/*     */   
/*     */   static <Params, Result> IncomingRpcMethodBuilder<Params, Result> method(RpcMethodFunction<Params, Result> function) {
/* 198 */     return new IncomingRpcMethodBuilder<>(function);
/*     */   }
/*     */   
/*     */   static <Result> IncomingRpcMethodBuilder<Void, Result> method(Function<MinecraftApi, Result> supplier) {
/* 202 */     return new IncomingRpcMethodBuilder<>(supplier);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/IncomingRpcMethod.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */