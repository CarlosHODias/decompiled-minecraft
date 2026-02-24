/*    */ package net.minecraft.client.model.geom.builders;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface MeshTransformer {
/*    */   static {
/*  7 */     IDENTITY = (mesh -> mesh);
/*    */   }
/*    */   public static final MeshTransformer IDENTITY;
/*    */   static MeshTransformer scaling(float factor) {
/* 11 */     float yOffset = 24.016F * (1.0F - factor);
/* 12 */     return mesh -> mesh.transformed(());
/*    */   }
/*    */   
/*    */   MeshDefinition apply(MeshDefinition paramMeshDefinition);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/MeshTransformer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */