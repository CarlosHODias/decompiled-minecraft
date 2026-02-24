/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.math.Quadrant;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class Variant extends Record implements BlockModelPart.Unbaked {
/*    */   private final net.minecraft.resources.Identifier modelLocation;
/*    */   private final SimpleModelState modelState;
/*    */   public static final com.mojang.serialization.MapCodec<Variant> MAP_CODEC;
/*    */   
/* 12 */   public Variant(net.minecraft.resources.Identifier modelLocation, SimpleModelState modelState) { this.modelLocation = modelLocation; this.modelState = modelState; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/Variant;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant; } public net.minecraft.resources.Identifier modelLocation() { return this.modelLocation; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/Variant;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/Variant;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/Variant;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public SimpleModelState modelState() { return this.modelState; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.resources.Identifier.CODEC.fieldOf("model").forGetter(Variant::modelLocation), (App)SimpleModelState.MAP_CODEC.forGetter(Variant::modelState)).apply((com.mojang.datafixers.kinds.Applicative)i, Variant::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final com.mojang.serialization.Codec<Variant> CODEC = MAP_CODEC.codec();
/*    */   
/*    */   public Variant(net.minecraft.resources.Identifier modelLocation) {
/* 24 */     this(modelLocation, SimpleModelState.DEFAULT);
/*    */   }
/*    */   
/*    */   public Variant withXRot(Quadrant x) {
/* 28 */     return withState(this.modelState.withX(x));
/*    */   }
/*    */   
/*    */   public Variant withYRot(Quadrant y) {
/* 32 */     return withState(this.modelState.withY(y));
/*    */   }
/*    */   
/*    */   public Variant withZRot(Quadrant z) {
/* 36 */     return withState(this.modelState.withZ(z));
/*    */   }
/*    */   
/*    */   public Variant withUvLock(boolean uvLock) {
/* 40 */     return withState(this.modelState.withUvLock(uvLock));
/*    */   }
/*    */   
/*    */   public Variant withModel(net.minecraft.resources.Identifier modelLocation) {
/* 44 */     return new Variant(modelLocation, this.modelState);
/*    */   }
/*    */   
/*    */   public Variant withState(SimpleModelState modelState) {
/* 48 */     return new Variant(this.modelLocation, modelState);
/*    */   }
/*    */   
/*    */   public Variant with(VariantMutator mutator) {
/* 52 */     return mutator.apply(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockModelPart bake(net.minecraft.client.resources.model.ModelBaker modelBakery) {
/* 57 */     return SimpleModelWrapper.bake(modelBakery, this.modelLocation, this.modelState.asModelState());
/*    */   }
/*    */ 
/*    */   
/*    */   public void resolveDependencies(net.minecraft.client.resources.model.ResolvableModel.Resolver resolver) {
/* 62 */     resolver.markDependency(this.modelLocation);
/*    */   }
/*    */   public static final class SimpleModelState extends Record { private final Quadrant x; private final Quadrant y; private final Quadrant z; private final boolean uvLock; public static final com.mojang.serialization.MapCodec<SimpleModelState> MAP_CODEC;
/* 65 */     public SimpleModelState(Quadrant x, Quadrant y, Quadrant z, boolean uvLock) { this.x = x; this.y = y; this.z = z; this.uvLock = uvLock; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;
/* 65 */       //   0	8	1	o	Ljava/lang/Object; } public Quadrant x() { return this.x; } public Quadrant y() { return this.y; } public Quadrant z() { return this.z; } public boolean uvLock() { return this.uvLock; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 71 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Quadrant.CODEC.optionalFieldOf("x", Quadrant.R0).forGetter(SimpleModelState::x), (App)Quadrant.CODEC.optionalFieldOf("y", Quadrant.R0).forGetter(SimpleModelState::y), (App)Quadrant.CODEC.optionalFieldOf("z", Quadrant.R0).forGetter(SimpleModelState::z), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("uvlock", false).forGetter(SimpleModelState::uvLock)).apply((com.mojang.datafixers.kinds.Applicative)i, SimpleModelState::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 78 */     public static final SimpleModelState DEFAULT = new SimpleModelState(Quadrant.R0, Quadrant.R0, Quadrant.R0, false);
/*    */     
/*    */     public net.minecraft.client.resources.model.ModelState asModelState() {
/* 81 */       net.minecraft.client.resources.model.BlockModelRotation rotation = net.minecraft.client.resources.model.BlockModelRotation.get(Quadrant.fromXYZAngles(this.x, this.y, this.z));
/* 82 */       return this.uvLock ? rotation.withUvLock() : (net.minecraft.client.resources.model.ModelState)rotation;
/*    */     }
/*    */     
/*    */     public SimpleModelState withX(Quadrant x) {
/* 86 */       return new SimpleModelState(x, this.y, this.z, this.uvLock);
/*    */     }
/*    */     
/*    */     public SimpleModelState withY(Quadrant y) {
/* 90 */       return new SimpleModelState(this.x, y, this.z, this.uvLock);
/*    */     }
/*    */     
/*    */     public SimpleModelState withZ(Quadrant z) {
/* 94 */       return new SimpleModelState(this.x, this.y, z, this.uvLock);
/*    */     }
/*    */     
/*    */     public SimpleModelState withUvLock(boolean uvLock) {
/* 98 */       return new SimpleModelState(this.x, this.y, this.z, uvLock);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/Variant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */