/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.resource.RenderTargetDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PostChain
/*     */   implements AutoCloseable
/*     */ {
/*  27 */   public static final Identifier MAIN_TARGET_ID = Identifier.withDefaultNamespace("main");
/*     */   
/*     */   private final List<PostPass> passes;
/*     */   private final Map<Identifier, PostChainConfig.InternalTarget> internalTargets;
/*     */   private final Set<Identifier> externalTargets;
/*  32 */   private final Map<Identifier, RenderTarget> persistentTargets = new HashMap<>();
/*     */   private final CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer;
/*     */   
/*     */   private PostChain(List<PostPass> passes, Map<Identifier, PostChainConfig.InternalTarget> internalTargets, Set<Identifier> externalTargets, CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer) {
/*  36 */     this.passes = passes;
/*  37 */     this.internalTargets = internalTargets;
/*  38 */     this.externalTargets = externalTargets;
/*  39 */     this.projectionMatrixBuffer = projectionMatrixBuffer;
/*     */   }
/*     */   
/*     */   public static PostChain load(PostChainConfig config, TextureManager textureManager, Set<Identifier> allowedExternalTargets, Identifier id, CachedOrthoProjectionMatrixBuffer projectionMatrixBuffer) throws ShaderManager.CompilationException {
/*  43 */     Stream<Identifier> referencedTargets = config.passes().stream().flatMap(PostChainConfig.Pass::referencedTargets);
/*     */     
/*  45 */     Set<Identifier> referencedExternalTargets = (Set<Identifier>)
/*  46 */       referencedTargets.filter(targetId -> !config.internalTargets().containsKey(targetId))
/*  47 */       .collect(Collectors.toSet());
/*     */     
/*  49 */     Sets.SetView setView = Sets.difference(referencedExternalTargets, allowedExternalTargets);
/*  50 */     if (!setView.isEmpty()) {
/*  51 */       throw new ShaderManager.CompilationException("Referenced external targets are not available in this context: " + String.valueOf(setView));
/*     */     }
/*     */     
/*  54 */     ImmutableList.Builder<PostPass> passes = ImmutableList.builder();
/*  55 */     for (int i = 0; i < config.passes().size(); i++) {
/*  56 */       PostChainConfig.Pass pass = config.passes().get(i);
/*  57 */       passes.add(createPass(textureManager, pass, id.withSuffix("/" + i)));
/*     */     } 
/*     */     
/*  60 */     return new PostChain((List<PostPass>)passes.build(), config.internalTargets(), referencedExternalTargets, projectionMatrixBuffer);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static PostPass createPass(TextureManager textureManager, PostChainConfig.Pass config, Identifier id) throws ShaderManager.CompilationException {
/*     */     // Byte code:
/*     */     //   0: iconst_1
/*     */     //   1: anewarray com/mojang/blaze3d/pipeline/RenderPipeline$Snippet
/*     */     //   4: dup
/*     */     //   5: iconst_0
/*     */     //   6: getstatic net/minecraft/client/renderer/RenderPipelines.POST_PROCESSING_SNIPPET : Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;
/*     */     //   9: aastore
/*     */     //   10: invokestatic builder : ([Lcom/mojang/blaze3d/pipeline/RenderPipeline$Snippet;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   13: aload_1
/*     */     //   14: invokevirtual fragmentShaderId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   17: invokevirtual withFragmentShader : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   20: aload_1
/*     */     //   21: invokevirtual vertexShaderId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   24: invokevirtual withVertexShader : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   27: aload_2
/*     */     //   28: invokevirtual withLocation : (Lnet/minecraft/resources/Identifier;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   31: astore_3
/*     */     //   32: aload_1
/*     */     //   33: invokevirtual inputs : ()Ljava/util/List;
/*     */     //   36: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   41: astore #4
/*     */     //   43: aload #4
/*     */     //   45: invokeinterface hasNext : ()Z
/*     */     //   50: ifeq -> 85
/*     */     //   53: aload #4
/*     */     //   55: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   60: checkcast net/minecraft/client/renderer/PostChainConfig$Input
/*     */     //   63: astore #5
/*     */     //   65: aload_3
/*     */     //   66: aload #5
/*     */     //   68: invokeinterface samplerName : ()Ljava/lang/String;
/*     */     //   73: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*     */     //   78: invokevirtual withSampler : (Ljava/lang/String;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   81: pop
/*     */     //   82: goto -> 43
/*     */     //   85: aload_3
/*     */     //   86: ldc 'SamplerInfo'
/*     */     //   88: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   91: invokevirtual withUniform : (Ljava/lang/String;Lcom/mojang/blaze3d/shaders/UniformType;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   94: pop
/*     */     //   95: aload_1
/*     */     //   96: invokevirtual uniforms : ()Ljava/util/Map;
/*     */     //   99: invokeinterface keySet : ()Ljava/util/Set;
/*     */     //   104: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   109: astore #4
/*     */     //   111: aload #4
/*     */     //   113: invokeinterface hasNext : ()Z
/*     */     //   118: ifeq -> 146
/*     */     //   121: aload #4
/*     */     //   123: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   128: checkcast java/lang/String
/*     */     //   131: astore #5
/*     */     //   133: aload_3
/*     */     //   134: aload #5
/*     */     //   136: getstatic com/mojang/blaze3d/shaders/UniformType.UNIFORM_BUFFER : Lcom/mojang/blaze3d/shaders/UniformType;
/*     */     //   139: invokevirtual withUniform : (Ljava/lang/String;Lcom/mojang/blaze3d/shaders/UniformType;)Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   142: pop
/*     */     //   143: goto -> 111
/*     */     //   146: aload_3
/*     */     //   147: invokevirtual build : ()Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   150: astore #4
/*     */     //   152: new java/util/ArrayList
/*     */     //   155: dup
/*     */     //   156: invokespecial <init> : ()V
/*     */     //   159: astore #5
/*     */     //   161: aload_1
/*     */     //   162: invokevirtual inputs : ()Ljava/util/List;
/*     */     //   165: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */     //   170: astore #6
/*     */     //   172: aload #6
/*     */     //   174: invokeinterface hasNext : ()Z
/*     */     //   179: ifeq -> 433
/*     */     //   182: aload #6
/*     */     //   184: invokeinterface next : ()Ljava/lang/Object;
/*     */     //   189: checkcast net/minecraft/client/renderer/PostChainConfig$Input
/*     */     //   192: astore #7
/*     */     //   194: aload #7
/*     */     //   196: dup
/*     */     //   197: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   200: pop
/*     */     //   201: astore #8
/*     */     //   203: iconst_0
/*     */     //   204: istore #9
/*     */     //   206: aload #8
/*     */     //   208: iload #9
/*     */     //   210: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   215: lookupswitch default -> 240, 0 -> 250, 1 -> 356
/*     */     //   240: new java/lang/MatchException
/*     */     //   243: dup
/*     */     //   244: aconst_null
/*     */     //   245: aconst_null
/*     */     //   246: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   249: athrow
/*     */     //   250: aload #8
/*     */     //   252: checkcast net/minecraft/client/renderer/PostChainConfig$TextureInput
/*     */     //   255: astore #10
/*     */     //   257: aload #10
/*     */     //   259: invokevirtual samplerName : ()Ljava/lang/String;
/*     */     //   262: astore #16
/*     */     //   264: aload #16
/*     */     //   266: astore #11
/*     */     //   268: aload #10
/*     */     //   270: invokevirtual location : ()Lnet/minecraft/resources/Identifier;
/*     */     //   273: astore #16
/*     */     //   275: aload #16
/*     */     //   277: astore #12
/*     */     //   279: aload #10
/*     */     //   281: invokevirtual width : ()I
/*     */     //   284: istore #16
/*     */     //   286: iload #16
/*     */     //   288: istore #13
/*     */     //   290: aload #10
/*     */     //   292: invokevirtual height : ()I
/*     */     //   295: istore #16
/*     */     //   297: iload #16
/*     */     //   299: istore #14
/*     */     //   301: aload #10
/*     */     //   303: invokevirtual bilinear : ()Z
/*     */     //   306: istore #16
/*     */     //   308: iload #16
/*     */     //   310: istore #15
/*     */     //   312: aload_0
/*     */     //   313: aload #12
/*     */     //   315: <illegal opcode> apply : ()Ljava/util/function/UnaryOperator;
/*     */     //   320: invokevirtual withPath : (Ljava/util/function/UnaryOperator;)Lnet/minecraft/resources/Identifier;
/*     */     //   323: invokevirtual getTexture : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/AbstractTexture;
/*     */     //   326: astore #16
/*     */     //   328: aload #5
/*     */     //   330: new net/minecraft/client/renderer/PostPass$TextureInput
/*     */     //   333: dup
/*     */     //   334: aload #11
/*     */     //   336: aload #16
/*     */     //   338: iload #13
/*     */     //   340: iload #14
/*     */     //   342: iload #15
/*     */     //   344: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/client/renderer/texture/AbstractTexture;IIZ)V
/*     */     //   347: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   352: pop
/*     */     //   353: goto -> 430
/*     */     //   356: aload #8
/*     */     //   358: checkcast net/minecraft/client/renderer/PostChainConfig$TargetInput
/*     */     //   361: astore #16
/*     */     //   363: aload #16
/*     */     //   365: invokevirtual samplerName : ()Ljava/lang/String;
/*     */     //   368: astore #21
/*     */     //   370: aload #21
/*     */     //   372: astore #17
/*     */     //   374: aload #16
/*     */     //   376: invokevirtual targetId : ()Lnet/minecraft/resources/Identifier;
/*     */     //   379: astore #21
/*     */     //   381: aload #21
/*     */     //   383: astore #18
/*     */     //   385: aload #16
/*     */     //   387: invokevirtual useDepthBuffer : ()Z
/*     */     //   390: istore #21
/*     */     //   392: iload #21
/*     */     //   394: istore #19
/*     */     //   396: aload #16
/*     */     //   398: invokevirtual bilinear : ()Z
/*     */     //   401: istore #21
/*     */     //   403: iload #21
/*     */     //   405: istore #20
/*     */     //   407: aload #5
/*     */     //   409: new net/minecraft/client/renderer/PostPass$TargetInput
/*     */     //   412: dup
/*     */     //   413: aload #17
/*     */     //   415: aload #18
/*     */     //   417: iload #19
/*     */     //   419: iload #20
/*     */     //   421: invokespecial <init> : (Ljava/lang/String;Lnet/minecraft/resources/Identifier;ZZ)V
/*     */     //   424: invokeinterface add : (Ljava/lang/Object;)Z
/*     */     //   429: pop
/*     */     //   430: goto -> 172
/*     */     //   433: new net/minecraft/client/renderer/PostPass
/*     */     //   436: dup
/*     */     //   437: aload #4
/*     */     //   439: aload_1
/*     */     //   440: invokevirtual outputTarget : ()Lnet/minecraft/resources/Identifier;
/*     */     //   443: aload_1
/*     */     //   444: invokevirtual uniforms : ()Ljava/util/Map;
/*     */     //   447: aload #5
/*     */     //   449: invokespecial <init> : (Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;Ljava/util/Map;Ljava/util/List;)V
/*     */     //   452: areturn
/*     */     //   453: astore #6
/*     */     //   455: new java/lang/MatchException
/*     */     //   458: dup
/*     */     //   459: aload #6
/*     */     //   461: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   464: aload #6
/*     */     //   466: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   469: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #64	-> 0
/*     */     //   #65	-> 14
/*     */     //   #66	-> 21
/*     */     //   #67	-> 28
/*     */     //   #69	-> 32
/*     */     //   #70	-> 65
/*     */     //   #71	-> 82
/*     */     //   #72	-> 85
/*     */     //   #74	-> 95
/*     */     //   #75	-> 133
/*     */     //   #76	-> 143
/*     */     //   #78	-> 146
/*     */     //   #79	-> 152
/*     */     //   #81	-> 161
/*     */     //   #82	-> 194
/*     */     //   #83	-> 250
/*     */     //   #84	-> 264
/*     */     //   #83	-> 268
/*     */     //   #84	-> 275
/*     */     //   #83	-> 279
/*     */     //   #84	-> 286
/*     */     //   #83	-> 290
/*     */     //   #84	-> 297
/*     */     //   #83	-> 301
/*     */     //   #84	-> 308
/*     */     //   #86	-> 312
/*     */     //   #87	-> 328
/*     */     //   #88	-> 353
/*     */     //   #89	-> 356
/*     */     //   #90	-> 370
/*     */     //   #89	-> 374
/*     */     //   #90	-> 381
/*     */     //   #89	-> 385
/*     */     //   #90	-> 392
/*     */     //   #89	-> 396
/*     */     //   #90	-> 403
/*     */     //   #91	-> 407
/*     */     //   #93	-> 430
/*     */     //   #95	-> 433
/*     */     //   #89	-> 453
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   65	17	5	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */     //   133	10	5	uniformGroupName	Ljava/lang/String;
/*     */     //   328	25	16	texture	Lnet/minecraft/client/renderer/texture/AbstractTexture;
/*     */     //   268	88	11	samplerName	Ljava/lang/String;
/*     */     //   279	77	12	location	Lnet/minecraft/resources/Identifier;
/*     */     //   290	66	13	width	I
/*     */     //   301	55	14	height	I
/*     */     //   312	44	15	bilinear	Z
/*     */     //   374	56	17	samplerName	Ljava/lang/String;
/*     */     //   385	45	18	targetId	Lnet/minecraft/resources/Identifier;
/*     */     //   396	34	19	useDepthBuffer	Z
/*     */     //   407	23	20	bilinear	Z
/*     */     //   194	236	7	input	Lnet/minecraft/client/renderer/PostChainConfig$Input;
/*     */     //   32	421	3	pipelineBuilder	Lcom/mojang/blaze3d/pipeline/RenderPipeline$Builder;
/*     */     //   152	301	4	pipeline	Lcom/mojang/blaze3d/pipeline/RenderPipeline;
/*     */     //   161	292	5	inputs	Ljava/util/List;
/*     */     //   0	470	0	textureManager	Lnet/minecraft/client/renderer/texture/TextureManager;
/*     */     //   0	470	1	config	Lnet/minecraft/client/renderer/PostChainConfig$Pass;
/*     */     //   0	470	2	id	Lnet/minecraft/resources/Identifier;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   161	292	5	inputs	Ljava/util/List<Lnet/minecraft/client/renderer/PostPass$Input;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   259	262	453	java/lang/Throwable
/*     */     //   270	273	453	java/lang/Throwable
/*     */     //   281	284	453	java/lang/Throwable
/*     */     //   292	295	453	java/lang/Throwable
/*     */     //   303	306	453	java/lang/Throwable
/*     */     //   365	368	453	java/lang/Throwable
/*     */     //   376	379	453	java/lang/Throwable
/*     */     //   387	390	453	java/lang/Throwable
/*     */     //   398	401	453	java/lang/Throwable
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToFrame(FrameGraphBuilder frame, int screenWidth, int screenHeight, TargetBundle providedTargets) {
/*  99 */     GpuBufferSlice projectionBuffer = this.projectionMatrixBuffer.getBuffer(screenWidth, screenHeight);
/*     */     
/* 101 */     Map<Identifier, ResourceHandle<RenderTarget>> targets = new HashMap<>(this.internalTargets.size() + this.externalTargets.size());
/* 102 */     for (Identifier id : this.externalTargets) {
/* 103 */       targets.put(id, providedTargets.getOrThrow(id));
/*     */     }
/*     */     
/* 106 */     for (Map.Entry<Identifier, PostChainConfig.InternalTarget> entry : this.internalTargets.entrySet()) {
/* 107 */       Identifier id = entry.getKey();
/* 108 */       PostChainConfig.InternalTarget target = entry.getValue();
/* 109 */       RenderTargetDescriptor descriptor = new RenderTargetDescriptor((Integer)
/* 110 */           target.width().orElse(screenWidth), (Integer)
/* 111 */           target.height().orElse(screenHeight), true, 
/*     */           
/* 113 */           target.clearColor());
/*     */       
/* 115 */       if (target.persistent()) {
/* 116 */         RenderTarget persistentTarget = getOrCreatePersistentTarget(id, descriptor);
/* 117 */         targets.put(id, frame.importExternal(id.toString(), persistentTarget)); continue;
/*     */       } 
/* 119 */       targets.put(id, frame.createInternal(id.toString(), (ResourceDescriptor)descriptor));
/*     */     } 
/*     */ 
/*     */     
/* 123 */     for (PostPass pass : this.passes) {
/* 124 */       pass.addToFrame(frame, targets, projectionBuffer);
/*     */     }
/*     */     
/* 127 */     for (Identifier id : this.externalTargets) {
/* 128 */       providedTargets.replace(id, targets.get(id));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void process(RenderTarget mainTarget, GraphicsResourceAllocator resourceAllocator) {
/* 135 */     FrameGraphBuilder frame = new FrameGraphBuilder();
/* 136 */     TargetBundle targets = TargetBundle.of(MAIN_TARGET_ID, frame.importExternal("main", mainTarget));
/* 137 */     addToFrame(frame, mainTarget.width, mainTarget.height, targets);
/* 138 */     frame.execute(resourceAllocator);
/*     */   }
/*     */   
/*     */   private RenderTarget getOrCreatePersistentTarget(Identifier id, RenderTargetDescriptor descriptor) {
/* 142 */     RenderTarget target = this.persistentTargets.get(id);
/* 143 */     if (target == null || target.width != descriptor.width() || target.height != descriptor.height()) {
/* 144 */       if (target != null) {
/* 145 */         target.destroyBuffers();
/*     */       }
/* 147 */       target = descriptor.allocate();
/* 148 */       descriptor.prepare(target);
/* 149 */       this.persistentTargets.put(id, target);
/*     */     } 
/* 151 */     return target;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 156 */     this.persistentTargets.values().forEach(RenderTarget::destroyBuffers);
/* 157 */     this.persistentTargets.clear();
/* 158 */     for (PostPass pass : this.passes)
/* 159 */       pass.close(); 
/*     */   }
/*     */   
/*     */   public static interface TargetBundle
/*     */   {
/*     */     static TargetBundle of(final Identifier targetId, final ResourceHandle<RenderTarget> target) {
/* 165 */       return new TargetBundle() {
/* 166 */           private ResourceHandle<RenderTarget> handle = target;
/*     */ 
/*     */           
/*     */           public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/* 170 */             if (id.equals(targetId)) {
/* 171 */               this.handle = handle;
/*     */             } else {
/* 173 */               throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public ResourceHandle<RenderTarget> get(Identifier id) {
/* 179 */             return id.equals(targetId) ? this.handle : null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     void replace(Identifier param1Identifier, ResourceHandle<RenderTarget> param1ResourceHandle);
/*     */     
/*     */     ResourceHandle<RenderTarget> get(Identifier param1Identifier);
/*     */     
/*     */     default ResourceHandle<RenderTarget> getOrThrow(Identifier id) {
/* 189 */       ResourceHandle<RenderTarget> handle = get(id);
/* 190 */       if (handle == null) {
/* 191 */         throw new IllegalArgumentException("Missing target with id " + String.valueOf(id));
/*     */       }
/* 193 */       return handle;
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TargetBundle {
/*     */     private ResourceHandle<RenderTarget> handle = target;
/*     */     
/*     */     public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
/*     */       if (id.equals(targetId)) {
/*     */         this.handle = handle;
/*     */       } else {
/*     */         throw new IllegalArgumentException("No target with id " + String.valueOf(id));
/*     */       } 
/*     */     }
/*     */     
/*     */     public ResourceHandle<RenderTarget> get(Identifier id) {
/*     */       return id.equals(targetId) ? this.handle : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/PostChain.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */