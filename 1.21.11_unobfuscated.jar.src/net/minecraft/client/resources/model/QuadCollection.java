/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Multimap;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ 
/*     */ public class QuadCollection
/*     */ {
/*  14 */   public static final QuadCollection EMPTY = new QuadCollection(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
/*     */   
/*     */   private final List<BakedQuad> all;
/*     */   private final List<BakedQuad> unculled;
/*     */   private final List<BakedQuad> north;
/*     */   private final List<BakedQuad> south;
/*     */   private final List<BakedQuad> east;
/*     */   private final List<BakedQuad> west;
/*     */   private final List<BakedQuad> up;
/*     */   private final List<BakedQuad> down;
/*     */   
/*     */   private QuadCollection(List<BakedQuad> all, List<BakedQuad> unculled, List<BakedQuad> north, List<BakedQuad> south, List<BakedQuad> east, List<BakedQuad> west, List<BakedQuad> up, List<BakedQuad> down) {
/*  26 */     this.all = all;
/*  27 */     this.unculled = unculled;
/*  28 */     this.north = north;
/*  29 */     this.south = south;
/*  30 */     this.east = east;
/*  31 */     this.west = west;
/*  32 */     this.up = up;
/*  33 */     this.down = down;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getQuads(Direction direction) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: astore_2
/*     */     //   2: iconst_0
/*     */     //   3: istore_3
/*     */     //   4: aload_2
/*     */     //   5: iload_3
/*     */     //   6: <illegal opcode> enumSwitch : (Lnet/minecraft/core/Direction;I)I
/*     */     //   11: tableswitch default -> 52, -1 -> 62, 0 -> 69, 1 -> 76, 2 -> 83, 3 -> 90, 4 -> 97, 5 -> 104
/*     */     //   52: new java/lang/MatchException
/*     */     //   55: dup
/*     */     //   56: aconst_null
/*     */     //   57: aconst_null
/*     */     //   58: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   61: athrow
/*     */     //   62: aload_0
/*     */     //   63: getfield unculled : Ljava/util/List;
/*     */     //   66: goto -> 108
/*     */     //   69: aload_0
/*     */     //   70: getfield north : Ljava/util/List;
/*     */     //   73: goto -> 108
/*     */     //   76: aload_0
/*     */     //   77: getfield south : Ljava/util/List;
/*     */     //   80: goto -> 108
/*     */     //   83: aload_0
/*     */     //   84: getfield east : Ljava/util/List;
/*     */     //   87: goto -> 108
/*     */     //   90: aload_0
/*     */     //   91: getfield west : Ljava/util/List;
/*     */     //   94: goto -> 108
/*     */     //   97: aload_0
/*     */     //   98: getfield up : Ljava/util/List;
/*     */     //   101: goto -> 108
/*     */     //   104: aload_0
/*     */     //   105: getfield down : Ljava/util/List;
/*     */     //   108: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #37	-> 0
/*     */     //   #38	-> 62
/*     */     //   #39	-> 69
/*     */     //   #40	-> 76
/*     */     //   #41	-> 83
/*     */     //   #42	-> 90
/*     */     //   #43	-> 97
/*     */     //   #44	-> 104
/*     */     //   #37	-> 108
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	109	0	this	Lnet/minecraft/client/resources/model/QuadCollection;
/*     */     //   0	109	1	direction	Lnet/minecraft/core/Direction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getAll() {
/*  49 */     return this.all;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  53 */     private final ImmutableList.Builder<BakedQuad> unculledFaces = ImmutableList.builder();
/*  54 */     private final Multimap<Direction, BakedQuad> culledFaces = (Multimap<Direction, BakedQuad>)ArrayListMultimap.create();
/*     */     
/*     */     public Builder addCulledFace(Direction direction, BakedQuad quad) {
/*  57 */       this.culledFaces.put(direction, quad);
/*  58 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addUnculledFace(BakedQuad quad) {
/*  62 */       this.unculledFaces.add(quad);
/*  63 */       return this;
/*     */     }
/*     */     
/*     */     private static QuadCollection createFromSublists(List<BakedQuad> all, int unculledCount, int northCount, int southCount, int eastCount, int westCount, int upCount, int downCount) {
/*  67 */       int index = 0;
/*  68 */       List<BakedQuad> unculled = all.subList(index, index += unculledCount);
/*  69 */       List<BakedQuad> north = all.subList(index, index += northCount);
/*  70 */       List<BakedQuad> south = all.subList(index, index += southCount);
/*  71 */       List<BakedQuad> east = all.subList(index, index += eastCount);
/*  72 */       List<BakedQuad> west = all.subList(index, index += westCount);
/*  73 */       List<BakedQuad> up = all.subList(index, index += upCount);
/*  74 */       List<BakedQuad> down = all.subList(index, index + downCount);
/*  75 */       return new QuadCollection(all, unculled, north, south, east, west, up, down);
/*     */     }
/*     */ 
/*     */     
/*     */     public QuadCollection build() {
/*  80 */       ImmutableList<BakedQuad> unculledFaces = this.unculledFaces.build();
/*     */       
/*  82 */       if (this.culledFaces.isEmpty()) {
/*  83 */         if (unculledFaces.isEmpty()) {
/*  84 */           return QuadCollection.EMPTY;
/*     */         }
/*     */ 
/*     */         
/*  88 */         return new QuadCollection((List<BakedQuad>)unculledFaces, (List<BakedQuad>)unculledFaces, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
/*     */       } 
/*     */ 
/*     */       
/*  92 */       ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
/*  93 */       quads.addAll((Iterable)unculledFaces);
/*     */       
/*  95 */       Collection<BakedQuad> north = this.culledFaces.get(Direction.NORTH);
/*  96 */       quads.addAll(north);
/*  97 */       Collection<BakedQuad> south = this.culledFaces.get(Direction.SOUTH);
/*  98 */       quads.addAll(south);
/*  99 */       Collection<BakedQuad> east = this.culledFaces.get(Direction.EAST);
/* 100 */       quads.addAll(east);
/* 101 */       Collection<BakedQuad> west = this.culledFaces.get(Direction.WEST);
/* 102 */       quads.addAll(west);
/* 103 */       Collection<BakedQuad> up = this.culledFaces.get(Direction.UP);
/* 104 */       quads.addAll(up);
/* 105 */       Collection<BakedQuad> down = this.culledFaces.get(Direction.DOWN);
/* 106 */       quads.addAll(down);
/*     */       
/* 108 */       return createFromSublists((List<BakedQuad>)
/* 109 */           quads.build(), 
/* 110 */           unculledFaces.size(), 
/* 111 */           north.size(), 
/* 112 */           south.size(), 
/* 113 */           east.size(), 
/* 114 */           west.size(), 
/* 115 */           up.size(), 
/* 116 */           down.size());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/QuadCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */