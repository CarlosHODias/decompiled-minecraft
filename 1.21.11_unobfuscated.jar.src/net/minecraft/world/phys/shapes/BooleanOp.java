/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ public interface BooleanOp {
/*    */   static {
/*  5 */     NOT_OR = ((first, second) -> (!first && !second));
/*  6 */     ONLY_SECOND = ((first, second) -> (second && !first));
/*  7 */     NOT_FIRST = ((first, second) -> !first);
/*  8 */     ONLY_FIRST = ((first, second) -> (first && !second));
/*  9 */     NOT_SECOND = ((first, second) -> !second);
/* 10 */     NOT_SAME = ((first, second) -> (first != second));
/* 11 */     NOT_AND = ((first, second) -> (!first || !second));
/* 12 */     AND = ((first, second) -> (first && second));
/* 13 */     SAME = ((first, second) -> (first == second));
/* 14 */     SECOND = ((first, second) -> second);
/* 15 */     CAUSES = ((first, second) -> (!first || second));
/* 16 */     FIRST = ((first, second) -> first);
/* 17 */     CAUSED_BY = ((first, second) -> (first || !second));
/* 18 */     OR = ((first, second) -> (first || second));
/*    */   }
/*    */   
/*    */   public static final BooleanOp FALSE = (first, second) -> false;
/*    */   public static final BooleanOp NOT_OR;
/*    */   public static final BooleanOp ONLY_SECOND;
/*    */   public static final BooleanOp NOT_FIRST;
/*    */   public static final BooleanOp ONLY_FIRST;
/*    */   public static final BooleanOp NOT_SECOND;
/*    */   public static final BooleanOp NOT_SAME;
/*    */   public static final BooleanOp NOT_AND;
/*    */   public static final BooleanOp AND;
/*    */   public static final BooleanOp SAME;
/*    */   public static final BooleanOp SECOND;
/*    */   public static final BooleanOp CAUSES;
/*    */   public static final BooleanOp FIRST;
/*    */   public static final BooleanOp CAUSED_BY;
/*    */   public static final BooleanOp OR;
/*    */   public static final BooleanOp TRUE = (first, second) -> true;
/*    */   
/*    */   boolean apply(boolean paramBoolean1, boolean paramBoolean2);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/BooleanOp.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */