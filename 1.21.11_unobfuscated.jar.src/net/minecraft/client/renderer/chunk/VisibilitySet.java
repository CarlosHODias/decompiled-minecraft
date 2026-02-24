/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.Locale;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class VisibilitySet
/*    */ {
/* 10 */   private static final int FACINGS = (Direction.values()).length;
/*    */   
/* 12 */   private final BitSet data = new BitSet(FACINGS * FACINGS);
/*    */   
/*    */   public void add(Set<Direction> directions) {
/* 15 */     for (Direction direction1 : directions) {
/* 16 */       for (Direction direction2 : directions) {
/* 17 */         set(direction1, direction2, true);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public void set(Direction direction1, Direction direction2, boolean value) {
/* 23 */     this.data.set(direction1.ordinal() + direction2.ordinal() * FACINGS, value);
/* 24 */     this.data.set(direction2.ordinal() + direction1.ordinal() * FACINGS, value);
/*    */   }
/*    */   
/*    */   public void setAll(boolean visible) {
/* 28 */     this.data.set(0, this.data.size(), visible);
/*    */   }
/*    */   
/*    */   public boolean visibilityBetween(Direction direction1, Direction direction2) {
/* 32 */     return this.data.get(direction1.ordinal() + direction2.ordinal() * FACINGS);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     StringBuilder builder = new StringBuilder();
/* 38 */     builder.append(' ');
/* 39 */     for (Direction direction : Direction.values()) {
/* 40 */       builder.append(' ').append(direction.toString().toUpperCase(Locale.ROOT).charAt(0));
/*    */     }
/* 42 */     builder.append('\n');
/*    */     
/* 44 */     for (Direction direction1 : Direction.values()) {
/* 45 */       builder.append(direction1.toString().toUpperCase(Locale.ROOT).charAt(0));
/* 46 */       for (Direction direction2 : Direction.values()) {
/* 47 */         if (direction1 == direction2) {
/* 48 */           builder.append("  ");
/*    */         } else {
/* 50 */           boolean ok = visibilityBetween(direction1, direction2);
/* 51 */           builder.append(' ').append(ok ? 89 : 110);
/*    */         } 
/*    */       } 
/* 54 */       builder.append('\n');
/*    */     } 
/*    */     
/* 57 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/chunk/VisibilitySet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */