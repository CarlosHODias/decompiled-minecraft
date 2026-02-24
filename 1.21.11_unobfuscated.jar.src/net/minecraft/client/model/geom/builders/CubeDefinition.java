/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CubeDefinition
/*    */ {
/*    */   private final String comment;
/*    */   private final Vector3fc origin;
/*    */   private final Vector3fc dimensions;
/*    */   private final CubeDeformation grow;
/*    */   private final boolean mirror;
/*    */   private final UVPair texCoord;
/*    */   private final UVPair texScale;
/*    */   private final Set<Direction> visibleFaces;
/*    */   
/*    */   protected CubeDefinition(String comment, float xTexOffs, float yTexOffs, float minX, float minY, float minZ, float width, float height, float depth, CubeDeformation grow, boolean mirror, float xTexScale, float yTexScale, Set<Direction> visibleFaces) {
/* 23 */     this.comment = comment;
/* 24 */     this.texCoord = new UVPair(xTexOffs, yTexOffs);
/* 25 */     this.origin = (Vector3fc)new Vector3f(minX, minY, minZ);
/* 26 */     this.dimensions = (Vector3fc)new Vector3f(width, height, depth);
/* 27 */     this.grow = grow;
/* 28 */     this.mirror = mirror;
/* 29 */     this.texScale = new UVPair(xTexScale, yTexScale);
/* 30 */     this.visibleFaces = visibleFaces;
/*    */   }
/*    */   
/*    */   public ModelPart.Cube bake(int texScaleX, int texScaleY) {
/* 34 */     return new ModelPart.Cube((int)this.texCoord.u(), (int)this.texCoord.v(), this.origin.x(), this.origin.y(), this.origin.z(), this.dimensions.x(), this.dimensions.y(), this.dimensions.z(), this.grow.growX, this.grow.growY, this.grow.growZ, this.mirror, texScaleX * this.texScale.u(), texScaleY * this.texScale.v(), this.visibleFaces);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/CubeDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */