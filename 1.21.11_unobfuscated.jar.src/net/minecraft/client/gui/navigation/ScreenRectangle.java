/*     */ package net.minecraft.client.gui.navigation;
/*     */ 
/*     */ public final class ScreenRectangle extends Record {
/*     */   private final ScreenPosition position;
/*     */   private final int width;
/*     */   private final int height;
/*     */   
/*   8 */   public ScreenRectangle(ScreenPosition position, int width, int height) { this.position = position; this.width = width; this.height = height; } public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/ScreenRectangle;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*   8 */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenRectangle; } public ScreenPosition position() { return this.position; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/ScreenRectangle;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenRectangle; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/ScreenRectangle;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/navigation/ScreenRectangle;
/*   8 */     //   0	8	1	o	Ljava/lang/Object; } public int width() { return this.width; } public int height() { return this.height; }
/*   9 */    private static final ScreenRectangle EMPTY = new ScreenRectangle(0, 0, 0, 0);
/*     */   
/*     */   public ScreenRectangle(int x, int y, int width, int height) {
/*  12 */     this(new ScreenPosition(x, y), width, height);
/*     */   }
/*     */   
/*     */   public static ScreenRectangle empty() {
/*  16 */     return EMPTY;
/*     */   }
/*     */   
/*     */   public static ScreenRectangle of(ScreenAxis primaryAxis, int primaryIndex, int secondaryIndex, int primaryLength, int secondaryLength) {
/*  20 */     switch (primaryAxis) { default: throw new MatchException(null, null);case HORIZONTAL: case VERTICAL: break; }  return 
/*     */       
/*  22 */       new ScreenRectangle(secondaryIndex, primaryIndex, secondaryLength, primaryLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScreenRectangle step(ScreenDirection direction) {
/*  27 */     return new ScreenRectangle(this.position.step(direction), this.width, this.height);
/*     */   }
/*     */   
/*     */   public int getLength(ScreenAxis axis) {
/*  31 */     switch (axis) { default: throw new MatchException(null, null);case HORIZONTAL: case VERTICAL: break; }  return 
/*     */       
/*  33 */       this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBoundInDirection(ScreenDirection direction) {
/*  38 */     ScreenAxis axis = direction.getAxis();
/*  39 */     if (direction.isPositive()) {
/*  40 */       return this.position.getCoordinate(axis) + getLength(axis) - 1;
/*     */     }
/*  42 */     return this.position.getCoordinate(axis);
/*     */   }
/*     */   
/*     */   public ScreenRectangle getBorder(ScreenDirection direction) {
/*  46 */     int startFirst = getBoundInDirection(direction);
/*     */     
/*  48 */     ScreenAxis orthogonalAxis = direction.getAxis().orthogonal();
/*  49 */     int startSecond = getBoundInDirection(orthogonalAxis.getNegative());
/*  50 */     int length = getLength(orthogonalAxis);
/*     */     
/*  52 */     return of(direction.getAxis(), startFirst, startSecond, 1, length).step(direction);
/*     */   }
/*     */   
/*     */   public boolean overlaps(ScreenRectangle other) {
/*  56 */     return (overlapsInAxis(other, ScreenAxis.HORIZONTAL) && overlapsInAxis(other, ScreenAxis.VERTICAL));
/*     */   }
/*     */   
/*     */   public boolean overlapsInAxis(ScreenRectangle other, ScreenAxis axis) {
/*  60 */     int thisLower = getBoundInDirection(axis.getNegative());
/*  61 */     int otherLower = other.getBoundInDirection(axis.getNegative());
/*  62 */     int thisHigher = getBoundInDirection(axis.getPositive());
/*  63 */     int otherHigher = other.getBoundInDirection(axis.getPositive());
/*  64 */     return (Math.max(thisLower, otherLower) <= Math.min(thisHigher, otherHigher));
/*     */   }
/*     */   
/*     */   public int getCenterInAxis(ScreenAxis axis) {
/*  68 */     return (getBoundInDirection(axis.getPositive()) + getBoundInDirection(axis.getNegative())) / 2;
/*     */   }
/*     */   
/*     */   public ScreenRectangle intersection(ScreenRectangle other) {
/*  72 */     int left = Math.max(left(), other.left());
/*  73 */     int top = Math.max(top(), other.top());
/*  74 */     int right = Math.min(right(), other.right());
/*  75 */     int bottom = Math.min(bottom(), other.bottom());
/*  76 */     if (left >= right || top >= bottom) {
/*  77 */       return null;
/*     */     }
/*  79 */     return new ScreenRectangle(left, top, right - left, bottom - top);
/*     */   }
/*     */   
/*     */   public boolean intersects(ScreenRectangle other) {
/*  83 */     return (left() < other.right() && 
/*  84 */       right() > other.left() && 
/*  85 */       top() < other.bottom() && 
/*  86 */       bottom() > other.top());
/*     */   }
/*     */   
/*     */   public boolean encompasses(ScreenRectangle other) {
/*  90 */     return (other.left() >= left() && other.top() >= top() && other.right() <= right() && other.bottom() <= bottom());
/*     */   }
/*     */   
/*     */   public int top() {
/*  94 */     return this.position.y();
/*     */   }
/*     */   
/*     */   public int bottom() {
/*  98 */     return this.position.y() + this.height;
/*     */   }
/*     */   
/*     */   public int left() {
/* 102 */     return this.position.x();
/*     */   }
/*     */   
/*     */   public int right() {
/* 106 */     return this.position.x() + this.width;
/*     */   }
/*     */   
/*     */   public boolean containsPoint(int x, int y) {
/* 110 */     return (x >= left() && x < right() && y >= top() && y < bottom());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScreenRectangle transformAxisAligned(org.joml.Matrix3x2fc matrix) {
/* 117 */     org.joml.Vector2f topLeft = matrix.transformPosition(left(), top(), new org.joml.Vector2f());
/* 118 */     org.joml.Vector2f bottomRight = matrix.transformPosition(right(), bottom(), new org.joml.Vector2f());
/* 119 */     return new ScreenRectangle(
/* 120 */         net.minecraft.util.Mth.floor(topLeft.x), net.minecraft.util.Mth.floor(topLeft.y), 
/* 121 */         net.minecraft.util.Mth.floor(bottomRight.x - topLeft.x), net.minecraft.util.Mth.floor(bottomRight.y - topLeft.y));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScreenRectangle transformMaxBounds(org.joml.Matrix3x2fc matrix) {
/* 129 */     org.joml.Vector2f topLeft = matrix.transformPosition(left(), top(), new org.joml.Vector2f());
/* 130 */     org.joml.Vector2f topRight = matrix.transformPosition(right(), top(), new org.joml.Vector2f());
/* 131 */     org.joml.Vector2f bottomLeft = matrix.transformPosition(left(), bottom(), new org.joml.Vector2f());
/* 132 */     org.joml.Vector2f bottomRight = matrix.transformPosition(right(), bottom(), new org.joml.Vector2f());
/* 133 */     float minX = Math.min(Math.min(topLeft.x(), bottomLeft.x()), Math.min(topRight.x(), bottomRight.x()));
/* 134 */     float maxX = Math.max(Math.max(topLeft.x(), bottomLeft.x()), Math.max(topRight.x(), bottomRight.x()));
/* 135 */     float minY = Math.min(Math.min(topLeft.y(), bottomLeft.y()), Math.min(topRight.y(), bottomRight.y()));
/* 136 */     float maxY = Math.max(Math.max(topLeft.y(), bottomLeft.y()), Math.max(topRight.y(), bottomRight.y()));
/* 137 */     return new ScreenRectangle(
/* 138 */         net.minecraft.util.Mth.floor(minX), net.minecraft.util.Mth.floor(minY), 
/* 139 */         net.minecraft.util.Mth.ceil(maxX - minX), net.minecraft.util.Mth.ceil(maxY - minY));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/navigation/ScreenRectangle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */