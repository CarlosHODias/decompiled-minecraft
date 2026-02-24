/*     */ package net.minecraft.client.renderer.chunk;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
/*     */ import java.util.BitSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ public class VisGraph
/*     */ {
/*     */   private static final int SIZE_IN_BITS = 4;
/*     */   private static final int LEN = 16;
/*     */   private static final int MASK = 15;
/*     */   private static final int SIZE = 4096;
/*     */   private static final int X_SHIFT = 0;
/*     */   private static final int Z_SHIFT = 4;
/*     */   private static final int Y_SHIFT = 8;
/*  21 */   private static final int DX = (int)Math.pow(16.0D, 0.0D);
/*  22 */   private static final int DZ = (int)Math.pow(16.0D, 1.0D);
/*  23 */   private static final int DY = (int)Math.pow(16.0D, 2.0D);
/*     */   private static final int INVALID_INDEX = -1;
/*  25 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*  27 */   private final BitSet bitSet = new BitSet(4096);
/*     */   static {
/*  29 */     INDEX_OF_EDGES = (int[])Util.make(new int[1352], map -> {
/*     */           int min = 0, max = 15, index = 0;
/*     */           for (int x = 0; x < 16; x++) {
/*     */             for (int y = 0; y < 16; y++) {
/*     */               for (int z = 0; z < 16; z++) {
/*     */                 if (x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15) {
/*     */                   map[index++] = getIndex(x, y, z);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int[] INDEX_OF_EDGES;
/*  45 */   private int empty = 4096;
/*     */   
/*     */   public void setOpaque(BlockPos pos) {
/*  48 */     this.bitSet.set(getIndex(pos), true);
/*  49 */     this.empty--;
/*     */   }
/*     */   
/*     */   private static int getIndex(BlockPos pos) {
/*  53 */     return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */   
/*     */   private static int getIndex(int x, int y, int z) {
/*  57 */     return x << 0 | y << 8 | z << 4;
/*     */   }
/*     */   
/*     */   public VisibilitySet resolve() {
/*  61 */     VisibilitySet visibilitySet = new VisibilitySet();
/*     */     
/*  63 */     if (4096 - this.empty < 256) {
/*  64 */       visibilitySet.setAll(true);
/*  65 */     } else if (this.empty == 0) {
/*  66 */       visibilitySet.setAll(false);
/*     */     } else {
/*  68 */       for (int i : INDEX_OF_EDGES) {
/*  69 */         if (!this.bitSet.get(i)) {
/*  70 */           visibilitySet.add(floodFill(i));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     return visibilitySet;
/*     */   }
/*     */   
/*     */   private Set<Direction> floodFill(int startIndex) {
/*  79 */     Set<Direction> edges = EnumSet.noneOf(Direction.class);
/*     */     
/*  81 */     IntArrayFIFOQueue intArrayFIFOQueue = new IntArrayFIFOQueue();
/*  82 */     intArrayFIFOQueue.enqueue(startIndex);
/*  83 */     this.bitSet.set(startIndex, true);
/*     */     
/*  85 */     while (!intArrayFIFOQueue.isEmpty()) {
/*  86 */       int index = intArrayFIFOQueue.dequeueInt();
/*  87 */       addEdges(index, edges);
/*     */       
/*  89 */       for (Direction direction : DIRECTIONS) {
/*  90 */         int neighborIndex = getNeighborIndexAtFace(index, direction);
/*  91 */         if (neighborIndex >= 0 && !this.bitSet.get(neighborIndex)) {
/*  92 */           this.bitSet.set(neighborIndex, true);
/*  93 */           intArrayFIFOQueue.enqueue(neighborIndex);
/*     */         } 
/*     */       } 
/*     */     } 
/*  97 */     return edges;
/*     */   }
/*     */   
/*     */   private void addEdges(int index, Set<Direction> edges) {
/* 101 */     int x = index >> 0 & 0xF;
/* 102 */     if (x == 0) {
/* 103 */       edges.add(Direction.WEST);
/* 104 */     } else if (x == 15) {
/* 105 */       edges.add(Direction.EAST);
/*     */     } 
/*     */     
/* 108 */     int y = index >> 8 & 0xF;
/* 109 */     if (y == 0) {
/* 110 */       edges.add(Direction.DOWN);
/* 111 */     } else if (y == 15) {
/* 112 */       edges.add(Direction.UP);
/*     */     } 
/*     */     
/* 115 */     int z = index >> 4 & 0xF;
/* 116 */     if (z == 0) {
/* 117 */       edges.add(Direction.NORTH);
/* 118 */     } else if (z == 15) {
/* 119 */       edges.add(Direction.SOUTH);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getNeighborIndexAtFace(int index, Direction direction) {
/* 124 */     switch (direction) {
/*     */       
/*     */       case DOWN:
/* 127 */         if ((index >> 8 & 0xF) == 0) {
/* 128 */           return -1;
/*     */         }
/* 130 */         return index - DY;
/*     */       
/*     */       case UP:
/* 133 */         if ((index >> 8 & 0xF) == 15) {
/* 134 */           return -1;
/*     */         }
/* 136 */         return index + DY;
/*     */       
/*     */       case NORTH:
/* 139 */         if ((index >> 4 & 0xF) == 0) {
/* 140 */           return -1;
/*     */         }
/* 142 */         return index - DZ;
/*     */       
/*     */       case SOUTH:
/* 145 */         if ((index >> 4 & 0xF) == 15) {
/* 146 */           return -1;
/*     */         }
/* 148 */         return index + DZ;
/*     */       
/*     */       case WEST:
/* 151 */         if ((index >> 0 & 0xF) == 0) {
/* 152 */           return -1;
/*     */         }
/* 154 */         return index - DX;
/*     */       
/*     */       case EAST:
/* 157 */         if ((index >> 0 & 0xF) == 15) {
/* 158 */           return -1;
/*     */         }
/* 160 */         return index + DX;
/*     */     } 
/* 162 */     return -1;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/VisGraph.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */