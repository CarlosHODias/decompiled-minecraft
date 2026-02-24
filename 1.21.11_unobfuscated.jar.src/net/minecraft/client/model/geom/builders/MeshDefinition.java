/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ 
/*    */ public class MeshDefinition {
/*    */   private final PartDefinition root;
/*    */   
/*    */   public MeshDefinition() {
/* 12 */     this(new PartDefinition((List<CubeDefinition>)ImmutableList.of(), PartPose.ZERO));
/*    */   }
/*    */   
/*    */   private MeshDefinition(PartDefinition root) {
/* 16 */     this.root = root;
/*    */   }
/*    */   
/*    */   public PartDefinition getRoot() {
/* 20 */     return this.root;
/*    */   }
/*    */   
/*    */   public MeshDefinition transformed(UnaryOperator<PartPose> function) {
/* 24 */     return new MeshDefinition(this.root.transformed(function));
/*    */   }
/*    */   
/*    */   public MeshDefinition apply(MeshTransformer transformer) {
/* 28 */     return transformer.apply(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/MeshDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */