/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ 
/*    */ public class LayerDefinition {
/*    */   private final MeshDefinition mesh;
/*    */   private final MaterialDefinition material;
/*    */   
/*    */   private LayerDefinition(MeshDefinition mesh, MaterialDefinition material) {
/* 10 */     this.mesh = mesh;
/* 11 */     this.material = material;
/*    */   }
/*    */   
/*    */   public LayerDefinition apply(MeshTransformer transformer) {
/* 15 */     return new LayerDefinition(transformer.apply(this.mesh), this.material);
/*    */   }
/*    */   
/*    */   public ModelPart bakeRoot() {
/* 19 */     return this.mesh.getRoot().bake(this.material.xTexSize, this.material.yTexSize);
/*    */   }
/*    */   
/*    */   public static LayerDefinition create(MeshDefinition mesh, int xTexSize, int yTexSize) {
/* 23 */     return new LayerDefinition(mesh, new MaterialDefinition(xTexSize, yTexSize));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/LayerDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */