/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import java.util.Comparator;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.renderer.MultiBufferSource;
/*    */ import net.minecraft.client.renderer.entity.ItemRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public class Material
/*    */ {
/* 15 */   public static final Comparator<Material> COMPARATOR = Comparator.comparing(Material::atlasLocation).thenComparing(Material::texture);
/*    */   
/*    */   private final Identifier atlasLocation;
/*    */   private final Identifier texture;
/*    */   private RenderType renderType;
/*    */   
/*    */   public Material(Identifier atlasLocation, Identifier texture) {
/* 22 */     this.atlasLocation = atlasLocation;
/* 23 */     this.texture = texture;
/*    */   }
/*    */   
/*    */   public Identifier atlasLocation() {
/* 27 */     return this.atlasLocation;
/*    */   }
/*    */   
/*    */   public Identifier texture() {
/* 31 */     return this.texture;
/*    */   }
/*    */   
/*    */   public RenderType renderType(Function<Identifier, RenderType> renderType) {
/* 35 */     if (this.renderType == null) {
/* 36 */       this.renderType = renderType.apply(this.atlasLocation);
/*    */     }
/* 38 */     return this.renderType;
/*    */   }
/*    */   
/*    */   public VertexConsumer buffer(MaterialSet materials, MultiBufferSource bufferSource, Function<Identifier, RenderType> renderType) {
/* 42 */     return materials.get(this).wrap(bufferSource.getBuffer(renderType(renderType)));
/*    */   }
/*    */   
/*    */   public VertexConsumer buffer(MaterialSet materials, MultiBufferSource bufferSource, Function<Identifier, RenderType> renderType, boolean sheeted, boolean hasFoil) {
/* 46 */     return materials.get(this).wrap(ItemRenderer.getFoilBuffer(bufferSource, renderType(renderType), sheeted, hasFoil));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 51 */     if (this == o) {
/* 52 */       return true;
/*    */     }
/* 54 */     if (o == null || getClass() != o.getClass()) {
/* 55 */       return false;
/*    */     }
/* 57 */     Material material = (Material)o;
/* 58 */     return (this.atlasLocation.equals(material.atlasLocation) && this.texture.equals(material.texture));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hash(new Object[] { this.atlasLocation, this.texture });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "Material{atlasLocation=" + String.valueOf(this.atlasLocation) + ", texture=" + String.valueOf(this.texture) + "}";
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/Material.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */