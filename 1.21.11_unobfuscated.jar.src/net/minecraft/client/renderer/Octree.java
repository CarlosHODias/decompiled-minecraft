/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class Octree
/*     */ {
/*     */   private final Branch root;
/*     */   private final BlockPos cameraSectionCenter;
/*     */   
/*     */   public Octree(SectionPos cameraSection, int renderDistance, int sectionsPerChunk, int minBlockY) {
/*  18 */     int visibleAreaDiameterInSections = renderDistance * 2 + 1;
/*  19 */     int boundingBoxSizeInSections = Mth.smallestEncompassingPowerOfTwo(visibleAreaDiameterInSections);
/*  20 */     int distanceToBBEdgeInBlocks = renderDistance * 16;
/*  21 */     BlockPos cameraSectionOrigin = cameraSection.origin();
/*  22 */     this.cameraSectionCenter = cameraSection.center();
/*     */ 
/*     */ 
/*     */     
/*  26 */     int minX = cameraSectionOrigin.getX() - distanceToBBEdgeInBlocks;
/*  27 */     int maxX = minX + boundingBoxSizeInSections * 16 - 1;
/*     */     
/*  29 */     int minY = (boundingBoxSizeInSections >= sectionsPerChunk) ? minBlockY : (cameraSectionOrigin.getY() - distanceToBBEdgeInBlocks);
/*  30 */     int maxY = minY + boundingBoxSizeInSections * 16 - 1;
/*  31 */     int minZ = cameraSectionOrigin.getZ() - distanceToBBEdgeInBlocks;
/*  32 */     int maxZ = minZ + boundingBoxSizeInSections * 16 - 1;
/*     */     
/*  34 */     this.root = new Branch(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ));
/*     */   }
/*     */   
/*     */   public boolean add(SectionRenderDispatcher.RenderSection section) {
/*  38 */     return this.root.add(section);
/*     */   }
/*     */   
/*     */   public void visitNodes(OctreeVisitor visitor, Frustum frustum, int closeDistance) {
/*  42 */     this.root.visitNodes(visitor, false, frustum, 0, closeDistance, true);
/*     */   }
/*     */   
/*     */   private boolean isClose(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int closeDistance) {
/*  46 */     int cameraX = this.cameraSectionCenter.getX();
/*  47 */     int cameraY = this.cameraSectionCenter.getY();
/*  48 */     int cameraZ = this.cameraSectionCenter.getZ();
/*  49 */     return (cameraX > minX - closeDistance && cameraX < maxX + closeDistance && cameraY > minY - closeDistance && cameraY < maxY + closeDistance && cameraZ > minZ - closeDistance && cameraZ < maxZ + closeDistance);
/*     */   }
/*     */   
/*     */   private class Branch
/*     */     implements Node
/*     */   {
/*  55 */     private final Octree.Node[] nodes = new Octree.Node[8];
/*     */     
/*     */     private final BoundingBox boundingBox;
/*     */     
/*     */     private final int bbCenterX;
/*     */     
/*     */     private final int bbCenterY;
/*     */     private final int bbCenterZ;
/*     */     private final Octree.AxisSorting sorting;
/*     */     private final boolean cameraXDiffNegative;
/*     */     private final boolean cameraYDiffNegative;
/*     */     private final boolean cameraZDiffNegative;
/*     */     
/*     */     public Branch(BoundingBox boundingBox) {
/*  69 */       this.boundingBox = boundingBox;
/*  70 */       this.bbCenterX = this.boundingBox.minX() + this.boundingBox.getXSpan() / 2;
/*  71 */       this.bbCenterY = this.boundingBox.minY() + this.boundingBox.getYSpan() / 2;
/*  72 */       this.bbCenterZ = this.boundingBox.minZ() + this.boundingBox.getZSpan() / 2;
/*     */       
/*  74 */       int cameraXDiff = Octree.this.cameraSectionCenter.getX() - this.bbCenterX;
/*  75 */       int cameraYDiff = Octree.this.cameraSectionCenter.getY() - this.bbCenterY;
/*  76 */       int cameraZDiff = Octree.this.cameraSectionCenter.getZ() - this.bbCenterZ;
/*     */       
/*  78 */       this.sorting = Octree.AxisSorting.getAxisSorting(Math.abs(cameraXDiff), Math.abs(cameraYDiff), Math.abs(cameraZDiff));
/*     */       
/*  80 */       this.cameraXDiffNegative = (cameraXDiff < 0);
/*  81 */       this.cameraYDiffNegative = (cameraYDiff < 0);
/*  82 */       this.cameraZDiffNegative = (cameraZDiff < 0);
/*     */     }
/*     */     
/*     */     public boolean add(SectionRenderDispatcher.RenderSection section) {
/*  86 */       long sectionNode = section.getSectionNode();
/*  87 */       boolean sectionXDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.x(sectionNode)) - this.bbCenterX < 0);
/*  88 */       boolean sectionYDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode)) - this.bbCenterY < 0);
/*  89 */       boolean sectionZDiffNegative = (SectionPos.sectionToBlockCoord(SectionPos.z(sectionNode)) - this.bbCenterZ < 0);
/*     */       
/*  91 */       boolean xDiffsOppositeSides = (sectionXDiffNegative != this.cameraXDiffNegative);
/*  92 */       boolean yDiffsOppositeSides = (sectionYDiffNegative != this.cameraYDiffNegative);
/*  93 */       boolean zDiffsOppositeSides = (sectionZDiffNegative != this.cameraZDiffNegative);
/*     */       
/*  95 */       int nodeIndex = getNodeIndex(this.sorting, xDiffsOppositeSides, yDiffsOppositeSides, zDiffsOppositeSides);
/*     */       
/*  97 */       if (areChildrenLeaves()) {
/*  98 */         boolean alreadyExisted = (this.nodes[nodeIndex] != null);
/*  99 */         this.nodes[nodeIndex] = new Octree.Leaf(section);
/* 100 */         return !alreadyExisted;
/*     */       } 
/*     */       
/* 103 */       if (this.nodes[nodeIndex] != null) {
/* 104 */         Branch branch1 = (Branch)this.nodes[nodeIndex];
/* 105 */         return branch1.add(section);
/*     */       } 
/*     */       
/* 108 */       BoundingBox childBoundingBox = createChildBoundingBox(sectionXDiffNegative, sectionYDiffNegative, sectionZDiffNegative);
/* 109 */       Branch branch = new Branch(childBoundingBox);
/* 110 */       this.nodes[nodeIndex] = branch;
/* 111 */       return branch.add(section);
/*     */     }
/*     */     
/*     */     private static int getNodeIndex(Octree.AxisSorting sorting, boolean xDiffsOppositeSides, boolean yDiffsOppositeSides, boolean zDiffsOppositeSides) {
/* 115 */       int index = 0;
/* 116 */       if (xDiffsOppositeSides) {
/* 117 */         index += sorting.xShift;
/*     */       }
/* 119 */       if (yDiffsOppositeSides) {
/* 120 */         index += sorting.yShift;
/*     */       }
/* 122 */       if (zDiffsOppositeSides) {
/* 123 */         index += sorting.zShift;
/*     */       }
/* 125 */       return index;
/*     */     }
/*     */     
/*     */     private boolean areChildrenLeaves() {
/* 129 */       return (this.boundingBox.getXSpan() == 32);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BoundingBox createChildBoundingBox(boolean sectionXDiffNegative, boolean sectionYDiffNegative, boolean sectionZDiffNegative) {
/*     */       int minX, maxX, minY, maxY, minZ, maxZ;
/* 140 */       if (sectionXDiffNegative) {
/* 141 */         minX = this.boundingBox.minX();
/* 142 */         maxX = this.bbCenterX - 1;
/*     */       } else {
/* 144 */         minX = this.bbCenterX;
/* 145 */         maxX = this.boundingBox.maxX();
/*     */       } 
/* 147 */       if (sectionYDiffNegative) {
/* 148 */         minY = this.boundingBox.minY();
/* 149 */         maxY = this.bbCenterY - 1;
/*     */       } else {
/* 151 */         minY = this.bbCenterY;
/* 152 */         maxY = this.boundingBox.maxY();
/*     */       } 
/* 154 */       if (sectionZDiffNegative) {
/* 155 */         minZ = this.boundingBox.minZ();
/* 156 */         maxZ = this.bbCenterZ - 1;
/*     */       } else {
/* 158 */         minZ = this.bbCenterZ;
/* 159 */         maxZ = this.boundingBox.maxZ();
/*     */       } 
/* 161 */       return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitNodes(Octree.OctreeVisitor visitor, boolean skipFrustumCheck, Frustum frustum, int depth, int closeDistance, boolean isClose) {
/* 166 */       boolean isVisible = skipFrustumCheck;
/* 167 */       if (!skipFrustumCheck) {
/* 168 */         int checkResult = frustum.cubeInFrustum(this.boundingBox);
/* 169 */         skipFrustumCheck = (checkResult == -2);
/* 170 */         isVisible = (checkResult == -2 || checkResult == -1);
/*     */       } 
/* 172 */       if (isVisible) {
/* 173 */         isClose = (isClose && Octree.this.isClose(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), closeDistance));
/* 174 */         visitor.visit(this, skipFrustumCheck, depth, isClose);
/* 175 */         for (Octree.Node node : this.nodes) {
/* 176 */           if (node != null) {
/* 177 */             node.visitNodes(visitor, skipFrustumCheck, frustum, depth + 1, closeDistance, isClose);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection getSection() {
/* 185 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public AABB getAABB() {
/* 190 */       return new AABB(this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ(), (this.boundingBox.maxX() + 1), (this.boundingBox.maxY() + 1), (this.boundingBox.maxZ() + 1));
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Leaf implements Node {
/*     */     private final SectionRenderDispatcher.RenderSection section;
/*     */     
/*     */     private Leaf(SectionRenderDispatcher.RenderSection section) {
/* 198 */       this.section = section;
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitNodes(Octree.OctreeVisitor visitor, boolean skipFrustumCheck, Frustum frustum, int depth, int closeDistance, boolean isClose) {
/* 203 */       AABB boundingBox = this.section.getBoundingBox();
/* 204 */       if (skipFrustumCheck || frustum.isVisible(getSection().getBoundingBox())) {
/* 205 */         isClose = (isClose && Octree.this.isClose(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, closeDistance));
/* 206 */         visitor.visit(this, skipFrustumCheck, depth, isClose);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public SectionRenderDispatcher.RenderSection getSection() {
/* 212 */       return this.section;
/*     */     }
/*     */ 
/*     */     
/*     */     public AABB getAABB() {
/* 217 */       return this.section.getBoundingBox();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum AxisSorting
/*     */   {
/* 235 */     XYZ(4, 2, 1),
/* 236 */     XZY(4, 1, 2),
/* 237 */     YXZ(2, 4, 1),
/* 238 */     YZX(1, 4, 2),
/* 239 */     ZXY(2, 1, 4),
/* 240 */     ZYX(1, 2, 4);
/*     */     
/*     */     private final int xShift;
/*     */     private final int yShift;
/*     */     private final int zShift;
/*     */     
/*     */     AxisSorting(int xShift, int yShift, int zShift) {
/* 247 */       this.xShift = xShift;
/* 248 */       this.yShift = yShift;
/* 249 */       this.zShift = zShift;
/*     */     }
/*     */     
/*     */     public static AxisSorting getAxisSorting(int absXDiff, int absYDiff, int absZDiff) {
/* 253 */       if (absXDiff > absYDiff && absXDiff > absZDiff) {
/* 254 */         if (absYDiff > absZDiff) {
/* 255 */           return XYZ;
/*     */         }
/* 257 */         return XZY;
/*     */       } 
/* 259 */       if (absYDiff > absXDiff && absYDiff > absZDiff) {
/* 260 */         if (absXDiff > absZDiff) {
/* 261 */           return YXZ;
/*     */         }
/* 263 */         return YZX;
/*     */       } 
/*     */       
/* 266 */       if (absXDiff > absYDiff) {
/* 267 */         return ZXY;
/*     */       }
/* 269 */       return ZYX;
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OctreeVisitor {
/*     */     void visit(Octree.Node param1Node, boolean param1Boolean1, int param1Int, boolean param1Boolean2);
/*     */   }
/*     */   
/*     */   public static interface Node {
/*     */     void visitNodes(Octree.OctreeVisitor param1OctreeVisitor, boolean param1Boolean1, Frustum param1Frustum, int param1Int1, int param1Int2, boolean param1Boolean2);
/*     */     
/*     */     SectionRenderDispatcher.RenderSection getSection();
/*     */     
/*     */     AABB getAABB();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/Octree.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */