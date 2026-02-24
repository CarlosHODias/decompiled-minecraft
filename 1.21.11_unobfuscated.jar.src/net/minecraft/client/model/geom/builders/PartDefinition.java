/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ 
/*    */ public class PartDefinition {
/*    */   private final List<CubeDefinition> cubes;
/*    */   private final PartPose partPose;
/* 17 */   private final Map<String, PartDefinition> children = Maps.newHashMap();
/*    */   
/*    */   PartDefinition(List<CubeDefinition> cubes, PartPose partPose) {
/* 20 */     this.cubes = cubes;
/* 21 */     this.partPose = partPose;
/*    */   }
/*    */   
/*    */   public PartDefinition addOrReplaceChild(String name, CubeListBuilder cubes, PartPose partPose) {
/* 25 */     PartDefinition child = new PartDefinition(cubes.getCubes(), partPose);
/* 26 */     return addOrReplaceChild(name, child);
/*    */   }
/*    */   
/*    */   public PartDefinition addOrReplaceChild(String name, PartDefinition child) {
/* 30 */     PartDefinition previous = this.children.put(name, child);
/* 31 */     if (previous != null) {
/* 32 */       child.children.putAll(previous.children);
/*    */     }
/* 34 */     return child;
/*    */   }
/*    */   
/*    */   public PartDefinition clearRecursively() {
/* 38 */     for (String name : this.children.keySet()) {
/* 39 */       clearChild(name).clearRecursively();
/*    */     }
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public PartDefinition clearChild(String name) {
/* 45 */     PartDefinition child = this.children.get(name);
/* 46 */     if (child == null) {
/* 47 */       throw new IllegalArgumentException("No child with name: " + name);
/*    */     }
/*    */     
/* 50 */     return addOrReplaceChild(name, CubeListBuilder.create(), child.partPose);
/*    */   }
/*    */   
/*    */   public void retainPartsAndChildren(Set<String> parts) {
/* 54 */     for (Map.Entry<String, PartDefinition> entry : this.children.entrySet()) {
/* 55 */       PartDefinition child = entry.getValue();
/* 56 */       if (!parts.contains(entry.getKey())) {
/* 57 */         addOrReplaceChild(entry.getKey(), CubeListBuilder.create(), child.partPose).retainPartsAndChildren(parts);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void retainExactParts(Set<String> parts) {
/* 63 */     for (Map.Entry<String, PartDefinition> entry : this.children.entrySet()) {
/* 64 */       PartDefinition child = entry.getValue();
/* 65 */       if (parts.contains(entry.getKey())) {
/* 66 */         child.clearRecursively(); continue;
/*    */       } 
/* 68 */       addOrReplaceChild(entry.getKey(), CubeListBuilder.create(), child.partPose).retainExactParts(parts);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelPart bake(int texScaleX, int texScaleY) {
/* 74 */     Object2ObjectArrayMap<String, ModelPart> bakedChildren = (Object2ObjectArrayMap<String, ModelPart>)this.children.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> ((PartDefinition)e.getValue()).bake(texScaleX, texScaleY), (a, b) -> a, Object2ObjectArrayMap::new));
/* 75 */     List<ModelPart.Cube> bakedCubes = this.cubes.stream().map(definition -> definition.bake(texScaleX, texScaleY)).toList();
/*    */     
/* 77 */     ModelPart result = new ModelPart(bakedCubes, (Map)bakedChildren);
/* 78 */     result.setInitialPose(this.partPose);
/* 79 */     result.loadPose(this.partPose);
/* 80 */     return result;
/*    */   }
/*    */   
/*    */   public PartDefinition getChild(String name) {
/* 84 */     return this.children.get(name);
/*    */   }
/*    */   
/*    */   public Set<Map.Entry<String, PartDefinition>> getChildren() {
/* 88 */     return this.children.entrySet();
/*    */   }
/*    */   
/*    */   public PartDefinition transformed(UnaryOperator<PartPose> function) {
/* 92 */     PartDefinition newPart = new PartDefinition(this.cubes, function.apply(this.partPose));
/* 93 */     newPart.children.putAll(this.children);
/* 94 */     return newPart;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/PartDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */