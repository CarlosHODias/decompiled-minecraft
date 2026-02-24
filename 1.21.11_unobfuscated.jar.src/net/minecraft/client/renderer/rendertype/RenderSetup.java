/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RenderSetup
/*     */ {
/*     */   final RenderPipeline pipeline;
/*     */   final Map<String, TextureBinding> textures;
/*     */   final TextureTransform textureTransform;
/*     */   final OutputTarget outputTarget;
/*     */   final OutlineProperty outlineProperty;
/*     */   final boolean useLightmap;
/*     */   final boolean useOverlay;
/*     */   final boolean affectsCrumbling;
/*     */   final boolean sortOnUpload;
/*     */   final int bufferSize;
/*     */   final LayeringTransform layeringTransform;
/*     */   
/*     */   private RenderSetup(RenderPipeline pipeline, Map<String, TextureBinding> textures, boolean useLightmap, boolean useOverlay, LayeringTransform layeringTransform, OutputTarget outputTarget, TextureTransform textureTransform, OutlineProperty outlineProperty, boolean affectsCrumbling, boolean sortOnUpload, int bufferSize) {
/*  38 */     this.pipeline = pipeline;
/*  39 */     this.textures = textures;
/*  40 */     this.outputTarget = outputTarget;
/*  41 */     this.textureTransform = textureTransform;
/*  42 */     this.useLightmap = useLightmap;
/*  43 */     this.useOverlay = useOverlay;
/*  44 */     this.outlineProperty = outlineProperty;
/*  45 */     this.layeringTransform = layeringTransform;
/*  46 */     this.affectsCrumbling = affectsCrumbling;
/*  47 */     this.sortOnUpload = sortOnUpload;
/*  48 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  53 */     return "RenderSetup[layeringTransform=" + String.valueOf(this.layeringTransform) + ", textureTransform=" + String.valueOf(this.textureTransform) + ", textures=" + String.valueOf(this.textures) + ", outlineProperty=" + String.valueOf(this.outlineProperty) + ", useLightmap=" + this.useLightmap + ", useOverlay=" + this.useOverlay + "]";
/*     */   }
/*     */   
/*     */   public static RenderSetupBuilder builder(RenderPipeline pipeline) {
/*  57 */     return new RenderSetupBuilder(pipeline);
/*     */   }
/*     */   
/*     */   public Map<String, TextureAndSampler> getTextures() {
/*  61 */     if (this.textures.isEmpty() && !this.useOverlay && !this.useLightmap) {
/*  62 */       return Collections.emptyMap();
/*     */     }
/*     */     
/*  65 */     Map<String, TextureAndSampler> result = new HashMap<>();
/*  66 */     if (this.useOverlay) {
/*  67 */       result.put("Sampler1", new TextureAndSampler((Minecraft.getInstance()).gameRenderer.overlayTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)));
/*     */     }
/*  69 */     if (this.useLightmap) {
/*  70 */       result.put("Sampler2", new TextureAndSampler((Minecraft.getInstance()).gameRenderer.lightTexture().getTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)));
/*     */     }
/*  72 */     TextureManager textureManager = Minecraft.getInstance().getTextureManager();
/*  73 */     for (Map.Entry<String, TextureBinding> entry : this.textures.entrySet()) {
/*  74 */       AbstractTexture texture = textureManager.getTexture(((TextureBinding)entry.getValue()).location);
/*  75 */       GpuSampler samplerOverride = ((TextureBinding)entry.getValue()).sampler().get();
/*  76 */       result.put(entry.getKey(), new TextureAndSampler(texture.getTextureView(), (samplerOverride != null) ? samplerOverride : texture.getSampler()));
/*     */     } 
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   public enum OutlineProperty {
/*  82 */     NONE("none"),
/*  83 */     IS_OUTLINE("is_outline"),
/*  84 */     AFFECTS_OUTLINE("affects_outline");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     OutlineProperty(String name) {
/*  90 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  95 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RenderSetupBuilder {
/*     */     private final RenderPipeline pipeline;
/*     */     private boolean useLightmap = false;
/*     */     private boolean useOverlay = false;
/* 103 */     private LayeringTransform layeringTransform = LayeringTransform.NO_LAYERING;
/* 104 */     private OutputTarget outputTarget = OutputTarget.MAIN_TARGET;
/* 105 */     private TextureTransform textureTransform = TextureTransform.DEFAULT_TEXTURING;
/*     */     private boolean affectsCrumbling = false;
/*     */     private boolean sortOnUpload = false;
/* 108 */     private int bufferSize = 1536;
/* 109 */     private RenderSetup.OutlineProperty outlineProperty = RenderSetup.OutlineProperty.NONE;
/* 110 */     private final Map<String, RenderSetup.TextureBinding> textures = new HashMap<>();
/*     */     
/*     */     private RenderSetupBuilder(RenderPipeline pipeline) {
/* 113 */       this.pipeline = pipeline;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder withTexture(String name, Identifier texture) {
/* 117 */       this.textures.put(name, new RenderSetup.TextureBinding(texture, () -> null));
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder withTexture(String name, Identifier texture, Supplier<GpuSampler> sampler) {
/* 122 */       this.textures.put(name, new RenderSetup.TextureBinding(texture, (Supplier<GpuSampler>)Suppliers.memoize(() -> (sampler == null) ? null : sampler.get())));
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder useLightmap() {
/* 127 */       this.useLightmap = true;
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder useOverlay() {
/* 132 */       this.useOverlay = true;
/* 133 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder affectsCrumbling() {
/* 137 */       this.affectsCrumbling = true;
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder sortOnUpload() {
/* 142 */       this.sortOnUpload = true;
/* 143 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder bufferSize(int bufferSize) {
/* 147 */       this.bufferSize = bufferSize;
/* 148 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setLayeringTransform(LayeringTransform layeringTransform) {
/* 152 */       this.layeringTransform = layeringTransform;
/* 153 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setOutputTarget(OutputTarget outputTarget) {
/* 157 */       this.outputTarget = outputTarget;
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setTextureTransform(TextureTransform textureTransform) {
/* 162 */       this.textureTransform = textureTransform;
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetupBuilder setOutline(RenderSetup.OutlineProperty outlineProperty) {
/* 167 */       this.outlineProperty = outlineProperty;
/* 168 */       return this;
/*     */     }
/*     */     
/*     */     public RenderSetup createRenderSetup() {
/* 172 */       return new RenderSetup(this.pipeline, this.textures, this.useLightmap, this.useOverlay, this.layeringTransform, this.outputTarget, this.textureTransform, this.outlineProperty, this.affectsCrumbling, this.sortOnUpload, this.bufferSize);
/*     */     } }
/*     */   public static final class TextureAndSampler extends Record { private final GpuTextureView textureView; private final GpuSampler sampler;
/*     */     
/* 176 */     public TextureAndSampler(GpuTextureView textureView, GpuSampler sampler) { this.textureView = textureView; this.sampler = sampler; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 176 */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler; } public GpuTextureView textureView() { return this.textureView; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #176	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureAndSampler;
/* 176 */       //   0	8	1	o	Ljava/lang/Object; } public GpuSampler sampler() { return this.sampler; }
/*     */      } static final class TextureBinding extends Record { private final Identifier location; private final Supplier<GpuSampler> sampler;
/* 178 */     TextureBinding(Identifier location, Supplier<GpuSampler> sampler) { this.location = location; this.sampler = sampler; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #178	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/* 178 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier location() { return this.location; } public Supplier<GpuSampler> sampler() { return this.sampler; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/rendertype/RenderSetup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */