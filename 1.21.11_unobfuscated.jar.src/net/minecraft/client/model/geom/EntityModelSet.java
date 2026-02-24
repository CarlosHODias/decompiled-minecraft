/*    */ package net.minecraft.client.model.geom;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ 
/*    */ public class EntityModelSet
/*    */ {
/*  9 */   public static final EntityModelSet EMPTY = new EntityModelSet(Map.of());
/*    */   
/*    */   private final Map<ModelLayerLocation, LayerDefinition> roots;
/*    */   
/*    */   public EntityModelSet(Map<ModelLayerLocation, LayerDefinition> roots) {
/* 14 */     this.roots = roots;
/*    */   }
/*    */   
/*    */   public ModelPart bakeLayer(ModelLayerLocation id) {
/* 18 */     LayerDefinition result = this.roots.get(id);
/* 19 */     if (result == null) {
/* 20 */       throw new IllegalArgumentException("No model for layer " + String.valueOf(id));
/*    */     }
/* 22 */     return result.bakeRoot();
/*    */   }
/*    */   
/*    */   public static EntityModelSet vanilla() {
/* 26 */     return new EntityModelSet((Map<ModelLayerLocation, LayerDefinition>)ImmutableMap.copyOf(LayerDefinitions.createRoots()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/EntityModelSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */