/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class CubeListBuilder
/*    */ {
/* 12 */   private static final Set<Direction> ALL_VISIBLE = EnumSet.allOf(Direction.class);
/* 13 */   private final List<CubeDefinition> cubes = Lists.newArrayList();
/*    */   
/*    */   private int xTexOffs;
/*    */   private int yTexOffs;
/*    */   private boolean mirror;
/*    */   
/*    */   public CubeListBuilder texOffs(int xTexOffs, int yTexOffs) {
/* 20 */     this.xTexOffs = xTexOffs;
/* 21 */     this.yTexOffs = yTexOffs;
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder mirror() {
/* 26 */     return mirror(true);
/*    */   }
/*    */   
/*    */   public CubeListBuilder mirror(boolean mirror) {
/* 30 */     this.mirror = mirror;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(String id, float x0, float y0, float z0, int w, int h, int d, CubeDeformation g, int xTexOffs, int yTexOffs) {
/* 35 */     texOffs(xTexOffs, yTexOffs);
/* 36 */     this.cubes.add(new CubeDefinition(id, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, g, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(String id, float x0, float y0, float z0, int w, int h, int d, int xTexOffs, int yTexOffs) {
/* 41 */     texOffs(xTexOffs, yTexOffs);
/* 42 */     this.cubes.add(new CubeDefinition(id, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(float x0, float y0, float z0, float w, float h, float d) {
/* 47 */     this.cubes.add(new CubeDefinition(null, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(float x0, float y0, float z0, float w, float h, float d, Set<Direction> visibleSides) {
/* 52 */     this.cubes.add(new CubeDefinition(null, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, visibleSides));
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(String id, float x0, float y0, float z0, float w, float h, float d) {
/* 57 */     this.cubes.add(new CubeDefinition(id, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, CubeDeformation.NONE, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(String id, float x0, float y0, float z0, float w, float h, float d, CubeDeformation g) {
/* 62 */     this.cubes.add(new CubeDefinition(id, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, g, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(float x0, float y0, float z0, float w, float h, float d, boolean mirror) {
/* 67 */     this.cubes.add(new CubeDefinition(null, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, CubeDeformation.NONE, mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(float x0, float y0, float z0, float w, float h, float d, CubeDeformation g, float xTexScale, float yTexScale) {
/* 72 */     this.cubes.add(new CubeDefinition(null, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, g, this.mirror, xTexScale, yTexScale, ALL_VISIBLE));
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public CubeListBuilder addBox(float x0, float y0, float z0, float w, float h, float d, CubeDeformation g) {
/* 77 */     this.cubes.add(new CubeDefinition(null, this.xTexOffs, this.yTexOffs, x0, y0, z0, w, h, d, g, this.mirror, 1.0F, 1.0F, ALL_VISIBLE));
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public List<CubeDefinition> getCubes() {
/* 82 */     return (List<CubeDefinition>)ImmutableList.copyOf(this.cubes);
/*    */   }
/*    */   
/*    */   public static CubeListBuilder create() {
/* 86 */     return new CubeListBuilder();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/CubeListBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */