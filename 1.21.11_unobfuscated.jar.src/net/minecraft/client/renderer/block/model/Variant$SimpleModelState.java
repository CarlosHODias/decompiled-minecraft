/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.math.Quadrant;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.resources.model.BlockModelRotation;
/*    */ import net.minecraft.client.resources.model.ModelState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleModelState
/*    */   extends Record
/*    */ {
/*    */   private final Quadrant x;
/*    */   private final Quadrant y;
/*    */   private final Quadrant z;
/*    */   private final boolean uvLock;
/*    */   public static final MapCodec<SimpleModelState> MAP_CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/Variant$SimpleModelState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SimpleModelState(Quadrant x, Quadrant y, Quadrant z, boolean uvLock) {
/* 65 */     this.x = x; this.y = y; this.z = z; this.uvLock = uvLock; } public Quadrant x() { return this.x; } public Quadrant y() { return this.y; } public Quadrant z() { return this.z; } public boolean uvLock() { return this.uvLock; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 71 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Quadrant.CODEC.optionalFieldOf("x", Quadrant.R0).forGetter(SimpleModelState::x), (App)Quadrant.CODEC.optionalFieldOf("y", Quadrant.R0).forGetter(SimpleModelState::y), (App)Quadrant.CODEC.optionalFieldOf("z", Quadrant.R0).forGetter(SimpleModelState::z), (App)Codec.BOOL.optionalFieldOf("uvlock", false).forGetter(SimpleModelState::uvLock)).apply((Applicative)i, SimpleModelState::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   public static final SimpleModelState DEFAULT = new SimpleModelState(Quadrant.R0, Quadrant.R0, Quadrant.R0, false);
/*    */   
/*    */   public ModelState asModelState() {
/* 81 */     BlockModelRotation rotation = BlockModelRotation.get(Quadrant.fromXYZAngles(this.x, this.y, this.z));
/* 82 */     return this.uvLock ? rotation.withUvLock() : (ModelState)rotation;
/*    */   }
/*    */   
/*    */   public SimpleModelState withX(Quadrant x) {
/* 86 */     return new SimpleModelState(x, this.y, this.z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withY(Quadrant y) {
/* 90 */     return new SimpleModelState(this.x, y, this.z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withZ(Quadrant z) {
/* 94 */     return new SimpleModelState(this.x, this.y, z, this.uvLock);
/*    */   }
/*    */   
/*    */   public SimpleModelState withUvLock(boolean uvLock) {
/* 98 */     return new SimpleModelState(this.x, this.y, this.z, uvLock);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/Variant$SimpleModelState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */