/*   */ package net.minecraft.client.model.geom;
/*   */ public final class ModelLayerLocation extends Record { private final net.minecraft.resources.Identifier model;
/*   */   private final String layer;
/*   */   
/* 5 */   public ModelLayerLocation(net.minecraft.resources.Identifier model, String layer) { this.model = model; this.layer = layer; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/ModelLayerLocation;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/client/model/geom/ModelLayerLocation; } public net.minecraft.resources.Identifier model() { return this.model; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/ModelLayerLocation;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/model/geom/ModelLayerLocation;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public String layer() { return this.layer; }
/*   */   
/*   */   public String toString() {
/* 8 */     return String.valueOf(this.model) + "#" + String.valueOf(this.model);
/*   */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/ModelLayerLocation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */