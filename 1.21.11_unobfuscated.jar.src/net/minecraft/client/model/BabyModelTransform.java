/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ 
/*    */ public final class BabyModelTransform extends Record implements net.minecraft.client.model.geom.builders.MeshTransformer {
/*    */   private final boolean scaleHead;
/*    */   private final float babyYHeadOffset;
/*    */   private final float babyZHeadOffset;
/*    */   private final float babyHeadScale;
/*    */   private final float babyBodyScale;
/*    */   private final float bodyYOffset;
/*    */   private final java.util.Set<String> headParts;
/*    */   
/* 13 */   public BabyModelTransform(boolean scaleHead, float babyYHeadOffset, float babyZHeadOffset, float babyHeadScale, float babyBodyScale, float bodyYOffset, java.util.Set<String> headParts) { this.scaleHead = scaleHead; this.babyYHeadOffset = babyYHeadOffset; this.babyZHeadOffset = babyZHeadOffset; this.babyHeadScale = babyHeadScale; this.babyBodyScale = babyBodyScale; this.bodyYOffset = bodyYOffset; this.headParts = headParts; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/BabyModelTransform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/client/model/BabyModelTransform; } public boolean scaleHead() { return this.scaleHead; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/BabyModelTransform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/BabyModelTransform; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/BabyModelTransform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/BabyModelTransform;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public float babyYHeadOffset() { return this.babyYHeadOffset; } public float babyZHeadOffset() { return this.babyZHeadOffset; } public float babyHeadScale() { return this.babyHeadScale; } public float babyBodyScale() { return this.babyBodyScale; } public float bodyYOffset() { return this.bodyYOffset; } public java.util.Set<String> headParts() { return this.headParts; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BabyModelTransform(java.util.Set<String> headParts) {
/* 23 */     this(false, 5.0F, 2.0F, headParts);
/*    */   }
/*    */   
/*    */   public BabyModelTransform(boolean scaleHead, float babyYHeadOffset, float babyZHeadOffset, java.util.Set<String> headParts) {
/* 27 */     this(scaleHead, babyYHeadOffset, babyZHeadOffset, 2.0F, 2.0F, 24.0F, headParts);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.client.model.geom.builders.MeshDefinition apply(net.minecraft.client.model.geom.builders.MeshDefinition mesh) {
/* 32 */     float headScale = this.scaleHead ? (1.5F / this.babyHeadScale) : 1.0F;
/* 33 */     float bodyScale = 1.0F / this.babyBodyScale;
/*    */     
/*    */     java.util.function.UnaryOperator<net.minecraft.client.model.geom.PartPose> headTransform = p -> headScale.translated(0.0F, this.babyYHeadOffset, this.babyZHeadOffset).scaled(headScale);
/*    */     java.util.function.UnaryOperator<net.minecraft.client.model.geom.PartPose> bodyTransform = p -> bodyScale.translated(0.0F, this.bodyYOffset, 0.0F).scaled(bodyScale);
/* 37 */     net.minecraft.client.model.geom.builders.MeshDefinition babyMesh = new net.minecraft.client.model.geom.builders.MeshDefinition();
/*    */ 
/*    */     
/* 40 */     for (java.util.Map.Entry<String, net.minecraft.client.model.geom.builders.PartDefinition> entry : (Iterable<java.util.Map.Entry<String, net.minecraft.client.model.geom.builders.PartDefinition>>)mesh.getRoot().getChildren()) {
/* 41 */       String name = entry.getKey();
/* 42 */       net.minecraft.client.model.geom.builders.PartDefinition part = entry.getValue();
/* 43 */       babyMesh.getRoot().addOrReplaceChild(name, part.transformed(this.headParts.contains(name) ? headTransform : bodyTransform));
/*    */     } 
/*    */     
/* 46 */     return babyMesh;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/BabyModelTransform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */