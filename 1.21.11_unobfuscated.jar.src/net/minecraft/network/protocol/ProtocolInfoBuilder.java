/*     */ package net.minecraft.network.protocol;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.ConnectionProtocol;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.ProtocolInfo;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Unit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProtocolInfoBuilder<T extends PacketListener, B extends ByteBuf, C>
/*     */ {
/*     */   private final ConnectionProtocol protocol;
/*     */   private final PacketFlow flow;
/*  21 */   private final List<CodecEntry<T, ?, B, C>> codecs = new ArrayList<>();
/*     */   private BundlerInfo bundlerInfo;
/*     */   
/*     */   public ProtocolInfoBuilder(ConnectionProtocol protocol, PacketFlow flow) {
/*  25 */     this.protocol = protocol;
/*  26 */     this.flow = flow;
/*     */   }
/*     */   private static final class CodecEntry<T extends PacketListener, P extends Packet<? super T>, B extends ByteBuf, C> extends Record { private final PacketType<P> type; private final StreamCodec<? super B, P> serializer; private final CodecModifier<B, P, C> modifier;
/*  29 */     private CodecEntry(PacketType<P> type, StreamCodec<? super B, P> serializer, CodecModifier<B, P, C> modifier) { this.type = type; this.serializer = serializer; this.modifier = modifier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  29 */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; } public PacketType<P> type() { return this.type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #29	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  29 */       //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; } public StreamCodec<? super B, P> serializer() { return this.serializer; } public CodecModifier<B, P, C> modifier() { return this.modifier; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addToBuilder(ProtocolCodecBuilder<ByteBuf, T> codecBuilder, Function<ByteBuf, B> contextWrapper, C context) {
/*     */       StreamCodec<? super B, P> finalSerializer;
/*  36 */       if (this.modifier != null) {
/*  37 */         finalSerializer = this.modifier.apply(this.serializer, context);
/*     */       } else {
/*  39 */         finalSerializer = this.serializer;
/*     */       } 
/*  41 */       StreamCodec<ByteBuf, P> baseCodec = finalSerializer.mapStream(contextWrapper);
/*  42 */       codecBuilder.add(this.type, baseCodec);
/*     */     } }
/*     */ 
/*     */   
/*     */   public <P extends Packet<? super T>> ProtocolInfoBuilder<T, B, C> addPacket(PacketType<P> type, StreamCodec<? super B, P> serializer) {
/*  47 */     this.codecs.add(new CodecEntry<>(type, serializer, null));
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public <P extends Packet<? super T>> ProtocolInfoBuilder<T, B, C> addPacket(PacketType<P> type, StreamCodec<? super B, P> serializer, CodecModifier<B, P, C> modifier) {
/*  52 */     this.codecs.add(new CodecEntry<>(type, serializer, modifier));
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public <P extends BundlePacket<? super T>, D extends BundleDelimiterPacket<? super T>> ProtocolInfoBuilder<T, B, C> withBundlePacket(PacketType<P> bundlerPacket, Function<Iterable<Packet<? super T>>, P> constructor, D delimiterPacket) {
/*  57 */     StreamCodec<ByteBuf, D> delimitedCodec = StreamCodec.unit(delimiterPacket);
/*     */     
/*  59 */     PacketType<D> delimiterType = (PacketType)delimiterPacket.type();
/*  60 */     this.codecs.add(new CodecEntry<>(delimiterType, (StreamCodec)delimitedCodec, null));
/*  61 */     this.bundlerInfo = BundlerInfo.createForPacket(bundlerPacket, constructor, (BundleDelimiterPacket<? super T>)delimiterPacket);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   private StreamCodec<ByteBuf, Packet<? super T>> buildPacketCodec(Function<ByteBuf, B> contextWrapper, List<CodecEntry<T, ?, B, C>> codecs, C context) {
/*  66 */     ProtocolCodecBuilder<ByteBuf, T> codecBuilder = new ProtocolCodecBuilder<>(this.flow);
/*     */     
/*  68 */     for (CodecEntry<T, ?, B, C> codec : codecs) {
/*  69 */       codec.addToBuilder(codecBuilder, contextWrapper, context);
/*     */     }
/*  71 */     return codecBuilder.build();
/*     */   }
/*     */   
/*     */   private static ProtocolInfo.Details buildDetails(final ConnectionProtocol protocol, final PacketFlow flow, final List<? extends CodecEntry<?, ?, ?, ?>> codecs) {
/*  75 */     return new ProtocolInfo.Details()
/*     */       {
/*     */         public ConnectionProtocol id() {
/*  78 */           return protocol;
/*     */         }
/*     */ 
/*     */         
/*     */         public PacketFlow flow() {
/*  83 */           return flow;
/*     */         }
/*     */ 
/*     */         
/*     */         public void listPackets(ProtocolInfo.Details.PacketVisitor output) {
/*  88 */           for (int i = 0; i < codecs.size(); i++) {
/*  89 */             ProtocolInfoBuilder.CodecEntry<?, ?, ?, ?> entry = codecs.get(i);
/*  90 */             output.accept(entry.type, i);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public SimpleUnboundProtocol<T, B> buildUnbound(final C context) {
/*  97 */     final List<CodecEntry<T, ?, B, C>> codecs = List.copyOf(this.codecs);
/*  98 */     final BundlerInfo bundlerInfo = this.bundlerInfo;
/*     */     
/* 100 */     final ProtocolInfo.Details details = buildDetails(this.protocol, this.flow, codecs);
/*     */     
/* 102 */     return new SimpleUnboundProtocol<T, B>()
/*     */       {
/*     */         public ProtocolInfo<T> bind(Function<ByteBuf, B> contextWrapper) {
/* 105 */           return new ProtocolInfoBuilder.Implementation<>(ProtocolInfoBuilder.this.protocol, ProtocolInfoBuilder.this.flow, ProtocolInfoBuilder.this.buildPacketCodec(contextWrapper, codecs, (C)context), bundlerInfo);
/*     */         }
/*     */ 
/*     */         
/*     */         public ProtocolInfo.Details details() {
/* 110 */           return details;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public UnboundProtocol<T, B, C> buildUnbound() {
/* 116 */     final List<CodecEntry<T, ?, B, C>> codecs = List.copyOf(this.codecs);
/* 117 */     final BundlerInfo bundlerInfo = this.bundlerInfo;
/*     */     
/* 119 */     final ProtocolInfo.Details details = buildDetails(this.protocol, this.flow, codecs);
/*     */     
/* 121 */     return new UnboundProtocol<T, B, C>()
/*     */       {
/*     */         public ProtocolInfo<T> bind(Function<ByteBuf, B> contextWrapper, C context) {
/* 124 */           return new ProtocolInfoBuilder.Implementation<>(ProtocolInfoBuilder.this.protocol, ProtocolInfoBuilder.this.flow, ProtocolInfoBuilder.this.buildPacketCodec(contextWrapper, codecs, context), bundlerInfo);
/*     */         }
/*     */ 
/*     */         
/*     */         public ProtocolInfo.Details details() {
/* 129 */           return details;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static <L extends PacketListener, B extends ByteBuf> SimpleUnboundProtocol<L, B> protocol(ConnectionProtocol id, PacketFlow flow, Consumer<ProtocolInfoBuilder<L, B, Unit>> config) {
/* 135 */     ProtocolInfoBuilder<L, B, Unit> builder = new ProtocolInfoBuilder<>(id, flow);
/* 136 */     config.accept(builder);
/* 137 */     return builder.buildUnbound(Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.network.ServerboundPacketListener, B extends ByteBuf> SimpleUnboundProtocol<T, B> serverboundProtocol(ConnectionProtocol id, Consumer<ProtocolInfoBuilder<T, B, Unit>> config) {
/* 141 */     return protocol(id, PacketFlow.SERVERBOUND, config);
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.network.ClientboundPacketListener, B extends ByteBuf> SimpleUnboundProtocol<T, B> clientboundProtocol(ConnectionProtocol id, Consumer<ProtocolInfoBuilder<T, B, Unit>> config) {
/* 145 */     return protocol(id, PacketFlow.CLIENTBOUND, config);
/*     */   }
/*     */   
/*     */   private static <L extends PacketListener, B extends ByteBuf, C> UnboundProtocol<L, B, C> contextProtocol(ConnectionProtocol id, PacketFlow flow, Consumer<ProtocolInfoBuilder<L, B, C>> config) {
/* 149 */     ProtocolInfoBuilder<L, B, C> builder = new ProtocolInfoBuilder<>(id, flow);
/* 150 */     config.accept(builder);
/* 151 */     return builder.buildUnbound();
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.network.ServerboundPacketListener, B extends ByteBuf, C> UnboundProtocol<T, B, C> contextServerboundProtocol(ConnectionProtocol id, Consumer<ProtocolInfoBuilder<T, B, C>> config) {
/* 155 */     return contextProtocol(id, PacketFlow.SERVERBOUND, config);
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.network.ClientboundPacketListener, B extends ByteBuf, C> UnboundProtocol<T, B, C> contextClientboundProtocol(ConnectionProtocol id, Consumer<ProtocolInfoBuilder<T, B, C>> config) {
/* 159 */     return contextProtocol(id, PacketFlow.CLIENTBOUND, config);
/*     */   }
/*     */   private static final class Implementation<L extends PacketListener> extends Record implements ProtocolInfo<L> { private final ConnectionProtocol id; private final PacketFlow flow; private final StreamCodec<ByteBuf, Packet<? super L>> codec; private final BundlerInfo bundlerInfo;
/* 162 */     private Implementation(ConnectionProtocol id, PacketFlow flow, StreamCodec<ByteBuf, Packet<? super L>> codec, BundlerInfo bundlerInfo) { this.id = id; this.flow = flow; this.codec = codec; this.bundlerInfo = bundlerInfo; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 162 */       //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$Implementation<TL;>; } public ConnectionProtocol id() { return this.id; } public PacketFlow flow() { return this.flow; } public StreamCodec<ByteBuf, Packet<? super L>> codec() { return this.codec; } public BundlerInfo bundlerInfo() { return this.bundlerInfo; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/ProtocolInfoBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */