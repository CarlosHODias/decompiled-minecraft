/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class PostChainConfig extends Record {
/*     */   private final Map<Identifier, InternalTarget> internalTargets;
/*     */   private final List<Pass> passes;
/*     */   public static final Codec<PostChainConfig> CODEC;
/*     */   
/*  18 */   public PostChainConfig(Map<Identifier, InternalTarget> internalTargets, List<Pass> passes) { this.internalTargets = internalTargets; this.passes = passes; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*  18 */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig; } public Map<Identifier, InternalTarget> internalTargets() { return this.internalTargets; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #18	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig;
/*  18 */     //   0	8	1	o	Ljava/lang/Object; } public List<Pass> passes() { return this.passes; }
/*     */ 
/*     */   
/*     */   static {
/*  22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.unboundedMap(Identifier.CODEC, InternalTarget.CODEC).optionalFieldOf("targets", Map.of()).forGetter(PostChainConfig::internalTargets), (App)Pass.CODEC.listOf().optionalFieldOf("passes", List.of()).forGetter(PostChainConfig::passes)).apply((Applicative)i, PostChainConfig::new));
/*     */   }
/*     */   public static final class InternalTarget extends Record { private final Optional<Integer> width; private final Optional<Integer> height; private final boolean persistent; private final int clearColor;
/*     */     public static final Codec<InternalTarget> CODEC;
/*     */     
/*  27 */     public InternalTarget(Optional<Integer> width, Optional<Integer> height, boolean persistent, int clearColor) { this.width = width; this.height = height; this.persistent = persistent; this.clearColor = clearColor; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$InternalTarget;
/*  27 */       //   0	8	1	o	Ljava/lang/Object; } public Optional<Integer> width() { return this.width; } public Optional<Integer> height() { return this.height; } public boolean persistent() { return this.persistent; } public int clearColor() { return this.clearColor; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(InternalTarget::width), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(InternalTarget::height), (App)Codec.BOOL.optionalFieldOf("persistent", false).forGetter(InternalTarget::persistent), (App)ExtraCodecs.ARGB_COLOR_CODEC.optionalFieldOf("clear_color", 0).forGetter(InternalTarget::clearColor)).apply((Applicative)i, InternalTarget::new));
/*     */     } }
/*     */   public static final class Pass extends Record { private final Identifier vertexShaderId; private final Identifier fragmentShaderId;
/*     */     private final List<PostChainConfig.Input> inputs;
/*     */     private final Identifier outputTarget;
/*     */     private final Map<String, List<UniformValue>> uniforms;
/*     */     private static final Codec<List<PostChainConfig.Input>> INPUTS_CODEC;
/*     */     
/*  41 */     public Pass(Identifier vertexShaderId, Identifier fragmentShaderId, List<PostChainConfig.Input> inputs, Identifier outputTarget, Map<String, List<UniformValue>> uniforms) { this.vertexShaderId = vertexShaderId; this.fragmentShaderId = fragmentShaderId; this.inputs = inputs; this.outputTarget = outputTarget; this.uniforms = uniforms; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$Pass;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #41	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*  41 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier vertexShaderId() { return this.vertexShaderId; } public Identifier fragmentShaderId() { return this.fragmentShaderId; } public List<PostChainConfig.Input> inputs() { return this.inputs; } public Identifier outputTarget() { return this.outputTarget; } public Map<String, List<UniformValue>> uniforms() { return this.uniforms; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  48 */       INPUTS_CODEC = PostChainConfig.Input.CODEC.listOf().validate(inputs -> {
/*     */             it.unimi.dsi.fastutil.objects.ObjectArraySet<String> objectArraySet = new it.unimi.dsi.fastutil.objects.ObjectArraySet(inputs.size());
/*     */             for (PostChainConfig.Input input : (Iterable<PostChainConfig.Input>)inputs) {
/*     */               if (!objectArraySet.add(input.samplerName()))
/*     */                 return com.mojang.serialization.DataResult.error(()); 
/*     */             } 
/*     */             return com.mojang.serialization.DataResult.success(inputs);
/*     */           });
/*     */     }
/*  57 */     private static final Codec<Map<String, List<UniformValue>>> UNIFORM_BLOCKS_CODEC = (Codec<Map<String, List<UniformValue>>>)Codec.unboundedMap((Codec)Codec.STRING, UniformValue.CODEC.listOf()); public static final Codec<Pass> CODEC;
/*     */     static {
/*  59 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Identifier.CODEC.fieldOf("vertex_shader").forGetter(Pass::vertexShaderId), (App)Identifier.CODEC.fieldOf("fragment_shader").forGetter(Pass::fragmentShaderId), (App)INPUTS_CODEC.optionalFieldOf("inputs", List.of()).forGetter(Pass::inputs), (App)Identifier.CODEC.fieldOf("output").forGetter(Pass::outputTarget), (App)UNIFORM_BLOCKS_CODEC.optionalFieldOf("uniforms", Map.of()).forGetter(Pass::uniforms)).apply((Applicative)i, Pass::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public java.util.stream.Stream<Identifier> referencedTargets() {
/*  68 */       java.util.stream.Stream<Identifier> inputTargets = this.inputs.stream().flatMap(input -> input.referencedTargets().stream());
/*  69 */       return java.util.stream.Stream.concat(inputTargets, java.util.stream.Stream.of(this.outputTarget));
/*     */     } }
/*     */   public static interface Input { public static final Codec<Input> CODEC;
/*     */     
/*     */     static {
/*  74 */       CODEC = Codec.xor(PostChainConfig.TextureInput.CODEC, PostChainConfig.TargetInput.CODEC).xmap(either -> (Input)either.map(java.util.function.Function.identity(), java.util.function.Function.identity()), input -> {
/*     */             // Byte code:
/*     */             //   0: aload_0
/*     */             //   1: dup
/*     */             //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */             //   5: pop
/*     */             //   6: astore_1
/*     */             //   7: iconst_0
/*     */             //   8: istore_2
/*     */             //   9: aload_1
/*     */             //   10: iload_2
/*     */             //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */             //   16: lookupswitch default -> 44, 0 -> 54, 1 -> 66
/*     */             //   44: new java/lang/MatchException
/*     */             //   47: dup
/*     */             //   48: aconst_null
/*     */             //   49: aconst_null
/*     */             //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */             //   53: athrow
/*     */             //   54: aload_1
/*     */             //   55: checkcast net/minecraft/client/renderer/PostChainConfig$TextureInput
/*     */             //   58: astore_3
/*     */             //   59: aload_3
/*     */             //   60: invokestatic left : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   63: goto -> 77
/*     */             //   66: aload_1
/*     */             //   67: checkcast net/minecraft/client/renderer/PostChainConfig$TargetInput
/*     */             //   70: astore #4
/*     */             //   72: aload #4
/*     */             //   74: invokestatic right : (Ljava/lang/Object;)Lcom/mojang/datafixers/util/Either;
/*     */             //   77: areturn
/*     */             // Line number table:
/*     */             //   Java source line number -> byte code offset
/*     */             //   #76	-> 0
/*     */             //   #77	-> 54
/*     */             //   #78	-> 66
/*     */             // Local variable table:
/*     */             //   start	length	slot	name	descriptor
/*     */             //   59	7	3	texture	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*     */             //   72	5	4	target	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/*     */             //   7	70	1	selector0$temp	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */             //   9	68	2	index$1	I
/*     */             //   0	78	0	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */           });
/*     */     }
/*     */     java.util.Set<Identifier> referencedTargets();
/*     */     String samplerName(); }
/*     */   public static final class TextureInput extends Record implements Input { private final String samplerName;
/*     */     private final Identifier location;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final boolean bilinear;
/*     */     public static final Codec<TextureInput> CODEC;
/*     */     
/*  87 */     public TextureInput(String samplerName, Identifier location, int width, int height, boolean bilinear) { this.samplerName = samplerName; this.location = location; this.width = width; this.height = height; this.bilinear = bilinear; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TextureInput;
/*  87 */       //   0	8	1	o	Ljava/lang/Object; } public String samplerName() { return this.samplerName; } public Identifier location() { return this.location; } public int width() { return this.width; } public int height() { return this.height; } public boolean bilinear() { return this.bilinear; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  94 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TextureInput::samplerName), (App)Identifier.CODEC.fieldOf("location").forGetter(TextureInput::location), (App)ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(TextureInput::width), (App)ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(TextureInput::height), (App)Codec.BOOL.optionalFieldOf("bilinear", false).forGetter(TextureInput::bilinear)).apply((Applicative)i, TextureInput::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public java.util.Set<Identifier> referencedTargets() {
/* 104 */       return java.util.Set.of();
/*     */     } }
/*     */   public static final class TargetInput extends Record implements Input { private final String samplerName; private final Identifier targetId; private final boolean useDepthBuffer; private final boolean bilinear; public static final Codec<TargetInput> CODEC;
/*     */     
/* 108 */     public TargetInput(String samplerName, Identifier targetId, boolean useDepthBuffer, boolean bilinear) { this.samplerName = samplerName; this.targetId = targetId; this.useDepthBuffer = useDepthBuffer; this.bilinear = bilinear; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/PostChainConfig$TargetInput;
/* 108 */       //   0	8	1	o	Ljava/lang/Object; } public String samplerName() { return this.samplerName; } public Identifier targetId() { return this.targetId; } public boolean useDepthBuffer() { return this.useDepthBuffer; } public boolean bilinear() { return this.bilinear; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 114 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("sampler_name").forGetter(TargetInput::samplerName), (App)Identifier.CODEC.fieldOf("target").forGetter(TargetInput::targetId), (App)Codec.BOOL.optionalFieldOf("use_depth_buffer", false).forGetter(TargetInput::useDepthBuffer), (App)Codec.BOOL.optionalFieldOf("bilinear", false).forGetter(TargetInput::bilinear)).apply((Applicative)i, TargetInput::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public java.util.Set<Identifier> referencedTargets() {
/* 123 */       return java.util.Set.of(this.targetId);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/PostChainConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */