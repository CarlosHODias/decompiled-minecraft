/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.server.jsonrpc.IncomingRpcMethod;
/*    */ import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
/*    */ import net.minecraft.server.jsonrpc.api.MethodInfo;
/*    */ import net.minecraft.server.jsonrpc.api.Schema;
/*    */ import net.minecraft.server.jsonrpc.api.SchemaComponent;
/*    */ 
/*    */ public class DiscoveryService {
/*    */   public static DiscoverResponse discover(List<SchemaComponent<?>> schemaRegistry) {
/* 20 */     List<MethodInfo.Named<?, ?>> methods = new java.util.ArrayList<>(BuiltInRegistries.INCOMING_RPC_METHOD.size() + BuiltInRegistries.OUTGOING_RPC_METHOD.size());
/* 21 */     BuiltInRegistries.INCOMING_RPC_METHOD.listElements().forEach(e -> {
/*    */           if (((IncomingRpcMethod)e.value()).attributes().discoverable()) {
/*    */             methods.add(((IncomingRpcMethod)e.value()).info().named(e.key().identifier()));
/*    */           }
/*    */         });
/*    */     
/* 27 */     BuiltInRegistries.OUTGOING_RPC_METHOD.listElements().forEach(e -> {
/*    */           if (((OutgoingRpcMethod)e.value()).attributes().discoverable()) {
/*    */             methods.add(((OutgoingRpcMethod)e.value()).info().named(e.key().identifier()));
/*    */           }
/*    */         });
/*    */     
/* 33 */     Map<String, Schema<?>> schemas = new java.util.HashMap<>();
/* 34 */     for (SchemaComponent<?> component : schemaRegistry) {
/* 35 */       schemas.put(component.name(), component.schema().info());
/*    */     }
/*    */     
/* 38 */     DiscoverInfo discoverInfo = new DiscoverInfo("Minecraft Server JSON-RPC", "2.0.0");
/* 39 */     return new DiscoverResponse("1.3.2", discoverInfo, methods, new DiscoverComponents(schemas));
/*    */   }
/*    */   public static final class DiscoverResponse extends Record { private final String jsonRpcProtocolVersion; private final DiscoveryService.DiscoverInfo discoverInfo; private final List<MethodInfo.Named<?, ?>> methods; private final DiscoveryService.DiscoverComponents components; public static final MapCodec<DiscoverResponse> CODEC;
/* 42 */     public DiscoverResponse(String jsonRpcProtocolVersion, DiscoveryService.DiscoverInfo discoverInfo, List<MethodInfo.Named<?, ?>> methods, DiscoveryService.DiscoverComponents components) { this.jsonRpcProtocolVersion = jsonRpcProtocolVersion; this.discoverInfo = discoverInfo; this.methods = methods; this.components = components; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 42 */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse; } public String jsonRpcProtocolVersion() { return this.jsonRpcProtocolVersion; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #42	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverResponse;
/* 42 */       //   0	8	1	o	Ljava/lang/Object; } public DiscoveryService.DiscoverInfo discoverInfo() { return this.discoverInfo; } public List<MethodInfo.Named<?, ?>> methods() { return this.methods; } public DiscoveryService.DiscoverComponents components() { return this.components; } static {
/* 43 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("openrpc").forGetter(DiscoverResponse::jsonRpcProtocolVersion), (App)DiscoveryService.DiscoverInfo.CODEC.codec().fieldOf("info").forGetter(DiscoverResponse::discoverInfo), (App)Codec.list(MethodInfo.Named.CODEC).fieldOf("methods").forGetter(DiscoverResponse::methods), (App)DiscoveryService.DiscoverComponents.CODEC.codec().fieldOf("components").forGetter(DiscoverResponse::components)).apply((Applicative)i, DiscoverResponse::new));
/*    */     } }
/*    */ 
/*    */   
/*    */   public static final class DiscoverComponents extends Record
/*    */   {
/*    */     private final Map<String, Schema<?>> schemas;
/*    */     
/* 51 */     public DiscoverComponents(Map<String, Schema<?>> schemas) { this.schemas = schemas; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #51	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverComponents;
/* 51 */       //   0	8	1	o	Ljava/lang/Object; } public Map<String, Schema<?>> schemas() { return this.schemas; }
/* 52 */      public static final MapCodec<DiscoverComponents> CODEC = typedSchema();
/*    */ 
/*    */     
/*    */     private static MapCodec<DiscoverComponents> typedSchema() {
/* 56 */       return RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap((Codec)Codec.STRING, Schema.CODEC).fieldOf("schemas").forGetter(DiscoverComponents::schemas)).apply((Applicative)i, DiscoverComponents::new));
/*    */     } }
/*    */   public static final class DiscoverInfo extends Record { private final String title;
/*    */     private final String version;
/*    */     public static final MapCodec<DiscoverInfo> CODEC;
/*    */     
/* 62 */     public DiscoverInfo(String title, String version) { this.title = title; this.version = version; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #62	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/DiscoveryService$DiscoverInfo;
/* 62 */       //   0	8	1	o	Ljava/lang/Object; } public String title() { return this.title; } public String version() { return this.version; } static {
/* 63 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("title").forGetter(DiscoverInfo::title), (App)Codec.STRING.fieldOf("version").forGetter(DiscoverInfo::version)).apply((Applicative)i, DiscoverInfo::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/methods/DiscoveryService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */